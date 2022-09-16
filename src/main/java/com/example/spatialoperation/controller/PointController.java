package com.example.spatialoperation.controller;

import com.example.spatialoperation.entity.MyPoint;
import com.example.spatialoperation.service.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
public class PointController {
    @Autowired
    private PointService pointService;

    @RequestMapping("/point/getById")
    public MyPoint getByID(int id){
    return pointService.getPointByID(id);
    };

    @RequestMapping("/point/getByName")
    public List<MyPoint> getByName(String name){
        return pointService.getPointByName(name);
    }

    @RequestMapping("/point/getPointsGeometry")
    public String getPointGeometry(int id){
        return pointService.getPointGeometry(id);
    };

    @RequestMapping("/point/getPointsByBuffer")
    public List<MyPoint> getPointsByBuffer(String wkt){
        return pointService.getPointsByBuffer(wkt);
    };

    @RequestMapping("/point/getPointsByDistance")
    public List<MyPoint> getPointsByDistance(String wkt){
        return pointService.getPointsByDistance(wkt);
    };
    @RequestMapping("/point/getPointsByPolygon")
    public List<MyPoint> getPointsByPloygon(String wkt){
        return pointService.getPointsByPolygon(wkt);
    }



}
