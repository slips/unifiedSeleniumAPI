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


import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.usapi.common.USAPIException;

public class HiddenfieldNodeTest extends AbstractNodeTest {

	private String TYPE = "hiddenfield";
	private String NODE_NAME = "hf1";
 
	
	@Before
	public void setUp() throws Exception {
		super.webDriver.setCallbackNode(this);
		nodeUnderTest = new HiddenfieldNode( super.webDriver, TYPE, NODE_NAME, AbstractNodeTest.LOCATOR);
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
	public void testGetValue() throws USAPIException
	{
		nodeUnderTest.getValue();
		assertEquals("getAttribute(value)", getWebElementMethodCalled());
	}
	

}
