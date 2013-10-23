package org.usapi.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.usapi.BaseSeleniumTest;
import org.usapi.PropertyHelper;

public class AjaxMonitor 
{
	private static Log log = LogFactory.getLog(AjaxMonitor.class);
	
	public static void waitForAjax( WebDriver drvr )
	{
		if( PropertyHelper.getJQueryWaitForAjax() )
		{
			JavascriptExecutor jsx = (JavascriptExecutor)drvr;
			long start = System.currentTimeMillis();
			long stop = 0;
			long timeout = PropertyHelper.getTimeout();
			do
			{
				// from http://selenium.googlecode.com/git/docs/api/java/org/openqa/selenium/JavascriptExecutor.html:
				// JavascriptExecutor returns java.lang.Boolean for javascript calls that return boolean values
				Boolean ajaxComplete = (Boolean)jsx.executeScript("return ( ( window.jQuery != undefined ) && ( jQuery.active == 0 ) )");
				if( ajaxComplete.booleanValue() )
					return;
				BaseSeleniumTest.sleep(1000);
				stop = System.currentTimeMillis();
			} while ( stop - start < timeout );
			
			log.warn( "JQuery ajax calls still active after " + timeout / 1000 + " seconds." );
		}
	}
	
	public static void turnOn()
	{
		PropertyHelper.setJQueryWaitForAjax( "true" );
	}
	
	public static void turnOff()
	{
		PropertyHelper.setJQueryWaitForAjax( "false" );
	}

}
