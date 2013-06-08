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
	public void testParamCombination() throws Exception {
		// none
		assertThrown( "no param", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{} ); }} );
		assertThrown( "no param", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ null } ); }} );
		assertThrown( "no param", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ null, null } ); }} );
		assertThrown( "no param", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "-" } ); }} );
		assertThrown( "no param", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "--" } ); }} );
		assertThrown( "no param", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "---" } ); }} );

		// single - '-h'
		assertEquals( "-h", null, Main.parseCommandLine( Main.getOptions(), new String[]{ "-h" } ) );
		assertEquals( "-h", null, Main.parseCommandLine( Main.getOptions(), new String[]{ "--h" } ) );
		assertThrown( "-h", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "--help" } ); }} );
		assertThrown( "-h", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "-H" } ); }} );
		assertThrown( "-h", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "--H" } ); }} );

		// single - '-l'
		assertThrown( "-l", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "-l", "-1" } ); }} );
		assertThrown( "-l", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "-l", "0" } ); }} );
		assertTrue( "-l", Main.parseCommandLine( Main.getOptions(), new String[]{ "-l", "1" } ) instanceof DataReceiverConfig );
		assertTrue( "-l", Main.parseCommandLine( Main.getOptions(), new String[]{ "-l", "65535" } ) instanceof DataReceiverConfig );
		assertThrown( "-l", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "-l", "65536" } ); }} );
		assertThrown( "-l", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "--l", "-1" } ); }} );
		assertThrown( "-l", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "--l", "0" } ); }} );
		assertTrue( "-l", Main.parseCommandLine( Main.getOptions(), new String[]{ "--l", "1" } ) instanceof DataReceiverConfig );
		assertTrue( "-l", Main.parseCommandLine( Main.getOptions(), new String[]{ "--l", "65535" } ) instanceof DataReceiverConfig );
		assertThrown( "-l", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "--l", "65536" } ); }} );
		assertThrown( "-l", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "--listen" } ); }} );
		assertThrown( "-l", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "-L" } ); }} );
		assertThrown( "-l", new D() { public void b() throws Exception { Main.parseCommandLine( Main.getOptions(), new String[]{ "--L" } ); }} );

		// one - success
		assertTrue( Main.parseCommandLine( Main.getOptions(), new String[]{ "0.0.0.0", "1234" } ) instanceof DataSenderConfig );

		// one - fail
	}

	/**
	 * test -h.
	 */
//	@Test
	public void test_h() throws Exception {
		assertEquals( null, Main.parseCommandLine( Main.getOptions(), new String[]{ "-l 1234" } ) );
	}

}
