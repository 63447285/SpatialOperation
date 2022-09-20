package com.example.spatialoperation.KmeanPolygon;

import java.util.ArrayList;
import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KmeanPolygonResult {

    private Polygon polygon;
    /**
     * 随机点集合
     */
    private ArrayList<Point> pointList;
    /**
     * 分组后组id
     */
    private int[] assignments;
    /**
     * 中心集合
     */
    private double[][] centroids;
    /**
     * xlist
     */
    private ArrayList<Double> xlist;
    /**
     * ylist
     */
    private ArrayList<Double> ylist;
    /**
     * 构造泰森多边形
     */
    private List<Geometry> voronoi;
}
