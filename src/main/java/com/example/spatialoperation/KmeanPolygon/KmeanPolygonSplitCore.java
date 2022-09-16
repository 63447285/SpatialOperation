package com.example.spatialoperation.KmeanPolygon;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.WKTReader;


public class KmeanPolygonSplitCore implements KmeanPolygonSplitInterface{
    private static double random(double max, double min) {
        double d = (Math.random() * (max - min) + min);
        return d;
    }

    @Override
    public KmeanPolygonResult splitPolygon(String wkt, int setp, int k) throws ParseException {
        KmeanPolygonResult result = new KmeanPolygonResult();
        Polygon polygon = (Polygon) new WKTReader().read(wkt);
        Coordinate[] coordinates = polygon.getCoordinates();
        ArrayList<Double> xList = new ArrayList<>();
        ArrayList<Double> yList = new ArrayList<>();
        Arrays.stream(coordinates).forEach(
                s -> {
                    xList.add(s.x);
                    yList.add(s.y);
                }
        );
        // xy 最大最小值
        Double xMax = xList.stream().reduce(Double::max).get();
        Double xMin = xList.stream().reduce(Double::min).get();
        Double yMax = yList.stream().reduce(Double::max).get();
        Double yMin = yList.stream().reduce(Double::min).get();

        // 当前点数量
        int pointCount = 0;
        ArrayList<Point> pointArrayList = new ArrayList<>();

        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            // 最大最小值随机
            if (pointCount <= setp) {
                double rx = random(xMax, xMin);
                double ry = random(yMax, yMin);
                Point nowPoint = (Point) new WKTReader().read("POINT(" + rx + " " + ry + ")");
                boolean contains = polygon.contains(nowPoint);
                if (contains) {
                    pointArrayList.add(nowPoint);
                    pointCount++;
                }
            } else {
                break;
            }
        }
        // k-means 数据 构造
        double[][] kmData = new double[pointArrayList.size()][2];
        for (int i = 0; i < pointArrayList.size(); i++) {
            Point point = pointArrayList.get(i);
            double[] oneData = new double[2];
            oneData[0] = point.getX();
            oneData[1] = point.getY();
            kmData[i] = oneData;
        }

        // k-means 结果

        Kmeans kmeans = new Kmeans(kmData, k);

        // 构造泰森多边形
        VoronoiInterface vo = new VoronoiInterfaceImpl();
        List<Geometry> voronoi = vo.voronoi(kmeans.getCentroids());

        result.setPolygon(polygon);
        result.setPointList(pointArrayList);
        result.setAssignments(kmeans.getAssignments());
        result.setCentroids(kmeans.getCentroids());
        result.setXlist(xList);
        result.setYlist(yList);
        result.setVoronoi(voronoi);
        return result;
    }
}
