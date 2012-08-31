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

package corg.usapi;

import junit.framework.TestCase;

import org.junit.Test;
import org.openqa.selenium.WebDriver;

import com.thoughtworks.selenium.Selenium;

/**
 * This uses simple Junit TestCase to verify SeleniumFactory (and other tests) without
 * using the Spring fremeworks Junit4 runners as the Spring runners are sensitive to the
 * combination of Junit and Spring versions (some inner class in Junit 4.4 are removed in Junit 4.5)
 *
 */
public class SeleniumFactoryTest extends TestCase {

	@Test
    public void testSeleniumFactory()
    {
        Selenium selenium = SeleniumFactory.getSeleniumInstance();
        assertNotNull("SeleniumFactory returns null", selenium);
        
        WebDriver webDriver = SeleniumFactory.getWebDriverInstance();
        assertNotNull("SeleniumFactory returns null", webDriver);
        
        SeleniumFactory.reset();
        Selenium s2 = SeleniumFactory.getSeleniumInstance();
        assertFalse("Received same selenium instance after factory re-set", selenium == s2 );
        
        WebDriver wd2 = SeleniumFactory.getWebDriverInstance();
        assertFalse("Received same webDriver instance after factory re-set", wd2 == webDriver );
        
        SeleniumFactory.reset();
    }
}
