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

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.security.Credentials;

public class AlertDummy extends Alert implements org.openqa.selenium.Alert 
{

	private String webDriverMethodCalled = null;
		
	public AlertDummy( WebDriver driver )
	{
		super( driver );
	}
	
	public String getWebDriverMethodCalled()
	{
		return webDriverMethodCalled;
	}
	
	
	@Override
	public void accept() 
	{
		super.accept();
		webDriverMethodCalled = "accept()";
	}

	@Override
	public void dismiss() 
	{
		super.dismiss();
		webDriverMethodCalled = "dismiss()";
	}

	@Override
	public String getText() 
	{
		webDriverMethodCalled = "getText()";
		return super.getText();
	}

	@Override
	public void sendKeys(String arg0) 
	{
		super.sendKeys(arg0);
		webDriverMethodCalled = "sendKeys(" + arg0 + ")";
	}
	
	public boolean exists()
	{
		webDriverMethodCalled = "exists()";
		return super.exists();
	}
	
	public boolean exists(long timeout)
	{
		webDriverMethodCalled = "exists(" + timeout + ")";
		return super.exists(timeout);
	}

	// no-op implementations to satisfy Alert interface
	public void authenticateUsing( Credentials creds ) {}
	
}
