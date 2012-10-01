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

import com.thoughtworks.selenium.Selenium;

/**
 * 
 * @author slips
 *
 * Defines methods to access DOM nodes of a given type, with a given name (UI display string).
 */
public interface IApplication {
	public IDOMNode button		( String nodeName ) 	throws DOMNodeNotFoundException;
	public IDOMNode checkbox	( String nodeName ) 	throws DOMNodeNotFoundException;
	public IDOMNode file		( String nodeName )		throws DOMNodeNotFoundException;
	public IDOMNode hiddenfield	( String nodeName )		throws DOMNodeNotFoundException;
	public IDOMNode image 		( String nodeName )		throws DOMNodeNotFoundException;
	public IDOMNode link		( String nodeName ) 	throws DOMNodeNotFoundException;
	public IDOMNode menu 		( String nodeName )		throws DOMNodeNotFoundException;
	public IDOMNode menuitem 	( String nodeName) 		throws DOMNodeNotFoundException;
	public IDOMNode tablecell	( String nodeName ) 	throws DOMNodeNotFoundException;
	public IDOMNode tablerow	( String nodeName ) 	throws DOMNodeNotFoundException;
	public IDOMNode tab			( String nodeName ) 	throws DOMNodeNotFoundException;
	public IDOMNode text		( String txt )			throws DOMNodeNotFoundException;
	public IDOMNode textfield	( String nodeName ) 	throws DOMNodeNotFoundException;
	public IDOMNode radiobutton	( String nodeName ) 	throws DOMNodeNotFoundException;
	public IDOMNode selectbox	( String nodeName ) 	throws DOMNodeNotFoundException;
	public IDOMNode window		( String windowName )	throws DOMNodeNotFoundException;

	public IDOMNode element		( NodeType type, String locator );
}
