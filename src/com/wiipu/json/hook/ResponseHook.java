package com.wiipu.json.hook;

import android.content.Context;
import com.wiipu.json.entity.Response;

/**
 * 处理回复消息的接口
 */
public interface ResponseHook {
	public void deal(Context context, Response response);
}
