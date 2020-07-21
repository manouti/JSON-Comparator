package com.manouti.jsoncomparator.internal;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;

final class Tolerance {

    @JsonProperty
    private Type type;

    @JsonProperty
    private String path;

    @JsonProperty
    private Optional<Double> tolerance;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Optional<Double> getTolerance() {
        return tolerance;
    }

    public void setTolerance(Optional<Double> tolerance) {
        this.tolerance = tolerance;
    }

}
