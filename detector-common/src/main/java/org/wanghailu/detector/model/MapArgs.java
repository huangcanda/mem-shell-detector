package org.wanghailu.detector.model;

import org.wanghailu.detector.util.SerializeUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * java的Map对象和字符串互相转换
 *
 * @author cdhuang
 * @date 2023/6/9
 */
public class MapArgs implements Serializable {
    
    private static final long serialVersionUID = -6743567631108323096L;
    
    private Map<String, String> args = new HashMap<>();
    
    public static MapArgs getMapArgs(String str) {
        MapArgs mapArgs = SerializeUtils.deserializeFromString(str);
        if (mapArgs == null) {
            mapArgs = new MapArgs();
        }
        return mapArgs;
    }
    
    public static String mapToString(MapArgs mapArgs) {
        if (mapArgs == null || mapArgs.isEmpty()) {
            return "";
        }
        return SerializeUtils.serializeToString(mapArgs);
    }
    
    public String[] getArrayValue(String key) {
        String value = args.getOrDefault(key, "");
        return value.split(",");
    }
    
    public String get(String key) {
        return args.get(key);
    }
    
    public String getOrDefault(String key, String defaultValue) {
        return args.getOrDefault(key, defaultValue);
    }
    
    public String put(String key, String value) {
        return args.put(key, value);
    }
    
    public boolean isEmpty() {
        return args.isEmpty();
    }
    
    public Map<String, String> getArgs() {
        return args;
    }
    
    @Override
    public String toString() {
        return MapArgs.mapToString(this);
    }
}
