package com.iap.phenologyweather.config.gson.retrofit2.converter.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by chenxueqing on 2017/4/8.
 */

public class StringDefaultAdapter implements JsonSerializer<String>, JsonDeserializer<String> {
    @Override
    public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        try {
            return json.getAsString();
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public JsonElement serialize(String src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src);
    }
}
