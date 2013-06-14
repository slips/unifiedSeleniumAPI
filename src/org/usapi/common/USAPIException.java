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

package org.usapi.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.usapi.PropertyHelper;


public class USAPIException extends Exception 
{
	private WebDriver webDriver = null;
    private Log log = LogFactory.getLog(this.getClass());
    
	public USAPIException( String msg, WebDriver webDriver)
	{
		super( msg );
		this.webDriver = webDriver;
		captureFailureArtifacts();
	}
	
	
    protected String captureFailureArtifacts()
    {
        String fileSeparator = System.getProperty("file.separator");
        File dir = new File ( PropertyHelper.getFailureCaptureDir() );

        if( !dir.exists() )
        {
            dir.mkdir();
        }
        
        // Filename is the name of the host name, os, browser, currently executing class, 
        // plus the current method name.  We need host name, os and browser as tests may 
        // execute distributed and screen shots stored in a central location.  Plus, using
        // this naming convention makes it self-documenting what platform a failure occurred on,
        // in case the image is communicated.
        // Using this naming convention ensures that we don't fill up the screenCapture directory
        // over time.  Disadvantage is that history for a given failure is only 1-deep, but
        // since failures should be investigated immediately, this is preferable to having 
        // disk space being eaten silently over time.
        String filename = getFailureArtifactFileName();
        if( webDriver instanceof RemoteWebDriver)
        {
        	captureScreenshot( dir + fileSeparator + filename + ".png" );
        }
        captureHtmlSource( dir + fileSeparator + filename + ".html" );
        
        return filename;
    }
    
    private void captureScreenshot( String imgAbsolutePath )
    {
        log.info( "Capturing screenshot at " + imgAbsolutePath );
        String base64Screenshot = ((TakesScreenshot)webDriver).getScreenshotAs(OutputType.BASE64);
    	try
    	{
    		Base64.decodeToFile(base64Screenshot, imgAbsolutePath);
    	}
    	catch( IOException ioe )
    	{
    		log.error("Exception writing screenshot to " + imgAbsolutePath, ioe );
    	}
    }
    
    private void captureHtmlSource( String htmlSourceAbsolutePath )
    {
        log.info( "Capturing html source at " + htmlSourceAbsolutePath );
        saveStringToFile( webDriver.getPageSource(), htmlSourceAbsolutePath);    	
    }
    
    private void saveStringToFile( String string, String absoluteFilePath )
    {
        try {
	        BufferedWriter out = new BufferedWriter(new FileWriter(absoluteFilePath));
	        out.write(string);
	        out.close();
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
	 }


    
    private String getFailureArtifactFileName()
    {
    	String hostname = "";
    	try
    	{
	    	InetAddress addr = InetAddress.getLocalHost();
	    	hostname = addr.getHostName() + "_";
    	}
    	catch( UnknownHostException e )
    	{
    		// ignore, default host name to empty string
    	}
    	
    	String os = System.getProperty( "os.name" );
    	os = os.replace(' ', '_');
    	
    	String browser = PropertyHelper.getSeleniumBrowserCommand();
    	
    	browser = browser.charAt(0) == '*' ? browser.substring(1) : browser; 
    	
    	return hostname + os + "_" + browser + "_" + getCaller().getClassName() + "_" + getCaller().getMethodName(); 
    }
    
    private StackTraceElement getCaller()
    {
    	StackTraceElement ste[] = Thread.currentThread().getStackTrace();
    	StackTraceElement candidate = null;
    	// if browser (chrome?) hangs stacktrace can seemingly not have expected contents (invoke0), so
    	// providing default value
    	StackTraceElement caller = new StackTraceElement( "UNKNOWN_CLASS", "UNKNOWN_METHOD", "UNKNOWN_FILE", -1);
    	for( int ndx = 0; ndx < ste.length; ndx++ )
    	{
    		candidate = ste[ ndx ];
    		if( candidate.getClassName().equals("sun.reflect.NativeMethodAccessorImpl")
    				&& candidate.getMethodName().equals("invoke0"))
    		{
    			// no worries about index out of bounds here, the match is guaranteed to 
    			// happen anywhere between index 5-8,  total stack depth ~20
    			caller = ste[ ndx - 1 ];
    			break;
    		}
    	}
    	return caller;
    }
    
    	
}
