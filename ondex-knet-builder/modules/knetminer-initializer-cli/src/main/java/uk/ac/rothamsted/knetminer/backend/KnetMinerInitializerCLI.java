package uk.ac.rothamsted.knetminer.backend;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sourceforge.ondex.parser.oxl.Parser;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.ExitCode;
import picocli.CommandLine.Option;

/**
 * TODO: comment me!
 *
 * @author brandizi
 * <dl><dt>Date:</dt><dd>25 Feb 2022</dd></dl>
 *
 */
@Command ( 
	name = "knet-init", 
		description = "\n\n  *** KnetMiner Data Initialiser ***\n" +
			"\nCreates/updates KnetMiner data (Lucene, traverser) for an OXL.\n",
	exitCodeOnVersionHelp = ExitCode.USAGE, // else, it's 0 and you can't know about this event
	exitCodeOnUsageHelp = ExitCode.USAGE, // ditto
	mixinStandardHelpOptions = true,
	usageHelpAutoWidth = true,
	usageHelpWidth = 120
)
public class KnetMinerInitializerCLI implements Callable<Integer>
{
	@Option (
		names = { "-i", "--input", "--in" },
		paramLabel = "<path/to/oxl>",		
		description = "The path of the OXL to start from",
		required = true
	)
	private String oxlInputPath = null;
	

	@Option (
		names = { "-d", "--data" },
		paramLabel = "<path/to/dir>",		
		description = KnetMinerInitializerPlugIn.OPT_DESCR_DATA_PATH
	)
	private String dataPath;
	
	@Option (
		names = { "-f", "--file"},
		paramLabel = "<fqn/of/graphtraverser>",		
		description = KnetMinerInitializerPlugIn.OPT_DESCR_TRAVERSER
	)
	private String graphTraverserFQN;
	
	@Option (
		names = { "-c", "--conf"},
		paramLabel = "<path/to/oxl>",		
		description = KnetMinerInitializerPlugIn.OPT_DESCR_CONFIG_XML,
		required = true
	)
	private String configXmlPath;
	
	@Option (
		names = { "-t", "--tid"},
		paramLabel = "<taxs/numerical/identifiers>",		
		description = KnetMinerInitializerPlugIn.OPT_DESCR_TAXIDS
	)
	private Set<String> taxIds;
	
	@Option (
		names = { "-o", "--opt"},
		paramLabel = "<extend/the/options>",		
		description = KnetMinerInitializerPlugIn.OPT_DESCR_OPTS
	)
	private Map<String, String> options;	
	
	private Logger log = LoggerFactory.getLogger ( this.getClass () ); 
	
	
	@Override
	public Integer call ()
	{
		log.info ( "Loading the OXL: '{}'", this.oxlInputPath );
		var graph = Parser.loadOXL ( oxlInputPath );
		
		KnetMinerInitializer initializer = new KnetMinerInitializer ();
		initializer.setGraph ( graph );
		initializer.setConfigXmlPath ( configXmlPath );
		initializer.setDataPath(dataPath);		
		initializer.initKnetMinerData ();
		
		return 0;
	}

	/**
	 * Does all the job of {@link #main(String...)}, except exiting, useful for 
	 * testing.
	 * 
	 * This uses {@link CommandLine}, as prescribed by the picocli library.
	 */
	public static int invoke ( String... args )
	{
    int exitCode = new CommandLine ( new KnetMinerInitializerCLI () ).execute ( args );
    return exitCode; 
	}
	
	/**
	 * The usual wrapper for the external invocation. This just invokes {@link #invoke(String...)}
	 * and exits with its result.
	 * 
	 */
	public static void main ( String... args )
	{
		System.exit ( invoke ( args ) );
	}	
	
}
