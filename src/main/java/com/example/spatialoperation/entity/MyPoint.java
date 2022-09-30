package com.example.spatialoperation.entity;

import com.vividsolutions.jts.geom.Geometry;

public class MyPoint {
    private int objectid;
    private String shape;

    private String dlmc;



    @Override
    public String toString() {
        return "MyPoint{" +
                "objectid=" + objectid +
                ", shape=" + shape +
                ", dlmc='" + dlmc + '\'' +
                '}';
    }

    public String getDlmc() {
        return dlmc;
    }

    public void setDlmc(String dlmc) {
        this.dlmc = dlmc;
    }

    public MyPoint(int objectid, String shape, String dlmc) {
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


}
