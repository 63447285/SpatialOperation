package com.example.spatialoperation.controller;

import com.example.spatialoperation.entity.MyPoint;
import com.example.spatialoperation.entity.MyPolygon;
import com.example.spatialoperation.myCallable.ClipCallable;
import com.example.spatialoperation.myCallable.IntersectCallable;
import com.example.spatialoperation.myCallable.SelectDataCallable;
import com.example.spatialoperation.myCallable.UnionCallable;
import com.example.spatialoperation.service.PointService;
import com.example.spatialoperation.service.PolygonService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.WKTWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@RestController
public class PolygonController {
    @Autowired
    private PolygonService polygonService;

    //线程池
    private static ExecutorService executorService = Executors.newFixedThreadPool(8);

    private static Logger log = LoggerFactory.getLogger(PolygonController.class);

    //id获取MyPolygon
    @RequestMapping("/polygon/getById")
    public MyPolygon getByID(int id){
        return polygonService.getPolygonByID(id);
    };

    //多线程分页获取所有多边形shape
    @RequestMapping("/polygon/getAllPolygonGeometry")
    public List<String> getAllPolygonGeometry() throws ExecutionException, InterruptedException {
        StopWatch watch = new StopWatch();
        watch.start();
        List<Future<List<String>>> futures = new ArrayList<>();
        int index=0;
        int num=8000;
        SelectDataCallable c = null;
        // 2个线程
        for (int subId=1;subId<=2;subId++){
            log.info("执行次数"+String.valueOf(subId));
            c = new SelectDataCallable(polygonService,index,num);
            // 提交线程任务
            Future<List<String>> f = executorService.submit(c);
            futures.add(f);
            index=index+num;
        }
        log.info("futures数量："+String.valueOf(futures.size()));
        List<String> result=new ArrayList<>();
        for(Future<List<String>> f:futures){
            try{
            result.addAll(f.get());
            }catch (Exception e){
            }
        }
        watch.stop();
        log.info("result数量："+String.valueOf(result.size()));
        log.info("获取所有多边形耗时：{} s", new DecimalFormat("#.000").format(watch.getTotalTimeSeconds()));
        return result;
    };

//    旧方法
//    @RequestMapping("/polygon/getIntersectPolygons")
//    public List<MyPolygon> getIntersectPolygons(String wkt) throws ParseException {
//        return polygonService.getIntersectPolygons(wkt);
//    };
//
//    @RequestMapping("/polygon/getIntersectionClippingGeometrys")
//    public List<String> getIntersectionClippingGeometrys(String wkt) throws ParseException {
//        return polygonService.getIntersectionClippingGeometry(wkt);
//    }
//
//    @RequestMapping("/polygon/getUnionGeometry")
//    public String getUnionGeometry(String dlmc) throws ParseException {
//        return polygonService.getUnionGeometry(dlmc);
//    }

    //新方法 使用postgis空间索引
    //多边形相交
    @RequestMapping("/polygon/getPolygonByIntersects")
    public List<String> getPolygonByIntersects(String wkt) throws ParseException {
        StopWatch watch = new StopWatch();
        watch.start();
        List<Future<List<String>>> futures = new ArrayList<>();
        int index=0;
        int num=400000;
        IntersectCallable c = null;
        // 8个线程
        for (int subId=1;subId<=8;subId++){
            c = new IntersectCallable(polygonService,wkt,index,num);
            // 提交线程任务
            Future<List<String>> f = executorService.submit(c);
            futures.add(f);
            index=index+num;
        }
        log.info("相交计算中......");
        List<String> result=new ArrayList<>();
        for(Future<List<String>> f:futures){
            try{
                result.addAll(f.get());
            }catch (Exception e){
            }
        }
        watch.stop();
        log.info("result数量："+String.valueOf(result.size()));
        log.info("获取所有相交多边形耗时：{} s", new DecimalFormat("#.000").format(watch.getTotalTimeSeconds()));
        return result;
    };

    @RequestMapping("/polygon/getPolygonByClipping")
    public List<String> getPolygonByIntersectionClipping(String wkt) throws ParseException {
        StopWatch watch = new StopWatch();
        watch.start();
        List<Future<List<String>>> futures = new ArrayList<>();
        int index=0;
        int num=400000;
        ClipCallable c = null;
        // 8个线程
        for (int subId=1;subId<=8;subId++){
            c = new ClipCallable(polygonService,wkt,index,num);
            // 提交线程任务
            Future<List<String>> f = executorService.submit(c);
            futures.add(f);
            index=index+num;
        }
        log.info("裁剪中......");
        List<String> result=new ArrayList<>();
        for(Future<List<String>> f:futures){
            try{
                result.addAll(f.get());
            }catch (Exception e){
            }
        }
        watch.stop();
        log.info("result数量："+String.valueOf(result.size()));
        log.info("裁剪多边形耗时：{} s", new DecimalFormat("#.000").format(watch.getTotalTimeSeconds()));
        return result;
    };

    @RequestMapping("/polygon/getPolygonByUnion")
    public String getPolygonByUnion(String dlmc) throws ParseException {
        StopWatch watch = new StopWatch();
        watch.start();
        List<Future<String>> futures = new ArrayList<>();
        int index=0;
        int num=400000;
        UnionCallable c = null;
        // 8个线程
        for (int subId=1;subId<=8;subId++){
            c = new UnionCallable(polygonService,index,num,dlmc);
            // 提交线程任务
            Future<String> f = executorService.submit(c);
            futures.add(f);
            index=index+num;
        }
        log.info("合并中......");
        List<String> result=new ArrayList<>();
        for(Future<String> f:futures){
            try{
                result.add(f.get());
            }catch (Exception e){
            }
        }
        Geometry geometry=new WKTReader().read(result.get(0));
        for(int i=1;i<result.size();i++){
            if(null!=result.get(i)){
            geometry=geometry.union(new WKTReader().read(result.get(i)));
            }
            }
        WKTWriter writer = new WKTWriter(2);
        watch.stop();
        log.info("合并多边形耗时：{} s", new DecimalFormat("#.000").format(watch.getTotalTimeSeconds()));
        return writer.write(geometry);
    };

}
