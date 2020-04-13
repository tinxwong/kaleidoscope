package com.xnew.kaleidoscope.dbgen.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName MapUtils
 * @Description TODO
 * @Author tinx
 * @Date 2019/3/15 0015 下午 1:04
 * @Version 1.0
 **/
public class MapUtils {

    public static Map<String,Object> map = new HashMap<>();

    public static Object getValue(String key){
        return map.get(key);
    }

    public static void put(String key,Object object){
        map.put(key,object);
    }

    public static Object remove(String key){
        return map.remove(key);
    }
}
