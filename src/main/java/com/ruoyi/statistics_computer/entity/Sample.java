package com.ruoyi.statistics_computer.entity;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Sample {

    private  DoubleProperty value = new SimpleDoubleProperty();
    private  IntegerProperty sortedValue = new SimpleIntegerProperty();
    public Sample(double v){
        setValue(v);
    }
    public void setValue(double v){
        value.set(v);
    }
    public DoubleProperty ValueProperty(){
        return value;
    }
    public double getValue(){
        return value.getValue();
    }
    public int getSortedValue() {
        return sortedValue.get();
    }

    public IntegerProperty sortedValueProperty() {
        return sortedValue;
    }

    public void setSortedValue(int sortedValue) {
        this.sortedValue.set(sortedValue);
    }
}
