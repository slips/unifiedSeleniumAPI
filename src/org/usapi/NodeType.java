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

public enum NodeType
{
    BUTTON ( "button"),
    CATEGORY ( "category" ),
    CHECKBOX ( "checkbox" ),
    ELEMENT ( "element" ),
    FILE ( "file" ),
    HIDDENFIELD ( "hiddenfield" ),
    IMAGE ( "image" ),
    LINK ( "link" ),
    MENUITEM ( "menuitem" ),
    MENU ( "menu" ),
    MODALDIALOG ( "modaldialog" ),
    RADIOBUTTON ( "radiobutton" ),
    SELECT ( "selectbox" ),
    TAB ( "tab" ),
    TABLE ( "table" ),
    TABLECELL ( "tablecell" ),
    TABLEROW ( "tablerow" ),
    TEXTFIELD ( "textfield" ),
    TEXT ( "text" ),
    TREENODE ( "treenode" ),
    WINDOW ( "window" );
    
    private final String _type;
    
    NodeType( String type ) { _type = type; }
    
    public String getType() { return _type; }
}
