package com.ruoyi.statistics_computer.Util;

import javafx.scene.control.Alert;

public class AlertUtil {
    public static void Warning(String text){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("警告");
        alert.setHeaderText(null);              // 可省略头部
        alert.setContentText(text);
        alert.showAndWait();
    }
}
