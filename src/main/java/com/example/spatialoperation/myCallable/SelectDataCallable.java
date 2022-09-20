package com.example.spatialoperation.myCallable;
import com.example.spatialoperation.service.PolygonService;
import java.util.List;
import java.util.concurrent.Callable;

public class SelectDataCallable  implements Callable<List<String>> {

    private PolygonService polygonService;
    private int bindex;//分页index
    private int num;//数量




    public SelectDataCallable(PolygonService polygonService,int bindex,int num) {
        this.polygonService = polygonService;
        this.bindex=bindex;
        this.num=num;
    }

    @Override
    public List<String> call() throws Exception {
        return polygonService.getAllPolygonGeometryByGroup(bindex,num);
    }
}
