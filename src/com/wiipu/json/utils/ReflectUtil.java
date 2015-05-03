package com.wiipu.json.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectUtil {

	/**
	 * 判断是否是get方法
	 */
	public static boolean isGetterMethod(Method m) {
		String name = m.getName();
		if (name.startsWith("get"))
			return true;
		else
			return false;
	}

	/**
	 * 获取get或set方法的参数名称
	 */
	public static String getName(Method m) {
		StringBuffer sb = new StringBuffer(m.getName());
		sb.delete(0, 3);
		sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
		return sb.toString();
	}

	/**
	 * 获取get方法的返回值
	 */
	public static Object getValue(Method m, Object obj) {
		Object o = null;
		try {
			o = m.invoke(obj, (Object[]) null);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return o;
	}

}
