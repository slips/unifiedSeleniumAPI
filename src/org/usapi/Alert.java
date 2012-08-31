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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;

public class Alert
{
	private WebDriver webDriver = null;
	private Log log = LogFactory.getLog(DOMNodeFactory.class);
	
	// no instantiation without WebDriver argument
	private Alert() {}
	
	public Alert( WebDriver webDriver )
	{
		this.webDriver = webDriver;
	}
	
	
	private org.openqa.selenium.Alert getSeleniumAlert( long timeout )
	{
		org.openqa.selenium.Alert seleniumAlert = null;
		
		long start = System.currentTimeMillis();
		long stop = 0;
		
		while( ( stop - start < timeout ) && ( seleniumAlert == null ) )
		{
			try
			{
				seleniumAlert = webDriver.switchTo().alert();
			}
			catch( Exception e )
			{
				// Alert does not exist yet, so wait some, and try again,
				// until it either exists or we hit the timeout.
			}
			
	        try
	        {
	            Thread.sleep(500);
	        }
	        catch (Exception e)
	        {
	            // Just continue, hope for the best.
	        }
	        
	        log.debug("isNull: " + ( seleniumAlert == null ) + ", stop - start: " + ( stop - start ) );
	        stop = System.currentTimeMillis();			
		}
		
		if( seleniumAlert == null )
		{
			log.warn("Alert did not come up within " + timeout + " milliseconds.");
		}
		
		return seleniumAlert;
	}
	
	/**
	 * Click the OK button (or whatever button indicates acceptance)
	 */
	public void accept() 
	{
		org.openqa.selenium.Alert seleniumAlert = getSeleniumAlert( PropertyHelper.getTimeout() );
		if( seleniumAlert != null )
		{
			log.info("Accepting alert with text <" + seleniumAlert.getText() + ">." );
			seleniumAlert.accept();
		}
	}

	/**
	 * Click the Cancel button (or whatever button indicates non-acceptance)
	 */
	public void dismiss() 
	{
		org.openqa.selenium.Alert seleniumAlert = getSeleniumAlert( PropertyHelper.getTimeout() );
		if( seleniumAlert != null )
		{
			log.info("Dismissing alert with text <" + seleniumAlert.getText() + ">." );
			seleniumAlert.dismiss();
		}
	}
	
	/**
	 * @return the text displayed by this dialog
	 */
	public String getText() 
	{
		org.openqa.selenium.Alert seleniumAlert = getSeleniumAlert( PropertyHelper.getTimeout() );
		String txt = null;
		if( seleniumAlert != null )
		{
			log.info("Getting text from alert: <" + seleniumAlert.getText() + ">." );
			txt = seleniumAlert.getText();
		}
		return txt;
	}

	/**
	 * Enter a value into the input field.
	 * @param value to enter
	 */
	public void sendKeys(String s) 
	{
		org.openqa.selenium.Alert seleniumAlert = getSeleniumAlert( PropertyHelper.getTimeout() );
		if( seleniumAlert != null )
		{
			log.info("Sending input value <" + s + "> to alert with text <" + seleniumAlert.getText() + ">." );
			seleniumAlert.sendKeys( s );
		}
	}
	
	/**
	 * Check if the dialog exists, for up &lt;timeout&gt; milliseconds.  Timeout is system-wide
	 * timeout, specified by selenium.timeout property, default 30000 ms.
	 * @return 
	 */
	public boolean exists()
	{
		org.openqa.selenium.Alert seleniumAlert = getSeleniumAlert( PropertyHelper.getTimeout() );
		return seleniumAlert != null;
	}
	

	/**
	 * Check for up to &lt;timout&gt; milliseconds whether a dialog exists.  Use this method if a dialog
	 * may not be in existence immediately following the UI interaction (button click, etc) that is
	 * expected to trigger it.
	 * @param maximum time to wait for dialog to come up, milliseconds
	 * @return
	 */
	public boolean exists( long timeout )
	{
		org.openqa.selenium.Alert seleniumAlert = getSeleniumAlert( timeout );
		return seleniumAlert != null;
	}

}
