package com.example.grupo6.appgrup6;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class RecetarioActivity extends AppCompatActivity {

    //private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recetario);

        final WebView myWebView = (WebView) findViewById(R.id.web);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setAppCacheEnabled(true);
        myWebView.loadUrl("https://www.vitonica.com/categoria/recetas-saludables");
        myWebView.setWebViewClient(new WebViewClient(){
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl){
                myWebView.loadUrl("file:///android_asset/error.html");
            }
        });


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        WebView mWebView;
        mWebView = (WebView) findViewById(R.id.web);
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (mWebView.canGoBack()) {
                        mWebView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
