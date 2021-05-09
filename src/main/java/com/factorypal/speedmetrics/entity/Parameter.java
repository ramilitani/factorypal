package com.factorypal.speedmetrics.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document
public class Parameter {

    String key;
    double value;

    public Parameter(String key, double value) {
        this.key = key;
        this.value = value;
    }


    public String getKey() {
        return key;
    }

    public double getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Parameter)) return false;
        Parameter that = (Parameter) o;
        return Double.compare(that.getValue(), getValue()) == 0 && Objects.equals(getKey(), that.getKey());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey(), getValue());
    }
}
