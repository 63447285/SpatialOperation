package com.example.spatialoperation.myCallable;

import com.example.spatialoperation.service.PolygonService;

import java.util.List;
import java.util.concurrent.Callable;

public class UnionCallable implements Callable<String> {
    private PolygonService polygonService;
    private int bindex;//分页index
    private int num;//数量
    private String dlmc;

    public UnionCallable(PolygonService polygonService, int bindex, int num, String dlmc) {
        this.polygonService = polygonService;
        this.bindex = bindex;
        this.num = num;
        this.dlmc = dlmc;
    }

    @Override
    public String call() throws  org.locationtech.jts.io.ParseException {
        return polygonService.getUnionGeometry(bindex,num,dlmc);

    }
}
