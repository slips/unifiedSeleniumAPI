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

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Properties;

import org.junit.Assert.*;
import junit.framework.AssertionFailedError;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.usapi.common.DataInjector;
import org.usapi.common.USAPIException;

import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.Wait;


/**
 * <h4>BaseSeleniumTest base class for all selenium tests.</h4>
 * <p>
 * Specific tests may require helper functions to perform non-selenium
 * specific operations (SQL queries, verify successful sending/receipt
 * of email notification), or simply batch common operations (login, etc).
 * 
 * Those helper functions should be implemented in helper modules
 * for that particular test package.  The functionality in this package
 * is application-agnostic, helper functions in this class are generic.
 */
public abstract class BaseSeleniumTest
{
    /** The default webdriver instance */
    protected WebDriver webDriver;
    
    AbstractXmlApplicationContext elements = null;

    protected BaseApplication app;
    
    protected Properties data;
    
    private static Log log = LogFactory.getLog(BaseSeleniumTest.class);
    
    private long timeout = PropertyHelper.getTimeout();
    
    // make NodeTypes available to test case without requiring them
    // to import org.usapi.NodeType
    public final NodeType TYPE_BUTTON       = NodeType.BUTTON;
    public final NodeType TYPE_CATEGORY		= NodeType.CATEGORY;
    public final NodeType TYPE_CHECKBOX     = NodeType.CHECKBOX;
    public final NodeType TYPE_ELEMENT      = NodeType.ELEMENT;
    public final NodeType TYPE_HIDDENFIELD	= NodeType.HIDDENFIELD;
    public final NodeType TYPE_IMAGE        = NodeType.IMAGE;
    public final NodeType TYPE_LINK         = NodeType.LINK;
    public final NodeType TYPE_MENU         = NodeType.MENU;
    public final NodeType TYPE_MENUITEM     = NodeType.MENUITEM;
    public final NodeType TYPE_MODALDIALOG	= NodeType.MODALDIALOG;
    public final NodeType TYPE_RADIOBUTTON  = NodeType.RADIOBUTTON;
    public final NodeType TYPE_SELECT       = NodeType.SELECT;
    public final NodeType TYPE_TAB          = NodeType.TAB;
    public final NodeType TYPE_TABLE	    = NodeType.TABLE;
    public final NodeType TYPE_TABLECELL    = NodeType.TABLECELL;
    public final NodeType TYPE_TABLEROW	    = NodeType.TABLEROW;
    public final NodeType TYPE_TEXT         = NodeType.TEXT;
    public final NodeType TYPE_TEXTFIELD    = NodeType.TEXTFIELD;
    public final NodeType TYPE_TREENODE		= NodeType.TREENODE;
    public final NodeType TYPE_WINDOW       = NodeType.WINDOW;
    
    private static ChromeDriverService chromeDrvSvc;
    private static boolean isRunningOnGrid = PropertyHelper.getSeleniumServerHost().length() > 0
    		&& PropertyHelper.getSeleniumServerPort().length() > 0;
    
    public BaseSeleniumTest()
    {
    	elements = getElements();
	}
    
    /**
     * When running on the grid, chromedriver does not need to be started.  Trial-and-error has shown that
     * the selenium server appears to be taking care of running chromedriver on the remote machine.
     * We therefore launch chrome driver only when running local, and expect it to be in the path.
     * @throws IOException
     */
    @BeforeClass
    public static void createAndStartChromeDriverService() throws IOException
    {
    	String browser = PropertyHelper.getSeleniumBrowserCommand();
    	if( browser.toLowerCase().contains("chrome") && !isRunningOnGrid )
    	{
    		log.info("Using chromedriver from <" + PropertyHelper.getChromeDriverExecutable() + ">");
    		// chromedriver is expected to be in the path
    		chromeDrvSvc = new ChromeDriverService.Builder()
    			.usingDriverExecutable( new File( PropertyHelper.getChromeDriverExecutable() ) )
    			.usingPort( Integer.parseInt( PropertyHelper.getChromeDriverPort() ) )
    			.build();
    		chromeDrvSvc.start();
    	}
    }
    
    @AfterClass
    public static void stopChromeDriverService()
    {
    	String browser = PropertyHelper.getSeleniumBrowserCommand();
    	if( browser.toLowerCase().contains("chrome") && !isRunningOnGrid )
    	{
    		chromeDrvSvc.stop();
    	}
    }

    /**
     * JUnit setUp.  Starts selenium and maximizes browser if JVM env var selenium.maximize is set.
     */
    @Before
    public void setUpWebDriver()
    {
        webDriver = SeleniumFactory.getWebDriverInstance();
		app = new BaseApplication(webDriver, elements );    
		
		if ( PropertyHelper.getMaximize() && !isRunningOnGrid )
        {
        	webDriver.manage().window().maximize();
        }
    }

    /**
     * JUnit teardown.  Stops selenium.
     */
    @After
    public void tearDownSelenium()
    {
    	SeleniumFactory.reset();
    }

    /**
     * Returns the instance of {@link org.openqa.selenium.WebDriver} that is used by a given test.
     * SeleniumFactory::getWebDriver returns the same singleton.
     *
     * This also serves as a wrapper to shield helper functions from having to know or deal with the SeleniumFactory.
     *
     * @return WebDriver instance (singleton)
     */
    public WebDriver getWebDriver()
    {
    	return webDriver;
    }
    
    /**
     * Open the specified URL in the currently open browser.
     * @param url
     */
    public void open( String url )
    {
    	log.info("Navigating to <" + url + ">" );
    	webDriver.navigate().to( url );
    }
    
    /**
     * Test if an element is absent, or non-present.  This addresses timing issues where a
     * call to isElementPresent can return 'true' in cases where page-load takes excessive amounts of time, and 
     * verification that a given element is NOT present will fail.  
     * @param locator Locator that identifies element in the current DOM
     * @return whether the specified element is absent, or non-present
     */
    public boolean isElementAbsent( final String locator )
    {
        boolean isElementAbsent = false;
        
        long start = System.currentTimeMillis();
        long stop = 0;
        // would love to use selenium's Wait, sadly though the .wait(String, long)
        // method does not work as advertised (surprise), it ignores the specified
        // long and uses the default timeout.  Rolling my own instead ...
        while ( stop - start < this.timeout )
        {
            isElementAbsent = findElementsBy( locator ).isEmpty();
            if ( isElementAbsent ) break;
            sleep( 500 );
            stop = System.currentTimeMillis();
        }
        
        return isElementAbsent;
    }

    /**
     * Test if an element is absent, or non-present.  This addresses timing issues where a
     * call to isElementPresent can return 'true' in cases where page-load takes excessive amounts of time, and 
     * verification that a given element is NOT present will fail.  
     * @param element element to check for absence/non-presence
     * @return whether the specified element is absent, or non-present
     */
    public boolean isElementAbsent( final IDOMNode element )
    {
    	return isElementAbsent( element.getLocator() );
    }
    
    /**
     * Check for element to be absent/non-present up to user-specified timeout
     * @param locator Locator that identifies element in the current DOM
     * @param timeout up to how many milliseconds to check for absence
     * @return whether the specified element is absent, or non-present
     */
    public boolean isElementAbsent( final String locator, final long timeout )
    {
        long currentTimeout = this.timeout;
        this.timeout = timeout;
        boolean isElementAbsent = isElementAbsent( locator );
        this.timeout = currentTimeout;
        return isElementAbsent;
    }
    
    /**
     * Check for element to be absent/non-present up to user-specified timeout
     * @param element element to check for absence/non-presence
     * @param timeout up to how many milliseconds to check for absence
     * @return whether the specified element is absent, or non-present
     */
    public boolean isElementAbsent( final IDOMNode element, final long timeout )
    {
        long currentTimeout = this.timeout;
        this.timeout = timeout;
        boolean isElementAbsent = isElementAbsent( element );
        this.timeout = currentTimeout;
        return isElementAbsent;
    }
    
    
    /**
     * Wrapper for selenium::isElementPresent, that provides handling of timing issues.  Will check for up
     * to selenium.timeout JVM env var value if element is present
     * @param locator Locator that identifies element in the current DOM
     * @return whether element described by @locator is present on the current screen
     */
    public boolean isElementPresent( final String locator )
    {
    	boolean isElementPresent = false;
    	
		long start = System.currentTimeMillis();
		long stop = 0;
		// would love to use selenium's Wait, sadly though the .wait(String, long)
		// method does not work as advertised (surprise), it ignores the specified
		// long and uses the default timeout.  Rolling my own instead ...
		while ( stop - start < this.timeout )
		{
			isElementPresent = !findElementsBy( locator ).isEmpty();
			if ( isElementPresent ) break;
			sleep( 500 );
			stop = System.currentTimeMillis();
		}
		
    	return isElementPresent;
    }
    
    /**
     * Wrapper for selenium::isElementPresent, that provides handling of timing issues.  Will check for up
     * to selenium.timeout JVM env var value if element is present
     * @param element element to check for absence/non-presence
     * @return whether element described by @locator is present on the current screen
     */    
    public boolean isElementPresent( final IDOMNode element )
    {
    	return isElementPresent( element.getLocator() );
    }

    /**
     * Wrapper for selenium::isElementPresent, that provides handling of timing issues.  Will check for up
     * to selenium.timeout JVM env var value if element is present
     * @param locator Locator that identifies element in the current DOM
     * @param timeout How long to check for element, milliseconds.
     * @return wether element described by @locator is present on the current screen
     */
    public boolean isElementPresent( final String locator, final long timeout )
    {
    	long currentTimeout = this.timeout;
    	this.timeout = timeout;
    	boolean isElementPresent = isElementPresent( locator );
    	this.timeout = currentTimeout;
    	return isElementPresent;
    }
    
    /**
     * Wrapper for selenium::isElementPresent, that provides handling of timing issues.  Will check for up
     * to selenium.timeout JVM env var value if element is present
     * @param element element to check for absence/non-presence
     * @param timeout How long to check for element, milliseconds.
     * @return wether element described by @locator is present on the current screen
     */    
    public boolean isElementPresent( final IDOMNode element, final long timeout )
    {
    	long currentTimeout = this.timeout;
    	this.timeout = timeout;
    	boolean isElementPresent = isElementPresent( element );
    	this.timeout = currentTimeout;
    	return isElementPresent;
    }
    
    /**
     * Asserts that a condition is true. If it isn't it captures the current
     * screen to a .png and .html file and throws an {@link AssertionFailedError} with the 
     * given message.  Override to junit's assertTrue.
     * 
     * @param message
     *            the identifying message for the {@link AssertionFailedError} (<code>null</code>
     * @param condition
     *            condition to be checked
     */
    public void assertTrue( String message, boolean condition ) throws USAPIException
    {
        try
        {
            org.junit.Assert.assertTrue( message, condition );
        }
        catch ( AssertionFailedError afe )
        {
            logFailedAssertion( message, afe );
    		throw new USAPIException( "Assertion failed: " + message, getWebDriver() ) ;
    	}
    }

    /**
     * Asserts that a condition is false. If it isn't it captures the current
     * screen to a .png and .html file and throws an {@link AssertionFailedError} with the 
     * given message.  Override to junit's assertTrue.
     * 
     * @param message
     *            the identifying message for the {@link AssertionFailedError} (<code>null</code>
     * @param condition
     *            condition to be checked
     */
    public void assertFalse( String message, boolean condition ) throws USAPIException
    {
        try
        {
            org.junit.Assert.assertFalse( message, condition );
        }
        catch ( AssertionFailedError afe )
        {
            logFailedAssertion( message, afe );
    		throw new USAPIException( "Assertion failed: " + message, getWebDriver() ) ;
        }
    }
    
    /**
     * Assert that the message from the alert triggered by the statement preceding the call to this 
     * method is msg.  If it isn't it captures the current
     * screen to a .png and .html file and throws an {@link AssertionFailedError} with the 
     * given message.  Override to junit's assertTrue.
     * NOTE: selenium does not show javascript visual alerts when executing a test.  If an alert is
     * expected, it MUST be asserted in the call immediately following the call that triggered the
     * alert.
     * @param msg the expected message of the alert 
     */
    public void assertAlert( String msg ) throws USAPIException
    {
    	String actual = app.alert().getText();
    	String failureMsg = "Alert message not as expected, expected: " + msg + ", actual: " + actual;
    	try
    	{
    		org.junit.Assert.assertTrue( actual.equals( msg ) );
    	}
    	catch( AssertionFailedError afe )
    	{
    		logFailedAssertion( failureMsg, afe );
    		throw new USAPIException( "Assertion failed: " + failureMsg, getWebDriver() ) ;
    	}
    }
    
    
    /**
     * Waits until the element appears (default timeout of 30s is used),
     * useful for Ajax calls.
     * @param locator element locator
     */
    public void waitForElement(final String locator)
    {
        new Wait()
        {
            public boolean until()
            {
                return isElementPresent(locator);
            }
        }.wait("Timeout watiting for '" + locator + "'");
    }

    /**
     * Default convenience logging method, writes to INFO.
     * @param msg Message to write
     */
    public void log ( String msg )
    {
    	log.info ( msg );
    }
    
    /**
     * Convenience logging method, write to WARNING.
     * @param msg Message to write
     */
    public void logWarning ( String msg )
    {
    	log.warn( msg );
    }
    
    /**
     * Convenience logging method, writes to ERROR
     * @param msg Message to write
     */
    public void logError ( String msg )
    {
    	log.error( msg );
    }
    
    /**
     * Wait for the specified time.
     *
     * @param milli The time in milliseconds to wait.
     */
    public void sleep(long milli)
    {
        try
        {
            Thread.sleep(milli);
        }
        catch (Exception e)
        {
            // Just continue, hope for the best.
        }
    }
    
    private AbstractXmlApplicationContext getElements()
    {
        AbstractXmlApplicationContext elements = null;
        String appElementsXML = PropertyHelper.getApplicationElementsXML();

        data = PropertyHelper.getData();
        if ( data != null )
        {
        	// 'file:' prefix is needed to work around spring issue, where spring will change
        	// any absolute Unix file path (beginning with '/') to resource path by stripping
        	// the leading '/'.
        	appElementsXML = "file:" + DataInjector.inject( appElementsXML, data );
			log.info("Loading application elements from " + appElementsXML );
        	elements = new FileSystemXmlApplicationContext( appElementsXML );
        }
        else
        {
    		try
    		{
    			log.info("Loading application elements from " + appElementsXML );
    			elements = new ClassPathXmlApplicationContext( appElementsXML );
    		} 
    		catch (Exception e)
    		{
    			log.error(e.getMessage());
    		}
        }
    	return elements;
    }
    
    private void logFailedAssertion( String assertionMsg, AssertionFailedError afe )
    {
        log.error( "Assertion failed: " + assertionMsg );
        log.error( "Error message: " + afe.getMessage() );
        log.error( getStacktraceAsString( afe ) );
        
    }
    
    private String getStacktraceAsString( Throwable t )
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter( sw, true );
        t.printStackTrace( pw );
        pw.flush();
        sw.flush();
        return sw.toString();
    }
    
    private List<WebElement> findElementsBy( String locator )
    {
    	List<WebElement> webElements = null;
    	if( locator.startsWith( "/" ) || locator.startsWith( "(" ) )
    	{
    		webElements = getWebDriver().findElements( By.xpath(locator));
    	}
    	if( webElements == null )
    	{
    		if( locator.toLowerCase().startsWith("css=") )
    		{
    			webElements = getWebDriver().findElements( By.cssSelector( locator.split("=")[1] ) );
    		}
    	}
    	if( webElements == null )
    	{
    		if( locator.toLowerCase().startsWith("id=") )
    		{
    			webElements = getWebDriver().findElements( By.id( locator.split("=")[1]));
    		}
    	}
    	if( webElements == null )
    	{
    		if( locator.toLowerCase().startsWith("name=") )
    		{
    			webElements = getWebDriver().findElements( By.name( locator.split("=")[1]));
    		}
    	}
    	if( webElements == null )
    	{
    		if( locator.toLowerCase().startsWith("link=") )
    		{
    			webElements = getWebDriver().findElements( By.linkText( locator.split("=")[1]));
    		}
    		if( webElements == null )
    		{
    			webElements = getWebDriver().findElements( By.partialLinkText( locator.split("=")[1]));
    		}
    	}
    	
    	return webElements;
    	
    }
    
    private static String getChromeDriverFilename()
    {
    	return System.getProperty("os.name").startsWith("Windows") ? "chromedriver.exe" : "chromedriver";
    }

}