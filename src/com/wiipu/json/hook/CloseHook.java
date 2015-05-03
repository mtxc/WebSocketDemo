package com.wiipu.json.hook;

import android.content.Context;

/**
 * 处理连接关闭的接口
 */
public interface CloseHook {

	public void deal(Context context, int code, String reason);

}
