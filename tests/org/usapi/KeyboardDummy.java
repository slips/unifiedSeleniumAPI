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

import org.openqa.selenium.Keyboard;
import org.openqa.selenium.Keys;

public class KeyboardDummy implements Keyboard
{
	private RemoteWebDriverDummy wd = null;
	public KeyboardDummy(RemoteWebDriverDummy wd)
	{
		this.wd = wd;
	}
	
	public void pressKey( Keys keyToPress ) 
	{
		switch ( keyToPress )
		{
			case ALT: 
				wd.getCallbackNode().setKeyboardMethodCalled("pressKey-ALT"); 
				break;
			case CONTROL: 
				wd.getCallbackNode().setKeyboardMethodCalled("pressKey-CONTROL"); 
				break;
			case SHIFT: 
				wd.getCallbackNode().setKeyboardMethodCalled("pressKey-SHIFT"); 
				break;
		}
	}
	
	public void releaseKey( Keys keyToRelease )
	{
		switch ( keyToRelease )
		{
			case ALT: 
				wd.getCallbackNode().setKeyboardMethodCalled("releaseKey-ALT"); 
				break;
			case CONTROL: 
				wd.getCallbackNode().setKeyboardMethodCalled("releaseKey-CONTROL"); 
				break;
			case SHIFT: 
				wd.getCallbackNode().setKeyboardMethodCalled("releaseKey-SHIFT"); 
				break;
		}
	}
	
	public void sendKeys( CharSequence... keysToSend ) {}
	
}
