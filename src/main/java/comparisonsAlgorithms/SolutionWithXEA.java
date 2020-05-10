package comparisonsAlgorithms;

import utils.BigIntegerUtils;

import java.math.BigInteger;

public class SolutionWithXEA implements ComparisonSolution {

    @Override
    public String getName() {
        return "Решение с помощью РАЕ";
    }

    @Override
    public BigInteger solute(BigInteger a, BigInteger b, BigInteger p) {
        BigInteger[] xea = BigIntegerUtils.XEA(a, p);
        BigInteger x = xea[2].multiply(b).mod(p);
        return x;
    }
}
