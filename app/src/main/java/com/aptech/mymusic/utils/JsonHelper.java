package com.aptech.mymusic.utils;

import androidx.annotation.NonNull;

import com.aptech.mymusic.utils.annotation.IgnoreField;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Set;

public class JsonHelper {

    private static final Gson sGson = new GsonBuilder()
            .setExclusionStrategies(new IgnoreStrategy())
            .create();

    public static String objToJson(Object o) {
        return sGson.toJson(o);
    }

    public static <T> T jsonToObj(String strJson, Class<T> clazz) {
        return sGson.fromJson(strJson, clazz);
    }

    public static <T> List<T> jsonToList(String strJson, Class<T> clazz) {
        return sGson.fromJson(strJson, TypeToken.getParameterized(List.class, clazz).getType());
    }
    public static <T> Set<T> jsonToSet(String strJson, Class<T> clazz) {
        return sGson.fromJson(strJson, TypeToken.getParameterized(Set.class, clazz).getType());
    }

    private static class IgnoreStrategy implements ExclusionStrategy {

        public boolean shouldSkipField(@NonNull FieldAttributes f) {
            return f.getAnnotation(IgnoreField.class) != null;
        }

        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }

    }
}
