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

  private ImageButton refresh_button;
  private ImageView splash_image;
  private String facebook_url = "https://m.facebook.com/";
  private WebView facebook_webview;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.activity_index);
    if (!isNetworkAvailable()) {
   	  setContentView(R.layout.activity_offline);
    } else {
      setContentView(R.layout.activity_index);
      splash_image = (ImageView) findViewById(R.id.splash_image);
      refresh_button = (ImageButton) findViewById(R.id.refresh_button);
      facebook_webview = (WebView) findViewById(R.id.webview);
      facebook_webview.getSettings().setJavaScriptEnabled(true);
      facebook_webview.getSettings().setLoadsImagesAutomatically(true);
      facebook_webview.setWebViewClient(new WebViewClient() {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
          if (url != null && !url.startsWith("https://m.facebook")) {
            view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            return true;
          } else {
            return false;
          }
        }
        public void onPageFinished(WebView view, String url) {
          facebook_webview.setVisibility(View.VISIBLE);
          refresh_button.setVisibility(View.VISIBLE);
          splash_image.setVisibility(View.GONE);
        }
      });
      facebook_webview.loadUrl(facebook_url);
    }

    refresh_button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        facebook_webview.loadUrl(facebook_url);
      }
    });
  }

  private boolean isNetworkAvailable() {
    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
  }

  @Override
  public void onBackPressed() {
    if (facebook_webview.canGoBack()) {
      facebook_webview.goBack();
    } else {
      super.onBackPressed();
    }
  }

}
