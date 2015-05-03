package com.wiipu.json.entity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Response {

	private int status;
	private long time;
	private String method;
	private String message;
	private Map<String, Object> data;
	
	/**
	 * 用反射将data解析出来
	 */
	public void getData(Object obj) {
		for (String key : data.keySet()) {
			try {
				Method m = obj.getClass().getMethod("set"+Character.toUpperCase(key.charAt(0))+key.substring(1), data.get(key).getClass());
				m.invoke(obj, data.get(key));
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	public void putData(String key, Object value) {
		if (data == null) {
			data = new HashMap<String, Object>();
		}
		data.put(key, value);
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "Response [status=" + status + ", time=" + time + ", method="
				+ method + ", message=" + message + ", data=" + data + "]";
	}

}