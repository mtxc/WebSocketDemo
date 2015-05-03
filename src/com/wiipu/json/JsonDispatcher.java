package com.wiipu.json;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import android.content.Context;
import android.util.Log;

import com.wiipu.json.entity.Request;
import com.wiipu.json.entity.Response;
import com.wiipu.json.hook.CloseHook;
import com.wiipu.json.hook.OpenHook;
import com.wiipu.json.hook.ResponseHook;
import com.wiipu.json.utils.Constants;
import com.wiipu.json.utils.JsonTransformUtil;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

public class JsonDispatcher {

	private WebSocketConnection mConnection = null;
	private Map<String, Class<?>> hook = null;
	private Context mContext = null;
	private String mMethod = "";
	private Object mReqObj = null;

	/**
	 * 使用静态内部类的方式实现单例模式
	 */
	private static class DispatcherHolder {
		private final static JsonDispatcher INSTANCE = new JsonDispatcher();
	}

	private JsonDispatcher() {
		init();
	}

	/**
	 * 获取发送器单例
	 */
	public static JsonDispatcher getInstance() {
		return DispatcherHolder.INSTANCE;
	}

	/**
	 * 发送请求
	 */
	public void dispatch(Context context, String method, Object reqObj) {
		mContext = context;
		mMethod = method;
		mReqObj = reqObj;
		new Thread(){
			public void run() {
				Request request = new Request(mMethod, mReqObj);
				String json = JsonTransformUtil.RequestToJson(request);
				// ---------------------------------------------
				// ---------------------------------------------
				Log.e("request", json);
				// ---------------------------------------------
				// ---------------------------------------------
				if(mConnection.isConnected()){
					mConnection.sendTextMessage(json);
				}
			};
		}.start();
	}

	/**
	 * 重新连接
	 */
	public void reConnect() {
		// 如果已连接，返回
		if (mConnection.isConnected())
			return;
		// 如果未连接，建立连接
		try {
			mConnection.connect(Constants.wsUri, new WebSocketHandler() {

				@Override
				public void onOpen() {
					// 连接成功
					try {
						((OpenHook) hook.get("openCallBack").newInstance())
								.deal();
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onTextMessage(String json) {
					// 消息回传
					System.out.println(json);
					Response response = JsonTransformUtil.jsonToResponse(json);
					// ---------------------------------------------
					// ----------------------------------------------
					Log.e("response", response.toString());
					// ---------------------------------------------
					// ----------------------------------------------
					// 根据返回消息的method调用对应的实现处理请求接口的类型
					String method = response.getMethod();
					try {
						((ResponseHook) hook.get(method).newInstance()).deal(
								mContext, response);
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onClose(int code, String reason) {
					// 连接关闭
					try {
						((CloseHook) hook.get("closeCallBack").newInstance())
								.deal(mContext, code, reason);
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			});
			// 休眠200ms等待连接结果
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} catch (WebSocketException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化
	 */
	@SuppressWarnings("unchecked")
	private void init() {
		hook = new HashMap<String, Class<?>>();
		// 将所有的实现处理请求的接口的类对象从配置文件中读入
		try {
			Properties properties = new Properties();
			properties.load(this.getClass().getResourceAsStream(
					"/assets/hook.properties"));
			for (Object key : properties.keySet()) {
				hook.put(key.toString(), (Class<? extends ResponseHook>) Class
						.forName(properties.getProperty(key.toString())));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		// 开启与服务器的webSocket连接
		mConnection = new WebSocketConnection();
		reConnect();
	}

}
