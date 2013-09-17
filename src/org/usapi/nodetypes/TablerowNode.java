package org.usapi.nodetypes;

import org.openqa.selenium.WebDriver;

public class TablerowNode extends AbstractNode 
{
	public TablerowNode( WebDriver webDriver,
			String type,
			String nodeName,
			String locator)
	{
		super( webDriver, type, nodeName, locator);
	}
}
