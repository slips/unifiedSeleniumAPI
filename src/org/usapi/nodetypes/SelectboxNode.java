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

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.usapi.common.USAPIException;


public class SelectboxNode extends AbstractNode 
{
	
	public SelectboxNode(WebDriver webDriver,
			String type, 
			String nodeName, 
			String locator) 
	{
		super(webDriver, type, nodeName, locator);
	}

	@Override
	public void select ( String selection ) throws USAPIException
	{
		log("Selecting <" + selection + "> in " + getType() + " <" + getNodeName() + "> [locator: " + getLocator() + "]", LOGLEVEL_INFO);
		executeMethod( this, "nativeSelect", new Object [] { selection }, getLocator() );
	}
	
	@Override
	public void addSelection( String selection ) throws USAPIException
	{
		log("Adding selection <" + selection + "> in " + getType() + " <" + getNodeName() + "> [locator: " + getLocator() + "]", LOGLEVEL_INFO);
		executeMethod( this, "nativeAddSelection", new Object [] { selection }, getLocator() );
	}
	
	@Override
	public String getSelectedValue() throws USAPIException
	{
		log("Getting selected (displayed) value from " + getType() + " <" + getNodeName() + "> [locator: " + getLocator() + "]", LOGLEVEL_INFO);
		return (String)executeMethod( this, "nativeGetSelectedValue", new Object [] {}, getLocator() );
	}
	
	@Override 
	public String[] getSelectOptions() throws USAPIException
	{
		log("Getting all options from " + getType() + " <" + getNodeName() + "> [locator: " + getLocator() + "]", LOGLEVEL_INFO);
		return (String[])executeMethod( this, "nativeGetSelectOptions", new Object [] {}, getLocator() );
	}
	
	/****************************************************************************/
	// native* methods are not intended to be called directly.  Invocation is 
	// expected to occur through AbstractNode:executeMethod, which provides
	// for a checked/safe execution environment, and convenience features in
	// case of failure.
	/****************************************************************************/
	public void nativeSelect( String selection ) throws USAPIException
	{
		Select selectBox = new Select( findElement( getLocator() ) );
		if( selectBox.isMultiple() )
		{
			selectBox.deselectAll();
		}
		select( selectBox, selection );
	}
	
	public void nativeAddSelection( String selection ) throws USAPIException
	{
		Select selectBox = new Select( findElement( getLocator() ) );
		select( selectBox, selection );
	}

	public String nativeGetSelectedValue () throws USAPIException
	{
		Select select = new Select( findElement( getLocator() ) );
		return select.getFirstSelectedOption().getText();
	}
	
	public String [] nativeGetSelectOptions() throws USAPIException
	{
		List<WebElement> optionElements = new Select( findElement( getLocator() )).getOptions();
		String [] options = new String [ optionElements.size() ];
		int optionNdx = 0;
		for (WebElement option : optionElements )
		{
			options[ optionNdx++ ] = option.getText();
		}
		return options;		
	}
	
	private void select( Select select, String selection )
	{
		// maintain the functionality from selenium v1
		// http://release.seleniumhq.org/selenium-remote-control/0.9.2/doc/java/index.html?com/thoughtworks/selenium/Selenium.html
		// where selection by index/value/displayString was based on argument format to 
		if( selection.toLowerCase().startsWith("index=") )
		{
			int ndx = Integer.parseInt( selection.split("=")[1]);
			select.selectByIndex(ndx);
		}
		else if( selection.toLowerCase().startsWith("value=") )
		{
			select.selectByValue( selection.split("=")[1]);
		}
		else
		{
			select.selectByVisibleText( selection );
		}		
	}
	
}
