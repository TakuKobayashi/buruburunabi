package com.buruburu.nabi;

import android.webkit.WebView;

public class ApplicationHelper{
  //WebViewを使用したときのメモリリーク対策
  public static void releaseWebView(WebView webview){
    webview.stopLoading();
    webview.setWebChromeClient(null);
    webview.setWebViewClient(null);
    webview.destroy();
    webview = null;
  }
}
