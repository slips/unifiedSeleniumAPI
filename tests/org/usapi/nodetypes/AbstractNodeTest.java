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
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.remote.RemoteWebDriver;

import org.usapi.RemoteWebDriverDummy;
import org.usapi.SeleniumDummy;
import org.usapi.common.USAPIException;
import com.thoughtworks.selenium.Selenium;

public class AbstractNodeTest {

	protected final RemoteWebDriverDummy	webDriver 	= new RemoteWebDriverDummy();
	protected final SeleniumDummy 			selenium 	= new SeleniumDummy();
	protected final static String 			TYPE 		= "abstractNode";
	protected final static String 			NODE_NAME 	= "abstractNodeName";
	protected final static String 			LOCATOR 	= "/nodeLocator";
	
	protected AbstractNode 	nodeUnderTest	= null;
	
	private class NotSoAbstractNode extends AbstractNode
	{
		protected NotSoAbstractNode(RemoteWebDriver wd, String type, String nodeName, String locator)
		{
			super(wd, type, nodeName, locator);
		}
	}
	
	@Before
	public void setUp() throws Exception {
		selenium.setCallbackNode(this);
		webDriver.setCallbackNode(this);
		nodeUnderTest = new NotSoAbstractNode(webDriver, TYPE, NODE_NAME, LOCATOR);
		
		_seleniumMethod = null;
		_webDriverMethod = null;
		_webElementMethod = null;
		_selectMethod = null;
		_mouseMethod = null;
		_keyboardMethod = null;
		_compositeMethod = null;		
	}

	@After
	public void tearDown() throws Exception {
		nodeUnderTest = null;
	}
	
	@Test
	public void testNodeInstance()
	{
		assertNotNull(nodeUnderTest.getWebDriver());
		assertEquals(nodeUnderTest.getWebDriver(), webDriver);
		assertNotNull(nodeUnderTest.getType());
		assertEquals(nodeUnderTest.getType(), TYPE);
		assertNotNull(nodeUnderTest.getNodeName());
		assertEquals(nodeUnderTest.getNodeName(), NODE_NAME);
		assertNotNull(nodeUnderTest.getLocator());
		assertEquals(nodeUnderTest.getLocator(), LOCATOR);
	}

	@Test
	public void testCheck() throws USAPIException
	{
		nodeUnderTest.check();
	}
	
	@Test
	public void testControlKeyDown() throws USAPIException
	{
		nodeUnderTest.controlKeyDown();
		assertEquals("pressKey-CONTROL", getKeyboardMethodCalled());
	}
	
	@Test
	public void testControlKeyUp() throws USAPIException
	{
		nodeUnderTest.controlKeyUp();
		assertEquals("releaseKey-CONTROL", getKeyboardMethodCalled());
	}

	@Test
	public void testClick() throws USAPIException
	{
		nodeUnderTest.click();
		assertEquals("click", getWebElementMethodCalled());
	}
	
	@Test
	public void testAltClick() throws USAPIException
	{
		nodeUnderTest.altClick();
		assertEquals("pressKey-ALT,click,releaseKey-ALT", getCompositeMethodCalled());
	}

	@Test
	public void testCtrlClick() throws USAPIException
	{
		nodeUnderTest.ctrlClick();
		assertEquals("pressKey-CONTROL,click,releaseKey-CONTROL", getCompositeMethodCalled());
	}

	@Test
	public void testShiftClick() throws USAPIException
	{
		nodeUnderTest.shiftClick();
		assertEquals("pressKey-SHIFT,click,releaseKey-SHIFT", getCompositeMethodCalled());
	}
	
	@Test
	public void testContextMenu() throws USAPIException
	{
		nodeUnderTest.contextMenu();
		assertEquals("contextClick", getMouseMethodCalled());
	}

	// In an effort to remove selenium v1 completely, the remaining selenium v1 methods
	// obtain their selenium v1 instance directly from SeleniumFactory.  Therefore the 
	// .focus call will fail 
	// @Test
	public void testFocus() throws USAPIException
	{
		nodeUnderTest.focus();
		assertEquals("focus", getSeleniumMethodCalled());
	}
	
	@Test
	public void testGetValue() throws USAPIException
	{
		nodeUnderTest.getValue();
	}	

	@Test
	public void testSelect() throws USAPIException
	{
		nodeUnderTest.select("");
	}
	
	@Test
	public void testAddSelection() throws USAPIException
	{
		nodeUnderTest.addSelection("");
	}

	@Test
	public void testType() throws USAPIException
	{
		nodeUnderTest.type("");
	}

	@Test
	public void testUncheck() throws USAPIException
	{
		nodeUnderTest.uncheck();
	}
	
	@Test
	public void testMouseDown() throws USAPIException
	{
		nodeUnderTest.mouseDown();
	}
	
	@Test
	public void testMouseUp() throws USAPIException
	{
		nodeUnderTest.mouseUp();
	}
	
	@Test
	public void testMouseDownUp() throws USAPIException
	{
		nodeUnderTest.mouseDownUp();
	}
	
	@Test
	public void testCtrlMouseDownUp() throws USAPIException
	{
		nodeUnderTest.ctrlMouseDownUp();
	}
	
	@Test
	public void testAltMouseDownUp() throws USAPIException
	{
		nodeUnderTest.altMouseDownUp();
	}
	
	@Test
	public void testShiftMouseDownUp() throws USAPIException
	{
		nodeUnderTest.shiftMouseDownUp();
	}
	
	@Test
	public void testGetSelectOptions() throws USAPIException
	{
		nodeUnderTest.getSelectOptions();
	}
	
	@Test
	public void testGetSelectedValue() throws USAPIException
	{
		nodeUnderTest.getSelectedValue();
	}
	
	@Test
	public void testIsEnabled() throws USAPIException
	{
		nodeUnderTest.isEnabled();
	}
	
	@Test
	public void testIsVisible() throws USAPIException
	{
		nodeUnderTest.isVisible();
	}
	
	@Test
	public void testMouseOver() throws USAPIException
	{
		nodeUnderTest.mouseOver();
	}
	
	@Test
	public void testSleep() throws USAPIException
	{
		long sleepTime = 1234;
		long start = System.currentTimeMillis();
		nodeUnderTest.sleep(sleepTime);
		long stop = System.currentTimeMillis();
		assertTrue( "Snow White got woken early (sleep() did not sleep for specified length)", stop - start >= sleepTime);
	}
	
	
	/**
	 * Helper for use with SeleniumDummy.  Allows test to verify expected method on selenium was called
	 * @param methodName Name of the method that was called
	 */
	private String _seleniumMethod = null;
	private String _webDriverMethod = null;
	private String _webElementMethod = null;
	private String _selectMethod = null;
	private String _mouseMethod = null;
	private String _keyboardMethod = null;
	private String _compositeMethod = null;
	public void setSeleniumMethodCalled(String methodName)
	{
		// support cases where more than one method are called, example: WindowNode::focus()
		setCompositeMethodCalled(methodName);
		_seleniumMethod = ( _seleniumMethod == null ? methodName : _seleniumMethod.concat("," + methodName));  
	}
	public void setWebDriverMethodCalled( String methodName )
	{
		// support cases where more than one method are called, example: WindowNode::focus()
		setCompositeMethodCalled(methodName);
		_webDriverMethod = ( _webDriverMethod == null ? methodName : _webDriverMethod.concat("," + methodName));  
	}
	public void setWebElementMethodCalled( String methodName )
	{
		setCompositeMethodCalled(methodName);
		_webElementMethod = ( _webElementMethod == null ? methodName : _webElementMethod.concat("," + methodName));  
	}
	public void setSelectMethodCalled(String methodName)
	{
		setCompositeMethodCalled(methodName);
		_selectMethod = ( _selectMethod == null ? methodName : _selectMethod.concat("," + methodName));  
	}
	public void setMouseMethodCalled(String methodName)
	{
		setCompositeMethodCalled(methodName);
		_mouseMethod = ( _mouseMethod == null ? methodName : _mouseMethod.concat("," + methodName));  
	}
	public void setKeyboardMethodCalled(String methodName)
	{
		setCompositeMethodCalled(methodName);
		_keyboardMethod = ( _keyboardMethod == null ? methodName : _keyboardMethod.concat("," + methodName));  
	}
	public void setCompositeMethodCalled(String methodName)
	{
		_compositeMethod = ( _compositeMethod == null ? methodName : _compositeMethod.concat("," + methodName));  
	}
	public String getSeleniumMethodCalled()
	{
		return _seleniumMethod;
	}
	public String getWebDriverMethodCalled()
	{
		return _webDriverMethod;
	}
	public String getWebElementMethodCalled()
	{
		return _webElementMethod;
	}
	public String getSelectMethodCalled()
	{
		return _selectMethod;
	}
	public String getMouseMethodCalled()
	{
		return _mouseMethod;
	}
	public String getKeyboardMethodCalled()
	{
		return _keyboardMethod;
	}
	public String getCompositeMethodCalled()
	{
		return _compositeMethod;
	}
	
	

}
