package com.aptech.mymusic.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class JsonHelper {

    private static final Gson sGson = new Gson();

    public static String objToJson(Object o) {
        return sGson.toJson(o);
    }

    public static <T> T jsonToObj(String strJson, Class<T> clazz) {
        return sGson.fromJson(strJson, clazz);
    }

    public static <T> List<T> jsonToList(String strJson, Class<T> clazz) {
        return sGson.fromJson(strJson, TypeToken.getParameterized(List.class, clazz).getType());
    }

}
