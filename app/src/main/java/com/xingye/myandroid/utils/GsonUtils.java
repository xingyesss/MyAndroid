package com.xingye.myandroid.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Gson的简单封装
 * Created by ruhuakeji-ios on 16/12/19.
 */

public class GsonUtils {
    private static Gson gson = null;

    static {
        if (gson == null) {
            gson = new Gson();
        }
    }

    private GsonUtils() {
    }

    /**
     * 转成json字符串
     *
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        String gsonString = null;
        if (gson == null) {
            gson = new Gson();
        }
        gsonString = gson.toJson(obj);
        return gsonString;
    }

    /**
     * 转成bean对象
     *
     * @param json
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> T toBean(String json, Class<T> cls) {
        T t = null;
        if (gson == null) {
            gson = new Gson();
        }
        t = gson.fromJson(json, cls);
        return t;
    }

    public static <T> T toBean(JsonReader jsonReader, Type typeOfT){
        T t = null;
        if(gson == null){
            gson = new Gson();
        }
        t = gson.fromJson(jsonReader,typeOfT);
        return t;
    }

    public static <T> List<T> toList(String json, Class<T> cls) {
        List<T> list = null;
        if (gson == null) {
            gson = new Gson();
        }
        list = gson.fromJson(json, new TypeToken<List<T>>() {
        }.getType());
        return list;
    }
}
