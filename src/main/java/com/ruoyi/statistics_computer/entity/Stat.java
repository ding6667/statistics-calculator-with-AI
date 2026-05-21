package com.ruoyi.statistics_computer.entity;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Stat {
    private StringProperty name = new SimpleStringProperty();
    private DoubleProperty value = new SimpleDoubleProperty();
    public Stat(String name, double value){
        setName(name);
        setValue(value);
    }
    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setValue(double v){
        value.set(v);
    }

    public double getValue() {
        return value.get();
    }
    public void setName(String name) {
        this.name.set(name);
    }
    public DoubleProperty valueProperty() {
        return value;
    }
}
