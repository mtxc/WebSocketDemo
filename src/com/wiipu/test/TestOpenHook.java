package com.wiipu.test;

import com.wiipu.json.hook.OpenHook;

public class TestOpenHook implements OpenHook {

	@Override
	public void deal() {
		System.out.println("OpenHook:");
	}

}
