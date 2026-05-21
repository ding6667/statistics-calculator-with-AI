package com.ruoyi.statistics_computer;

import com.ruoyi.statistics_computer.Util.AlertUtil;
import com.ruoyi.statistics_computer.Util.OllamaService;
import com.ruoyi.statistics_computer.entity.Sample;
import com.ruoyi.statistics_computer.instance.DataModel;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.util.converter.DoubleStringConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;

public class SampleController extends BaseController {
    @FXML
    private TableView table;
    @FXML
    private Button saveButton;
    @FXML
    private Button backButton;
    @FXML
    private TextField inputTextarea;
    @FXML
    private ComboBox<String> separatorComboBox;

    private ObservableList<Sample> dataList;

    @FXML
    private TableColumn<Sample, Integer> id;
    @FXML
    private TableColumn<Sample, Double> value;
    @FXML
    TableColumn<Sample, Integer> sortedValue;

    @FXML
    private TextArea input;
    @FXML
    private TextArea output;
    @FXML
    private void initialize() {
        //初始化分隔符下拉列表
        separatorComboBox.getItems().addAll("空格", ",", "|", "#", "无");
        dataList = FXCollections.observableArrayList();
        //序号自动填充

        id.setCellValueFactory(cellData -> {
            int index = table.getItems().indexOf(cellData.getValue()) + 1;
            return new SimpleIntegerProperty(index).asObject();
        });
        output.setEditable(false);
        //数据绑定
        value.setCellValueFactory(cellData -> cellData.getValue().ValueProperty().asObject());
        sortedValue.setCellValueFactory(cellData -> cellData.getValue().sortedValueProperty().asObject());
        //设置样本值列可编辑(为每个单元格生成一个包含textfield的单元格，且添加了string转double的转换器)
        value.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        //回车或失焦触发
        value.setOnEditCommit(event -> {
            Sample row = event.getRowValue();
            row.setValue(event.getNewValue());
            refreshSortedValues();
        });
        input.setOnKeyPressed(event -> {
            if(event.isShortcutDown() && event.getCode() == KeyCode.ENTER){
                call();
                input.clear();
                event.consume();
            }
        });
        //数据绑定列表
        table.setItems(dataList);
        //监听列表，排序值自动计算
        dataList.addListener((ListChangeListener<? super Sample>) c -> refreshSortedValues());
        refreshSortedValues();

    }

    private void refreshSortedValues() {
        if(dataList.isEmpty()) return;
        List<Double> sortedValues = dataList.stream().mapToDouble(Sample::getValue).sorted().boxed().collect(Collectors.toList());
        for (Sample sample : dataList) {
            int idx = sortedValues.indexOf(sample.getValue());
            sample.setSortedValue(idx >= 0? idx + 1 : 0);
        }
    }

    @FXML
    private void saveDatafromOutput(){
        String text = output.getText();
        if(text.isEmpty() || !text.matches("-?\\d+(?:\\.\\d+)?(?:,-?\\d+(?:\\.\\d+)?)*")){
            AlertUtil.Warning("本次输入无有效数据！");
        }
        saveData(text,",");
        output.clear();
        output.setPromptText("本次输出成功保存到样本数据中！");
    }
    /**
     * 保存输入数据
     */
    @FXML
    private void saveData() {
        if (inputTextarea.getText().trim().isEmpty()) {
            AlertUtil.Warning("输入不能为空！");
            return;
        }
        saveData(inputTextarea.getText().trim(),separatorComboBox.getValue());
    }
    private void saveData(String data,String separator) {
        if (data.trim().isEmpty()) {
            AlertUtil.Warning("输入不能为空！");
            return;
        }
        try {
            //需检验是否是一个数据
            if (separator == null || separator.equals("无")) {
                if (data.matches("^-?\\d+(\\.\\d+)?$")) {
                    double d = Double.parseDouble(data);
                    table.getItems().add(new Sample(d));
                    DataModel.getInstance().getRawData().add(d);
                } else {
                    AlertUtil.Warning("请检查输入的数据，多组数据需要选择分隔符");
                }
            } else {
                //填充数据
                List<Double> datas = Arrays.stream(data.split(separator.equals("空格") ? " " : Pattern.quote(separator)))//对正则转义普通字符串
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .mapToDouble(Double::parseDouble)
                        .boxed()
                        .collect(toList());
                DataModel.getInstance().getRawData().addAll(datas);
                List<Sample> samples = datas.stream().map(Sample::new).collect(toList());
                table.getItems().addAll(samples);
            }
            refreshSortedValues();
        } catch (NumberFormatException e) {
            AlertUtil.Warning("数据格式错误，请检查输入");
        }
    }

    @FXML
    private void backData() {
    }
    @FXML
    private void refresh(){

    }

    private void call(){
        String s = input.getText();
        if(s.isEmpty()){
            AlertUtil.Warning("输入不为空");
        }
        input.setEditable(false);
        output.clear();
        output.setText("正在思考中。。。耐心等待！");
        Task<String> task = OllamaService.getInstance().generateAsync(s);
        task.setOnSucceeded(e -> {
            output.setText(task.getValue());
            input.setEditable(true);
        });
        new Thread(task).start();

    }

}
