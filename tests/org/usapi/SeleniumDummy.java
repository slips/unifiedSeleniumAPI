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

import com.thoughtworks.selenium.DefaultSelenium;
import org.usapi.nodetypes.AbstractNodeTest;;

/**
 * 
 * @author slips
 *
 * Dummy selenium to use for unit testing.  Overrides methods used by code under test.
 * Provides call back functionality to validate correct execution of function calls by
 * test client.
 */
public class SeleniumDummy extends DefaultSelenium {

	AbstractNodeTest node = null;
	
	public SeleniumDummy() { super ("noServer", 1234, "noBrowser", "noURL"); }
	
	public void setCallbackNode(AbstractNodeTest node)
	{
		this.node = node;
	}
	
	public void click ( String locator )
	{
		node.setSeleniumMethodCalled("click");
	}
	
	public void ctrlClick ( String locator )
	{
		node.setSeleniumMethodCalled("ctrlClick");
	}

	public void check ( String locator )
	{
		node.setSeleniumMethodCalled("check");
	}
	
	public void focus ( String locator )
	{
		node.setSeleniumMethodCalled("focus");
	}
	
	public String getValue( String locator )
	{
		node.setSeleniumMethodCalled("getValue");
		return "getValue()";
	}
	
	public void selectWindow ( String locator )
	{
		node.setSeleniumMethodCalled("selectWindow");
	}
	
	public void windowFocus ()
	{
		node.setSeleniumMethodCalled("windowFocus");
	}

	public void uncheck ( String locator )
	{
		node.setSeleniumMethodCalled("uncheck");
	}
	
	public void select ( String locator, String selection )
	{
		node.setSeleniumMethodCalled("select");
	}
	
	public void addSelection(String locator, String selection)
	{
		node.setSeleniumMethodCalled("addSelection");
	}
	
	public void type ( String locator, String value )
	{
		node.setSeleniumMethodCalled("type");
	}
	
	public void altKeyDown() 
	{ 
		node.setSeleniumMethodCalled("altKeyDown"); 
	}
	
	public void altKeyUp() 
	{ 
		node.setSeleniumMethodCalled("altKeyUp"); 
	}

	public void controlKeyDown() 
	{ 
		node.setSeleniumMethodCalled("controlKeyDown"); 
	}
	
	public void controlKeyUp() 
	{ 
		node.setSeleniumMethodCalled("controlKeyUp"); 
	}

	public void shiftKeyDown() 
	{ 
		node.setSeleniumMethodCalled("shiftKeyDown"); 
	}
	
	public void shiftKeyUp() 
	{ 
		node.setSeleniumMethodCalled("shiftKeyUp"); 
	}

	public void fireEvent( String locator, String eventName ) 
	{
		node.setSeleniumMethodCalled("fireEvent(" + eventName + ")");
	}
	
	public void contextMenu( String locator )
	{
		node.setSeleniumMethodCalled("contextMenu");
	}

	public String[] getSelectOptions( String locator ) 
	{ 
		node.setSeleniumMethodCalled("getSelectOptions");
		return new String [] {""}; 
	}
	
	public void mouseDown( String locator )
	{
		node.setSeleniumMethodCalled("mouseDown");
	}
	
	public void mouseUp( String locator )
	{
		node.setSeleniumMethodCalled("mouseUp");
	}

	public void close()
	{
		node.setSeleniumMethodCalled("close");
	}

	// no-op override so no exception gets thrown when those are called as part of other/above operation
	public String [] getAllWindowNames() { return new String [] {""}; }
	public String [] getAllWindowTitles() { return new String [] {""}; }
	public String [] getAllWindowIds() { return new String [] {""}; }
	public String getEval( String js ) { return ""; };
	public void openWindow( String url, String windowId ) {}
	public String captureScreenshotToString() { return ""; }
	public String getHtmlSource() { return ""; }
	public boolean isElementPresent( String locator ) { return true; }
	
}
