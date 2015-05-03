package com.wiipu.json.entity;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.wiipu.json.utils.Constants;
import com.wiipu.json.utils.MD5Utils;
import com.wiipu.json.utils.ReflectUtil;

public class Request {

	private long time;
	private String method;
	private String sign;
	private String sn;
	private Map<String, Object> data;

	public Request(String method, Object obj) {
		time = new Date().getTime()/1000;
		this.method = method;
		sn = Constants.SN;
		// 将要请求的内容放入data中
		setData(obj);
		// 生成签名
		generateSign();
	}

	/**
	 * 生成签名
	 */
	private void generateSign() {
		TreeMap<String, Object> map = new TreeMap<String, Object>();
		// 将request外层除Sign外的内容放入map
		Method[] methods = this.getClass().getDeclaredMethods();
		for (Method m : methods) {
			if (ReflectUtil.isGetterMethod(m) && isNeed(m))
				map.put(ReflectUtil.getName(m), ReflectUtil.getValue(m, this));
		}
		// 将request中data的内容放入map中
		map.putAll(data);
		// 设置固定格式，并将secret追加到最后
		String result = map.toString().replace("{", "").replace("}", "")
				.replace(", ", "&")
				+ "&secret=" + Constants.SECRET;
		// 进行MD5签名
		sign = MD5Utils.string2MD5(result);
	}

	/**
	 * 判断是否是需要的方法
	 */
	private boolean isNeed(Method m) {
		String name = m.getName();
		if (name.endsWith("Sign") || name.endsWith("Data"))
			return false;
		else
			return true;
	}

	/**
	 * 通过反射将请求的内容放入data中
	 */
	public void setData(Object obj) {
		data = new HashMap<String, Object>();
		if(obj != null){
			Method[] methods = obj.getClass().getDeclaredMethods();
			for (Method m : methods) {
				if (ReflectUtil.isGetterMethod(m)) {
					data.put(ReflectUtil.getName(m), ReflectUtil.getValue(m, obj));
				}
			}
		}
	}

	/**
	 * 往data中补充值
	 */
	public void putData(String key, Object value) {
		if (data == null) {
			data = new HashMap<String, Object>();
		}
		data.put(key, value);
	}

	public Map<String, Object> getData() {
		return data;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getSn() {
		return sn;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "Request [time=" + time + ", method=" + method + ", sign="
				+ sign + ", sn=" + sn + ", data=" + data + "]";
	}

}
