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

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

public class AlertTest extends TestCase
{
	private BaseSeleniumTestDummy baseSeleniumTest = null;
	private AlertDummy alert = null;

	
	@Before
	public void setUp()
	{
		baseSeleniumTest = new BaseSeleniumTestDummy();
		alert = new AlertDummy(baseSeleniumTest.webDriver);
	}

	@Test
	public void testAccept()
	{
		alert.accept();
		assertEquals("accept()", alert.getWebDriverMethodCalled() );
	}
	
	@Test
	public void testDismiss()
	{
		alert.dismiss();
		assertEquals("dismiss()", alert.getWebDriverMethodCalled() );
	}
	
	@Test
	public void testGetText()
	{
		String txt = alert.getText();
		assertEquals( "alertText", txt );
		assertEquals( "getText()", alert.getWebDriverMethodCalled() );
	}
	
	@Test
	public void testSendKeys()
	{
		alert.sendKeys("value");
		assertEquals( "sendKeys(value)", alert.getWebDriverMethodCalled() );
	}
	
	@Test
	public void testExists()
	{
		alert.exists();
		assertEquals( "exists()", alert.getWebDriverMethodCalled() );
		
		alert.exists(1000);
		assertEquals( "exists(" + 1000 + ")", alert.getWebDriverMethodCalled() );
	}
}
