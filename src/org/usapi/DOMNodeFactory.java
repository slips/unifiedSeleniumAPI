/*
Copyright 2011 Software Freedom Conservatory.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package org.usapi;

import java.lang.reflect.Constructor;
import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.springframework.context.ApplicationContext;

import com.thoughtworks.selenium.Selenium;


/**
 * @author slips
 *
 * <tt>DOMNodeFactory</tt> singleton to create DOM node instances
 */
public class DOMNodeFactory {
	
	private Hashtable<String, Hashtable> domNodes = null;
	
	private Selenium selenium = null;
	private WebDriver webDriver = null;
	
	private static DOMNodeFactory me = null;
	private Log log = LogFactory.getLog(DOMNodeFactory.class);
	
	private static boolean initialized = false;
	
	private DOMNodeFactory() {}
	
	public static DOMNodeFactory getInstance()
	{
		if ( me == null )
		{
			me = new DOMNodeFactory();
		}
		return me;
	}
	
	// make initialization status publicly available.
	public boolean getInitialized()
	{
		return initialized;
	}
	
	/**
	 * Due to use of singleton pattern, explicit initialization is required.  Factory methods
	 * will return null without initialization.
	 * 
	 * @param appCtx The application context in which the DOM nodes are defined
	 * @param selenium The selenium instance used to execute actions on the DOM nodes
	 */
	public void initialize(ApplicationContext appCtx, WebDriver webDriver /*, Selenium selenium*/)
	{
		this.selenium = selenium;
		this.webDriver = webDriver;
		domNodes = (Hashtable<String, Hashtable>)appCtx.getBean("domNodes");

		initialized = true;
	}
	
	/**
	 * Get a DOM node of a given type, with a given display string.
	 * 
	 * @param nodeType Type of DOM node.  For comprehensive listing of supported types, please see application
	 * elements XML file for a given (set of) tests.
	 * @param nodeName UI display string by which the node is identified
	 * @return IDOMNode instance if successful, null if unsuccessful
	 * @throws DOMNodeNotFoundException Thrown if no nodes of the specified type and/or with the specified UI
	 * 	display string are defined in the application elements XML file.
	 */
	public IDOMNode getDOMNode( NodeType nodeType, String nodeName ) throws DOMNodeNotFoundException
	{
		IDOMNode domNode = null;

		if ( initialized )
		{
			Hashtable<String, String> nodes = (Hashtable<String, String>)domNodes.get(nodeType.getType());
			if ( nodes == null )
			{
				throw new DOMNodeNotFoundException("Sorry, I do not know of any DOM nodes of type " + nodeType.getType() 
						+ ".  The types I know of are " + domNodes.keySet().toString());
			}
			String locator = (String)nodes.get(nodeName);
			if ( locator == null )
			{
				throw new DOMNodeNotFoundException("Sorry, I do not know of any DOM nodes of type " + nodeType.getType()
						+ " that are named " + nodeName + ".  Please review your Spring configuration file"
						+ "(default: application-elements.xml) for all known nodes of type " + nodeType.getType() + ".");
			}
			
			domNode = getNode( nodeType, nodeName, locator );
		}		
		else
		{
			log.error("DOMNodeFactory has not been initialized.");
		}
		return domNode;
	}
	
	/**
	 * Type-safe mechanism to get a node of desired type without requiring an entry in
	 * application elements XML.
	 * @param nodeType type of node to create.  See {@link org.usapi.NodeType}
	 * for enumeration of all available types
	 * @param locator Locator to identify the node in the DOM.
	 * @return node of specified type, with specified locator
	 */
	public IDOMNode getElementNode( NodeType nodeType, String locator )
	{
	    IDOMNode elementNode = null;
	    
	    if( initialized )
	    {
	        elementNode = getNode( nodeType, "", locator );
	    }
	    else
	    {
	        log.error( "DOMNodeFactory has not been initialized." );
	    }
	    return elementNode;
	}
	
	private IDOMNode getNode( NodeType nodeType, String nodeName, String locator )
	{
        IDOMNode domNode = null;
	    
        // create classname that begins with upper case, followed by all lower-case for type, appended by 'Node'
        char [] classname = nodeType.getType().toLowerCase().toCharArray();
        classname[0] = Character.toUpperCase(classname[0]);
        String nodeClassName = "org.usapi.nodetypes." 
            + new String ( classname )
            + "Node";
        
        try
        {
            Class<IDOMNode> domNodeClass = (Class<IDOMNode>)Class.forName(nodeClassName);
            Constructor<IDOMNode> nodeConstructor = domNodeClass.getConstructor(new Class [] 
                    { WebDriver.class,
                        String.class,
                        String.class,
                        String.class
                    } 
            );
            domNode = nodeConstructor.newInstance(new Object [] { webDriver, /*selenium,*/ nodeType.getType(), nodeName, locator });
        } 
        catch ( Exception e )
        {
            log.error(e.getMessage(), e);
        }
        
        return domNode;	    
	}
	
}
