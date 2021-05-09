package com.factorypal.speedmetrics.vo;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.*;

public class MachineMetricsVO {

    String machine;
    String parameter;
    List<Double> values;
    double average;
    double median;
    double min;
    double max;

    public MachineMetricsVO(String machine, String parameter, double average, double median, double min, double max) {
        this.machine = machine;
        this.parameter = parameter;
        this.average = average;
        this.median = median;
        this.min = min;
        this.max = max;
        this.values = new ArrayList<>();
    }

    public MachineMetricsVO(String machine, String parameter, double value) {
        this(machine, parameter, 0,0,0,0);
        addValue(value);
    }

    public String getMachine() {
        return machine;
    }

    public String getParameter() {
        return parameter;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public double getMedian() {
        return median;
    }

    public void setMedian(double median) {
        this.median = median;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public void addValue(double value) {
        values.add(value);
    }

    public void calculate() {
        Collections.sort(values);
        int size = values.size();
        if (size % 2 == 0) {
            this.median = (values.get(size/2) + values.get(size/2 -1))/2;
        } else {
            this.median = values.get(size/2);
        }

        this.min = values.get(0);
        this.max = values.get(size-1);

        this.average = values.stream().reduce(0d, (a,b) -> a+b)/size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MachineMetricsVO)) return false;
        MachineMetricsVO that = (MachineMetricsVO) o;
        return Objects.equals(machine, that.machine) && Objects.equals(parameter, that.parameter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(machine, parameter);
    }
}
