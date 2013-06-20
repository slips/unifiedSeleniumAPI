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

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import org.usapi.common.USAPIException;


public class BaseSeleniumTestTest extends TestCase {
	
	private BaseSeleniumTestDummy myTest = null;
    protected ApplicationContext 	appCtx		= null;	
    Log log = LogFactory.getLog(BaseSeleniumTestTest.class);   
    IDOMNode node = null;
	
	@Before
	public void setUp()
	{
		myTest = new BaseSeleniumTestDummy();
		try{
			appCtx = new ClassPathXmlApplicationContext("junit-application-elements.xml");
		} catch (Exception e)
		{
			log.error(e.getMessage());
		}				
	}
	
	@After
	public void tearDown()
	{
		
	}
	
	@Test
	public void testAssertAlert( String msg ) throws Exception
	{
		try
		{
			myTest.assertAlert("alertText");
		}
		catch( USAPIException e )
		{
			throw new AssertionFailedError("Failed assertion: alert message");
		}
		
		boolean assertionThrown = false;
		try
		{
			myTest.assertAlert("some other alert text, dufus" );
		}
		catch( USAPIException e )
		{
			assertionThrown = true;
		}
		assertTrue( "Failed alert assetion did not throw exception", assertionThrown );
	}
	
	@Test
	public void testAssertFalse()
	{
		try
		{
			myTest.assertFalse("msg", false);
		}
		catch( USAPIException e )
		{
			throw new AssertionFailedError("assertFalse threw excpetion when encountering false condition");
		}
		boolean assertionThrown = false;
		try
		{
			myTest.assertFalse("msg", true);
		}
		catch( USAPIException e )
		{
			assertEquals(e.getMessage(), "Assertion failed: msg");
			assertionThrown = true;
		}
		assertTrue( "", assertionThrown );
	}
	
	@Test
	public void testAssertTrue()
	{
		try
		{
			myTest.assertTrue("msg", true);
		}
		catch( USAPIException e )
		{
			throw new AssertionFailedError("assertTrue threw exception when encountering true condition");
		}
		boolean assertionThrown = false;
		try
		{
			myTest.assertTrue("msg", false);
		}
		catch( USAPIException e )
		{
			assertEquals(e.getMessage(), "Assertion failed: msg");
			assertionThrown = true;
		}
		assertTrue( "", assertionThrown );
	}
	
	@Test
	public void testGetWebDriver()
	{
		myTest.setUpWebDriver();
		assertNotNull( "Got null for webDriver instance", myTest.getParentWebDriver() );
		SeleniumFactory.reset();
	}
	
	@Test
	public void testIsElementAbsent()
	{
		long start = System.currentTimeMillis();
		boolean isElementAbsent = myTest.isElementAbsent( "locator", 500 );
		assertTrue( "Specified timeout exceeded when testing for absent element", System.currentTimeMillis() - start < 510 );
		assertFalse( "isElementAbsent returned unexpected true", isElementAbsent );
		
		isElementAbsent = true;
		IDOMNode node = getNode();
		isElementAbsent = myTest.isElementAbsent(node, 500);
		assertFalse( "isElementPresent returned unexpected false", isElementAbsent );		
	}

	
	@Test
	public void testIsElementPresent()
	{
		long start = System.currentTimeMillis();
		boolean isElementPresent = myTest.isElementPresent( "locator", 500 );
		assertTrue( "Specified timeout exceeded when testing for present element", System.currentTimeMillis() - start < 510 );
		assertTrue( "isElementPresent returned unexpected false", isElementPresent );

		isElementPresent = false;
		IDOMNode node = getNode();
		isElementPresent = myTest.isElementPresent(node, 500);
		assertTrue( "isElementPresent returned unexpected false", isElementPresent );
	}

	
	//@Test
	public void testLog()
	{
		
	}
	
	//@Test
	public void testLogError()
	{
		
	}
	
	//@Test
	public void testLogWarning()
	{
		
	}
	
	@Test
	public void testSetUpSelenium()
	{
		myTest.setUpWebDriver();
		assertNotNull( "Got null for web driver instance", myTest.getParentWebDriver() );
		SeleniumFactory.reset();
	}
	
	@Test
	public void testSleep()
	{
		long start = System.currentTimeMillis();
		myTest.sleep(500);
		assertTrue( "Specified sleep time exceeded by more than 10 ms.", System.currentTimeMillis() - start < 510 );
	}

	@Test
	public void testWaitForElement()
	{
		long start = System.currentTimeMillis();
		myTest.waitForElement( "locator" );
		assertTrue( "Default timeout exceeded when waiting for element.", System.currentTimeMillis() - start < 300000 /* default timeout */ );
		
	}
	
	private IDOMNode getNode()
	{
		DOMNodeFactory nodeFactory = DOMNodeFactory.getInstance();
		nodeFactory.initialize(appCtx, myTest.getWebDriver());
		IDOMNode node = null;
		
		try
		{
			node = nodeFactory.getDOMNode(NodeType.BUTTON, "k1");
			assertNotNull(node);
		} catch (DOMNodeNotFoundException e )
		{
			fail("Unexpected DOMNodeNotFoundException: " + e.getMessage() );
		}		
		return node;
	}
	
	
	
	
}
