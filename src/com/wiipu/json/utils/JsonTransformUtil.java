package com.wiipu.json.utils;

import org.codehaus.jackson.map.ObjectMapper;
import com.wiipu.json.entity.Request;
import com.wiipu.json.entity.Response;

public class JsonTransformUtil {

	public static ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * 将json解析成为Response
	 */
	public static Response jsonToResponse(String json) {
		Response response = null;
		try {
			response = objectMapper.readValue(json, Response.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * 将Request解析成为json
	 */
	public static String RequestToJson(Request request) {
		String json = "";
		try {
			json = objectMapper.writeValueAsString(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

}
