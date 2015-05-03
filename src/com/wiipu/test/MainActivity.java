package com.wiipu.test;

import android.app.Activity;
import android.os.Bundle;

import com.wiipu.R;
import com.wiipu.R.layout;
import com.wiipu.json.JsonDispatcher;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		TestRequest request = new TestRequest();
		request.setName("mtxc");
		request.setNum(20);
		JsonDispatcher.getInstance().dispatch(this, "test", request);
	}
	
}