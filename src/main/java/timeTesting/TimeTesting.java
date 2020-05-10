package timeTesting;

import graphic.LineGraphicData;
import utils.BigIntegerUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TimeTesting {

    private int bitLengthLimit;
    private AlgorithmTesting[] algorithms;
    private List<Integer>[] xData;
    private List<Long>[] yData;

    public TimeTesting(AlgorithmTesting[] algorithms, int bitLengthLimit) {
        this.bitLengthLimit = bitLengthLimit;
        this.algorithms = algorithms;
    }

    public void start() {
        BigInteger a;
        BigInteger b;
        BigInteger p;

        int startBit = 2;

        xData = new List[algorithms.length];
        yData = new List[algorithms.length];

        for (int i = 0; i < algorithms.length; i++) {
            yData[i] = new ArrayList<>();
        }

        List<Integer> xGeneralData = new ArrayList<>();

        for (int i = startBit; i <= bitLengthLimit; i++) {
            xGeneralData.add(i);

            p = getRandomPrimeNumber(i);
            a = BigIntegerUtils.getRandomBigNumber(p.subtract(BigInteger.ONE)).add(BigInteger.ONE);
            b = BigIntegerUtils.getRandomBigNumber(p);

            System.out.println();
            System.out.printf("Количество бит числа p - %d\n", i);

            System.out.printf("%s * x = %s mod %s\n", a, b, p);

            for (int j = 0; j < algorithms.length; j++) {
                if (!algorithms[j].isEnable()) {
                    continue;
                }
                long time = algorithms[j].getTime(a, b, p);
                yData[j].add(time);

                System.out.printf("Время выполнения алгоритма - %d нс\n", time);
            }
        }

        for (int i = 0; i < algorithms.length; i++) {
            xData[i] = xGeneralData;
        }
    }

    private BigInteger getRandomPrimeNumber(int bitesLength) {
        BigInteger number;
        Random random = new Random();
        do {
            number = new BigInteger(bitesLength, random).setBit(bitesLength - 1);
        } while (!BigIntegerUtils.MillerRabinTest(number));

        return number;
    }

    public LineGraphicData<Integer, Long>[] getData() {
        LineGraphicData<Integer, Long>[] data = new LineGraphicData[algorithms.length];
        for (int i = 0; i < algorithms.length; i++) {
            data[i] = new LineGraphicData<>(xData[i], yData[i], algorithms[i].getName());
        }

        return data;
    }

}
