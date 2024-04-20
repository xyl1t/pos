package pkgBankTest;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pkgBank.Validation;

public class ValidationAmountTest {
	
	Validation v = null;

	@Before
	public void setUp() throws Exception {
		System.out.println("=== setUp ===");
		v = new Validation();
	}

	@After
	public void tearDown() throws Exception {
		System.out.println("=== tearDown ===");
		v = null;
	}

	@Test
	public void testIsAmountValid_LowerBountInvalid() {
		System.out.println("=== test lower bound invalid ===");
		assertEquals(false, v.isAmountValid(BigDecimal.valueOf(-0.01))); // 'x'
		assertEquals(false, v.isAmountValid(BigDecimal.valueOf(0.0)));
		assertEquals(false, v.isAmountValid(BigDecimal.valueOf(-0.02))); // 'y'
		assertEquals(false, v.isAmountValid(BigDecimal.valueOf(-0.03))); // 'y'
//		fail("Not yet implemented");
	}

	@Test
	public void testIsAmountValid_LowerBountValid() {
		System.out.println("=== test lower bound valid ===");
		assertEquals(true, v.isAmountValid(BigDecimal.valueOf(0.01)));
		assertEquals(true, v.isAmountValid(BigDecimal.valueOf(0.02)));
		assertEquals(true, v.isAmountValid(BigDecimal.valueOf(1)));
	}

	@Test
	public void testIsAmountValid_UpperBountInvalid() {
		System.out.println("=== test upper bound invalid ===");
		assertEquals(false, v.isAmountValid(BigDecimal.valueOf(500.01)));
		assertEquals(false, v.isAmountValid(BigDecimal.valueOf(501)));
	}

	@Test
	public void testIsAmountValid_UpperBountValid() {
		System.out.println("=== test upper bound valid ===");
		assertEquals(true, v.isAmountValid(BigDecimal.valueOf(499)));
		assertEquals(true, v.isAmountValid(BigDecimal.valueOf(499.99)));
		assertEquals(true, v.isAmountValid(BigDecimal.valueOf(500)));
	}

	@Test
	public void testIsAmountValid_DecimalPosition() {
		System.out.println("=== test decimal position ===");
		assertEquals(true,  v.isAmountValid(BigDecimal.valueOf(499.9)));
		assertEquals(false, v.isAmountValid(BigDecimal.valueOf(499.999)));
		assertEquals(true,  v.isAmountValid(BigDecimal.valueOf(499.990)));
		assertEquals(true,  v.isAmountValid(BigDecimal.valueOf(499.000)));
	}
}
