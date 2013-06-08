/**
 *
 */
package com.github.gbz3.jnetcat;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author gbz3
 *
 */
public class Main {
	private static final Logger log = LoggerFactory.getLogger( Main.class );

	private static final String OPT_H = "h";
	private static final String OPT_L = "l";

	/**
	 * @param args
	 */
	public static void main( String[] args ) throws ParseException {
		final RunnerConfig config = parseCommandLine( getOptions(), args );
		if( config != null ) {
			log.debug( "starting runner by the {}.", config );
		}
		log.debug( "finished {}.", Main.class );
	}

	static Options getOptions() {
		final Options result = new Options();
		result.addOption( "h", false, "print this help." );
		result.addOption( "l", false, "listen-mode. it must specify one or two parameters." );
		return result;
	}

	static RunnerConfig parseCommandLine( Options opts, String[] args ) throws ParseException {
		final CommandLine cl = new BasicParser().parse( opts, args );

		if( cl.hasOption( OPT_H ) ) {
			new HelpFormatter().printHelp( "Jnetcat [ -h ] [ -l listenPort [ listenAddr ] ] ", opts );
			return null;

		} else if( cl.hasOption( OPT_L ) ) {
			if( cl.getArgs().length != 1 && cl.getArgs().length != 2 ) {
				throw new IllegalArgumentException( "You must specify one or two parameters." );
			}
		}

		return null;
	}
}
