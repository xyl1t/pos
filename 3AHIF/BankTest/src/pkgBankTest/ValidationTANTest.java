package pkgBankTest;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pkgBank.Validation;

public class ValidationTANTest {

	Validation val = null;
	@Before
	public void setUp() throws Exception {
		System.out.println("=== init test tan =====");
		val = new Validation();
	}

	@After
	public void tearDown() throws Exception {
		System.out.println("=== end test tan =====");
	}

	@Test
	public final void testIsTANValidEC1() {
		System.out.println("=== EC1 TAN ok =====");
		assertEquals(true,val.isTANValid("10000"));
		assertEquals(true,val.isTANValid("10001"));
		assertEquals(true,val.isTANValid("99990"));
		assertEquals(true,val.isTANValid("99999"));
	}

	@Test
public final void testIsTANValidEC2() {
		System.out.println("=== EC2 TAN not ok =====");
		assertEquals(false,val.isTANValid("1000"));
		assertEquals(false,val.isTANValid("9999"));
		assertEquals(false,val.isTANValid("01234"));
		assertEquals(false,val.isTANValid("012345"));
		assertEquals(false,val.isTANValid("00000"));
	}
}