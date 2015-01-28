package com.hiddenhost.webfacebook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;

public class Index extends Activity {

	private ImageButton RefreshButton;
	private ImageView SplashImage;
	private String url = "https://m.facebook.com/";
	private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    	setContentView(R.layout.activity_index);
    	if (!isNetworkAvailable()) {
    		setContentView(R.layout.activity_offline);
        } else {
        	setContentView(R.layout.activity_index);
        	SplashImage = (ImageView) findViewById(R.id.splash_image);
        	RefreshButton = (ImageButton) findViewById(R.id.refresh_button);
        	mWebView = (WebView) findViewById(R.id.webview);
        	mWebView.getSettings().setJavaScriptEnabled(true);
        	mWebView.getSettings().setLoadsImagesAutomatically(true);
        	mWebView.setWebViewClient(new WebViewClient() {
        		public boolean shouldOverrideUrlLoading(WebView view, String url) {
        			if (url != null && !url.startsWith("https://m.facebook")) {
        				view.getContext().startActivity(
        				new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        				return true;
        			} else {
        				return false;
        			}
        		}
        		public void onPageFinished(WebView view, String url) {
        			mWebView.setVisibility(View.VISIBLE);
        			RefreshButton.setVisibility(View.VISIBLE);
        			SplashImage.setVisibility(View.GONE);
        		}
  			});
        	mWebView.loadUrl(url);
        }

    	RefreshButton.setOnClickListener(new View.OnClickListener() {
    	    @Override
    	    public void onClick(View view) {
    	    	mWebView.loadUrl(url);
    	    }
    	});
    }

    private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
    
    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

}