/**
 *
 */
package com.github.gbz3.jnetcat;

import java.net.InetAddress;

/**
 * @author gbz3
 *
 */
public class DataReceiverConfig implements RunnerConfig {

	static final DataReceiverConfig getInstance( int port ) {
		return getInstance( port, null );
	}

	static final DataReceiverConfig getInstance( int port, InetAddress addr ) {
		return new DataReceiverConfig();
	}

}
