package pkgMathTest;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pkgMath.Math;

public class ValidationPrimeNumber {

	Math m;

	@Before
	public void setUp() throws Exception {
		m = new Math();
	}

	@After
	public void tearDown() throws Exception {
		m = null;
	}

	boolean isPrime(int n)
    {
        if (n <= 1)
            return false;
  
        for (int i = 2; i < n; i++)
            if (n % i == 0)
                return false;
  
        return true;
    }
  
	@Test
	public void testIsPrimeNumber() throws Exception {
		System.out.println("=== test is prime number ===");
		for (int i = 2; i <= 100; i++) {
			boolean actual = m.isPrimeNumber(i);
			boolean expected = isPrime(i);
			if (actual != expected)
				System.out.println(i);
			assertEquals(expected, actual);

		}
	}

	@Test
	public void testGetNextPrimeNumber() throws Exception {
		System.out.println("=== test get next prime number ===");
		for (int i = 2; i <= 100; i++) {
			long prim = m.getNextPrimeNumber(i);
			boolean isPrime = isPrime(i);

			if (isPrime)
				assertEquals(true, prim == i);
			else 
				assertEquals(false, prim == i);

		}
	}
}
