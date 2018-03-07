package com.allen.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;



public class GsonUtils {

	/**
	 * 时间格式
	 */
	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * 创建GSON
	 * @return
	 */
	public static Gson getGson(){
		return getGson(DATE_FORMAT);
	}
	
	/**
	 * 通过指定时间格式创建GSON
	 * @param dateFormat
	 * @return
	 */
	public static Gson getGson(String dateFormat){
		return new GsonBuilder().disableHtmlEscaping().setDateFormat(dateFormat).create();
	}
 
    /**
     * 将对象转化为字符串
     * @param obj
     * @return
     */
    public static String toJson(Object obj){
    	return getGson().toJson(obj);
    }
    
    /**
     * 将对象转化为字符串(需要指定时间格式)
     * @param obj
     * @param dateFormat时间格式
     * @return
     */
    public static String toJson(Object obj,String dateFormat){
    	return getGson(dateFormat).toJson(obj);
    }
 
    /**
     * 将字符转化为对象
     * @param <T>
     * @param jsonString
     * @param clazz
     * @return
     */
    public static <T> T fromJson(String jsonString , Class<T> clazz){
        return new Gson().fromJson(jsonString,clazz);
    }
    
    /**
     * 将字符转化为对象
     * @param <T>
     * @param jsonString
     * @param clazz
     * @return
     */
    public static <T> T fromJson(String jsonString , Type type){
        return new Gson().fromJson(jsonString,type);
    }
 
}
