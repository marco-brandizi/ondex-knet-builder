package uk.ac.rothamsted.knetminer.backend;

import java.io.File;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import net.sourceforge.ondex.core.ONDEXGraph;
import net.sourceforge.ondex.parser.oxl.Parser;

/**
 * TODO: comment me!
 *
 * @author brandizi
 * <dl><dt>Date:</dt><dd>13 Feb 2022</dd></dl>
 *
 */
public class KnetMinerInitializerTest
{
	
	private KnetMinerInitializer initializer;
	
	private String testCaseOut;
	
	@Before
	public void initKnetMinerInitializer() {
		String mavenBuildPath = System.getProperty ( "maven.buildDirectory", "target" ) + "/";

		// Maven copies test files here.
		var testCasePath = mavenBuildPath + "/test-classes/test-case";
		testCaseOut = testCasePath + "/output";
		
		ONDEXGraph graph = Parser.loadOXL ( testCasePath + "/text-mining.oxl" );
		Assert.assertNotNull ( "graph not loaded!", graph );

		initializer = new KnetMinerInitializer ();
		initializer.setGraph ( graph );
		initializer.setConfigXmlPath ( testCasePath + "/data-source-config.xml" );
		initializer.setDataPath ( testCaseOut );
		
		initializer.loadOptions();
	}
	
	@Test
	public void testInitLuceneData ()
	{
		
		initializer.initLuceneData ();
		
		// check Lucene index files exist, using testCaseOut
		File testCaseOutFolder = new File ( testCaseOut );
		File[] listOfFiles = testCaseOutFolder.listFiles ();
		Assert.assertTrue ( "Index folder not created ", Arrays.asList ( listOfFiles ).stream ().anyMatch (
				file -> file.exists () && file.isDirectory ()&& ( file.getName ().endsWith ( "index" ))));
		
		File indexFolder = new File ( testCaseOut + "/index/" );
		File[] indexFiles = indexFolder.listFiles ();
		Assert.assertTrue ( "Index files not created ",
				Arrays.asList ( indexFiles ).stream ()
						.anyMatch ( file -> file.exists () && file.isFile () && ( file.getName ().endsWith ( "cfe" ))
								|| file.getName ().endsWith ( "cfs" ) || file.getName ().endsWith ( "si" )
								|| file.getName ().endsWith ( "fdt" ) || file.getName ().endsWith ( "fdx" )
								|| file.getName ().endsWith ( "fnm" ) || file.getName ().endsWith ( "nvd" )
								|| file.getName ().endsWith ( "nvm" ) || file.getName ().endsWith ( "tvd" )
								|| file.getName ().endsWith ( "tvx" ) || file.getName ().endsWith ( "pos" )
								|| file.getName ().endsWith ( "tim" ) || file.getName ().endsWith ( "tip" )));
	}
	
	@Test
	public void testInitSemanticMotifData ()
	{
		initializer.initSemanticMotifData();
		// check traverser files exist, using testCaseOut
		File folder = new File ( testCaseOut );
		File[] traverserFiles = folder.listFiles ();
		Assert.assertTrue( "Graph Traverser files not created",
				Arrays.asList ( traverserFiles ).stream ()
						.anyMatch( file -> file.exists () && file.isFile ()
								&& ( file.getName ().equalsIgnoreCase ( "concepts2Genes.ser" )
										|| file.getName ().equalsIgnoreCase ( "genes2Concepts.ser" )
										|| file.getName ().equalsIgnoreCase ( "genes2PathLengths.ser" ))));
	}
}
