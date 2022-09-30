package com.example.spatialoperation.mapper;

import com.example.spatialoperation.entity.MyPolygon;
import com.vividsolutions.jts.geom.Geometry;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.util.List;

@Mapper
public interface PolygonMapper {

    MyPolygon getPolygonByID(@Param("id") int id);

    List<String> getAllPolygonGeometry(@Param("index") int index,@Param("num") int num);
    //新
    List<MyPolygon> getPolygonByIntersects(@Param("wkt") String wkt, @Param("index") int index, @Param("num") int num);

    List<MyPolygon> getPolygonByClipping(@Param("wkt") String wkt,@Param("index") int index, @Param("num") int num);

    String getPolygonByUnion(@Param("index") int index, @Param("num") int num,@Param("dlmc") String dlmc);

    void insertPolygon1(MyPolygon myPolygon);

    MyPolygon getMyPolygonById(@Param("id") int id);

    void insertPolygon2(MyPolygon myPolygon);

    void deletePolygon1();

    void deletePolygon2();

    MyPolygon getMyPolygonByIdNew(@Param("id") int id);
}
