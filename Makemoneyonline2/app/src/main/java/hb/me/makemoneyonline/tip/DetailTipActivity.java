package hb.me.makemoneyonline.tip;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;

import hb.me.makemoneyonline.LogUtils;
import hb.me.makemoneyonline.PopupDialog;
import hb.me.makemoneyonline.R;
import hb.me.makemoneyonline.Utils.Constant;


public class DetailTipActivity extends AppCompatActivity {
    private WebView webView;
    private String textUrl = "";
    private TextView txtTitle;
    private TextView txtHost;
    String link2 = "";
    String title2 = "";
    private AdView adView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(PreferenceManager.getDefaultSharedPreferences(this).getInt("theme", Constant.theme));
        setContentView(R.layout.activity_detail_tip);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        webView = (WebView) findViewById(R.id.webView);
        //event
        findViewById(R.id.image_left_menu).setOnClickListener(view -> {
            finish();
        });

        ImageView imagePopUp = (ImageView) findViewById(R.id.image_right_menu);
        imagePopUp.setOnClickListener(view -> {
            showShareIntent(imagePopUp);
        });

        txtTitle = (TextView) findViewById(R.id.txt_title_page);
        txtHost = (TextView) findViewById(R.id.txt_host);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        title2= title;
        String link = intent.getStringExtra("url");
        link2 = link;
        LogUtils.d("host 222222: " + link);
        txtHost.setText("Chapter I");
        txtTitle.setText(title);
        startWebView(link);
        LogUtils.d("host : " + link);
        RelativeLayout adViewContainer = (RelativeLayout) findViewById(R.id.adViewContainer);

        adView = new AdView(this, "1662992173775526_1662994517108625", AdSize.BANNER_320_50);
        adViewContainer.addView(adView);
        adView.loadAd();
    }

    private void startWebView(String url) {
        webView.setWebViewClient(new WebViewClient() {

            //If you will not use this method mEditUrl links are opeen in new brower not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                textUrl = url;
                view.loadUrl(url);
                return true;
            }

            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                //method to handle PAGE NOT AVAILABLE
                if (description.equals("net::ERR_NAME_NOT_RESOLVED")) {
                    Snackbar.make(findViewById(android.R.id.content), "Check the URL", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }

                if (description.equals("net::ERR_INTERNET_DISCONNECTED")) {
                    Snackbar.make(findViewById(android.R.id.content), "Check Your Internet Connection", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }

            @TargetApi(android.os.Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                // Redirect to deprecated method, so you can use it in all SDK versions
                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
            }

            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //link.setText(mEditUrl);
                textUrl = url;
            }
        });

        webView.setDrawingCacheBackgroundColor(0);
        webView.setFocusableInTouchMode(true);
        webView.setFocusable(true);
        webView.setDrawingCacheEnabled(true);
        webView.setWillNotCacheDrawing(false);
        webView.setScrollbarFadingEnabled(true);
        webView.setHorizontalScrollBarEnabled(false);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        if (Build.VERSION.SDK_INT > 16) {
            settings.setAllowUniversalAccessFromFileURLs(true);
            settings.setAllowFileAccessFromFileURLs(true);
            settings.setAllowFileAccess(true);
        }
        settings.setAppCacheEnabled(false);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        if (Build.VERSION.SDK_INT >= 19) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        webView.loadUrl(url);
    }

    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    void showShareIntent(View v) {

        try {
            PopupDialog dialog = new PopupDialog(this, new PopupDialog.OnMenuPopup() {
                @Override
                public void onActionOne() {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("https://www.google.com.vn"));
                    startActivity(i);
                }

                @Override
                public void onActionTwo() {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra("android.intent.extra.TEXT", title2 + " : \n" + link2);
                    startActivity(Intent.createChooser(intent, getResources().getString(R.string.share)));
                }
            }, "To Browser", "Share");
            dialog.showDropDownAtView(v);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
