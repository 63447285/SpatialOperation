package com.example.spatialoperation.mapper;

import com.example.spatialoperation.entity.MyPoint;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.opengis.geometry.Geometry;

import java.util.List;

@Mapper
public interface PointMapper {

    MyPoint getPointByID(@Param("objectid") int objectid);

    List<MyPoint> getPointByName(@Param("dlmc") String dlmc);

    List<MyPoint> getPointsByDistance(@Param("wkt") String wkt);

    String getPointGeometry(@Param("objectid") int objectid);

    List<MyPoint> getAll();

    int getPointsCount();

}
