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

import java.util.Set;

import org.openqa.selenium.WebDriver;
import org.usapi.common.USAPIException;



//If the pop-up window is created by window.createPopup() then you will not be able to 
//switch to it - such windows are not supported by Selenium 2. There's some progress in 
//implementing it - see http://code.google.com/p/selenium/issues/detail?id=27

public class WindowNode extends AbstractNode 
{
	
	public WindowNode(WebDriver webDriver,
			String type, 
			String nodeName, 
			String locator) 
	{
		super(webDriver, type, nodeName, locator);
	}
	
	/**
	 * Override to default implementation on AbstractNode.
	 */
	@Override
	public void focus() throws USAPIException
	{
//		log("Window handles: " + getWebDriver().getWindowHandles(), LOGLEVEL_INFO );
		log("Switching focus to " + getType() + " <" + getNodeName() + "> [locator: " + getLocator() + "]", LOGLEVEL_INFO);
		Set<String> windowHandles = getWebDriver().getWindowHandles();
		// in case a spawned child window is gone, we want to switch back to the primary/main window.  There will be only one
		// handle
		if( windowHandles.size() == 1 )
		{
			getWebDriver().switchTo().window(windowHandles.iterator().next());
		}
		// more than one window handle indicates we have a spawned child window.  Assuming there is only going to be 
		// one open child window at a time, no other instances of the currently used browser open either.  Switch to it.
		else
		{
			String windowHandle = getWebDriver().getWindowHandle();
			for( String handle : windowHandles )
			{
				if( !handle.equals(windowHandle))
				{
					getWebDriver().switchTo().window(handle);
				}
			}
		}
	}
	
	public void close()
	{
		log("Closing " + getType() + " <" + getNodeName() + "> [locator: " + getLocator() + "]", LOGLEVEL_INFO);
		getWebDriver().close();
	}

}
