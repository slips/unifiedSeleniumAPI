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

package org.usapi.nodetypes;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.Keyboard;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Mouse;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.usapi.IDOMNode;
import org.usapi.PropertyHelper;
import org.usapi.SeleniumFactory;
import org.usapi.common.USAPIException;

import com.thoughtworks.selenium.Selenium;

public abstract class AbstractNode implements IDOMNode {

	private WebDriver webDriver = null;
	private String type = null;
	private String nodeName = null;
	private String locator = null;
	private Log log = LogFactory.getLog(AbstractNode.class);
	
	public static final String LOGLEVEL_INFO = "info";
	public static final String LOGLEVEL_DEBUG = "debug";
	public static final String LOGLEVEL_WARN = "warn";
	public static final String LOGLEVEL_ERROR = "error";
	
	private static final int NO_KEY = -1;
	private static final int ALT_KEY = 0;
	private static final int CTRL_KEY = 1;
	private static final int SHIFT_KEY = 2;
	
	protected AbstractNode () {}
	
	protected AbstractNode(WebDriver webDriver, String type, String nodeName, String locator )
	{
		this.webDriver = webDriver;
		this.type = type;
		this.nodeName = nodeName;
		this.locator = locator;
	}
	
	protected WebDriver getWebDriver()
	{
		return webDriver;
	}
	
	protected String getType()
	{
		return type;
	}
	
	protected String getNodeName()
	{
		return nodeName;
	}

	
	// Validation actions performed by Selenium should use the abstraction of the name-locator
	// mapping in the spring config files.  Making the locator publicly accessible to support this.
	public String getLocator()
	{
		return locator;
	}
	
	protected boolean notSupported ( String method )
	{
		log("Nodes of type " + getType() + " do not support actions of type " + method + ".", LOGLEVEL_WARN);
		return false;
	}

	public void check()  throws USAPIException
	{
		notSupported ( "check" );
	}

	/**
	 * Every node supports the click method 
	 */
	public void click() throws USAPIException 
	{
		log("Clicking on " + type + " <" + nodeName + "> [locator: " + getLocator() + "]", LOGLEVEL_INFO);
		click( NO_KEY );
	}
	
//	/**
//	 * Every node supports the doubleClick method
//	 */
//	public void doubleClick() throws USAPIException
//	{
//		log("Double-Clicking on " + type + " <" + nodeName + "> [locator: " + getLocator() + "]", LOGLEVEL_INFO);
//		executeMethod( this, "nativeDoubleClick", new Object [] {}, getLocator() );		
//	}
	
	/**
	 * Every node supports the controlKeyDown method
	 */
	public void controlKeyDown() throws USAPIException
	{
		log("Pressing Control modifier key on " + type + nodeName + "> [locator: " + getLocator() + "]", LOGLEVEL_INFO);
		modifierKeyDown(CTRL_KEY);
	}
	
	/**
	 * Every node supports the controlKeyUp method
	 */
	public void controlKeyUp() throws USAPIException
	{
		log("Releasing Control modifier key on " + type + nodeName + "> [locator: " + getLocator() + "]", LOGLEVEL_INFO);
		modifierKeyUp(CTRL_KEY);
	}
	
	/**
	 * Every node supports the mouseOver method
	 */
	public void mouseOver() throws USAPIException
	{
		log("Mouse-ing over " + type + " <" + nodeName + "> [locator: " + getLocator() + "]", LOGLEVEL_INFO);
		getMouse().mouseMove( ((RemoteWebElement)findElement(locator)).getCoordinates());		
	}

	/**
	 * Every node supports the ctrlClick method 
	 */
	public void ctrlClick() throws USAPIException
	{
		log("Pressing <Ctrl> key while clicking on " + type + " <" + nodeName + "> [locator: " + getLocator() + "]", LOGLEVEL_INFO);
		click( CTRL_KEY );
	}
	
	/**
	 * Every node supports the altClick method 
	 */
	public void altClick() throws USAPIException
	{
		log("Pressing <Alt> key while clicking on " + type + " <" + nodeName + "> [locator: " + getLocator() + "]", LOGLEVEL_INFO);
		click( ALT_KEY );
	}
	
	/**
	 * Every node supports the shiftClick method 
	 */
	public void shiftClick() throws USAPIException
	{
		log("Pressing <Shift> key while clicking on " + type + " <" + nodeName + "> [locator: " + getLocator() + "]", LOGLEVEL_INFO);
		click( SHIFT_KEY );
	}
	
	/**
	 * Every node supports the mouseDown method 
	 */
	public void mouseDown() throws USAPIException
	{
		log("Triggering mouseDown on " + type + " <" + nodeName + "> [locator: " + getLocator() + "]", LOGLEVEL_INFO);
		getMouse().mouseDown( ((RemoteWebElement)findElement(locator)).getCoordinates());
	}
	
	/**
	 * Every node supports the mouseUp method 
	 */
	public void mouseUp() throws USAPIException
	{
		log("Triggering mouseUp on " + type + " <" + nodeName + "> [locator: " + getLocator() + "]", LOGLEVEL_INFO);
		getMouse().mouseUp( ((RemoteWebElement)findElement(locator)).getCoordinates());		
	}

	/**
	 * Every node supports the mouseDownUp method
	 */
	public void mouseDownUp() throws USAPIException
	{
        log("Triggering mouseDown followed by mouseUp on " + type + " <" + nodeName + "> [locator: " + getLocator() + "]", LOGLEVEL_INFO);
	    mouseDownUp( NO_KEY );
	}
	
	/**
	 * Every node supports the ctrlMouseDownUp method
	 */
	public void ctrlMouseDownUp() throws USAPIException
	{
        log("Pressing <Ctrl> key while triggering mouseDown followed by mouseUp on " + type + " <" + nodeName + "> [locator: " + getLocator() + "]", LOGLEVEL_INFO);
        mouseDownUp( CTRL_KEY );
	}
	
	/**
	 * Every node supports the altMouseDownUp method
	 */
	public void altMouseDownUp() throws USAPIException
	{
        log("Pressing <Alt> key while triggering mouseDown followed by mouseUp on " + type + " <" + nodeName + "> [locator: " + getLocator() + "]", LOGLEVEL_INFO);
        mouseDownUp( ALT_KEY );
	}
	
    /**
     * Every node supports the shiftMouseDownUp method
     */
	public void shiftMouseDownUp() throws USAPIException
	{
        log("Pressing <Shift> key while triggering mouseDown followed by mouseUp on " + type + " <" + nodeName + "> [locator: " + getLocator() + "]", LOGLEVEL_INFO);
        mouseDownUp( SHIFT_KEY );
	}
	
	public void select( String visibleText )  throws USAPIException
	{
		notSupported ( "select" );
	}
	
	public void addSelection(String selection) throws USAPIException
	{
		notSupported("addSelection");
	}

	public int getRowCount() throws USAPIException
	{
		notSupported("getRowCount");
		return -1;
	}

	public WebElement getRowElement(int rowIndex) throws USAPIException
	{
		notSupported("getRowElement");
		return null;
	}

	public String getRowText(int rowIndex) throws USAPIException
	{
		notSupported("getRowText");
		return null;
	}
	
	public WebElement getCellElement(int rowIndex, int columnIndex) throws USAPIException
	{
		notSupported("getCellElement");
		return null;
	}

	public String getCellText(int rowIndex, int columnIndex) throws USAPIException
	{
		notSupported("getCellText");
		return null;
	}
	
	
	/**
	 * Every node supports the contextMenu method 
	 */
	public void contextMenu() throws USAPIException 
	{
		log("Bringing up context menu on " + type + " <" + nodeName + "> [locator: " + getLocator() + "]", LOGLEVEL_INFO);
		getMouse().contextClick( ((RemoteWebElement)findElement(locator)).getCoordinates());
	}
	
	/**
	 * Unsupported for DOM nodes/UI elements, current support only to focus on browser windows/pop-ups
	 */
	public void focus() throws USAPIException
	{
		notSupported( "focus" );
	}
	
	/**
	 * Every node supports getting the text content of the current node.
	 */
	public String getText() throws USAPIException
	{
		log("Getting text from " + getType() + " <" + getNodeName() + "> [locator: " + getLocator() + "]", LOGLEVEL_INFO);
		Object retVal = executeMethod( this, "nativeGetText", new Object [] {}, getLocator() );
		return (String)retVal;		
	}
	
	/**
	 * Every node supports getting the value of the value attribute (if present).
	 */
	public String getValue() throws USAPIException
	{
		log("Getting value from " + getType() + " <" + getNodeName() + "> [locator: " + getLocator() + "]", LOGLEVEL_INFO);
		Object retVal = executeMethod( this, "nativeGetValue", new Object [] {}, getLocator() );
		return (String)retVal;		
	}
	
	public String[] getSelectOptions() throws USAPIException
	{
		notSupported( "getSelectOptions" );
		return null;
	}
	
	public String getSelectedValue() throws USAPIException
	{
		notSupported( "getSelectedValue" );
		return null;
	}
	
	public void type ( String value ) throws USAPIException 
	{
		notSupported ( "type" );
	}

	public void uncheck() throws USAPIException 
	{
		notSupported ( "uncheck" );
	}
	
	public boolean isEnabled() throws USAPIException
	{
	    log( "Checking if " + type + " <" + nodeName + "> [locator: " + getLocator() + "] is enabled", LOGLEVEL_INFO);
	    return ( (Boolean)executeMethod( this, "nativeIsEnabled", new Object [] {}, getLocator() ) ).booleanValue();
	}

	public boolean isVisible() throws USAPIException
	{
	    log( "Checking if " + type + " <" + nodeName + "> [locator: " + getLocator() + "] is visible", LOGLEVEL_INFO);
	    return ( (Boolean)executeMethod( this, "nativeIsVisible", new Object [] {}, getLocator() ) ).booleanValue();
	}
	
	protected Object executeMethod( Object declaringClassInstance, String methodName, Object[] args, String locator ) throws USAPIException
	{
		boolean success = false;		
		Object returnValue = null;		
		long start = System.currentTimeMillis();
		long stop = 0;
		String errMsg = null;
		long timeOut = PropertyHelper.getTimeout();
		
		Class<?> [] parms = new Class [ args.length];
		for ( int ndx = 0; ndx < args.length; ndx++ )
		{
			parms [ ndx ] = args [ ndx ].getClass();
		}
		
		while ( stop - start < timeOut )
		{
			try
			{
//				highlightElement( locator );
				Method methodToCall = declaringClassInstance.getClass().getMethod(methodName, parms);
				returnValue = methodToCall.invoke ( declaringClassInstance, args );
				// if the function call succeeded, then exit the loop
				success = true;
				errMsg = null;
				break;
			} catch ( Exception e )
			{
				// NoSuchMethodException
				// IllegalAccessException
				// IllegalArgumentException
				// InvocationTargetException
				// NullPointerException
				// ExceptionInInitializerError
				
				// regardless of exception, execution failed, so sleep a sec and try again, until max timeout is reached
				// this of course assumes that the exception was caused by a timing issue, such as AJAX
				// if it is a legit exception, e.g. element not found due to incorrect element definition in the spring
				// config, then the first time through a test developer will have to sit and wait, or set the default
				// timeout to something lower then the default 30 secs.
				errMsg = ( e.getCause() == null ) ? e.getClass() + ": " + e.getMessage() : e.getCause().getMessage();
				errMsg += "Element locator: <" + locator + ">";

				log( errMsg, LOGLEVEL_DEBUG );
				
				sleep ( 1000 );
				stop = System.currentTimeMillis();
			}
		}
		if (!success)
		{
			log( errMsg, LOGLEVEL_ERROR );
			throw new USAPIException( errMsg, getWebDriver() );
		}		
		return returnValue;		
	}
	
	protected void log(String msg, String level)
	{
		if ( level.equalsIgnoreCase(LOGLEVEL_WARN))
		{
			log.warn(msg);
		}
		else if ( level.equalsIgnoreCase(LOGLEVEL_ERROR))
		{
			log.error(msg);
		}
		else if ( level.equalsIgnoreCase(LOGLEVEL_DEBUG))
		{
			log.debug(msg);
		}
		else	// default is info
		{
			log.info( msg );
		}
	}

    protected void sleep ( long ms )
    {
        try
        {
            Thread.sleep ( ms );
        }
        catch (Exception e)
        {
            // ignore
        }
    }
    
    private void click ( int modifierKey ) throws USAPIException
    {
        modifierKeyDown( modifierKey );
        executeMethod( this, "nativeClick", new Object [] {}, getLocator() );
    	modifierKeyUp( modifierKey );
    }
    
    private void mouseDownUp( int modifierKey ) throws USAPIException
    {
        modifierKeyDown( modifierKey );
        getMouse().mouseDown( ((RemoteWebElement)findElement(locator)).getCoordinates());
        getMouse().mouseUp( ((RemoteWebElement)findElement(locator)).getCoordinates());
        modifierKeyUp( modifierKey );
    }
    
    private void modifierKeyDown( int modifierKey ) throws USAPIException
    {
        switch ( modifierKey )
        {
        case ALT_KEY:
        	getKeyboard().pressKey(Keys.ALT); break;
        case CTRL_KEY:
        	getKeyboard().pressKey(Keys.CONTROL); break;
        case SHIFT_KEY:
        	getKeyboard().pressKey(Keys.SHIFT); break;
        }
    }
    
    private void modifierKeyUp( int modifierKey ) throws USAPIException
    {
        switch ( modifierKey )
        {
        case ALT_KEY:
        	getKeyboard().releaseKey(Keys.ALT); break;
        case CTRL_KEY:
        	getKeyboard().releaseKey(Keys.CONTROL); break;
        case SHIFT_KEY:
        	getKeyboard().releaseKey(Keys.SHIFT); break;
        }
    }
    
    protected List<WebElement> findElements( String selector )
    {
    	List<WebElement> webElements = null;
    	if( selector.startsWith( "/" ) || selector.startsWith( "(" ) )
    	{
    		webElements = getWebDriver().findElements( By.xpath(selector));
    	}
    	if( webElements == null )
    	{
    		if( selector.toLowerCase().startsWith("css=") )
    		{
    			webElements = getWebDriver().findElements( By.cssSelector( selector.split("=")[1] ) );
    		}
    	}
    	if( webElements == null )
    	{
    		if( selector.toLowerCase().startsWith("id=") )
    		{
    			webElements = getWebDriver().findElements( By.id( selector.split("=")[1]));
    		}
    	}
    	if( webElements == null )
    	{
    		if( selector.toLowerCase().startsWith("name=") )
    		{
    			webElements = getWebDriver().findElements( By.name( selector.split("=")[1]));
    		}
    	}
    	if( webElements == null )
    	{
    		if( selector.toLowerCase().startsWith("link=") )
    		{
    			webElements = getWebDriver().findElements( By.linkText( selector.split("=")[1]));
    		}
    		if( webElements == null )
    		{
    			webElements = getWebDriver().findElements( By.partialLinkText( selector.split("=")[1]));
    		}
    	}
    	
    	if( webElements.isEmpty() )
    	{
    		throw new NoSuchElementException( "Unable to find elements matching provided selector: <" + selector + ">" );
    	}
    	
    	return webElements;
    }
    
    protected WebElement findElement( String selector ) throws NoSuchElementException
    {
    	WebElement webElement = null;
    	if( selector.startsWith( "/" ) || selector.startsWith( "(" ) )
    	{
    		webElement = getWebDriver().findElement( By.xpath(selector));
    	}
    	if( webElement == null )
    	{
    		if( selector.toLowerCase().startsWith("css=") )
    		{
    			webElement = getWebDriver().findElement( By.cssSelector( selector.split("=")[1] ) );
    		}
    	}
    	if( webElement == null )
    	{
    		if( selector.toLowerCase().startsWith("id=") )
    		{
    			webElement = getWebDriver().findElement( By.id( selector.split("=")[1]));
    		}
    	}
    	if( webElement == null )
    	{
    		if( selector.toLowerCase().startsWith("name=") )
    		{
    			webElement = getWebDriver().findElement( By.name( selector.split("=")[1]));
    		}
    	}
    	if( webElement == null )
    	{
    		if( selector.toLowerCase().startsWith("link=") )
    		{
    			webElement = getWebDriver().findElement( By.linkText( selector.split("=")[1]));
    		}
    		if( webElement == null )
    		{
    			webElement = getWebDriver().findElement( By.partialLinkText( selector.split("=")[1]));
    		}
    	}
    	
    	if( webElement == null )
    	{
    		throw new NoSuchElementException( "Unable to find elements matching provided selector: <" + selector + ">" );
    	}
    	
    	return webElement;

    }

    protected List<WebElement> timedFindElements( String selector, long timeout ) throws NoSuchElementException
    {
		long start = System.currentTimeMillis();
		long stop = 0;
		List<WebElement> e = null;
		NoSuchElementException noSuchElementException = null;
		while ( stop - start < timeout )
		{
			try
			{
				e = findElements( selector );
				break;
			} catch ( NoSuchElementException nsee )
			{
				noSuchElementException = nsee;
				sleep ( 1000 );
				stop = System.currentTimeMillis();
			}
		}
		if( e.isEmpty() )
		{
			throw noSuchElementException;
		}
		return e;
    	
    }
    
    protected WebElement timedFindElement( String selector, long timeout ) throws NoSuchElementException
    {
    	return timedFindElements( selector, timeout ).get( 0 );
    }
    
    
    private Mouse getMouse()
    {
    	Mouse m = null;
    	if(webDriver instanceof HtmlUnitDriver)
    	{
    		m = ((HtmlUnitDriver)webDriver).getMouse();
    	}
    	else if( webDriver instanceof RemoteWebDriver )
    	{
    		m = ((RemoteWebDriver)webDriver).getMouse();
    	}
    	return m;
    }
    
    private Keyboard getKeyboard()
    {
    	Keyboard k = null;
    	if(webDriver instanceof HtmlUnitDriver)
    	{
    		k = ((HtmlUnitDriver)webDriver).getKeyboard();
    	}
    	else if( webDriver instanceof RemoteWebDriver )
    	{
    		k = ((RemoteWebDriver)webDriver).getKeyboard();
    	}
    	return k;
    }
    
    public WebElement getWebElement()
    {
    	return findElement( getLocator() );
    }
    
    public List<WebElement> getWebElements()
    {
    	return findElements( getLocator() );
    }
    
	/****************************************************************************/
	// Following (native*) methods are not intended to be called directly.   
	// Invocation is expected to occur through AbstractNode:executeMethod, 
    // which provides for a checked/safe execution environment, and convenience 
    // features in case of failure.
	/****************************************************************************/
	public boolean nativeIsVisible() throws USAPIException
	{
		WebElement webElement = findElement( getLocator() );
		return webElement.isDisplayed();
	}    

    public void nativeClick() throws USAPIException
    {
    	   //getMouse().mouseMove( ((RemoteWebElement)findElement(locator)).getCoordinates());
    	   //getMouse().click(((RemoteWebElement)findElement(locator)).getCoordinates());
    	   //log("Setting focus to element prior to click", LOGLEVEL_INFO);
    	   //focus();
    	   WebElement webElement = findElement( getLocator() );
    	   webElement.click();
    }
    
//    public void nativeDoubleClick() throws USAPIException
//    {
//    	// Discard the location (Point), we just want to scroll the element into view
//    	((RemoteWebElement)findElement(locator)).getLocationOnScreenOnceScrolledIntoView();
//    	getMouse().doubleClick( ((RemoteWebElement)findElement(locator)).getCoordinates());	    	
//    }
    
	public boolean nativeIsEnabled() throws USAPIException
	{
		WebElement webElement = findElement( getLocator() );
		return webElement.isEnabled();
	}    
	
	public String nativeGetValue() throws USAPIException
	{
		WebElement webElement = findElement( getLocator() );
		return webElement.getAttribute( "value" );
	}
	
	public String nativeGetText() throws USAPIException
	{
		WebElement webElement = findElement( getLocator() );
		return webElement.getText();
	}

	public void nativeClear()
	{
		WebElement webElement = findElement( getLocator() );
		webElement.clear();
	}
	
	public void nativeSendKeys( String value )
	{
		WebElement webElement = findElement( getLocator() );
		webElement.sendKeys( new CharSequence[] { value } );
	}		
}
