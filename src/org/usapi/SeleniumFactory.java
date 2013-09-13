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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import com.thoughtworks.selenium.Selenium;

/**
 * Factory to provide an instance that implements the {@link com.thoughtworks.selenium.Selenium} interface
 *
 */
public class SeleniumFactory
{
	private static final Log log = LogFactory.getLog(SeleniumFactory.class);
	private static WebDriver webDriver = null;
    
    private static final String BROWSER_TYPE_FIREFOX = "firefox";
    private static final String BROWSER_TYPE_IEXPLORE = "iexplore";
    private static final String BROWSER_TYPE_CHROME = "chrome";
    private static final String BROWSER_TYPE_SAFARI = "safari";
    private static final String BROWSER_TYPE_HTMLUNIT = "htmlunit";
    
    /**
     * Returns an instance of WebDriver.
     *
     * This is a singleton that is instantiated with parameters 
     * from system properties with overrides
     */
    public static WebDriver getWebDriverInstance()
    {
    	if( webDriver == null )
    	{
        	String browser = PropertyHelper.getSeleniumBrowserCommand();
        	
        	String seleniumServerHost = PropertyHelper.getSeleniumServerHost();
        	String seleniumServerPort = PropertyHelper.getSeleniumServerPort();
        	// if selenium server host/port properties are set, they were passed in via 
        	// .properties or -D.  Either way, this tells us that we are running on the
        	// grid and need to instantiate our web driver such that it knows to use
        	// selenium server
        	if( seleniumServerHost.length() > 0 && seleniumServerPort.length() > 0 )
        	{
            	URL seleniumServerUrl = null;
	        	try
	        	{
	        		seleniumServerUrl = new URL( "http://" + seleniumServerHost + ":" + seleniumServerPort + "/wd/hub");
	        	}
	        	catch( MalformedURLException e )
	        	{
	        		// this should never happen, as the default values for serverHost and port are localhost and 4444.  Only if
	        		// overrides (from .properties or cmd line) result in an invalid URL this exception handler would get invoked.
	        		log.error("Invalid value for Selenium Server Host or Selenium Server Port.  Provided values: <" 
	        				+ seleniumServerHost + "> <" 
	        				+ seleniumServerPort + ">");
	
	        	}
            	DesiredCapabilities capability = getDesiredCapabilities(browser);
            	if( browser.toLowerCase().contains(BROWSER_TYPE_FIREFOX))
            	{
            		capability.setCapability("nativeEvents", PropertyHelper.getEnableNativeEvents());
            		capability.setCapability("name", "Remote File Upload using Selenium 2's FileDetectors");
            	}
            	RemoteWebDriver rwd = new RemoteWebDriver( seleniumServerUrl, capability );
            	rwd.setFileDetector( new LocalFileDetector() );
	        	webDriver = new Augmenter().augment( (WebDriver)rwd);
        	}
        	// no, we are not running on the grid.  Just instantiate the driver for the desired
        	// browser directly.
        	else
        	{
            	if( browser.toLowerCase().contains(BROWSER_TYPE_FIREFOX))
            	{
            		FirefoxProfile p = new FirefoxProfile();
            		p.setEnableNativeEvents(PropertyHelper.getEnableNativeEvents());
            		webDriver = new FirefoxDriver(p);
            	}
            	else if( browser.toLowerCase().contains( BROWSER_TYPE_IEXPLORE ) )
            	{
            		webDriver = new InternetExplorerDriver();
            	}
            	else if(browser.toLowerCase().contains( BROWSER_TYPE_CHROME ) )
            	{
					webDriver = new RemoteWebDriver( getChromeDriverURL(), DesiredCapabilities.chrome());
					webDriver = new Augmenter().augment( (WebDriver)webDriver);
            	}
            	else if( browser.toLowerCase().contains( BROWSER_TYPE_SAFARI ) )
            	{
            		webDriver = new SafariDriver();
            	}
            	else if( browser.toLowerCase().contains( BROWSER_TYPE_HTMLUNIT ) )
            	{
            		DesiredCapabilities capabilities = getDesiredCapabilities( browser );
            		capabilities.setJavascriptEnabled(PropertyHelper.getHtmlUnitEnableJavascript());

            		webDriver = new HtmlUnitDriver(capabilities);
            	}
        	}

        	// default in PropertyHelper is 0.  Set this options only if they were explicitly specified (.properties or 
        	// in the environment
        	long scriptTimeout = PropertyHelper.getScriptTimeout();
        	long implicitlyWait = PropertyHelper.getImplicitlyWait();
        	long pageLoadTimeout = PropertyHelper.getPageLoadTimeout();
        	if (scriptTimeout > 0) webDriver.manage().timeouts().setScriptTimeout(scriptTimeout, TimeUnit.MILLISECONDS);
        	if (implicitlyWait > 0) webDriver.manage().timeouts().implicitlyWait(implicitlyWait, TimeUnit.MILLISECONDS);
        	if (pageLoadTimeout > 0) webDriver.manage().timeouts().pageLoadTimeout(pageLoadTimeout, TimeUnit.MILLISECONDS);
        }
    	return webDriver;
    }
    
    public static void reset()
    {
    	if( webDriver != null )
    	{
    		webDriver.quit();
        	webDriver = null;
    	}
    }
    
    private static DesiredCapabilities getDesiredCapabilities(String browser)
    {
    	DesiredCapabilities c = new DesiredCapabilities();
    	if( browser.toLowerCase().contains(BROWSER_TYPE_FIREFOX))
    	{
    		c = DesiredCapabilities.firefox();
    	}
    	else if( browser.toLowerCase().contains(BROWSER_TYPE_IEXPLORE))
    	{
    		c = DesiredCapabilities.internetExplorer();
    	}
    	else if(browser.toLowerCase().contains(BROWSER_TYPE_CHROME))
    	{
    		c = DesiredCapabilities.chrome();
    	}
    	else if(browser.toLowerCase().contains(BROWSER_TYPE_HTMLUNIT))
    	{
    		c = DesiredCapabilities.htmlUnit();
    	}
    	
    	String v = PropertyHelper.getSeleniumBrowserVersion();
    	if( v != null && v.length() > 0 )
    	{
    		c.setVersion(v);
    	}
    	
    	// valid values are Android, Linux, Mac, Any, Unix, Vista, Windows, XP, see
    	// http://selenium.googlecode.com/svn/trunk/docs/api/java/org/openqa/selenium/Platform.html
    	String p = PropertyHelper.getSeleniumBrowserPlatform();
    	if( p != null && p.length() > 0 )
    	{
    		c.setPlatform( Platform.valueOf(p) );
    	}
    	
    	return c;
    }
    
    private static URL getChromeDriverURL()
    {
    	URL url = null;
    	try
    	{
    		url = new URL( "http://" + PropertyHelper.getChromeDriverHost() 
    				+ ":" + PropertyHelper.getChromeDriverPort() );
    	}
    	catch( MalformedURLException e )
    	{
    		// this should never happen, as the default values for serverHost and port are localhost and 4444.  Only if
    		// overrides (from .properties or cmd line) result in an invalid URL this exception handler would get invoked.
    		log.error("Invalid value for ChromeDriver Server Host or ChromeDriver Server Port.  Provided values: <" 
    				+ PropertyHelper.getChromeDriverHost() + "> <" 
    				+ PropertyHelper.getChromeDriverPort() + ">");

    	}
    	return url;
    }
}
