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

package org.usapi.common;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.*;
import org.junit.Test;

import org.usapi.common.DataInjector;

public class DataInjectorTest {
	
	String JUNIT_APPLICATION_ELEMENTS_XML = "junit-application-elements.xml";
	// file with expected contents after data has been injected.  Is loaded from classpath
	// as resource.  The file with injected data has the same name, but is written to/loaded 
	// from java's temp dir 
	String JUNIT_APPLICATION_ELEMENTS_SUBST_XML = "junit-application-elements.subst.xml";

	@Test
	public void testInjectMultipleValues()
	{
		Properties data = new Properties();
		data.put("injectedValue", "injected value");
		data.put("anotherInjectedValue", "another injected value");
		DataInjector.inject( JUNIT_APPLICATION_ELEMENTS_XML, data );
		
		boolean filesAreEqual = filesAreEqual ( JUNIT_APPLICATION_ELEMENTS_SUBST_XML, 
				DataInjector.getOutputFilename(JUNIT_APPLICATION_ELEMENTS_XML ) );
		assertTrue("Injected data file not as expected",  filesAreEqual );
	}
	
	private boolean filesAreEqual( String resourceName, String fileToCompareTo )
	{
		boolean filesAreEqual = false;
		InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream( resourceName );
		BufferedReader expectedReader = new BufferedReader( new InputStreamReader( in ) );
		
		BufferedReader actualReader = null;
		try
		{
			actualReader = new BufferedReader( new FileReader (  fileToCompareTo ) );
			String expected = expectedReader.readLine();
			String actual = actualReader.readLine();
			
			while ( ( expected != null ) && ( actual != null ) )
			{
				assertEquals( expected, actual );
				expected = expectedReader.readLine();
				actual = actualReader.readLine();
			}
			// if we make it here then comparison was successfull
			filesAreEqual = true;
		}
		catch( IOException ioe )
		{
			fail( "IOException during verification, using classpath resource " + resourceName + " and file " + fileToCompareTo + ".  " + ioe.getMessage() );
		}
		return filesAreEqual;
	}
}
