package com.example.spatialoperation.entity;

import com.vividsolutions.jts.geom.Geometry;

public class MyPoint {
    private int objectid;
    private Geometry shape;
    private String name;
    private String address;
    private float x;
    private float y;

    public MyPoint() {
    }

    public MyPoint(int objectid, Geometry shape, String name, String address, float x, float y) {
        this.objectid = objectid;
        this.shape = shape;
        this.name = name;
        this.address = address;
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "MyPoint{" +
                "objectid=" + objectid +
                ", shape=" + shape +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
