package com.example.spatialoperation.service;

import com.example.spatialoperation.KmeanPolygon.KmeanPolygonResult;
import com.example.spatialoperation.KmeanPolygon.KmeanPolygonSplitCore;
import com.example.spatialoperation.entity.MyPolygon;
import com.example.spatialoperation.mapper.PolygonMapper;
import com.example.spatialoperation.myCallable.SelectDataCallable;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.geotools.geometry.jts.JTS;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.WKTWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PolygonService {
    @Autowired
    private PolygonMapper polygonMapper;
    private static Logger log = LoggerFactory.getLogger(PolygonService.class);

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


//    //不使用postgis
//    /**
//     *
//     * @param wkt
//     * @return 返回所有与绘制多边形相交的MyPolygon
//     * @throws ParseException
//     */
//    public List<MyPolygon> getIntersectPolygons(String wkt) throws ParseException {
//        StopWatch watch = new StopWatch();
//        watch.start();
//        double count= polygonMapper.getPolygonsCount();
//        List<MyPolygon> myPolygonList=new ArrayList<>();
//        Geometry g1 = new WKTReader().read(wkt);
//        for(int i=1;i<count+1;i++){
//                if(new WKTReader().read(polygonMapper.getPolygonGeometry(i)).intersects(g1)){
//                    myPolygonList.add(polygonMapper.getPolygonByID(i));
//            }
//        }
//        watch.stop();
//        log.info("多边形相交耗时：{} s", new DecimalFormat("#.000").format(watch.getTotalTimeSeconds()));
//        return myPolygonList;
//    }
//
//    /**
//     *
//     * @param wkt
//     * @return 相交裁剪返回所有相交部分形成的多边形集合
//     * @throws ParseException
//     */
//    public List<String> getIntersectionClippingGeometry(String wkt) throws ParseException {
//        StopWatch watch = new StopWatch();
//        watch.start();
//        int count= polygonMapper.getPolygonsCount();
//        List<Geometry> geometryList=new ArrayList<>();
//        List<String> wktList=new ArrayList<>();
//        Geometry g1 = new WKTReader().read(wkt);
//        Geometry g2 = null;
//        for(int i=1;i<count+1;i++){
//            g2=new WKTReader().read(polygonMapper.getPolygonGeometry(i));
//            if(g2.intersects(g1)){
//                Geometry g= g1.intersection(g2);
//                geometryList.add(g);
//                WKTWriter writer = new WKTWriter(2);
//                String wkt2 = writer.write(g);
//                wktList.add(wkt2);
//            }
//        }
//        watch.stop();
//        log.info("多边形裁剪耗时：{} s", new DecimalFormat("#.000").format(watch.getTotalTimeSeconds()));
//        return wktList;
//    }
//
//    /**
//     *
//     * @param dlmc
//     * @return 返回同一用地类型的多边形合并形成的多边形
//     * @throws ParseException
//     */
//    public String getUnionGeometry(String dlmc) throws ParseException {
//        StopWatch watch = new StopWatch();
//        watch.start();
//        List<String> wktList=polygonMapper.getPolygonsGeometryByDlmc(dlmc);
//        Geometry geometry=new WKTReader().read(wktList.get(0));
//        String count= String.valueOf(wktList.size());
//        for(int i=1;i<wktList.size();i++){
//            geometry=geometry.union(new WKTReader().read(wktList.get(i)));
//            }
//        WKTWriter writer = new WKTWriter(2);
//        watch.stop();
//        log.info("用地类型为"+dlmc+"的多边形有"+count+"个，合并耗时：{} s", new DecimalFormat("#.000").format(watch.getTotalTimeSeconds()));
//        return writer.write(geometry);
//    }

    //使用postgis空间索引
    //相交
    public List<MyPolygon> getPolygonByIntersects(String wkt,int index,int num) throws ParseException {
        StopWatch watch = new StopWatch();
        watch.start();
        Geometry g=new WKTReader().read(wkt);
        final Envelope envelope = g.getEnvelopeInternal();
        double minX = envelope.getMinX();
        double maxX = envelope.getMaxX();
        double midX = minX + (maxX - minX) / 2.0;
        double minY = envelope.getMinY();
        double maxY = envelope.getMaxY();
        double midY = minY + (maxY - minY) / 2.0;
        Envelope llEnv = new Envelope(minX, midX, minY, midY);
        Envelope lrEnv = new Envelope(midX, maxX, minY, midY);
        Envelope ulEnv = new Envelope(minX, midX, midY, maxY);
        Envelope urEnv = new Envelope(midX, maxX, midY, maxY);
        Geometry ll = JTS.toGeometry(llEnv).intersection(g);
        Geometry lr = JTS.toGeometry(lrEnv).intersection(g);
        Geometry ul = JTS.toGeometry(ulEnv).intersection(g);
        Geometry ur = JTS.toGeometry(urEnv).intersection(g);
        WKTWriter writer = new WKTWriter(2);
        String wktll = writer.write(ll);
        String wktlr = writer.write(lr);
        String wktul = writer.write(ul);
        String wktur = writer.write(ur);
        List<MyPolygon> myPolygonList1;
        List<MyPolygon> myPolygonList2;
        List<MyPolygon> myPolygonList3;
        List<MyPolygon> myPolygonList4;
        myPolygonList1=polygonMapper.getPolygonByIntersects(wktll,index,num);
        myPolygonList2=polygonMapper.getPolygonByIntersects(wktlr,index,num);
        myPolygonList3=polygonMapper.getPolygonByIntersects(wktul,index,num);
        myPolygonList4=polygonMapper.getPolygonByIntersects(wktur,index,num);
        List collect = Stream.of(myPolygonList1,myPolygonList2,myPolygonList3,myPolygonList4)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
        watch.stop();
        log.info("使用空间索引语句多边形相交耗时：{} s", new DecimalFormat("#.000").format(watch.getTotalTimeSeconds()));
        return collect;
    }
    //裁剪
    public List<MyPolygon> getPolygonByClipping(String wkt,int index,int num) throws ParseException {
        StopWatch watch = new StopWatch();
        watch.start();
        Geometry g=new WKTReader().read(wkt);
//        final Envelope envelope = g.getEnvelopeInternal();
//        double minX = envelope.getMinX();
//        double maxX = envelope.getMaxX();
//        double midX = minX + (maxX - minX) / 2.0;
//        double minY = envelope.getMinY();
//        double maxY = envelope.getMaxY();
//        double midY = minY + (maxY - minY) / 2.0;
//        Envelope llEnv = new Envelope(minX, midX, minY, midY);
//        Envelope lrEnv = new Envelope(midX, maxX, minY, midY);
//        Envelope ulEnv = new Envelope(minX, midX, midY, maxY);
//        Envelope urEnv = new Envelope(midX, maxX, midY, maxY);
//        Geometry ll = JTS.toGeometry(llEnv).intersection(g);
//        Geometry lr = JTS.toGeometry(lrEnv).intersection(g);
//        Geometry ul = JTS.toGeometry(ulEnv).intersection(g);
//        Geometry ur = JTS.toGeometry(urEnv).intersection(g);
//        WKTWriter writer = new WKTWriter(2);
//        String wktll = writer.write(ll);
//        String wktlr = writer.write(lr);
//        String wktul = writer.write(ul);
//        String wktur = writer.write(ur);
        List<MyPolygon> myPolygonList1;
//        List<MyPolygon> myPolygonList2;
//        List<MyPolygon> myPolygonList3;
//        List<MyPolygon> myPolygonList4;
//        myPolygonList1=polygonMapper.getPolygonByClipping(wktll,index,num);
//        myPolygonList2=polygonMapper.getPolygonByClipping(wktlr,index,num);
//        myPolygonList3=polygonMapper.getPolygonByClipping(wktul,index,num);
//        myPolygonList4=polygonMapper.getPolygonByClipping(wktur,index,num);
//        List collect = Stream.of(myPolygonList1,myPolygonList2,myPolygonList3,myPolygonList4)
//                .flatMap(Collection::stream)
//                .distinct()
//                .collect(Collectors.toList());
      myPolygonList1=polygonMapper.getPolygonByClipping(wkt,index,num);
        watch.stop();
        log.info("使用空间索引语句多边形裁剪耗时：{} s", new DecimalFormat("#.000").format(watch.getTotalTimeSeconds()));
        return myPolygonList1;
    }

    public String getUnionGeometry(int index,int num,String dlmc) throws ParseException {
        StopWatch watch = new StopWatch();
        watch.start();
        String wkt=polygonMapper.getPolygonByUnion(index,num,dlmc);
        log.info(wkt);
        watch.stop();
        log.info("用地类型为"+dlmc+"的多边形合并耗时：{} s", new DecimalFormat("#.000").format(watch.getTotalTimeSeconds()));
        return wkt;
    }


    public void insert1(MyPolygon myPolygon) {
        polygonMapper.insertPolygon1(myPolygon);
    }

    public MyPolygon getMyPolygonById(int id) {
        return polygonMapper.getMyPolygonById(id);
    }

    public MyPolygon getMyPolygonByIdNew(int id) {return polygonMapper.getMyPolygonByIdNew(id);}

    public void insert2(MyPolygon myPolygon) {
        polygonMapper.insertPolygon2(myPolygon);
    }

    public void deletePolygon1(){
        polygonMapper.deletePolygon1();
    }

    public void deletePolygon2(){
        polygonMapper.deletePolygon2();
    }


}
