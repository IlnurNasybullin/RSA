package graphic;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GraphicCreator<T extends Number, U extends Number> extends Application {

    private static final String PNG_FILE_NAME = "./src/main/resources/graphic.png";
    private static GraphicData data;

    public GraphicCreator() { }

    public GraphicCreator(GraphicData<T, U> data) {
        this.data = data;
    }

    private NumberAxis getNumberAxis(GraphicData.NumberAxisData axisData) {
        NumberAxis axis = new NumberAxis();
        axis.setLabel(axisData.getTitle());

        if (!axisData.isAutoRange()) {
            Double data;
            axis.autoRangingProperty().set(false);

            if ((data = axisData.getLowerBound()) != null) {
                axis.setLowerBound(data);
            }

            if ((data = axisData.getUpperBound()) != null) {
                axis.setUpperBound(data);
            }

            if ((data = axisData.getTickUnit()) != null) {
                axis.setTickUnit(data);
            }
        }

        return axis;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle(data.getWindowTitle());
        primaryStage.setAlwaysOnTop(true);

        NumberAxis xNumberAxis = getNumberAxis(data.getxAxis());
        NumberAxis yNumberAxis = getNumberAxis(data.getyAxis());

        LineChart<Number, Number> numberLineChart = new LineChart<>(xNumberAxis, yNumberAxis);
        numberLineChart.setTitle(data.getWindowTitle());

        List<XYChart.Series> seriesList = new ArrayList<>();
        LineGraphicData[] graphicData = data.getGraphicData();

        for (LineGraphicData graphicumData: graphicData) {
            XYChart.Series series = new XYChart.Series();
            series.setName(graphicumData.getSerialName());
            seriesList.add(series);
        }

        for (int i = 0; i < graphicData.length; i++) {
            ObservableList<XYChart.Data> data = FXCollections.observableArrayList();
            List<T> xArray = graphicData[i].getX();
            List<U> yArray = graphicData[i].getY();

            for (int j = 0; j < yArray.size(); j++) {
                data.add(new XYChart.Data(xArray.get(j), yArray.get(j)));
            }

            seriesList.get(i).setData(data);
        }

        seriesList.forEach(numberLineChart.getData()::add);
        numberLineChart.setCreateSymbols(false);
        numberLineChart.setHorizontalGridLinesVisible(false);

        Scene scene = new Scene(numberLineChart, 1980, 1080);
        primaryStage.setScene(scene);

        primaryStage.show();
        saveInFile(primaryStage);
    }

    private void saveInFile(Stage primaryStage) {
        Image fxmlImage = primaryStage.getScene().snapshot(null);

        try(FileOutputStream outputStream = new FileOutputStream(PNG_FILE_NAME);) {
            ImageIO.write(SwingFXUtils.fromFXImage(fxmlImage, null), "PNG", outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void createGraphic() {
        launch();
    }
}
