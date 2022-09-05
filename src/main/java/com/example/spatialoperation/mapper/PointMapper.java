package com.example.spatialoperation.mapper;

import com.example.spatialoperation.entity.MyPoint;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.opengis.geometry.Geometry;

import java.util.List;

@Mapper
public interface PointMapper {

    MyPoint getPointByID(@Param("id") int id);

    List<MyPoint> getPointByName(@Param("name") String name);

    String getPointGeometry(@Param("id") int id);

    List<MyPoint> getAll();

    int getPointsCount();

}
