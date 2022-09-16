package com.example.spatialoperation.KmeanPolygon;

import org.locationtech.jts.io.ParseException;

public interface KmeanPolygonSplitInterface {
    KmeanPolygonResult splitPolygon(String wkt, int setp, int k) throws ParseException, com.vividsolutions.jts.io.ParseException;
}
