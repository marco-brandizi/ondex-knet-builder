package uk.ac.rothamsted.knetminer.backend;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Junit test class for KnetMinerInitializerCLI
 * 
 * @author brandizi
 * @author jojicunnunni
 * <dl><dt>Date:</dt><dd>25 Feb 2022</dd></dl>
 *
 */
public class KnetMinerInitializerCLITest
{
	private static String testCasePath;
	private static String testCaseOut;

	@BeforeClass
	public static void init() throws IOException
	{
		var mavenBuildPath = System.getProperty ( "maven.buildDirectory", "target" );
		mavenBuildPath = Path.of ( mavenBuildPath ).toRealPath ().toString ();
		mavenBuildPath = mavenBuildPath.replace ( '\\', '/' );

		// Maven copies test files here.
		testCasePath = mavenBuildPath + "/test-classes/test-case";
		testCaseOut = testCasePath + "/output-cli";		
	}
		
	@Test
	public void testBasics ()
	{
		FileUtils.deleteQuietly ( new File ( testCaseOut ) );
		
		var exitCode = KnetMinerInitializerCLI.invoke (
			"-i", testCasePath + "/poaceae-sample.oxl", 
			"-d", testCaseOut,
			"-c" , testCasePath + "/data-source-config.xml"
		);
		
		Assert.assertEquals ( "Wrong exit code!", 0, exitCode );
		
		assertTrue ( "Lucene output not found!", new File ( testCaseOut + "/index" ).exists () );
		assertTrue ( "Traverser output not found!", new File ( testCaseOut + "/concepts2Genes.ser" ).exists () );
	}


	@Test
	public void testAdvancedOpts () throws IOException
	{
		testCaseOut = testCasePath + "/output-cli-advanced";
		FileUtils.deleteQuietly ( new File ( testCaseOut ) );

		var exitCode = KnetMinerInitializerCLI.invoke (
			"-i", testCasePath + "/poaceae-sample.oxl", 
			"-d", testCaseOut, 
			"-o", "StateMachineFilePath=file:///" + testCasePath + "/SemanticMotifs.txt",  
			"--tax-id", "4565",
			"--tax-id", "3702"
		);
		
		Assert.assertEquals ( "Wrong exit code!", 0, exitCode );
				
		assertTrue ( "Lucene output not found!", new File ( testCaseOut + "/index" ).exists () );
		assertTrue ( "Traverser output not found!", new File ( testCaseOut + "/concepts2Genes.ser" ).exists () );
			
	}

}
