package com.example.spatialoperation.service;

import com.example.spatialoperation.entity.MyPoint;
import com.example.spatialoperation.entity.MyPolygon;
import com.example.spatialoperation.mapper.PointMapper;
import com.example.spatialoperation.mapper.PolygonMapper;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PointService {
    @Autowired
    private PointMapper pointMapper;

    public MyPoint getPointByID(int id){
        return pointMapper.getPointByID(id);
    }

    public List<MyPoint> getPointByName(String name){
        return pointMapper.getPointByName(name);
    }

    public String getPointGeometry(int id){
        return pointMapper.getPointGeometry(id);
    }

    /**
     *
     * @param wkt
     * @return 返回输入点500米以内的点集合
     */
    public List<MyPoint> getPointsByBuffer(String wkt) {
        int count= pointMapper.getPointsCount();
        double distance=500.0;
        List<MyPoint> myPointList=new ArrayList<>();
        try {
            Geometry g1 = new WKTReader().read(wkt);
            Polygon g2 = (Polygon) g1.buffer( distance/(2 * Math.PI * 6371004) * 360);
            for(int i=1;i<count+1;i++){
                if(new WKTReader().read(pointMapper.getPointGeometry(i)).within(g2)){
                    myPointList.add(pointMapper.getPointByID(i));
                }
            }
            return myPointList;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param wkt
     * @return 返回在所绘制多边形内的点集
     */
    public List<MyPoint> getPointsByPolygon(String wkt){
        int count= pointMapper.getPointsCount();
        List<MyPoint> myPointList=new ArrayList<>();
        try {
            Geometry g1 = new WKTReader().read(wkt);
            for(int i=1;i<count+1;i++){
                if(new WKTReader().read(pointMapper.getPointGeometry(i)).within(g1)){
                    myPointList.add(pointMapper.getPointByID(i));
                }
            }
            return myPointList;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }






}
