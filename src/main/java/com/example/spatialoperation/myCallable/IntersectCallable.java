package com.example.spatialoperation.myCallable;

import com.example.spatialoperation.service.PolygonService;

import java.util.List;
import java.util.concurrent.Callable;

public class IntersectCallable implements Callable<List<String>> {
    private PolygonService polygonService;
    private int bindex;//分页index
    private int num;//数量

    private String wkt;


    public IntersectCallable(PolygonService polygonService,String wkt,int bindex,int num) {
        this.polygonService = polygonService;
        this.wkt=wkt;
        this.bindex=bindex;
        this.num=num;
    }

    @Override
    public List<String> call() throws Exception {
        return polygonService.getPolygonByIntersects(wkt,bindex,num);
    }
}
