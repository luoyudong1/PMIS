package com.kthw.common;

/*
 * 文件名：GsonUtils.java
 * 版权：Copyright 2010-2012 北京康拓红外. All Rights Reserved. 
 * 修改内容：新增 
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.TimeTypeAdapter;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import net.sf.json.util.JSONUtils;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 转换bean为json字符串
 *
 * @version V1.00 2011-5-18
 * @see [相关类/方法]
 */
public class GsonUtils {

	public static String object2Json(Object obj) {
		String result = "";
		Gson gson = new GsonBuilder().registerTypeAdapter(Timestamp.class, new TimeTypeAdapter())
			.setDateFormat("yyyy-MM-dd HH:mm:ss")
			.create();
		result = gson.toJson(obj);
		return result;
	}

	//转换list对象为json字符串
	public static String list2Json(List<?> list) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", true);
		map.put("totalCount", list.size());
		map.put("result", list);
		return object2Json(map);
	}

	//转换json字符串为java对象数组
	public static Object json2Array(String json, Class<?> cls) {
		String[] dateFormats = new String[]{"yyyy-MM-dd HH:mm:ss"};
		JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(dateFormats));
		JSONArray arry = JSONArray.fromObject(json);
		JsonConfig j = new JsonConfig();
		j.setArrayMode(JsonConfig.MODE_OBJECT_ARRAY);
		j.setRootClass(cls);
		return JSONSerializer.toJava(arry, j);
	}

	//转换json字符串为java对象
	public static Object json2Object(String json, Class<?> cls) {
		String[] dateFormats = new String[]{"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd"};
		JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(dateFormats));
		JSONObject jsonObject = JSONObject.fromObject(json);
		Object obj = JSONObject.toBean(jsonObject, cls);
		return obj;
	}
	public static void main(String[] args) {
		/*Map  map = new HashedMap();
		List rootList = new ArrayList();
        rootList.add("str1");
        rootList.add("str2");
        map.put("total", 144);
        map.put("root", rootList);
        System.out.println(object2Json(map));*/
	}
}