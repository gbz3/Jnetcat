/**
 *
 */
package com.github.gbz3.jnetcat;

import java.net.InetAddress;

/**
 * @author gbz3
 *
 */
public class DataSenderConfig implements RunnerConfig {

	static final DataSenderConfig getInstance( InetAddress addr, int port ) {
		return new DataSenderConfig();
	}

}
