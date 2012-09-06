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

import org.openqa.selenium.WebElement;
import org.usapi.common.USAPIException;


/**
 * 
 * @author slips
 *
 * Defines methods that perform actions on a DOM node.  Implementor is responsible
 * that only methods applicable to a particular type of node perform any actions. 
 * For example, a .check() action on a button should not perform any actual action.
 * 
 */
public interface IDOMNode {
	/**
	 * Triggers a {@link com.thoughtworks.selenium.Selenium} click event on this DOM node.
     * 
     * @throws USAPIException
	 */
	public void click() throws USAPIException;
	
//	/**
//	 * Uses Selenium's Mouse object to generate a doubleClick event on this DOM node.
//     * 
//     * @throws USAPIException
//	 */
//	public void doubleClick() throws USAPIException;
	
	/**
	 * Triggers a {@link com.thoughtworks.selenium.Selenium} click event on this DOM node,
	 * while holding down the Alt key.
     * 
     * @throws USAPIException
	 */
	public void altClick() throws USAPIException;

	/**
	 * Triggers a {@link com.thoughtworks.selenium.Selenium} click event on this DOM node,
	 * while holding down the Ctrl key.
     * 
     * @throws USAPIException
	 */
	public void ctrlClick() throws USAPIException;

	/**
	 * Triggers a {@link com.thoughtworks.selenium.Selenium} click event on this DOM node,
	 * while holding down the Shift key.
     * 
     * @throws USAPIException
	 */
	public void shiftClick() throws USAPIException;
	
	/**
	 * Triggers a {@link com.thoughtworks.selenium.Selenium} mouseDown, followed by a mouseUp
	 * event on this DOM node.  This is to accommodate UI elements that do not recognize the
	 * click event.
	 * 
	 * @throws USAPIException
	 */
	public void mouseDownUp() throws USAPIException;
	
    /**
     * Triggers a {@link com.thoughtworks.selenium.Selenium} mouseDown, followed by a mouseUp
     * event on this DOM node.  This is to accommodate UI elements that do not recognize the
     * click event.
     * 
     * @throws USAPIException
     */
	public void altMouseDownUp() throws USAPIException;
	
    /**
     * Triggers a {@link com.thoughtworks.selenium.Selenium} mouseDown, followed by a mouseUp
     * event on this DOM node while holding down the Alt key.  This is to accommodate UI 
     * elements that do not recognize the click event.
     * 
     * @throws USAPIException
     */
	public void ctrlMouseDownUp() throws USAPIException;
	
    /**
     * Triggers a {@link com.thoughtworks.selenium.Selenium} mouseDown, followed by a mouseUp
     * event on this DOM node while holding down the Control key.  This is to accommodate UI 
     * elements that do not recognize the click event.
     * 
     * @throws USAPIException
     */
	public void shiftMouseDownUp() throws USAPIException;
	
	
	/**
     * Triggers a {@link com.thoughtworks.selenium.Selenium} mouseDown, followed by a mouseUp
     * event on this DOM node while holding down the Shift key.  This is to accommodate UI 
     * elements that do not recognize the click event.
     * 
     * @throws USAPIException
	 */
	public void type(String value) throws USAPIException;

	/**
	 * Simulate pressing the Control key on this DOM node.
	 */
	public void controlKeyDown() throws USAPIException;
	
	/**
	 * Simulate releasing the Control key on this DOM node.
	 */
	public void controlKeyUp() throws USAPIException;
	
	/**
	 * Selects specified value on this DOM node.
	 * @param selection option to be selected
	 */
	public void select(String selection) throws USAPIException;

	/**
	 * Add a selection to the set of selected options in a multi-select DOM node.
	 * @param selection
	 */
	public void addSelection(String selection) throws USAPIException;
	/**
	 * Brings up the context menu on this DOM node.
	 */
	public void contextMenu() throws USAPIException;

	/**
	 * Triggers a {@link com.thoughtworks.selenium.Selenium} check event on this DOM node.
	 */
	public void check() throws USAPIException;

	/**
	 * Triggers a {@link com.thoughtworks.selenium.Selenium} uncheck event on this DOM node.
	 */
	public void uncheck() throws USAPIException;

	/**
	 * Triggers a {@link com.thoughtworks.selenium.Selenium} focus event on this DOM node.
	 */
	public void focus() throws USAPIException;
	
	/**
	 * Gets the (whitespace-trimmed) value of an input field (or anything else with a value parameter). 
	 * For checkbox/radio elements, the value will be "on" or "off" depending on whether the element is checked or not.
	 */
	public String getValue() throws USAPIException;
	
	/**
	 * Get all option values for a given control.
	 */
	public String[] getSelectOptions() throws USAPIException;
	
	/**
	 * Get the currently selected value for a given select element.
	 */
	public String getSelectedValue() throws USAPIException;
	
	/**
	 * Triggers a {@link com.thoughtworks.selenium.Selenium} mouseDown event on this DOM node
	 */	public void mouseDown() throws USAPIException;
	
	/**
	 * Triggers a {@link com.thoughtworks.selenium.Selenium} mouseUp event on this DOM node
	 */	public void mouseUp() throws USAPIException;
	
	/**
	 * Triggers a {@link com.thoughtworks.selenium.Selenium} mouseOver event on this DOM node
	 */
	public void mouseOver() throws USAPIException;
	
	/**
	 * Checks whether this DOM node is enabled/editable
	 * @return enabled state of this DOM node
	 * @throws USAPIException
	 */
	public boolean isEnabled() throws USAPIException;
	
	/**
	 * Checks whether this DOM node is visible
	 * @return visible state of this DOM node
	 * @throws USAPIException
	 */
	public boolean isVisible() throws USAPIException;
	
	/**
	 * Get the {@link com.thoughtworks.selenium.Selenium} locator for this DOM node.
	 * @return {@link com.thoughtworks.selenium.Selenium} locator.
	 */
	public String getLocator();
	
	public WebElement getWebElement();
}
