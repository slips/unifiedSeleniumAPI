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

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;


public class SelectDummy extends Select
{
	private RemoteWebDriverDummy wd = null;
	private ArrayList<WebElement> options = new ArrayList<WebElement>();
	private WebElement option = null;
	
	public SelectDummy( RemoteWebDriverDummy wd, WebElementDummy we )
	{
		super( we );
		this.wd = wd;
		this.option = new WebElementDummy( wd, "option" );
		options.add( option );
	}
	
	public WebElement getFirstSelectedOption()
	{
		wd.getCallbackNode().setSelectMethodCalled("getFirstSelectedOption");
		return option;
	}
	
	public List<WebElement> getOptions()
	{
		wd.getCallbackNode().setSelectMethodCalled("getOptions");
		return options;
	}
	
	public void selectByVisibleText(String txt)
	{
		wd.getCallbackNode().setSelectMethodCalled("isMultiple");
		wd.getCallbackNode().setSelectMethodCalled("deselectAll");
		wd.getCallbackNode().setSelectMethodCalled("selectByVisibleText");
	}
	
	public void selectByValue( String value )
	{
		wd.getCallbackNode().setSelectMethodCalled("selectByValue");
	}
	
	public boolean isMultiple()
	{
		wd.getCallbackNode().setSelectMethodCalled("isMultiple");
		return true;
	}
	
	public void deselectAll()
	{
		wd.getCallbackNode().setSelectMethodCalled("deselectAll");
	}
	

}
