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

import org.openqa.selenium.remote.RemoteWebElement;

public class WebElementDummy extends RemoteWebElement
{
	private RemoteWebDriverDummy wd = null;
	private String tagName = null;
	private static String checked = "true";
	public WebElementDummy(RemoteWebDriverDummy wd, String tagName )
	{
		this.wd = wd;
		this.tagName = tagName;
	}
	
	public void click()
	{
		wd.getCallbackNode().setWebElementMethodCalled("click");
	}
	
	public String getAttribute( String name )
	{
		wd.getCallbackNode().setWebElementMethodCalled("getAttribute(" + name + ")");
		String value = "attributeValue";
		
		// special case for 'checked' ... flip-flop the return value
		if( name.equals("checked"))
		{
			if( checked.equals("false")) checked = "true";
			else if( checked.equals("true")) checked = "false";
			value = checked;
			
		}
		return value;
	}
	
	public String getText()
	{
		wd.getCallbackNode().setWebElementMethodCalled("getText");
		return "text";
	}
	
	public boolean isDisplayed()
	{
		wd.getCallbackNode().setWebElementMethodCalled("isDisplayed");
		return false;
	}
	
	public boolean isEnabled()
	{
		wd.getCallbackNode().setWebElementMethodCalled("isEnabled");
		return false;
	}
	
	public boolean isSelected()
	{
		wd.getCallbackNode().setWebElementMethodCalled("isSelected");
		return false;
	}
	
	public void sendKeys(CharSequence... keysToSend)
	{
		wd.getCallbackNode().setWebElementMethodCalled("sendKeys");
	}
	
	public void sendKeys(String keysToSend)
	{
		wd.getCallbackNode().setWebElementMethodCalled("sendKeys");
	}
	
	public void clear()
	{
		wd.getCallbackNode().setWebDriverMethodCalled("clear");
	}
	
	public String getTagName()
	{
		return tagName;
	}
}
