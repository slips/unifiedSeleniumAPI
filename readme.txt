This document contains notes and observations on the selenium framework.  It is intended to explain some of the design decisions in the framework, thus enabling the reader to gain a better understanding in less time.


o	All tests will derive from org.usapi.BaseSeleniumTest.  
	o	This will expose (among others) an object named 'app', which is of type BaseApplication.  All interactions with the GUI utilize this app object, at no time should there be any need to invoke selenium methods directly.
	o	BaseSeleniumTest configures, starts and stops the selenium client transparently to the test developer.
	o	BaseSeleniumTest provides generic methods for use in tests, such as assertTrue, assertFalse, isElementPresent, etc.  Note that these methods are application-agnostic.  Methods required for a particular application (e.g. to execute SQL queries) do not belong in this class.
	o	BaseSeleniumTest loads the selenium locator<->id mappings file (so-called application elements).
		o	Application Elements are conceptually a three-dimensional array.  It is segmented into types (textfields, links, buttons, etc), which represent the actual types of GUI elements in the browser as we refer to them in the human (non-developer!) language.  Each type contains definitions for all GUI elements of this particular type as a name-value pair.  The name is the display string AS IT APPEARS ON THE SCREEN that identifies the element.  In the case of a button, it is the caption.  In the case of a textfield, it is the (usually) preceding label.  Etc.  The value is the locator that selenium uses to uniquely identify the GUI element at test execution time.  Using this approach, we arrive at a unified API and code that reads (almost) like human language and is self-documenting.
		o	Application Elements are currently represented in a spring configuration file for convenience sake, as no XML parser configuration/code is required to read it.  Depending on facilities available in PHP, an alternative mechanism can be utilized.
		o	For cases where the locator can change dynamically depending on what server/application a (suite of) test(s) executes, a data injection facility is provided.  At test execution time, this will perform a search-and-replace in the locators, based on user-provided data.
	o	BaseSeleniumTest is highly configurable via a set of overridable properties.  The order of precedence is default value -> definition in .properties file -> definition in system environment.
		
o	BaseApplication is the entry point into the actual framework.
	o	It provides methods to obtain objects for all GUI elements known in the application elements definition, e.g. links, buttons, textfields, etc.  These objects are referred to as DOM Nodes.
	o	DOM Nodes are provided by a factory, which has access to the application element definitions, and uses those definitions to locate and grant access to a requested DOM node (UI element)
		o	The DOM node factory instantiates nodes using Java reflection.  This is to allow for easy extension of the framework for nodes who may require support in the future.
	o	The test uses the BaseApplication instance as proxy to all UI elements to perform all actions and verifications on them during the test.
	
o	DOM nodes support methods specific to each type.  For example, all DOM nodes support the click() method, however only checkbox nodes support the check() method, only select nodes support the select() method, etc.
	o	Methods common to all nodes, such as click(), are implemented in an abstract base class.  All non-common methods are defined as abstract, requiring deriving DOM nodes to provide implementations for methods that are supported by a particular node.
	o	All DOM node methods are providing logging of their action, which includes type of action, description of the DOM node the action is performed on, and input value (where applicable, for example when entering a value into a text field).
	o	All DOM node methods are invoked using reflection from within a timed loop.  This mechanism addresses timing issues, where a DOM node may not have loaded/rendered at the time when the command is issued.  When writing code that drives selenium directly, this would require a sleep/wait statement.  Instead we use a configurable threshold up to which execution will be re-attempted.
	
	
	
Example for application element XML
===================================

<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:util="http://www.springframework.org/schema/util"
       xsi:schemaLocation="
          http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.0.xsd
          http://www.springframework.org/schema/util http://www.springframework.org/schema/util/spring-util-2.0.xsd">

		<util:map id="domNodes" map-class="java.util.Hashtable">
			<entry key="button" value-ref="buttons" />
			<entry key="checkbox" value-ref="checkboxes" />
			<entry key="image" value-ref="images" />
			<entry key="link" value-ref="links" />
			<entry key="listitem" value-ref="listitems" />
			<entry key="menu" value-ref="menus" />
			<entry key="menuitem" value-ref="menuitems" />
			<entry key="radiobutton" value-ref="radiobuttons" />
			<entry key="select" value-ref="selectboxes" />
			<entry key="tab" value-ref="tabs" />
			<entry key="text" value-ref="texts" />
			<entry key="textfield" value-ref="textfields" />
			<entry key="treenode" value-ref="treenodes" />
			<entry key="window" value-ref="windows" />
		</util:map>
		
		<util:map id="buttons" map-class="java.util.Hashtable">
  			<entry key="b1" value="button.v1" />
  		</util:map>

		<util:map id="checkboxes" map-class="java.util.Hashtable">
  			<entry key="c1" value="checkbox.v1" />
  		</util:map>

  		<util:map id="images" map-class="java.util.Hashtable">
  			<entry key="i1" value="image.v1" />
  		</util:map>	
  		
  </beans>
  
  
  
  
  Example for test code using the above application element definitions
  =====================================================================
  
  // Check a checkbox that has the label 'k1' on the screen
  app.checkbox("c1").check();

  // Click on a button that has the caption 'b1' on the screen
  app.button("b1").click();

  // Verify that an image that shows the string or has an associated label 'i1' is present
  assertTrue( app.isElementPresent( app.image("i1").getLocator() );
  
  
  Configuration Options
  =====================
  o	selenium.timeout: THE master timeout setting.  Controls how long a given action (click, type, etc) is attempted before aborting.
  
  o	selenium.scriptTimeout: Sets the amount of time to wait for an asynchronous script to finish execution before throwing an error.
  
  o	selenium.implicitlyWait: Specifies the amount of time the driver should wait when searching for an element if it is not immediately present.
  
  o	selenium.pageLoadTimeout: Sets the amount of time to wait for a page load to complete before throwing an error.
    
  o	selenium.applicationElements: Name of the file containing the application element definitions (locators).  Defaults to application-elements.xml
  
  o	selenium.maximize: Whether to maximize the browser immediately following launch.  Defaults to false.
  
  o	selenium.data: comma-separated name-value pairs that are processed into a Properties instance.  Can be accessed in code by calling 
  	PropertyHelper.getData();
  
  o	selenium.failureCaptureDir:  Directory where to store screenshots taken in case of failure.  
    Defaults to <current user's home directory>/seleniumFailureCaptures
  
  o	selenium.enableNativeEvents:  Disabled by default for Firefox on Linux as it may cause tests which open many windows in parallel to 
    be unreliable.	However, native events work quite well otherwise and are essential for some of the new actions of the Advanced User 
    Interaction.  Native Firefox event, passed through.
    
  o	selenium.serverHost: If running on the grid, specifies the machine name or IP of the selenium server host.
  
  o	selenium.serverPort: If running on the grid, specifies the port on which the selenium server is listening
  
  o	selenium.browserCommand: The browser to user when executing tests, i.e. firefox, iexplore, chrome, safari.
  
  o	selenium.browserUrl: URL to which to open the browser to immediately after launching, if specified.

  o	selenium.properties: If using a .properties file other than the default selenium.properties, 
  
  
  
  
  
  