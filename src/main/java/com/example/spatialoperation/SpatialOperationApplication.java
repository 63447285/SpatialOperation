package com.example.spatialoperation;

import com.example.spatialoperation.entity.MyPoint;
import com.example.spatialoperation.mapper.PointMapper;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.*;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.swing.JMapFrame;
import org.geotools.swing.data.JFileDataStoreChooser;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
@MapperScan("com.example.spatialoperation.mapper")
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class SpatialOperationApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(SpatialOperationApplication.class, args);
        long max=Runtime.getRuntime().maxMemory();
        long total=Runtime.getRuntime().totalMemory();
        System.out.println("虚拟机试图使用的最大内存量："+max/1024/1024+"mb");
        System.out.println("虚拟机内存总量："+total/1024/1024+"mb");
    }





}
