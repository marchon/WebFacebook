package com.hiddenhost.webfacebook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Index extends Activity {

    private WebView mWebView;
    private String url = "https://m.facebook.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isNetworkAvailable()) {
        	setContentView(R.layout.activity_offline);
        } else {
        	requestWindowFeature(Window.FEATURE_NO_TITLE);
        	setContentView(R.layout.activity_index);
        	mWebView = (WebView) findViewById(R.id.webview);
        	mWebView.getSettings().setJavaScriptEnabled(true);
        	mWebView.getSettings().setLoadsImagesAutomatically(true);
        	mWebView.setWebViewClient(new WebViewClient() {
        		public boolean shouldOverrideUrlLoading(WebView view, String url) {
        			if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
        				view.getContext().startActivity(
        				new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        				return true;
        			} else {
        				return false;
        			}
        		}
        	});
        	mWebView.loadUrl(url);
        }
    }

    private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

}