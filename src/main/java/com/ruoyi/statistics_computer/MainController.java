package com.ruoyi.statistics_computer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class MainController extends BaseController{
    @FXML
    Button sampleDataButton;
    @FXML
    Button BasicStatisticsButton;
    @FXML
    private StackPane contentArea;

    private Parent sampleDataView;
    private Parent basicStatisticsView;

    @FXML
    private void initialise(){}
    @FXML
    private void sampleData() throws IOException {
        if(sampleDataView == null){
            sampleDataView = FXMLLoader.load(getClass().getResource("sampleData.fxml"));
        }
        contentArea.getChildren().clear();
        contentArea.getChildren().add(sampleDataView);
    }

    @FXML
    private void basicStatistics() throws IOException {
        if(basicStatisticsView == null){
            basicStatisticsView = FXMLLoader.load(getClass().getResource("basicStatistics.fxml"));
        }
        contentArea.getChildren().clear();
        contentArea.getChildren().add(basicStatisticsView);
    }

}