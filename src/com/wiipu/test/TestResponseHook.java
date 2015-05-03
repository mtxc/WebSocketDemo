package com.wiipu.test;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

import com.wiipu.R;
import com.wiipu.json.entity.Response;
import com.wiipu.json.hook.ResponseHook;

public class TestResponseHook implements ResponseHook {

	@Override
	public void deal(Context context, Response response) {
		TextView tv = (TextView)((Activity)context).findViewById(R.id.tv);
		TestResponse resp = new TestResponse();
		response.getData(resp);
		tv.setText("ResponseHook:" + response+ "\nResponse:" + resp);
	}

}
