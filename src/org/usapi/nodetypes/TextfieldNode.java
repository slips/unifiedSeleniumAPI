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


public class TextfieldNode extends AbstractNode 
{
	
	public TextfieldNode(WebDriver webDriver, 
			String type, 
			String nodeName, 
			String locator)
	{
		super(webDriver, type, nodeName, locator);
	}

	@Override
	public void type ( String value ) throws USAPIException
	{
		log("Entering <" + value + "> in " + getType() + " <" + getNodeName() + "> [locator: " + getLocator() + "]", LOGLEVEL_INFO);
		executeMethod( this, "nativeClear", new Object[] {}, getLocator() );
		executeMethod( this, "nativeSendKeys", new Object[] { value }, getLocator() );
	}
	
	/****************************************************************************/
	// Following methods are not intended to be called directly.  Invocation is 
	// expected to occur through AbstractNode:executeMethod, which provides
	// for a checked/safe execution environment, and convenience features in
	// case of failure.
	/****************************************************************************/
	public void nativeClear()
	{
		WebElement webElement = findElement( getLocator() );
		webElement.clear();
	}
	
	public void nativeSendKeys( String value )
	{
		WebElement webElement = findElement( getLocator() );
		webElement.sendKeys( new CharSequence[] { value } );
	}

}
