package utils;

import static org.testng.Assert.*;
import org.testng.annotations.*;
import org.testng.annotations.Test;
import static utils.BigIntegerUtils.*;

import java.math.BigInteger;

public class BigIntegerUtilTest {

    @DataProvider
    public static Object[][] quickExpModNullPointerException() {
        return new Object[][] {
                {null, BigInteger.ONE, BigInteger.TWO, null},
                {BigInteger.ONE, null, BigInteger.TWO, null},
                {BigInteger.TWO, BigInteger.ONE, null, null},
                {null, null, null, null}
        };
    }

    @DataProvider
    public static Object[][] quickExpModArithmeticException() {
        return new Object[][] {
                {BigInteger.ZERO, BigInteger.valueOf(-1), BigInteger.TWO, null},
                {BigInteger.ONE, BigInteger.ONE, BigInteger.ZERO, null},
                {BigInteger.TWO, BigInteger.valueOf(-1), BigInteger.ZERO, null},
                {BigInteger.TWO, BigInteger.valueOf(3), BigInteger.valueOf(-1), null}
        };
    }

    @DataProvider
    public static Object[][] quickExpModPass() {
        return new Object[][] {
                {BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ZERO},
                {BigInteger.valueOf(3), BigInteger.valueOf(6), BigInteger.valueOf(7), BigInteger.ONE},
                {BigInteger.valueOf(8), BigInteger.ZERO, BigInteger.valueOf(3), BigInteger.ONE},
                {BigInteger.valueOf(8), BigInteger.ONE, BigInteger.valueOf(3), BigInteger.TWO},
                {BigInteger.ZERO, BigInteger.ZERO, BigInteger.valueOf(3), BigInteger.ONE},
                {BigInteger.valueOf(4), BigInteger.valueOf(13), BigInteger.valueOf(497), BigInteger.valueOf(445)}
        };
    }

    @DataProvider
    public static Object[][] quickExpModFail() {
        return new Object[][] {
                {BigInteger.TWO, BigInteger.valueOf(5), BigInteger.valueOf(3), BigInteger.ZERO},
                {BigInteger.valueOf(3), BigInteger.valueOf(6), BigInteger.valueOf(7), BigInteger.ZERO},
                {BigInteger.valueOf(8), BigInteger.ONE, BigInteger.valueOf(3), BigInteger.ONE},
                {BigInteger.valueOf(8), BigInteger.ZERO, BigInteger.valueOf(3), BigInteger.TWO}
        };
    }

    @DataProvider
    public static Object[][] MillerRabinTest_1_IllegalArgumentException() {
        return new Object[][] {
                {BigInteger.valueOf(-3)},
                {BigInteger.ZERO}
        };
    }

    @DataProvider
    public static Object[][] MillerRabinTest_1_Fail() {
        return new Object[][] {
                {BigInteger.ONE, true},
                {BigInteger.valueOf(7), false},
                {BigInteger.valueOf(4), true},
                {BigInteger.valueOf(961), true}
        };
    }

    @DataProvider
    public static Object[][] MillerRabinTest_1_Pass() {
        return new Object[][] {
                {BigInteger.TWO, true},
                {BigInteger.valueOf(3), true},
                {BigInteger.valueOf(4), false},
                {BigInteger.valueOf(691), true}
        };
    }

    @BeforeMethod
    public void setUp() {
    }

    @AfterMethod
    public void tearDown() {
    }

    @Test(dataProvider = "quickExpModNullPointerException", expectedExceptions = NullPointerException.class)
    public void testQuickExpModNPE(BigInteger base, BigInteger power, BigInteger mod, BigInteger result) {
        assertEquals(result, quickExpMod(base, power, mod));
    }

    @Test(dataProvider = "quickExpModArithmeticException", expectedExceptions = ArithmeticException.class)
    public void testQuickExpArithmeticException(BigInteger base, BigInteger power, BigInteger mod, BigInteger result) {
        assertEquals(result, quickExpMod(base, power, mod));
    }

    @Test(dataProvider = "quickExpModPass")
    public void testQuickExpModPass(BigInteger base, BigInteger power, BigInteger mod, BigInteger result) {
        assertEquals(result, quickExpMod(base, power, mod));
    }

    @Test(dataProvider = "quickExpModFail")
    public void testQuickExpModFail(BigInteger base, BigInteger power, BigInteger mod, BigInteger result) {
        assertEquals(result, quickExpMod(base, power, mod));
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testMillerRabinTest() {
        assertEquals(null, MillerRabinTest(null));
    }

    @Test(dataProvider = "MillerRabinTest_1_IllegalArgumentException", expectedExceptions = IllegalArgumentException.class)
    public void testMillerRabinTestIllegalArgumentException(BigInteger number) {
        assertEquals(null, MillerRabinTest(number));
    }

    @Test(dataProvider = "MillerRabinTest_1_Fail")
    public void testMillerRabinTestFail(BigInteger number, boolean isPrime) {
        assertEquals(isPrime, MillerRabinTest(number));
    }

    @Test(dataProvider = "MillerRabinTest_1_Pass")
    public void testMillerRabinTestPass(BigInteger number, boolean isPrime) {
        assertEquals(isPrime, MillerRabinTest(number));
    }
}