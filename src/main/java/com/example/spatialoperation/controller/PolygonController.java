package com.example.spatialoperation.controller;

import com.example.spatialoperation.entity.MyPoint;
import com.example.spatialoperation.entity.MyPolygon;
import com.example.spatialoperation.service.PointService;
import com.example.spatialoperation.service.PolygonService;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
public class PolygonController {
    @Autowired
    private PolygonService polygonService;

    @RequestMapping("/polygon/getById")
    public MyPolygon getByID(int id){
        return polygonService.getPolygonByID(id);
    };

    @RequestMapping("/polygon/getPolygonsGeometry")
    public String getPolygonGeometry(int id){
        return polygonService.getPolygonGeometry(id);
    };

    @RequestMapping("/polygon/getIntersectPolygons")
    public List<MyPolygon> getPointsByBuffer(String wkt) throws ParseException {
        return polygonService.getIntersectPolygons(wkt);
    };

    @RequestMapping("/getIntersectionClippingGeometrys")
    public List<String> getIntersectionClippingGeometrys(String wkt) throws ParseException {
        return polygonService.getIntersectionClippingGeometry(wkt);
    }

    @RequestMapping("/polygon/getUnionGeometry")
    public String getUnionGeometry(String dlmc) throws ParseException {
        return polygonService.getUnionGeometry(dlmc);
    }
}
