package com.factorypal.speedmetrics.entity;

import org.springframework.data.annotation.Id;

import java.util.Objects;

public class MachineDataEntity {

    @Id
    String id;

    String key;
    String name;

    public MachineDataEntity(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MachineDataEntity)) return false;
        MachineDataEntity that = (MachineDataEntity) o;
        return Objects.equals(getKey(), that.getKey()) && Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey(), getName());
    }
}
