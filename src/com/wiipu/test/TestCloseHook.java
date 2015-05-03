package com.wiipu.test;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

import com.wiipu.R;
import com.wiipu.json.hook.CloseHook;

public class TestCloseHook implements CloseHook {

	@Override
	public void deal(Context context, int code, String reason) {
		TextView tv = (TextView)((Activity)context).findViewById(R.id.tv);
		tv.setText("code:" + code + " reason:" + reason);
	}

}