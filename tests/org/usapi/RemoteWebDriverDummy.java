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

import java.util.HashSet;
import java.util.Set;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keyboard;
import org.openqa.selenium.Mouse;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import org.usapi.nodetypes.AbstractNodeTest;

public class RemoteWebDriverDummy extends RemoteWebDriver implements TakesScreenshot
{
	private Keyboard k = new KeyboardDummy( this );
	private Mouse m = new MouseDummy( this );
	private WebElement e = new WebElementDummy(this, "select");
	
	public Keyboard getKeyboard()
	{
		return k;
	}
	
	public Mouse getMouse()
	{
		return m;
	}
	
	AbstractNodeTest node = null;
	
	
	public void setCallbackNode(AbstractNodeTest node)
	{
		this.node = node;
	}
	
	public AbstractNodeTest getCallbackNode()
	{
		return this.node;
	}
	
	public WebElement findElement( By by )
	{
		return e;
	}
	
	public void quit() {}
	
	public Set<String> getWindowHandles()
	{
		HashSet<String> set = new HashSet<String>();
		set.add("windowHandle1");
		return set;
	}
	
	public WebDriver.TargetLocator switchTo()
	{
		AbstractNodeTest node = getCallbackNode();
		if( node != null )
		{
			node.setWebDriverMethodCalled("switchTo");
		}
		
		return new RemoteTargetLocatorDummy();
	}
	
	public <X> X getScreenshotAs(OutputType<X> target)
	{
		return (X)"screenshot this is not ... just a string.  Not even Base64.  But it's a string and that's all we need.  Hallelujah.";
	}
	
	public String getPageSource()
	{
		return "<html>Trick or Treat.</html>";
	}
	
	public void close()
	{
		getCallbackNode().setWebDriverMethodCalled("close");
	}
	
	class RemoteTargetLocatorDummy extends RemoteTargetLocator
	{
		public WebDriver window( String windowName )
		{
			getCallbackNode().setWebDriverMethodCalled("window");
			return RemoteWebDriverDummy.this;
		}
		
		public Alert alert()
		{
			return new SeleniumAlertDummy();
		}
	}
	
	class SeleniumAlertDummy implements org.openqa.selenium.Alert
	{
		public void accept() {}
		public void dismiss() {}
		public String getText() { return "alertText"; }
		public void sendKeys(String s) {}
	}

}
