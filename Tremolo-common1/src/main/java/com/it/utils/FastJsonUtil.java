package com.it.utils;

import java.util.HashMap;

import com.alibaba.fastjson.JSON;

/**
 * @Description: fastjson工具类
 * @author 001
 * @date 2019年7月04日
 *
 */
public class FastJsonUtil {

	public static HashMap<String, Object> json3Map(String jsonStr) {
		return  JSON.parseObject(jsonStr, new HashMap<String, Object>().getClass());
	}

}
