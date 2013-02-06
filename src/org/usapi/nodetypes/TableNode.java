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

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class TableNode extends AbstractNode 
{
	
	public TableNode(WebDriver webDriver, 
			String type, 
			String nodeName, 
			String locator) 
	{
		super(webDriver, type, nodeName, locator);
	}
	
	public int getRowCount()
	{
		return getWebElement().findElements(By.tagName("tr")).size();
	}
	
	public WebElement getRowElement( int rowIndex )
	{
		return (WebElement)getWebElement().findElements(By.tagName("tr")).get( rowIndex );
	}
	
	public String getRowText( int rowIndex )
	{
		return getRowElement( rowIndex ).getText();
	}
	
	public WebElement getCellElement( int rowIndex, int columnIndex )
	{
		return (WebElement)getWebElement().findElements(By.tagName("tr")).get( rowIndex ).findElements(By.tagName("td")).get( columnIndex );
	}
	
	public String getCellText( int rowIndex, int columnIndex )
	{
		return getCellElement( rowIndex, columnIndex ).getText();
	}
	
	
	
}
