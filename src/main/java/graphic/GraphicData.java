package graphic;

public class GraphicData<T extends Number, U extends Number> {

    private String windowTitle = "";
    private NumberAxisData xAxis;
    private NumberAxisData yAxis;

    private LineGraphicData<T, U> graphicData[];

    public GraphicData(LineGraphicData<T, U>[] graphicData) {
        this.graphicData = graphicData;
    }

    public GraphicData windowTitle(String windowTitle) {
        this.windowTitle = windowTitle;
        return this;
    }

    public GraphicData xAxis(NumberAxisData xAxis) {
        this.xAxis = xAxis;
        return this;
    }

    public GraphicData yAxis(NumberAxisData yAxis) {
        this.yAxis = yAxis;
        return this;
    }

    public void createGraphic() {
        GraphicCreator graphicCreator = new GraphicCreator(this);
        graphicCreator.createGraphic();
    }

    public String getWindowTitle() {
        return windowTitle;
    }

    public LineGraphicData<T, U>[] getGraphicData() {
        return graphicData;
    }

    public NumberAxisData getxAxis() {
        return xAxis;
    }

    public NumberAxisData getyAxis() {
        return yAxis;
    }

    public static class NumberAxisData {
        private String title = "";

        private Double lowerBound;
        private Double upperBound;
        private Double tickUnit;

        public NumberAxisData(String title) {
            this.title = title;
        }

        public NumberAxisData lowerBound(double lowerBound) {
            this.lowerBound = lowerBound;
            return this;
        }

        public NumberAxisData upperBound(double upperBound) {
            this.upperBound = upperBound;
            return this;
        }

        public NumberAxisData tickUnit(double tickUnit) {
            this.tickUnit = tickUnit;
            return this;
        }

        public String getTitle() {
            return title;
        }

        public Double getLowerBound() {
            return lowerBound;
        }

        public Double getUpperBound() {
            return upperBound;
        }

        public Double getTickUnit() {
            return tickUnit;
        }

        public boolean isAutoRange() {
            return lowerBound == null && upperBound == null && tickUnit == null;
        }
    }
}
