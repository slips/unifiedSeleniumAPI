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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Iterator;
import java.util.Properties;

public class DataInjector 
{
	
    private static Log log = LogFactory.getLog(DataInjector.class);

	
	/**
	 * 
	 * @param originalAppElementsClasspathResource if data injection is to be performed, placeholders 
	 * 			are expected to be delimited by {{ and }}, e.g. {{servername}}
	 * @param data expected format: name1=value1,name2=value2,...,nameN=valueN  
	 * @return Absolute path on the local file system to file where data has been injected.
	 * 		   Can be null if there was an error during data injection.  Client is responsible
	 * 		   for checking non-null return.
	 * @throws Exception
	 */
	public static String inject ( String originalAppElementsClasspathResource, Properties data )
	{
		// get buffered reader for input file (originalAppElements)
		BufferedReader input = openAsURL ( originalAppElementsClasspathResource );
		// construct file name that holds processed data   
		PrintWriter output = openOutputFileWriter( originalAppElementsClasspathResource );
		// now do the mojo ... inject the data
		// since we're just logging an error if source or destination
		// fails, explicitly check for non-null
		String outputFilename = null;
		if( input != null && output != null )
		{
			injectData( input, data, output );
			outputFilename = getOutputFilename( originalAppElementsClasspathResource );
			log.info("Injected data file written to " + outputFilename );
		}
		
		// can be null if there was an earlier failure during processing
		return outputFilename;
	}
	
	private static void injectData( BufferedReader input, Properties data, PrintWriter output )
	{
		try
		{
    		String inputLine = null;
    		while ( ( inputLine = input.readLine() ) != null )
    		{
    			String s = substitute( inputLine, data );
    			log.debug("Writing substituted string: " + s );
    			output.println ( s );
    		}
		}
		catch ( IOException ioe )
		{
			log.error( "Error reading from classpath resource: " + ioe.getMessage() );
		}
		finally
		{
			output.flush();
			output.close();
		}
	}
	
	public static String substitute( String input, Properties data )
	{
		Iterator keys = data.keySet().iterator();
		String key = null;
		while( keys.hasNext() )
		{
			key = (String)keys.next();
			input = substitute( input, "{{" + key + "}}", (String)data.getProperty(key));
		}
		return input;
	}
	
    private static String substitute(String string, String pattern, String replacement) 
    {
        int start = string.indexOf(pattern);
        while (start != -1) 
        {
            StringBuffer buffer = new StringBuffer ( string );
            buffer.delete(start, start + pattern.length () );
            buffer.insert ( start, replacement );
            string = new String ( buffer );
            start = string.indexOf ( pattern, start + replacement.length () );
        }
        return string;
    }
    
	private static BufferedReader openAsURL( String resourceName )
	{
		BufferedReader input = null;
		try
		{
    		URL inputFileURL = Thread.currentThread().getContextClassLoader().getResource( resourceName );
    		input = new BufferedReader ( new InputStreamReader ( inputFileURL.openStream() ) );
		}
		catch ( Exception e ) // FileNotFoundException, IOException
		{
			log.error( "Failed to load " + resourceName + " from classpath: " + e.getMessage() );
		}
		return input;
	}
	
	private static PrintWriter openOutputFileWriter( String resourceName )
	{
		PrintWriter processedFileWriter = null;
		try
		{
			String outFile = getOutputFilename( resourceName );
			log.info("Created writer for injected data file to be written to " + outFile );
    		processedFileWriter = new PrintWriter( new BufferedWriter( new FileWriter( outFile ) ) );
		}
		catch ( IOException ioe )
		{
			log.error( "Failed to open destination for processed application-elements file: " + ioe.getMessage() );
		}
		return processedFileWriter;
	}
	
	public static String getOutputFilename( String classpathResource)
	{
		String tmpDir = System.getProperty ( "java.io.tmpdir" );
		String pathSeparator = System.getProperty( "file.separator" );
		String  outFile = tmpDir + pathSeparator + classpathResource.replaceFirst(".xml", ".subst.xml");
		return outFile;
	}
	

}
