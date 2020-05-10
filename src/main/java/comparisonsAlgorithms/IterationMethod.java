package comparisonsAlgorithms;

import java.math.BigInteger;

public class IterationMethod implements ComparisonSolution {
    @Override
    public String getName() {
        return "Метод перебора";
    }

    @Override
    public BigInteger solute(BigInteger a, BigInteger b, BigInteger p) {
        if (b.compareTo(BigInteger.ZERO) == 0) {
            return BigInteger.ZERO;
        }

        BigInteger base = a;
        BigInteger x = BigInteger.ONE;

        int delta = 0;
        while (base.compareTo(b) != 0) {
            base = base.add(a).mod(p);
            delta++;

            if (delta == Integer.MAX_VALUE) {
                x = x.add(BigInteger.valueOf(delta));
                delta = 0;
            }
        }
        x = x.add(BigInteger.valueOf(delta));

        return x;
    }
}
