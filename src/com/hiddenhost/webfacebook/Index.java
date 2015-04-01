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
import android.widget.Toast;

public class Index extends Activity {

  int layout = 0;
  int retry = 0;

  private ImageButton refresh_button;
  private ImageButton refresh_offline_button;
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
      refresh_offline_button = (ImageButton) findViewById(R.id.refresh_offline_button);
      refresh_offline_button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          if (isNetworkAvailable()) {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
          } else if (retry == 2) {
            Toast.makeText(getApplicationContext(), "Don't you read? You're offline, dude.", Toast.LENGTH_SHORT).show();
            retry = 0;
          } else {
            retry += 1;
          }
        }
      });
    } else {
      splash_image = (ImageView) findViewById(R.id.splash_image);
      refresh_button = (ImageButton) findViewById(R.id.refresh_button);
      facebook_webview = (WebView) findViewById(R.id.webview);
      facebook_webview.getSettings().setJavaScriptEnabled(true);
      facebook_webview.getSettings().setLoadsImagesAutomatically(true);
      facebook_webview.setWebViewClient(new WebViewClient() {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
          if (url != null &&
             !url.startsWith("https://m.facebook") &&
             !url.startsWith("https://h.facebook") &&
             !url.startsWith("https://facebook") &&
             !url.startsWith("http://m.facebook") &&
             !url.startsWith("http://h.facebook") &&
             !url.startsWith("http://facebook")) {
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
      layout = 1;
      facebook_webview.loadUrl(facebook_url);
      refresh_button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          facebook_webview.loadUrl(facebook_url);
        }
      });
    }
  }

  private boolean isNetworkAvailable() {
    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
    return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
  }

  @Override
  public void onBackPressed() {
	if (layout == 1){
	  if (facebook_webview.canGoBack()) {
        facebook_webview.goBack();
      } else {
        facebook_webview.loadUrl(facebook_url);
      }
	} else {
      super.onBackPressed();
	}
  }

}
