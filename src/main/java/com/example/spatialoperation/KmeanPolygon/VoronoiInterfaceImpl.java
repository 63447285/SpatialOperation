package com.example.spatialoperation.KmeanPolygon;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.triangulate.VoronoiDiagramBuilder;
import org.locationtech.jts.triangulate.quadedge.QuadEdgeSubdivision;

public class VoronoiInterfaceImpl implements VoronoiInterface{
    @Override
    public List<Geometry> voronoi(double[][] doubles) {
        return voronoi(doublesToCoordinate(doubles));
    }

    @Override
    public Collection delaunay(double[][] doubles) {
        return delaunay(doublesToCoordinate(doubles));
    }

    @Override
    public List<Geometry> voronoi(ArrayList<Point> points) {
        List<Geometry> voronoi = voronoi(pointToCoordinate(points));
        return voronoi;
    }

    @Override
    public Collection delaunay(ArrayList<Point> points) {
        Collection delaunay = delaunay(pointToCoordinate(points));
        return delaunay;
    }

    private List<Coordinate> doublesToCoordinate(double[][] doubles) {
        List<Coordinate> coords = new ArrayList<Coordinate>();
        for (int i = 0; i < doubles.length; i++) {
            Coordinate coord = new Coordinate(doubles[i][0], doubles[i][1], i);
            coords.add(coord);
        }
        return coords;
    }

    private List<Coordinate> pointToCoordinate(ArrayList<Point> points) {
        List<Coordinate> coords = new ArrayList<Coordinate>();
        for (int i = 0; i < points.size(); i++) {
            Coordinate coord = new Coordinate(points.get(i).getX(), points.get(i).getY(), i);
            coords.add(coord);
        }
        return coords;
    }

    private VoronoiDiagramBuilder getVoronoiDiagramBuilder(List<Coordinate> coords) {
        VoronoiDiagramBuilder voronoiDiagramBuilder = new VoronoiDiagramBuilder();
        Envelope clipEnvelpoe = new Envelope();
        voronoiDiagramBuilder.setSites(coords);
        voronoiDiagramBuilder.setClipEnvelope(clipEnvelpoe);
        return voronoiDiagramBuilder;
    }


    @Override
    public List<Geometry> voronoi(List<Coordinate> coords) {
        VoronoiDiagramBuilder voronoiDiagramBuilder = getVoronoiDiagramBuilder(
                coords);
        Geometry geom = voronoiDiagramBuilder
                .getDiagram(JTSFactoryFinder.getGeometryFactory());

        List<Geometry> voronoi = new ArrayList<>();
        int numGeometries = geom.getNumGeometries();
        for (int i = 0; i < numGeometries; i++) {
            Geometry geometryN = geom.getGeometryN(i);
            voronoi.add(geometryN);
        }
        return voronoi;
    }

    @Override
    public Collection delaunay(List<Coordinate> coords) {
        VoronoiDiagramBuilder voronoiDiagramBuilder = getVoronoiDiagramBuilder(coords);
        QuadEdgeSubdivision subdivision = voronoiDiagramBuilder.getSubdivision();
        Collection delaunay = subdivision.getEdges();
        return delaunay;
    }
}
