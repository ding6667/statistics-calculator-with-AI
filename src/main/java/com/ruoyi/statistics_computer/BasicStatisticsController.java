package com.ruoyi.statistics_computer;

import com.ruoyi.statistics_computer.entity.Stat;
import com.ruoyi.statistics_computer.instance.DataModel;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class BasicStatisticsController extends BaseController{
    @FXML
    private TableView<Stat> table;
    @FXML
    private TableColumn<Stat,String> names;
    @FXML
    private TableColumn<Stat,Double> values;
    private ObservableList<Double> data;
    private Map<String,Double> stat;
    @FXML
    private void initialize(){
        names.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        values.setCellValueFactory(cellData -> cellData.getValue().valueProperty().asObject());
        stat = new HashMap<>();
        refresh();
    }
    @FXML
    private void exprotcsv(){

    }
    @FXML
    private void refresh(){
        data = DataModel.getInstance().getRawData();
        calculate();
        fill();
    }

    /**
     * n、均值、中位数、众数、标准差、方差、最小值、最大值、极差、Q1、Q3、四分位距、偏度、峰度、变异系数、总和。
     */
    private void calculate(){
        double[] sample = data.stream().mapToDouble(Double::doubleValue).toArray();
        DescriptiveStatistics stats = new DescriptiveStatistics(sample);
        stat.put("样本量", (double) stats.getN());
        stat.put("均值", stats.getMean());
        stat.put("中位数", stats.getPercentile(50));
        Double mode = getMode(sample);
        if(mode != null)
            stat.put("众数", mode);
        stat.put("方差", stats.getVariance());
        stat.put("标准差", stats.getStandardDeviation());
        stat.put("最小值", stats.getMin());
        stat.put("最大值", stats.getMax());
        stat.put("极差", stats.getMax() - stats.getMin());
        stat.put("Q1", stats.getPercentile(25));
        stat.put("Q3", stats.getPercentile(75));
        stat.put("四分位距", stats.getPercentile(75) - stats.getPercentile(25));
        stat.put("偏度", stats.getSkewness());
        stat.put("峰度", stats.getKurtosis());
        stat.put("变异系数", stats.getStandardDeviation() / stats.getMean());
    }
    private Double getMode(double[] sample){
        if(data.isEmpty()) return null;
        Map<Double,Integer> freq = new HashMap<>();
        for (double v : sample) {
            freq.merge(v,1,Integer::sum);
        }
        int maxF = freq.values().stream().max(Integer::compare).orElse(0);
        long count = freq.values().stream().filter(v -> v == maxF).count();
        if(maxF > 1 && count == 1 )
            return freq.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse(null);
        return null;
    }
    private void fill(){
        table.getItems().setAll(
                stat.entrySet()
                        .stream()
                        .map(e -> new Stat(e.getKey(),e.getValue()))
                        .collect(Collectors.toList()));

    }


}
