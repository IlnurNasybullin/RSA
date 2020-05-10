package keyGenerator;

import utils.BigIntegerUtils;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Supplier;

public class RSAKeyGenerator implements Serializable{
    private transient PrivateKey privateKey;
    private transient PublicKey publicKey;

    private transient Supplier<BigInteger> getCloseExponent = () -> null;

    public static final int PRIME_NUMBER_BIT_LENGTH = 512;

    public void generateKeys() {
        generateKeys(PRIME_NUMBER_BIT_LENGTH, PRIME_NUMBER_BIT_LENGTH);
    }

    public void generateKeys(int pBitesLength, int qBitesLength) {
        BigInteger p = getBigPrimeNumber(pBitesLength);
        BigInteger q = getBigPrimeNumber(qBitesLength);

        BigInteger n = p.multiply(q);
        BigInteger f_n = n.subtract(p).subtract(q).add(BigInteger.ONE);

        BigInteger openExponent = getOpenExponent(f_n);
        BigInteger closeExponent = getCloseExponent.get();

        publicKey = new RSAPublicKey(openExponent, n);
        privateKey = new RSAPrivateKey(closeExponent, p, q);
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    private BigInteger getOpenExponent(BigInteger f_n) {
        BigInteger exponent;
        BigInteger[] xea;

        BigInteger upperBound = f_n.subtract(BigInteger.TWO);

        do {
            exponent = BigIntegerUtils.getRandomBigNumber(upperBound).add(BigInteger.TWO);
            xea = BigIntegerUtils.XEA(f_n, exponent);
        } while (xea[0].compareTo(BigInteger.ONE) != 0);

        final BigInteger y_koef = xea[2];

        setCloseExponentSupplier(y_koef, f_n);
        return exponent;
    }

    private void setCloseExponentSupplier(BigInteger y_koef, BigInteger f_n) {
        getCloseExponent = () -> y_koef.mod(f_n);
    }

    private BigInteger getBigPrimeNumber(int bitesLength) {
        BigInteger number;
        Random random = new SecureRandom();
        do {
            number = new BigInteger(bitesLength, random).setBit(bitesLength - 1);
        } while (!BigIntegerUtils.MillerRabinTest(number));

        return number;
    }

    private class RSAPublicKey implements PublicKey {
        private BigInteger openExponent;
        private BigInteger mod;

        private int upperLimitBytes;

        private RSAPublicKey(BigInteger openExponent, BigInteger mod) {
            this.mod = mod;
            this.openExponent = openExponent;

            this.upperLimitBytes = mod.bitLength() >> 3;
        }

        @Override
        public BigInteger[] encrypt(String text) {
            BigInteger[] numbers = RSAConverter.stringToBigIntegers(text, upperLimitBytes);
            int len = numbers.length;

            BigInteger[] encryptedNumbers = new BigInteger[len];
            for (int i = 0; i < len; i++) {
                encryptedNumbers[i] = BigIntegerUtils.quickExpMod(numbers[i], openExponent, mod);
            }

            return encryptedNumbers;
        }
    }

    private class RSAPrivateKey implements PrivateKey {
        private BigInteger mod_1;
        private BigInteger mod_2;
        private BigInteger closeExponent;

        private BigInteger multiply_1;
        private BigInteger multiply_2;

        private BigInteger mod;

        private RSAPrivateKey(BigInteger closeExponent, BigInteger mod_1, BigInteger mod_2) {
            this.mod = mod_1.multiply(mod_2);
            this.mod_1 = mod_1.max(mod_2);
            this.mod_2 = mod_1.min(mod_2);
            this.closeExponent = closeExponent;

            calculateMultiplies();
        }

        private void calculateMultiplies() {
            BigInteger[] xea = BigIntegerUtils.XEA(mod_1, mod_2);

            BigInteger inverseMod_1 = xea[1].mod(mod_2);
            BigInteger inverseMod_2 = xea[2].mod(mod_1);

            multiply_1 = mod_1.multiply(inverseMod_1);
            multiply_2 = mod_2.multiply(inverseMod_2);
        }

        @Override
        public String decrypt(BigInteger[] numbers) {
            int len = numbers.length;
            BigInteger[] decryptedNumbers = new BigInteger[len];

            for (int i = 0; i < len; i++) {
                decryptedNumbers[i] = getDecryptedNumber(numbers[i]);
            }

            return RSAConverter.bigIntegerToString(decryptedNumbers);
        }

        private BigInteger getDecryptedNumber(BigInteger number) {
            BigInteger r_1 = BigIntegerUtils.quickExpMod(number, closeExponent, mod_1);
            BigInteger r_2 = BigIntegerUtils.quickExpMod(number, closeExponent, mod_2);

            BigInteger x = r_1.multiply(multiply_2).add(r_2.multiply(multiply_1));

            return x.mod(mod);
        }
    }

    private static class RSAConverter {
        private static BigInteger[] stringToBigIntegers(String text, int upperLimitBytes) {
            byte[] symbols = text.getBytes();

            int symbolsCount = symbols.length;
            int bigIntegersCount = symbolsCount / (upperLimitBytes - 1);

            if (symbolsCount % (upperLimitBytes - 1) != 0) {
                bigIntegersCount++;
            }

            BigInteger[] numbers = new BigInteger[bigIntegersCount];

            int index = 0;
            int i;

            byte[] bytes;
            for(i = 0; i < (bigIntegersCount - 1); i++) {
                bytes = Arrays.copyOfRange(symbols, index, index += upperLimitBytes - 1);
                numbers[i] = new BigInteger(1, bytes);
            }

            numbers[i] = new BigInteger(1, Arrays.copyOfRange(symbols, index, symbolsCount));
            return numbers;
        }

        private static String bigIntegerToString(BigInteger[] numbers) {
            byte[][] bytes = new byte[numbers.length][];
            int byteCounts = 0;

            for (int i = 0; i < numbers.length; i++) {
                bytes[i] = numbers[i].toByteArray();
                byteCounts += bytes[i].length;
                if (bytes[i][0] == 0) {
                    byteCounts--;
                }
            }

            byte[] mergeBytes = new byte[byteCounts];
            int index = 0;
            for (int i = 0; i < bytes.length; i++) {
                int j = bytes[i][0] == 0 ? 1 : 0;
                for (;j < bytes[i].length; j++) {
                    mergeBytes[index++] = bytes[i][j];
                }
            }

            String text = new String(mergeBytes);
            return text;
        }
    }
}
