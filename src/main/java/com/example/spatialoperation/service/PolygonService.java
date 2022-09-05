package com.example.spatialoperation.service;

import com.example.spatialoperation.entity.MyPolygon;
import com.example.spatialoperation.mapper.PolygonMapper;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PolygonService {
    @Autowired
    private PolygonMapper polygonMapper;

    public List<MyPolygon> getAllPolygon(){
        return polygonMapper.getAllPolygons();
    }

    public MyPolygon getPolygonByID(int id){
        return polygonMapper.getPolygonByID(id);
    }

    public List<MyPolygon> getPolygonByDlmc(String dlmc){
        return polygonMapper.getPolygonsByDlmc(dlmc);
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
        int count= polygonMapper.getPolygonsCount();
        List<MyPolygon> myPolygonList=new ArrayList<>();
        Geometry g1 = new WKTReader().read(wkt);
        for(int i=1;i<count+1;i++){
                if(new WKTReader().read(polygonMapper.getPolygonGeometry(i)).intersects(g1)){
                    myPolygonList.add(polygonMapper.getPolygonByID(i));
            }
        }
        return myPolygonList;
    }

    /**
     *
     * @param wkt
     * @return 相交裁剪返回所有相交部分形成的多边形集合
     * @throws ParseException
     */
    public List<String> getIntersectionClippingGeometry(String wkt) throws ParseException {
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
        return wktList;
    }

    /**
     *
     * @param dlmc
     * @return 返回同一用地类型的多边形合并形成的多边形
     * @throws ParseException
     */
    public String getUnionGeometry(String dlmc) throws ParseException {
        int count= polygonMapper.getPolygonsCount();
        String wkt="";
        Geometry geometry=new WKTReader().read(polygonMapper.getPolygonGeometry(1));
        for(int i=1;i<count+1;i++){
            geometry=geometry.union(new WKTReader().read(polygonMapper.getPolygonGeometry(i)));
            }
        WKTWriter writer = new WKTWriter(2);
        String wkt2 = writer.write(geometry);
        return wkt2;
    }

}
