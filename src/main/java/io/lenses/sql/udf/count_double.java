package io.lenses.sql.udf;

public class count_double implements UserDefinedAggregateFunction {

    private long count = 0;

    @Override
    public void aggregate(Object o) {
        count += 2;
    }

    @Override
    public Object result() {
        return count;
    }
}