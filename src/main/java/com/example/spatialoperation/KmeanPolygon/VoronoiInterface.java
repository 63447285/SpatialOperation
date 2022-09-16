package com.example.spatialoperation.KmeanPolygon;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface VoronoiInterface {

    public List<Geometry> voronoi(double[][] doubles);

    Collection delaunay(double[][] doubles);

    List<Geometry> voronoi(ArrayList<Point> points);

    Collection delaunay(ArrayList<Point> points);

    List<Geometry> voronoi(List<Coordinate> coords);

    Collection delaunay(List<Coordinate> coords);
}
