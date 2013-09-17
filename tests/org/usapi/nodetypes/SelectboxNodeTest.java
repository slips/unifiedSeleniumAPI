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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebElement;

import org.usapi.RemoteWebDriverDummy;
import org.usapi.SelectDummy;
import org.usapi.WebElementDummy;
import org.usapi.common.USAPIException;


public class SelectboxNodeTest extends AbstractNodeTest {

	private String TYPE = "selectbox";
	private String NODE_NAME = "s1";
	
	@Before
	public void setUp() throws Exception {
		super.webDriver.setCallbackNode(this);
		nodeUnderTest = new OverriddenSelectboxNode( super.webDriver, TYPE, NODE_NAME, AbstractNodeTest.LOCATOR);
	}

	@After
	public void tearDown() throws Exception {
		nodeUnderTest = null;
	}

	@Test
	public void testNodeInstance() {
		assertNotNull(nodeUnderTest.getWebDriver());
		assertEquals(nodeUnderTest.getWebDriver(), super.webDriver);
		assertNotNull(nodeUnderTest.getType());
		assertEquals(nodeUnderTest.getType(), TYPE);
		assertNotNull(nodeUnderTest.getNodeName());
		assertEquals(nodeUnderTest.getNodeName(), NODE_NAME);
		assertNotNull(nodeUnderTest.getLocator());
		assertEquals(nodeUnderTest.getLocator(), AbstractNodeTest.LOCATOR);
	}	

	@Test
	public void testSelect() throws USAPIException
	{
		nodeUnderTest.select("value");
		assertEquals("getAttribute(multiple),isMultiple,deselectAll,selectByVisibleText", super.getCompositeMethodCalled());
	}
	
	@Test
	public void testAddSelection() throws USAPIException
	{
		nodeUnderTest.addSelection("value");
		assertEquals("getAttribute(multiple),isMultiple,deselectAll,selectByVisibleText", super.getCompositeMethodCalled());
	}
	
	@Test
	public void testGetSelectedValue() throws USAPIException
	{
		nodeUnderTest.getSelectedValue();
		// getAttribute(multiple) is from invoking constructor of org.openqa.selenium.support.ui.Select
		// which is parent of SelectDummy
		assertEquals("getAttribute(multiple),getFirstSelectedOption,getText", getCompositeMethodCalled());
	}
	
	@Test
	public void testGetSelectOptions() throws USAPIException
	{
		nodeUnderTest.getSelectOptions();
		assertEquals("getOptions", getSelectMethodCalled());
	}
	
	
	class OverriddenSelectboxNode extends SelectboxNode
	{
		public OverriddenSelectboxNode( RemoteWebDriverDummy wd,
				String type, 
				String nodeName, 
				String locator)
		{
			super( wd, type, nodeName, locator);
		}
		
		
		public void nativeSelect( String visibleText ) throws USAPIException
		{
			SelectDummy select = new SelectDummy ( (RemoteWebDriverDummy)getWebDriver(), (WebElementDummy)findElement( getLocator() ) );
			select.selectByVisibleText( visibleText );		
		}
		
		public void nativeAddSelection( String visibleText ) throws USAPIException
		{
			SelectDummy select = new SelectDummy( (RemoteWebDriverDummy)getWebDriver(), (WebElementDummy)findElement( getLocator() ) );
			select.selectByVisibleText( visibleText );
		}
		
		public void nativeSelectByValue( String value ) throws USAPIException
		{
			SelectDummy select = new SelectDummy( (RemoteWebDriverDummy)getWebDriver(), (WebElementDummy)findElement( getLocator() ) );
			select.selectByValue(value);
		}
		
		public String nativeGetSelectedValue () throws USAPIException
		{
			SelectDummy select = new SelectDummy( (RemoteWebDriverDummy)getWebDriver(), (WebElementDummy)findElement( getLocator() ) );
			return select.getFirstSelectedOption().getText();
		}
		
		public String [] nativeGetSelectOptions() throws USAPIException
		{
			List<WebElement> optionElements = new SelectDummy( (RemoteWebDriverDummy)getWebDriver(), (WebElementDummy)findElement( getLocator() ) ).getOptions();
			String [] options = new String [ optionElements.size() ];
			int optionNdx = 0;
			for (WebElement option : optionElements )
			{
				options[ optionNdx++ ] = option.getText();
			}
			return options;		
		}		
		
	}


}

