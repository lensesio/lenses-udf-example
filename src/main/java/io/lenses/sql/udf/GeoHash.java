package io.lenses.sql.udf;

import io.lenses.sql.udf.datatype.DataType;
import io.lenses.sql.udf.value.DoubleValue;
import io.lenses.sql.udf.value.StringValue;
import io.lenses.sql.udf.value.Value;

public class GeoHash implements UserDefinedFunction2 {
    @Override
    public DataType typeMapping(DataType arg1, DataType arg2) throws UdfException {
        if (!arg1.isDouble())
            throw new UdfException("Expecting first argument to be double but found:" + arg1.name);
        if (!arg2.isDouble())
            throw new UdfException("Expecting first argument to be double but found:" + arg2.name);

        return DataType.ltString();
    }

    @Override
    public Value evaluate(Value arg1, Value arg2) throws UdfException {
        double lat = getDoubleOrThrow(arg1);
        double lng = getDoubleOrThrow(arg2);
        return new StringValue(ch.hsr.geohash.GeoHash.geoHashStringWithCharacterPrecision(lat, lng, 8));
    }

    private double getDoubleOrThrow(Value value) throws UdfException {
        if (value instanceof DoubleValue) {
            return ((DoubleValue) value).get();
        }
        throw new UdfException("Invalid input received for aggregateValue. Expecting a Long but received:" + value.getDataType().name);
    }

    @Override
    public String name() {
        return "geohash";
    }

    @Override
    public String version() {
        return "1.0";
    }

    @Override
    public String owner() {
        return "Lenses.io";
    }
}
