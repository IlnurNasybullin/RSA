package timeTesting;

import comparisonsAlgorithms.ComparisonSolution;

import java.math.BigInteger;
import java.util.function.LongSupplier;

public class AlgorithmTesting {

    private ComparisonSolution solver;

    private long timeLimit;
    private boolean enable;
    private LongSupplier disabledTime;

    public AlgorithmTesting(ComparisonSolution solver, long timeLimit) {
        this.solver = solver;
        this.timeLimit = timeLimit;
        enable = true;

        disabledTime = this::getTimeLimit;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setDisabledTime(LongSupplier disabledTime) {
        this.disabledTime = disabledTime;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public long getTime(BigInteger a, BigInteger b, BigInteger p) {
        if (enable) {
            return getEnableTime(a, b, p);
        } else {
            System.out.println("Превышен лимит ожидания!");
            return disabledTime.getAsLong();
        }
    }

    private long getEnableTime(BigInteger a, BigInteger b, BigInteger p) {
        long start = System.nanoTime();
        BigInteger x = solver.solute(a, b, p);
        long finish = System.nanoTime();
        System.out.printf("x = %s (%s)\n", x, solver.getName());

        long time = finish - start;
        if (time >= timeLimit) {
            enable = false;
        }

        return time;
    }

    public long getTimeLimit() {
        return timeLimit;
    }

    public String getName() {
        return solver.getName();
    }
}
