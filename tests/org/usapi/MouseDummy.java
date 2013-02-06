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

import org.openqa.selenium.Mouse;
import org.openqa.selenium.interactions.internal.Coordinates;

public class MouseDummy implements Mouse 
{
	private RemoteWebDriverDummy wd = null;
	public MouseDummy(RemoteWebDriverDummy wd)
	{
		this.wd = wd;
	}
	public void click( Coordinates where ) 
	{
		wd.getCallbackNode().setMouseMethodCalled("click");
	}
	public void contextClick( Coordinates where ) 
	{
		wd.getCallbackNode().setMouseMethodCalled("contextClick");
	}
	public void doubleClick( Coordinates where ) {}
	public void mouseDown( Coordinates where ) {}
	public void mouseMove( Coordinates where ) {}
	public void mouseMove( Coordinates where, long xOffset, long yOffset ) {}
	public void mouseUp( Coordinates where ) {}
}
