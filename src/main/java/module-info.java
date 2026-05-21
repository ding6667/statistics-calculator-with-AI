module com.ruoyi.statistics_computer {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires commons.math3;
    requires langchain4j.core;
    requires langchain4j.ollama;

    opens com.ruoyi.statistics_computer to javafx.fxml;
    exports com.ruoyi.statistics_computer;
    exports com.ruoyi.statistics_computer.instance;
    opens com.ruoyi.statistics_computer.instance to javafx.fxml;
}