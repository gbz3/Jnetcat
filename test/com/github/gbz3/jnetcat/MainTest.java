package com.github.gbz3.jnetcat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Dummy
interface D {
	// block
	void b() throws Exception;
}

public class MainTest {
	private static final Logger log = LoggerFactory.getLogger( MainTest.class );

	private static void assertThrown( String comment, D bk ) {
		try {
			bk.b();
			org.junit.Assert.fail( comment );
		} catch ( Exception e ) {
			// 例外が発生したら成功
			log.error( e.toString() );
		}
	}

	@Test
	public void test() throws Exception {
		// none
		assertThrown( "no param", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{} ); }} );
		assertThrown( "no param", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ null } ); }} );
		assertThrown( "no param", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ null, null } ); }} );
		assertThrown( "no param", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "-" } ); }} );
		assertThrown( "no param", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "--" } ); }} );
		assertThrown( "no param", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "---" } ); }} );
	}

	@Test
	public void testHelp() throws Exception {
		// '-h'
		assertEquals( "-h", null, Main.parseCommandLine( Main.getOptions(), new String[]{ "-h" } ) );
		assertEquals( "-h", null, Main.parseCommandLine( Main.getOptions(), new String[]{ "--h" } ) );

		assertThrown( "-h", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "--help" } ); }} );
		assertThrown( "-h", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "-H" } ); }} );
		assertThrown( "-h", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "--H" } ); }} );
	}

	@Test
	public void testReceiver() throws Exception {
		// '-l'
		assertTrue( "-l", Main.parseCommandLine( Main.getOptions(), new String[]{ "-l", "1" } ) instanceof DataReceiverConfig );
		assertTrue( "-l", Main.parseCommandLine( Main.getOptions(), new String[]{ "-l", "65535" } ) instanceof DataReceiverConfig );
		assertTrue( "-l", Main.parseCommandLine( Main.getOptions(), new String[]{ "--l", "1" } ) instanceof DataReceiverConfig );
		assertTrue( "-l", Main.parseCommandLine( Main.getOptions(), new String[]{ "--l", "65535" } ) instanceof DataReceiverConfig );

		assertThrown( "-l", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "-l", "-1" } ); }} );
		assertThrown( "-l", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "-l", "0" } ); }} );
		assertThrown( "-l", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "-l", "65536" } ); }} );
		assertThrown( "-l", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "--l", "-1" } ); }} );
		assertThrown( "-l", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "--l", "0" } ); }} );
		assertThrown( "-l", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "--l", "65536" } ); }} );
		assertThrown( "-l", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "--listen" } ); }} );
		assertThrown( "-l", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "-L" } ); }} );
		assertThrown( "-l", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "--L" } ); }} );

		// '-l'(optional)
		assertTrue( "-l", Main.parseCommandLine( Main.getOptions(), new String[]{ "-l", "1", "0.0.0.0" } ) instanceof DataReceiverConfig );

		assertThrown( "-l", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "-l", "1", null } ); }} );
		assertThrown( "-l", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "-l", "1", "" } ); }} );
		assertThrown( "-l", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "-l", "1", "0.0.0" } ); }} );
		assertThrown( "-l", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "-l", "1", "0.0.0.0.0" } ); }} );
		assertThrown( "-l", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "-l", "1", "-1.0.0.0" } ); }} );
		assertThrown( "-l", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "-l", "1", "0.-1.0.0" } ); }} );
		assertThrown( "-l", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "-l", "1", "0.0.-1.0" } ); }} );
		assertThrown( "-l", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "-l", "1", "0.0.0.-1" } ); }} );
		assertThrown( "-l", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "-l", "1", "256.0.0.0" } ); }} );
		assertThrown( "-l", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "-l", "1", "0.256.0.0" } ); }} );
		assertThrown( "-l", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "-l", "1", "0.0.256.0" } ); }} );
		assertThrown( "-l", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "-l", "1", "0.0.0.256" } ); }} );
		assertThrown( "-l", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "-l", "1", "O.O.O.O" } ); }} );
	}

	@Test
	public void testSender() throws Exception {
		// Sender(port)
		assertTrue( "Sender", Main.parseCommandLine( Main.getOptions(), new String[]{ "0.0.0.0", "1" } ) instanceof DataSenderConfig );
		assertTrue( "Sender", Main.parseCommandLine( Main.getOptions(), new String[]{ "0.0.0.0", "65535" } ) instanceof DataSenderConfig );

		assertThrown( "Sender", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "0.0.0.0", "-1" } ); }} );
		assertThrown( "Sender", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "0.0.0.0", "0" } ); }} );
		assertThrown( "Sender", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "0.0.0.0", "65536" } ); }} );

		// Sender(ip)
		assertTrue( "Sender", Main.parseCommandLine( Main.getOptions(), new String[]{ "255.255.255.255", "1" } ) instanceof DataSenderConfig );

		assertThrown( "Sender", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ null, "1" } ); }} );
		assertThrown( "Sender", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "", "1" } ); }} );
		assertThrown( "Sender", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "0.0.0", "1" } ); }} );
		assertThrown( "Sender", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "0.0.0.0.0", "1" } ); }} );
		assertThrown( "Sender", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "-1.0.0.0", "1" } ); }} );
		assertThrown( "Sender", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "0.-1.0.0", "1" } ); }} );
		assertThrown( "Sender", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "0.0.-1.0", "1" } ); }} );
		assertThrown( "Sender", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "0.0.0.-1", "1" } ); }} );
		assertThrown( "Sender", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "256.0.0.0", "1" } ); }} );
		assertThrown( "Sender", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "0.256.0.0", "1" } ); }} );
		assertThrown( "Sender", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "0.0.256.0", "1" } ); }} );
		assertThrown( "Sender", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "0.0.0.256", "1" } ); }} );
		assertThrown( "Sender", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "O.O.O.O", "1" } ); }} );

	}

}
