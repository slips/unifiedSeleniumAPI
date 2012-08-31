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

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.usapi.common.USAPIException;


public class CheckboxNode extends AbstractNode {

	public CheckboxNode(WebDriver webDriver,
			String type, 
			String nodeName, 
			String locator)
	{
		super(webDriver, type, nodeName, locator);
	}

	@Override
	public void check()  throws USAPIException
	{
		if( !isChecked())
		{
			log("Checking " + getType() + " <" + getNodeName() + "> [locator: " + getLocator() + "]", LOGLEVEL_INFO);
			executeMethod( this, "nativeClick", new Object [] {}, getLocator () );
		}
		else
		{
			log("Nothing to do: " + getType() + " <" + getNodeName() + "> [locator: " + getLocator() 
					+ "] is already checked.", LOGLEVEL_INFO);
		}
	}

	@Override
	public void uncheck() throws USAPIException
	{
		if( isChecked())
		{
			log("Unchecking " + getType() + " <" + getNodeName() + "> [locator: " + getLocator() + "]", LOGLEVEL_INFO);
			executeMethod( this, "nativeClick", new Object [] {}, getLocator () );
		}
		else
		{
			log("Nothing to do: " + getType() + " <" + getNodeName() + "> [locator: " + getLocator() 
					+ "] is already unchecked.", LOGLEVEL_INFO);
		}	
	}
	
	private boolean isChecked() throws USAPIException
	{
	    return Boolean.parseBoolean((String)(executeMethod( this, "nativeIsChecked", new Object [] {}, getLocator() ) ) );
	}
	
	public String nativeIsChecked()
	{
		WebElement webElement = findElement( getLocator() );
		return webElement.getAttribute("checked");		
	}

	

}
