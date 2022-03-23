package io.lenses.sql.udf;

import io.lenses.sql.udf.FinalStep;
import io.lenses.sql.udf.UdfException;
import io.lenses.sql.udf.UserDefinedAggregateFunction;
import io.lenses.sql.udf.UserDefinedTableAggregateFunction;
import io.lenses.sql.udf.datatype.DataType;
import io.lenses.sql.udf.datatype.LTStruct;
import io.lenses.sql.udf.value.LongValue;
import io.lenses.sql.udf.value.RepeatedValue;
import io.lenses.sql.udf.value.StructValue;
import io.lenses.sql.udf.value.Value;

import java.util.*;
import java.util.stream.Collectors;

public class id_aware_collect implements UserDefinedTableAggregateFunction {
    LTStruct innerType = DataType.ltStruct(
            new HashMap<String, DataType>() {{
                put("item_id", DataType.ltString());
                put("item_description", DataType.ltString());
                put("item_price", DataType.ltDouble());
                put("item_qty", DataType.ltInt());
                put("order_id", DataType.ltString());
                put("client", DataType.ltString());
                put("created_at", DataType.ltTimestampMillis());
                put("status", DataType.ltString());
            }}
    );

    @Override
    public DataType typer(DataType dataType) throws UdfException {
        if (dataType.isStruct()) {
            LTStruct struct = (LTStruct) dataType;
            if (struct.schema.containsKey("item_id") && struct.schema.containsKey("status")) {
                innerType = struct;
                return DataType.ltRepeated(dataType);
            } else {
                throw new UdfException("Expecting argument of type struct with a field `item_id` and a field 'status'");
            }
        } else {
            throw new UdfException("Expecting argument of type struct.");
        }
    }

    private RepeatedValue<StructValue> deduplicate(List<StructValue> structValues) throws UdfException {
        Collections.reverse(structValues);
        List<StructValue> newList = new ArrayList<>();
        List<String> idsVisited = new ArrayList<>();
        for (StructValue item : structValues) {
            String itemId = item.getField("item_id").toStringValue().get();
            if (!idsVisited.contains(itemId)) {
                idsVisited.add(itemId);
                newList.add(item);
            }
        }
        Collections.reverse(newList);
        return new RepeatedValue<StructValue>(newList, innerType);
    }

    @Override
    public Value empty() {
        return RepeatedValue.empty(innerType);
    }

    @Override
    public void init(Value[] args) {
    }

    @Override
    public Value add(Value aggregateKey, Value aggregatedValue, Value toBeAdded) throws UdfException {
        List<StructValue> elements = aggregatedValue.asRepeatedValue().get();
        ArrayList<StructValue> array = new ArrayList<StructValue>(elements);
        array.add(toBeAdded.asStructValue());
        return deduplicate(array);
    }

    @Override
    public Value merge(Value aggregtedKey, Value first, Value second) throws UdfException {
        List<StructValue> firstElements = first.asRepeatedValue().get();
        List<StructValue> secondElements = second.asRepeatedValue().get();
        List<StructValue> all = new ArrayList<>(firstElements);
        all.addAll(secondElements);
        return deduplicate(all);
    }

    @Override
    public Optional<FinalStep> finalStep() {
        return Optional.empty();
    }

    @Override
    public String name() {
        return "id_aware_collect";
    }

    @Override
    public String version() {
        return "1.0";
    }

    @Override
    public String owner() {
        return "Lenses.io";
    }

    @Override
    public Value subtract(Value value, Value value1, Value value2) throws UdfException {
        String itemId = value2.asStructValue().getField("item_id").toStringValue().get();
        List<StructValue> values = value1.asRepeatedValue().get();
        List<StructValue> result = values.stream().filter(f -> {
            boolean keep = true;
            try {
                keep = !f
                        .getField("item_id")
                        .toStringValue()
                        .get()
                        .equals(itemId);
            } catch (UdfException e) {
                e.printStackTrace();
            }
            return keep;
        }).collect(Collectors.toList());
        return new RepeatedValue(result, innerType);
    }
}