package org.usapi.training;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Keys;

import org.usapi.BaseSeleniumTest;


public class GithubTest extends BaseSeleniumTest 
{
	@Before
	public void setup()
	{
		webDriver.navigate().to("http://www.github.com");
	}
	
	@Test
	public void testGithub() throws Exception
	{
		app.textfield("Find any repository").type("unified selenium API" + Keys.ENTER );
		assertTrue("Advanced Search textfield does not show original search term.", app.textfield("Advanced Search").getValue().equals("unified selenium API"));
		app.link("slips / unifiedSeleniumAPI").click();
		app.link("readme.txt").click();
		assertTrue("Readme.txt does not show.", isElementPresent(app.element(TYPE_TEXT, "//div[@class='line'][contains(text(), 'This document contains notes and observations on the selenium framework.')]")));
	}
}
