package com.ruoyi.statistics_computer.instance;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DataModel {
    private static final DataModel instance = new DataModel();
    private final ObservableList<Double> rawData = FXCollections.observableArrayList();
    private DataModel() {}
    public static DataModel getInstance() { return instance; }
    public ObservableList<Double> getRawData() { return rawData; }
}
