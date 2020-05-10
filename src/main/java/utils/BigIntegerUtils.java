package utils;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;

public class BigIntegerUtils {
    //Максимальное число, до которого выполняется метод пробных делителей (для выявления простоты числа)
    private static final BigInteger MAX_NUMBER_FOR_SIMPLE_TEST = BigInteger.valueOf(256);

    public static BigInteger quickExpMod(BigInteger base, BigInteger power, BigInteger mod) {
        if (isNegative(power)) {
            throw new ArithmeticException("power is negative number!");
        }

        if (!isPositive(mod)) {
            throw new ArithmeticException("mod isn't positive number");
        }

        BigInteger res = BigInteger.ONE;
        BigInteger a = base;

        for (int i = 0; i < power.bitLength(); i++) {
            if (power.testBit(i)) {
                res = res.multiply(a).mod(mod);
            }
            a = a.multiply(a).mod(mod);
        }

        return res;
    }

    /**
     * Вероятностный тест Миллера-Рабина для выявления простоты числа. Количество раундов теста -
     * log<sub>2</>number. Реализацию см. в методе {@link BigIntegerUtils#MillerRabinTest(BigInteger, int)}
     * @param number - число, проверямое на простоту
     * @return результат теста: вероятностно простое (true) или составное (false)
     */
    public static boolean MillerRabinTest(BigInteger number) {
        return MillerRabinTest(number, number.bitLength() - 1);
    }

    /**
     * Вероятностный тест Миллера-Рабина для выявления простоты числа с заданным количеством раундов. Вероятность
     * ошибки не превышает 4<sup>-rounds</sup>.
     * @param number - число, проверямое на простоту
     * @param rounds - количество раундов в тесте
     * @return результат теста: вероятностно простое (true) или составное (или 1) (false)
     */
    public static boolean   MillerRabinTest(BigInteger number, int rounds) {
        if (!isPositive(number)) {
            throw new IllegalArgumentException("number isn't positive");
        }

        if (equals(number, BigInteger.TWO)) {
            return true;
        }

        if (!number.testBit(0) || number.bitLength() < 2) {
            return false;
        }

        if (number.compareTo(MAX_NUMBER_FOR_SIMPLE_TEST) < 0) {
            return isPrimeTrialDivision(number.intValue());
        }

        int s = 0;
        BigInteger lastNumber = number.subtract(BigInteger.ONE);
        while (!lastNumber.testBit(s)) {
            s++;
        }

        BigInteger d = lastNumber.shiftRight(s);

        int round_index = 0;
        boolean isPrime = true;
        while (isPrime && round_index < rounds) {
            isPrime = MillerRabinRound(number, d, s);
            round_index++;
        }

        return isPrime;
    }

    private static boolean isPrimeTrialDivision(int number) {
        int maxDivider = (int)Math.sqrt(number);

        int i = 3;
        boolean isPrime = true;
        while (isPrime && (i <= maxDivider)) {
            isPrime = (number % i) != 0;
            i += 2;
        }

        return isPrime;
    }

    //простое ли число (с точки зрения раунда)
    private static boolean MillerRabinRound(BigInteger number, BigInteger d, int s) {
        BigInteger randomNumber = getRandomBigNumber(number.subtract(BigInteger.TWO)).add(BigInteger.TWO);
        BigInteger lastNumber = number.subtract(BigInteger.ONE);

        BigInteger x = quickExpMod(randomNumber, d, number);

        if (equals(x, BigInteger.ONE) || equals(x, lastNumber)) {
            return true;
        }
        
        boolean isPrime = false;
        int count = 0;

        while (!isPrime && count < s) {
            x = quickExpMod(x, BigInteger.TWO, number);
            isPrime = equals(x, lastNumber);
            count++;
        }

        return isPrime;
    }

    /**
     * Возвращает случайное число {@link BigInteger} от [0, max)
     * @param max - верхняя граница для генерации случайного числа
     * @return случайное число
     */
    public static BigInteger getRandomBigNumber(BigInteger max) {
        if (max.compareTo(BigInteger.ZERO) <= 0) {
            throw new IllegalArgumentException("upper bound number mist be more than 0!");
        }

        int n = max.bitLength();
        Random random = new SecureRandom();
        return new BigInteger(n, random).mod(max);
    }

    /**
     * Метод, реализующий расширенный алгоритм Евклида и возвращающий в качестве ответа массив, содержащий следующие
     * значения: НОД(a,b), и два коэффициента Безу - x и y. Коэффициент x относится к большему числу,
     * коэффициент y - к меньшему.
     * @param a - первое число
     * @param b - второе число
     * @return массив содержащий НОД(a, b) и коэффициенты Безу.
     */
    public static BigInteger[] XEA(final BigInteger a, final BigInteger b) {
        BigInteger a_0 = a.max(b);
        BigInteger b_0 = a.min(b);

        BigInteger x_0 = BigInteger.ONE, y_0 = BigInteger.ZERO, x_1 = BigInteger.ZERO, y_1 = BigInteger.ONE;
        BigInteger temp;
        BigInteger q_r[];

        while (!b_0.equals(BigInteger.ZERO)) {
            q_r = a_0.divideAndRemainder(b_0);
            a_0 = b_0;
            b_0 = q_r[1];

            temp = x_0;
            x_0 = x_1;
            x_1 = temp.subtract(q_r[0].multiply(x_1));

            temp = y_0;
            y_0 = y_1;
            y_1 = temp.subtract(q_r[0].multiply(y_1));
        }

        return new BigInteger[]{a_0, x_0, y_0};
    }

    public static boolean isPositive(BigInteger number) {
        return number.compareTo(BigInteger.ZERO) > 0;
    }

    public static boolean isNegative(BigInteger number) {
        return number.compareTo(BigInteger.ZERO) < 0;
    }
    public static boolean equals(BigInteger one, BigInteger two) {
        return one.compareTo(two) == 0;
    }
}
