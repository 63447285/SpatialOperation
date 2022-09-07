package com.example.spatialoperation.mapper;

import com.example.spatialoperation.entity.MyPolygon;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PolygonMapper {

    MyPolygon getPolygonByID(@Param("id") int id);

    List<String> getAllPolygonGeometry();

    List<String> getAllDlmc();

    List<String> getPolygonsGeometryByDlmc(@Param("dlmc") String dlmc);

    List<MyPolygon> getAllPolygons();

    int getPolygonsCount();

    String getPolygonGeometry(@Param("id") int id);





}
