package com.example.controller;

import com.example.util.StatisticsConverter;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.IOException;

@Component
@FxmlView("/view/statisticsView.fxml")
public class StatisticsController {
    @FXML
    private Button exportButton;
    @FXML
    private LineChart<Number, Number> opinionChart;

    private int nextX = 0;
    private final XYChart.Series<Number, Number> supporting = new XYChart.Series<>();

    private void exportStats() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Statistics");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv"));
        File file = fileChooser.showSaveDialog(this.exportButton.getScene().getWindow());
        if (file != null) {
            StatisticsConverter.exportStats(file, supporting);
        }
    }

    @FXML
    public void initialize() {
        exportButton.setOnMouseClicked(
                event -> {
                    try {
                        this.exportStats();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        );

        opinionChart.getYAxis().setLabel("Generals for attack [%]");
        opinionChart.getXAxis().setLabel("Step");
        ((NumberAxis) opinionChart.getYAxis()).setLowerBound(0);
        ((NumberAxis) opinionChart.getXAxis()).setLowerBound(0);
        ((NumberAxis) opinionChart.getXAxis()).setUpperBound(10);
        ((NumberAxis) opinionChart.getYAxis()).setUpperBound(100);

        ((NumberAxis) opinionChart.getYAxis()).setMinorTickLength(0);
        ((NumberAxis) opinionChart.getXAxis()).setMinorTickLength(0);

        ((NumberAxis) opinionChart.getYAxis()).setTickUnit(10);

        opinionChart.getYAxis().setAutoRanging(false);
        opinionChart.getXAxis().setAutoRanging(true);

        StringConverter<Number> onlyIntegers = new StringConverter<>() {
            @Override
            public String toString(Number number) {
                return number.intValue() == number.doubleValue() ? String.valueOf(number.intValue()) : "";
            }

            @Override
            public Number fromString(String string) {
                return Double.parseDouble(string);
            }
        };

        ((NumberAxis) opinionChart.getYAxis()).setTickLabelFormatter(onlyIntegers);
        ((NumberAxis) opinionChart.getXAxis()).setTickLabelFormatter(onlyIntegers);

        supporting.setName("For attack [%]");
        opinionChart.getData().add(supporting);
        opinionChart.setCreateSymbols(false);
    }

    public void addStats(int numSupporting, int numNotSupporting) {
        Platform.runLater(
                () -> {
                    ((NumberAxis) opinionChart.getYAxis()).setUpperBound(100);
                    supporting.getData().add(new XYChart.Data<>(nextX,
                            (Double.valueOf(numSupporting) /
                                    (Double.valueOf(numNotSupporting) + Double.valueOf(numSupporting))) * 100));
                    nextX++;
                }
        );

    }

    public void clear() {
        opinionChart.setAnimated(false);
        supporting.getData().clear();
        opinionChart.getData().clear();
        opinionChart.getData().add(supporting);
        nextX = 0;
        opinionChart.setAnimated(true);
    }
}