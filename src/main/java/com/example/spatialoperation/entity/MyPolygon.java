package com.example.spatialoperation.entity;


import org.locationtech.jts.geom.Geometry;

import java.util.Objects;

public class MyPolygon {
    private int objectid;
    private String shape;
    private String dlmc;


    public MyPolygon(int objectid, String shape, String dlmc) {
        this.objectid = objectid;
        this.shape = shape;
        this.dlmc = dlmc;
    }

    public int getObjectid() {
        return objectid;
    }

    public void setObjectid(int objectid) {
        this.objectid = objectid;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public String getDlmc() {
        return dlmc;
    }

    public void setDlmc(String dlmc) {
        this.dlmc = dlmc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyPolygon myPolygon = (MyPolygon) o;
        return objectid == myPolygon.objectid && Objects.equals(shape, myPolygon.shape) && Objects.equals(dlmc, myPolygon.dlmc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectid, shape, dlmc);
    }
}
