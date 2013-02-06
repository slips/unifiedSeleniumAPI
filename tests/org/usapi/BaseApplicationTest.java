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


import org.usapi.NodeType;
import org.usapi.nodetypes.*;

import static org.junit.Assert.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class BaseApplicationTest {

	private ApplicationContext 	appCtx = null;
	private BaseApplication		baseApp	= null;
	
    private Log _log = LogFactory.getLog(BaseApplicationTest.class);
	
	private String NO_NODE_FOR_THIS_KEY_ERR_MSG = "Sorry, I do not know of any DOM nodes of "
		+ "type %nodeType% that are named non-existing-key.  Please review your Spring configuration "
		+ "file(default: application-elements.xml) for all known nodes of type %nodeType%.";

	@Before
	public void setUp() throws Exception {
		try{
			appCtx = new ClassPathXmlApplicationContext("junit-application-elements.xml");
		} catch (Exception e)
		{
			_log.error(e.getMessage());
		}
		// for unit-testing, use HtmlUnitDriver until we use/test actual webDriver methods
		baseApp = new BaseApplication(new RemoteWebDriverDummy(), appCtx);
	}

	@After
	public void tearDown() throws Exception {
		appCtx = null;
		baseApp = null;
	}


	@Test
	public void testLink() {
		IDOMNode node = null;
		// get and verify existing element
		try
		{
			node = baseApp.link("k1");
			assertNotNull(node);
			assertTrue(node instanceof LinkNode);
		} catch (DOMNodeNotFoundException e )
		{
			fail("Unexpected DOMNodeNotFoundException: " + e.getMessage() );
		}
		// verify correct behavior for non-existing element
		try
		{
			node = baseApp.link("non-existing-key");
			fail("Did not receive expected DOMNodeNotFoundException");
		} catch (DOMNodeNotFoundException e )
		{
			// we expect an exception
			assertEquals(e.getMessage(), getNoNodeForThisKeyErrorMsg("link"));
		}
	}

	@Test
	public void testButton() {
		IDOMNode node = null;
		// get and verify existing element
		try
		{
			node = baseApp.button("k1");
			assertNotNull(node);
			assertTrue(node instanceof ButtonNode);
		} catch (DOMNodeNotFoundException e )
		{
			fail("Unexpected DOMNodeNotFoundException: " + e.getMessage() );
		}
		// verify correct behavior for non-existing element
		try
		{
			node = baseApp.button("non-existing-key");
			fail("Did not receive expected DOMNodeNotFoundException");
		} catch (DOMNodeNotFoundException e )
		{
			// we expect an exception
			assertEquals(e.getMessage(), getNoNodeForThisKeyErrorMsg("button"));
		}
	}

	@Test
	public void testCheckbox() {
		IDOMNode node = null;
		// get and verify existing element
		try
		{
			node = baseApp.checkbox("k1");
			assertNotNull(node);
			assertTrue(node instanceof CheckboxNode);
		} catch (DOMNodeNotFoundException e )
		{
			fail("Unexpected DOMNodeNotFoundException: " + e.getMessage() );
		}
		// verify correct behavior for non-existing element
		try
		{
			node = baseApp.checkbox("non-existing-key");
			fail("Did not receive expected DOMNodeNotFoundException");
		} catch (DOMNodeNotFoundException e )
		{
			// we expect an exception
			assertEquals(e.getMessage(), getNoNodeForThisKeyErrorMsg("checkbox"));
		}
	}
	
	@Test
	public void testHiddenfield() {
		IDOMNode node = null;
		// get and verify existing element
		try
		{
			node = baseApp.hiddenfield("k1");
			assertNotNull(node);
			assertTrue(node instanceof HiddenfieldNode);
		} catch (DOMNodeNotFoundException e )
		{
			fail("Unexpected DOMNodeNotFoundException: " + e.getMessage() );
		}
		// verify correct behavior for non-existing element
		try
		{
			node = baseApp.hiddenfield("non-existing-key");
			fail("Did not receive expected DOMNodeNotFoundException");
		} catch (DOMNodeNotFoundException e )
		{
			// we expect an exception
			assertEquals(e.getMessage(), getNoNodeForThisKeyErrorMsg("hiddenfield"));
		}
	}

	@Test
	public void testElement() {
		IDOMNode node = null;
		// get and verify element.  Since elements are not aliased in application-elements.xml
		// there are no 'existing' and 'non-existing' elements 
		node = baseApp.element(NodeType.BUTTON, "e1");
		assertNotNull(node);
	}
	
	@Test
	public void testImage() {
		IDOMNode node = null;
		// get and verify existing element
		try
		{
			node = baseApp.image("k1");
			assertNotNull(node);
			assertTrue(node instanceof ImageNode);
		} catch (DOMNodeNotFoundException e )
		{
			fail("Unexpected DOMNodeNotFoundException: " + e.getMessage() );
		}
		// verify correct behavior for non-existing element
		try
		{
			node = baseApp.image("non-existing-key");
			fail("Did not receive expected DOMNodeNotFoundException");
		} catch (DOMNodeNotFoundException e )
		{
			// we expect an exception
			assertEquals(e.getMessage(), getNoNodeForThisKeyErrorMsg("image"));
		}
	}
	
	@Test
	public void testMenu() {
		IDOMNode node = null;
		// get and verify existing element
		try
		{
			node = baseApp.menu("k1");
			assertNotNull(node);
			assertTrue(node instanceof MenuNode);
		} catch (DOMNodeNotFoundException e )
		{
			fail("Unexpected DOMNodeNotFoundException: " + e.getMessage() );
		}
		// verify correct behavior for non-existing element
		try
		{
			node = baseApp.menu("non-existing-key");
			fail("Did not receive expected DOMNodeNotFoundException");
		} catch (DOMNodeNotFoundException e )
		{
			// we expect an exception
			assertEquals(e.getMessage(), getNoNodeForThisKeyErrorMsg("menu"));
		}
	}
	
	@Test
	public void testMenuitem() {
		IDOMNode node = null;
		// get and verify existing element
		try
		{
			node = baseApp.menuitem("k1");
			assertNotNull(node);
			assertTrue(node instanceof MenuitemNode);
		} catch (DOMNodeNotFoundException e )
		{
			fail("Unexpected DOMNodeNotFoundException: " + e.getMessage() );
		}
		// verify correct behavior for non-existing element
		try
		{
			node = baseApp.menuitem("non-existing-key");
			fail("Did not receive expected DOMNodeNotFoundException");
		} catch (DOMNodeNotFoundException e )
		{
			// we expect an exception
			assertEquals(e.getMessage(), getNoNodeForThisKeyErrorMsg("menuitem"));
		}
	}

	@Test
	public void testRadiobutton() {
		IDOMNode node = null;
		// get and verify existing element
		try
		{
			node = baseApp.radiobutton("k1");
			assertNotNull(node);
			assertTrue(node instanceof RadiobuttonNode);
		} catch (DOMNodeNotFoundException e )
		{
			fail("Unexpected DOMNodeNotFoundException: " + e.getMessage() );
		}
		// verify correct behavior for non-existing element
		try
		{
			node = baseApp.radiobutton("non-existing-key");
			fail("Did not receive expected DOMNodeNotFoundException");
		} catch (DOMNodeNotFoundException e )
		{
			// we expect an exception
			assertEquals(e.getMessage(), getNoNodeForThisKeyErrorMsg("radiobutton"));
		}
	}

	@Test
	public void testSelectbox() {
		IDOMNode node = null;
		// get and verify existing element
		try
		{
			node = baseApp.selectbox("k1");
			assertNotNull(node);
			assertTrue(node instanceof SelectboxNode);
		} catch (DOMNodeNotFoundException e )
		{
			fail("Unexpected DOMNodeNotFoundException: " + e.getMessage() );
		}
		// verify correct behavior for non-existing element
		try
		{
			node = baseApp.selectbox("non-existing-key");
			fail("Did not receive expected DOMNodeNotFoundException");
		} catch (DOMNodeNotFoundException e )
		{
			// we expect an exception
			assertEquals(e.getMessage(), getNoNodeForThisKeyErrorMsg("selectbox"));
		}
	}

	@Test
	public void testTab() {
		IDOMNode node = null;
		// get and verify existing element
		try
		{
			node = baseApp.tab("k1");
			assertNotNull(node);
			assertTrue(node instanceof TabNode);
		} catch (DOMNodeNotFoundException e )
		{
			fail("Unexpected DOMNodeNotFoundException: " + e.getMessage() );
		}
		// verify correct behavior for non-existing element
		try
		{
			node = baseApp.tab("non-existing-key");
			fail("Did not receive expected DOMNodeNotFoundException");
		} catch (DOMNodeNotFoundException e )
		{
			// we expect an exception
			assertEquals(e.getMessage(), getNoNodeForThisKeyErrorMsg("tab"));
		}
	}
	
	@Test
	public void testTable() {
		IDOMNode node = null;
		// get and verify existing element
		try
		{
			node = baseApp.table("k1");
			assertNotNull(node);
			assertTrue(node instanceof TableNode);
		} catch (DOMNodeNotFoundException e )
		{
			fail("Unexpected DOMNodeNotFoundException: " + e.getMessage() );
		}
		// verify correct behavior for non-existing element
		try
		{
			node = baseApp.table("non-existing-key");
			fail("Did not receive expected DOMNodeNotFoundException");
		} catch (DOMNodeNotFoundException e )
		{
			// we expect an exception
			assertEquals(e.getMessage(), getNoNodeForThisKeyErrorMsg("table"));
		}
	}
	
	@Test
	public void testText() {
		IDOMNode node = null;
		// get and verify existing element
		try
		{
			node = baseApp.text("k1");
			assertNotNull(node);
			assertTrue(node instanceof TextNode);
		} catch (DOMNodeNotFoundException e )
		{
			fail("Unexpected DOMNodeNotFoundException: " + e.getMessage() );
		}
		// verify correct behavior for non-existing element
		try
		{
			node = baseApp.text("non-existing-key");
			fail("Did not receive expected DOMNodeNotFoundException");
		} catch (DOMNodeNotFoundException e )
		{
			// we expect an exception
			assertEquals(e.getMessage(), getNoNodeForThisKeyErrorMsg("text"));
		}

	}

	@Test
	public void testTextfield() {
		IDOMNode node = null;
		// get and verify existing element
		try
		{
			node = baseApp.textfield("k1");
			assertNotNull(node);
			assertTrue(node instanceof TextfieldNode);
		} catch (DOMNodeNotFoundException e )
		{
			fail("Unexpected DOMNodeNotFoundException: " + e.getMessage() );
		}
		// verify correct behavior for non-existing element
		try
		{
			node = baseApp.textfield("non-existing-key");
			fail("Did not receive expected DOMNodeNotFoundException");
		} catch (DOMNodeNotFoundException e )
		{
			// we expect an exception
			assertEquals(e.getMessage(), getNoNodeForThisKeyErrorMsg("textfield"));
		}
	}
	
	@Test
	public void testWindow() {
		IDOMNode node = null;
		// get and verify existing element
		try
		{
			node = baseApp.window("k1");
			assertNotNull(node);
			assertTrue(node instanceof WindowNode);
		} catch (DOMNodeNotFoundException e )
		{
			fail("Unexpected DOMNodeNotFoundException: " + e.getMessage() );
		}
		// verify correct behavior for non-existing element
		try
		{
			node = baseApp.window("non-existing-key");
			fail("Did not receive expected DOMNodeNotFoundException");
		} catch (DOMNodeNotFoundException e )
		{
			// we expect an exception
			assertEquals(e.getMessage(), getNoNodeForThisKeyErrorMsg("window"));
		}

	}
	
	
	private String getNoNodeForThisKeyErrorMsg(String nodeType)
	{
		return NO_NODE_FOR_THIS_KEY_ERR_MSG.replace("%nodeType%", nodeType);
	}

}
