/**
 *
 */
package com.github.gbz3.jnetcat;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
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

	private static final String OPT_h = "h";
	private static final String OPT_l = "l";

	/**
	 * @param args
	 */
	public static void main( String[] args ) {

		RunnerConfig config;
		try {
			config = parseCommandLine( getOptions(), args );
			log.debug( "starting runner by the {}.", config );

		} catch ( Exception e ) {
			System.err.println( e.toString() );
			System.exit( 1 );
		}

		log.debug( "finished {}.", Main.class );
		System.exit( 0 );
	}

	static Options getOptions() {
		final Options result = new Options();
		result.addOption( OPT_h, false, "print this help." );
		result.addOption( OPT_l, true, "listen-mode. it must specify one or two parameters." );
		return result;
	}

	/**
	 * @param opts
	 * @param args
	 * @return
	 * @throws ParseException
	 * @throws UnknownHostException
	 */
	static RunnerConfig parseCommandLine( Options opts, String[] args ) throws ParseException, UnknownHostException {
		final CommandLine cl = new GnuParser().parse( opts, args );

		if( cl.hasOption( OPT_h ) ) {
			new HelpFormatter().printHelp( "Jnetcat [ -h ] [ -l listenPort [ listenAddr ] ] ", opts );
			return null;

		} else if( cl.hasOption( OPT_l ) ) {
			if( 2 <= cl.getArgs().length ) throw new IllegalArgumentException( "too much parameters for '-l'" );

			// Check listen port
			final int listenPort = Integer.parseInt( cl.getOptionValue( OPT_l ).trim() );
			if( listenPort < 1 || 65535 < listenPort ) throw new IllegalArgumentException( "listenPort range ( 1-65535 ): listenPort=" + listenPort );

			// Check bind address (optional)
			if( cl.getArgs().length == 1 ) {
				final String[] b = cl.getArgs()[0].split( "\\." );
				if( b.length != 4 ) {
					System.err.println( "bindAddr is not InetAddress format." );
					return null;
				}
//				for( int i = 0; i < 4; i++ ) {
//					if( b[i] < 0 || 255 < b[i] ) {
//
//					}
//				}
				final InetAddress bindAddr = InetAddress.getByName( cl.getArgs()[1] );
				log.debug( "bindAddr=[{}]", bindAddr );
				return DataReceiverConfig.getInstance( listenPort, bindAddr );
			}

			return DataReceiverConfig.getInstance( listenPort );

		} else if( cl.getArgs().length == 2 ) {
			return DataSenderConfig.getInstance();
		}

		throw new IllegalArgumentException( "invalid parameter." + cl.getArgList() );
	}

}
