package io.lenses.sql.udf;

import java.util.List;

public class GeoHash implements UserDefinedFunction {

    @Override
    public Object evaluate(List<Object> list) {
        Double lat = (Double) list.get(0);
        Double lng = (Double) list.get(1);
        return ch.hsr.geohash.GeoHash.geoHashStringWithCharacterPrecision(lat, lng, 8);
    }
}
