package com.buruburu.nabi;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

public class CustomHttpRequest extends HttpRequestBase<String> {
    protected Map<String, String> params;
    protected Map<String, String> headers;

    public CustomHttpRequest(int method, String url, Response.Listener response, ErrorListener errorListener) {
      super(method, url, response, errorListener);
    }

    public CustomHttpRequest(int method, String url, ErrorListener errorListener) {
        super(method, url, errorListener);
    }

    @SuppressWarnings("rawtypes")
	@Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        if(this.listener != null & this.listener instanceof HttpRequestBase.Listener){
          ((HttpRequestBase.Listener) listener).onNetworkResponse(response);
        }
        try {
            String result = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }
}