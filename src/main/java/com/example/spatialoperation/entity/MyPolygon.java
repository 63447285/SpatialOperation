package com.example.spatialoperation.entity;

import com.vividsolutions.jts.geom.Geometry;

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
}
