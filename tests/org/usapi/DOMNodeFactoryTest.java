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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import org.usapi.nodetypes.ButtonNode;
import org.usapi.nodetypes.LinkNode;
import com.thoughtworks.selenium.Selenium;

public class DOMNodeFactoryTest {

	ApplicationContext 	appCtx		= null;
	WebDriver			webDriver	= null;
	
	Log log = LogFactory.getLog(DOMNodeFactoryTest.class);

	private String NO_NODE_FOR_THIS_KEY_ERR_MSG = "Sorry, I do not know of any DOM nodes of "
		+ "type %nodeType% that are named non-existing-key.  Please review your Spring configuration "
		+ "file(default: application-elements.xml) for all known nodes of type %nodeType%.";
	
	private String NO_TYPE_FOR_THIS_KEY_ERR_MSG = "Sorry, I do not know of any DOM nodes of type" 
		+ " nonExisting.  The types I know of are %nodeTypes%";
	
	
	@Before
	public void setUp() throws Exception {
		webDriver = new RemoteWebDriverDummy();
		try{
			appCtx = new ClassPathXmlApplicationContext("junit-application-elements.xml");
		} catch (Exception e)
		{
			log.error(e.getMessage());
		}	
	}

	@After
	public void tearDown() throws Exception {
		appCtx = null;
	}

	@Test
	public void testGetInstance() {
		DOMNodeFactory f = DOMNodeFactory.getInstance();
		assertNotNull(f);
	}

	@Test
	public void testInitialize() {
		IDOMNode node = null;
		DOMNodeFactory f = DOMNodeFactory.getInstance();
		assertNotNull(f);
		assertFalse(f.getInitialized());
		// verify we get back null if factory is not initialized yet
		try
		{
			node = f.getDOMNode(NodeType.BUTTON, "bar");
			assertNull(node);
		} catch (DOMNodeNotFoundException e )
		{
			fail("Unexpected DOMNodeNotFoundException: " + e.getMessage() );
		}

		// verify we get back non-null if factory is initialized
		f.initialize(appCtx, webDriver);
		assertTrue(f.getInitialized());
		try
		{
			node = f.getDOMNode(NodeType.LINK, "k1");
			assertNotNull(node);
			assertTrue( node instanceof LinkNode );
		} catch (DOMNodeNotFoundException e )
		{
			fail("Unexpected DOMNodeNotFoundException: " + e.getMessage() );
		}
	}

	@Test
	public void testGetDOMNode() {
		DOMNodeFactory f = DOMNodeFactory.getInstance();
		f.initialize(appCtx, webDriver);
		IDOMNode node = null;

		// get and verify existing element
		try
		{
			node = f.getDOMNode(NodeType.BUTTON, "k1");
			assertNotNull(node);
			assertTrue(node instanceof ButtonNode);
		} catch (DOMNodeNotFoundException e )
		{
			fail("Unexpected DOMNodeNotFoundException: " + e.getMessage() );
		}
		
		// verify correct behavior for non-existing element
		try
		{
			node = f.getDOMNode(NodeType.BUTTON, "non-existing-key");
			assertNotNull(node);
			fail("Did not receive expected DOMNodeNotFoundException");
		} catch (DOMNodeNotFoundException e )
		{
			// we expect and exception
			assertEquals(e.getMessage(), getNodeForThisKeyErrorMsg("button"));
		}
	
	}

	private String getNodeForThisKeyErrorMsg(String nodeType)
	{
		return NO_NODE_FOR_THIS_KEY_ERR_MSG.replace("%nodeType%", nodeType);
	}


}
