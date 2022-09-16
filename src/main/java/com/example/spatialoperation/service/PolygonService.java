package com.example.spatialoperation.service;

import com.example.spatialoperation.entity.MyPolygon;
import com.example.spatialoperation.mapper.PolygonMapper;
import com.example.spatialoperation.myCallable.SelectDataCallable;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class PolygonService {
    @Autowired
    private PolygonMapper polygonMapper;

    private static Logger log = LoggerFactory.getLogger(PolygonService.class);
    public List<MyPolygon> getAllPolygon(){

        return polygonMapper.getAllPolygons();
    }

    public List<String> getAllPolygonGeometryByGroup(int index,int num){
        StopWatch watch = new StopWatch();
        watch.start();
        List<String> PolygonGeometrys=polygonMapper.getAllPolygonGeometry(index,num);
        watch.stop();
        log.info("获取到"+ PolygonGeometrys.size() +"个多边形耗时：{} s", new DecimalFormat("#.000").format(watch.getTotalTimeSeconds()));
        return PolygonGeometrys;
    }

    public MyPolygon getPolygonByID(int id){

        return polygonMapper.getPolygonByID(id);
    }


    public List<String> getAllDlmc(){
        return polygonMapper.getAllDlmc();
    }

    public String getPolygonGeometry(int id){
        return polygonMapper.getPolygonGeometry(id);
    }

    /**
     *
     * @param wkt
     * @return 返回所有与绘制多边形相交的MyPolygon
     * @throws ParseException
     */
    public List<MyPolygon> getIntersectPolygons(String wkt) throws ParseException {
        StopWatch watch = new StopWatch();
        watch.start();
        double count= polygonMapper.getPolygonsCount();
        List<MyPolygon> myPolygonList=new ArrayList<>();
        Geometry g1 = new WKTReader().read(wkt);
        for(int i=1;i<count+1;i++){
                if(new WKTReader().read(polygonMapper.getPolygonGeometry(i)).intersects(g1)){
                    myPolygonList.add(polygonMapper.getPolygonByID(i));
            }
        }
        watch.stop();
        log.info("多边形相交耗时：{} s", new DecimalFormat("#.000").format(watch.getTotalTimeSeconds()));
        return myPolygonList;
    }

    /**
     *
     * @param wkt
     * @return 相交裁剪返回所有相交部分形成的多边形集合
     * @throws ParseException
     */
    public List<String> getIntersectionClippingGeometry(String wkt) throws ParseException {
        StopWatch watch = new StopWatch();
        watch.start();
        int count= polygonMapper.getPolygonsCount();
        List<Geometry> geometryList=new ArrayList<>();
        List<String> wktList=new ArrayList<>();
        Geometry g1 = new WKTReader().read(wkt);
        Geometry g2 = null;
        for(int i=1;i<count+1;i++){
            g2=new WKTReader().read(polygonMapper.getPolygonGeometry(i));
            if(g2.intersects(g1)){
                Geometry g= g1.intersection(g2);
                geometryList.add(g);
                WKTWriter writer = new WKTWriter(2);
                String wkt2 = writer.write(g);
                wktList.add(wkt2);
            }
        }
        watch.stop();
        log.info("多边形裁剪耗时：{} s", new DecimalFormat("#.000").format(watch.getTotalTimeSeconds()));
        return wktList;
    }

    /**
     *
     * @param dlmc
     * @return 返回同一用地类型的多边形合并形成的多边形
     * @throws ParseException
     */
    public String getUnionGeometry(String dlmc) throws ParseException {
        StopWatch watch = new StopWatch();
        watch.start();
        List<String> wktList=polygonMapper.getPolygonsGeometryByDlmc(dlmc);
        Geometry geometry=new WKTReader().read(wktList.get(0));
        String count= String.valueOf(wktList.size());
        for(int i=1;i<wktList.size();i++){
            geometry=geometry.union(new WKTReader().read(wktList.get(i)));
            }
        WKTWriter writer = new WKTWriter(2);
        watch.stop();
        log.info("用地类型为"+dlmc+"的多边形有"+count+"个，合并耗时：{} s", new DecimalFormat("#.000").format(watch.getTotalTimeSeconds()));
        return writer.write(geometry);
    }

    //新方法
    public List<String> getPolygonByIntersects(String wkt,int index,int num){
        StopWatch watch = new StopWatch();
        watch.start();
        List<String> myPolygonList=polygonMapper.getPolygonByIntersects(wkt,index,num);
        watch.stop();
        log.info("使用空间索引语句多边形相交耗时：{} s", new DecimalFormat("#.000").format(watch.getTotalTimeSeconds()));
        return myPolygonList;
    }

//    public List<String> getPolygonByIntersectionClipping(String wkt) throws ParseException {
//        StopWatch watch = new StopWatch();
//        watch.start();
//        List<Geometry> geometryList=new ArrayList<>();
//        List<MyPolygon> myPolygonList=polygonMapper.getPolygonByIntersects(wkt);
//        List<String> wktList=new ArrayList<>();
//        Geometry g1 = new WKTReader().read(wkt);
//        Geometry g2 = null;
//        for(int i=0;i<myPolygonList.size();i++){
//            g2=new WKTReader().read(myPolygonList.get(i).getShape());
//            Geometry g= g1.intersection(g2);
//            geometryList.add(g);
//            WKTWriter writer = new WKTWriter(2);
//            String wkt2 = writer.write(g);
//            wktList.add(wkt2);
//        }
//        watch.stop();
//        log.info("使用空间索引语句多边形裁剪耗时：{} s", new DecimalFormat("#.000").format(watch.getTotalTimeSeconds()));
//        return wktList;
//    }



}
