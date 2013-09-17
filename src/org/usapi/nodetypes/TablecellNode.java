package org.usapi.nodetypes;

import org.openqa.selenium.WebDriver;

public class TablecellNode extends AbstractNode 
{
	public TablecellNode( WebDriver webDriver,
			String type,
			String nodeName,
			String locator)
	{
		super( webDriver, type, nodeName, locator);
	}
}
