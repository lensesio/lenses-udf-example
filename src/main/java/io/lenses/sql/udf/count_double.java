package io.lenses.sql.udf;

import io.lenses.sql.udf.FinalStep;
import io.lenses.sql.udf.UdfException;
import io.lenses.sql.udf.UserDefinedAggregateFunction;
import io.lenses.sql.udf.datatype.DataType;
import io.lenses.sql.udf.value.LongValue;
import io.lenses.sql.udf.value.Value;

import java.util.Optional;

public class count_double implements UserDefinedAggregateFunction {

    @Override
    public DataType typer(DataType dataType) {
        return DataType.ltLong();
    }

    @Override
    public Value empty() {
        return new LongValue(0);
    }

    @Override
    public void init(Value[] args) {
    }

    @Override
    public Value add(Value aggregateKey, Value aggregatedValue, Value toBeAdded) throws UdfException {
        if (aggregatedValue instanceof LongValue) {
            LongValue agg = (LongValue) aggregatedValue;
            return new LongValue(agg.get() + 2);
        }
        throw new UdfException("Invalid input received for aggregateValue. Expecting a Long but received:" + aggregatedValue.getDataType().name);
    }

    @Override
    public Value merge(Value aggregtedKey, Value first, Value second) throws UdfException {
        long merged = getLongOrThrow(first) + getLongOrThrow(second);
        return new LongValue(merged);
    }

    private long getLongOrThrow(Value value) throws UdfException {
        if (value instanceof LongValue) {
            return ((LongValue) value).get();
        }
        throw new UdfException("Invalid input received for aggregateValue. Expecting a Long but received:" + value.getDataType().name);
    }

    @Override
    public Optional<FinalStep> finalStep() {
        return Optional.empty();
    }

    @Override
    public String name() {
        return "count_double";
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