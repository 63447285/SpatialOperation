package com.example.spatialoperation.myCallable;

import com.example.spatialoperation.entity.MyPolygon;
import com.example.spatialoperation.service.PolygonService;

import java.util.List;
import java.util.concurrent.Callable;

public class ClipCallable implements Callable<List<MyPolygon>> {
    private PolygonService polygonService;
    private int bindex;//分页index
    private int num;//数量

    private String wkt;


    public ClipCallable(PolygonService polygonService, String wkt, int bindex, int num) {
        this.polygonService = polygonService;
        this.wkt=wkt;
        this.bindex=bindex;
        this.num=num;

    }

    @Override
    public List<MyPolygon> call() throws  org.locationtech.jts.io.ParseException {
        return polygonService.getPolygonByClipping(wkt,bindex,num);

    }
}
