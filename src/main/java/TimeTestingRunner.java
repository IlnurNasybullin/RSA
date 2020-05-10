import comparisonsAlgorithms.EulerTheorem;
import comparisonsAlgorithms.IterationMethod;
import comparisonsAlgorithms.SolutionWithXEA;
import graphic.GraphicData;
import graphic.LineGraphicData;
import timeTesting.AlgorithmTesting;
import timeTesting.TimeTesting;

public class TimeTestingRunner {

    private final static int MAX_BIT_LENGTH = 1024;
    private final static long TIME_LIMIT = 5_000_000_000L;

    public static void main(String[] args) {
        AlgorithmTesting iterationAlgorithm = new AlgorithmTesting(new IterationMethod(), TIME_LIMIT);
        AlgorithmTesting eulerFunction = new AlgorithmTesting(new EulerTheorem(), TIME_LIMIT);
        AlgorithmTesting xeaSolution = new AlgorithmTesting(new SolutionWithXEA(), TIME_LIMIT);

        TimeTesting timeTesting = new TimeTesting(new AlgorithmTesting[]{iterationAlgorithm, eulerFunction, xeaSolution}, MAX_BIT_LENGTH);
        timeTesting.start();
        LineGraphicData[] data = timeTesting.getData();

        GraphicData.NumberAxisData xAxis = new GraphicData.NumberAxisData("Длина простого числа (в битах)")
                .lowerBound(2)
                .upperBound(MAX_BIT_LENGTH)
                .tickUnit(16);
        GraphicData.NumberAxisData yAxis = new GraphicData.NumberAxisData("Время работы алгоритма (в нс)")
                .lowerBound(1_000d)
                .upperBound(1_000_000d)
                .tickUnit(1_000d);

        GraphicData<Integer, Long> graphicCreator = new GraphicData<>(data).
                windowTitle("График зависимости времени решения сравнения с одним неизвестным от длины простого числа")
                .xAxis(xAxis)
                .yAxis(yAxis);
        graphicCreator.createGraphic();
    }
}
