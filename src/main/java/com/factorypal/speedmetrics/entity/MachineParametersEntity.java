package com.factorypal.speedmetrics.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document
public class MachineParametersEntity {

    @Id
    String id;
    String machineKey;
    List<Parameter> parameters;
    Date timestamp;

    public MachineParametersEntity(String machineKey, List<Parameter> parameters) {
        this.machineKey = machineKey;
        this.parameters = parameters;
        this.timestamp = new Date(System.currentTimeMillis());
    }

    public String getId() {
        return id;
    }

    public String getMachineKey() {
        return machineKey;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
