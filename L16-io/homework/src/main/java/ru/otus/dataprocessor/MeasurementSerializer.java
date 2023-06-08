package ru.otus.dataprocessor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.Map;

public class MeasurementSerializer implements JsonSerializer<Map<String, Double>> {

    @Override
    public JsonElement serialize(
        Map<String, Double> src,
        Type typeOfSrc,
        JsonSerializationContext context
    ) {
        JsonObject result = new JsonObject();
        result.add("val1", context.serialize(src.get("val1")));
        result.add("val2", context.serialize(src.get("val2")));
        result.add("val3", context.serialize(src.get("val3")));
        return result;
    }
}
