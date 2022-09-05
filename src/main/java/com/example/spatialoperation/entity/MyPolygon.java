package com.example.spatialoperation.entity;

import com.vividsolutions.jts.geom.Geometry;

public class MyPolygon {
    private int objectid;
    private Geometry shape;

    private String wkt;
    private String dlmc;


    public MyPolygon(int objectid, String wkt, String dlmc) {
        this.objectid = objectid;
        this.dlmc = dlmc;
        this.wkt = wkt;
    }

    public MyPolygon(int objectid, Geometry shape, String dlmc) {
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

    public Geometry getShape() {
        return shape;
    }

    public void setShape(Geometry shape) {
        this.shape = shape;
    }

    public String getDlmc() {
        return dlmc;
    }

    public void setDlmc(String dlmc) {
        this.dlmc = dlmc;
    }
}
