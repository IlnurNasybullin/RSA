package comparisonsAlgorithms;

import utils.BigIntegerUtils;

import java.math.BigInteger;

public class EulerTheorem implements ComparisonSolution {

    @Override
    public String getName() {
        return "Теорема Эйлера";
    }

    @Override
    public BigInteger solute(BigInteger a, BigInteger b, BigInteger p) {
        if (b.compareTo(BigInteger.ZERO) == 0) {
            return BigInteger.ZERO;
        }

        BigInteger power = p.subtract(BigInteger.TWO);
        BigInteger x = BigIntegerUtils.quickExpMod(a, power, p);

        return x.multiply(b).mod(p);
    }
}
