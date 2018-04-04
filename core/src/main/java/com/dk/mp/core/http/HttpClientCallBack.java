package com.dk.mp.core.http;

import org.json.JSONObject;

public interface HttpClientCallBack {

	abstract void success(int waht, JSONObject json);

	abstract void fail(int what);
}
