package pkgBankTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ValidationAmountTest.class, ValidationTANTest.class, ValidationPasswordTest.class })
public class AllTests {

}
