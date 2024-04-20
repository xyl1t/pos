package pkgBankTest;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pkgBank.Validation;

public class ValidationPasswordTest {

	Validation val = null;
	@Before
	public void setUp() throws Exception {
		val = new Validation();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testIsPasswordValidOK() {
		System.out.println("=== EC1 pwd ok =====");
		assertEquals(true,val.isPasswordValid("AlfR1"));
		assertEquals(true,val.isPasswordValid("alfR0"));
		assertEquals(true,val.isPasswordValid("AAAAAAAAAAAAaaaaaaaaaaaa2"));
		assertEquals(true,val.isPasswordValid("alfaaaaaRRRRRRRRRRRRRRRR1"));
		assertEquals(true,val.isPasswordValid("00000000000000000000000000zZ"));
	}
	@Test
	public void testIsPasswordValidNotOK() {
		System.out.println("=== EC1 pwd not ok =====");
		assertEquals(false,val.isPasswordValid("AlR1"));
		assertEquals(false,val.isPasswordValid("AlfRr"));
		assertEquals(false,val.isPasswordValid("alfa0"));
		assertEquals(false,val.isPasswordValid("ALFA0"));
		assertEquals(false,val.isPasswordValid("alfaaaaaRRRRRRRRRRRRRRRR1."));
		assertEquals(false,val.isPasswordValid(".00000000000000000000000000zZ"));
		assertEquals(false,val.isPasswordValid("00000000000000zZ 000000000000zZ"));

	}
}
