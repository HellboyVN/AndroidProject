package com.tlvanelearning.ielts;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContentActivity extends com.tlvanelearning.ielts.parent.BaseActivity implements com.tlvanelearning.ielts.database.KEYConstants {
    @BindView(R.id.adsView)
    LinearLayout adsView;
    private JSONObject jsonObject;
    @BindView(R.id.empty)
    ProgressBar progressBar;
    private int tableIndex = 1;
    @BindView(R.id.webView)
    WebView webView;
    private String textUrl = "";
    private boolean checkurl = false;

    private class GetDataAsyncTask extends AsyncTask<Context, Void, String> {
        private GetDataAsyncTask() {
        }

        protected String doInBackground(Context... contexts) {
            try {
                if ((jsonObject.getInt("id") >= 153) && (jsonObject.getInt("id") <= 168)) {
                    return "file:///android_asset/page" + String.valueOf(jsonObject.getInt("id") - 152) + "/index.html";
                }else  if ((jsonObject.getInt("id") >= 1) && (jsonObject.getInt("id") <= 10) &&
                        (jsonObject.toString().contains("IELTS"))) {
                    return "file:///android_asset/speak" + String.valueOf(jsonObject.getInt("id")) + "/index.html";
                }
                return processHtmlWithObject(com.tlvanelearning.ielts.database.DatabaseIO.database(context).getContentWithId(jsonObject.getInt("id"), tableIndex));
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }

        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            facebookBanner(context, adsView);
        }

        protected void onPostExecute(String string) {
            super.onPostExecute(string);
            try {
                setTitle(jsonObject.getString("title"));
                if (!TextUtils.isEmpty(string)) {
                    if (string.contains("index.html")) {
                        startWebView(string);
                    } else {
                        webView.loadDataWithBaseURL(null, string, "text/html", "UTF-8", null);
                    }

                }
                progressBar.setVisibility(View.GONE);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.ielts_sample_detail);
        ButterKnife.bind((Activity) this);
//        this.webView.setScrollViewCallbacks(this);
    }

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        try {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                this.tableIndex = extras.getInt("TABLE_INDEX");
                this.jsonObject = new JSONObject(extras.getString("EXTRAS"));
                new GetDataAsyncTask().execute(new Context[]{this.context});
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String processHtmlWithObject(String _content) {
        StringBuilder stringBuilder = new StringBuilder("<!DOCTYPE html>");
        try {
            stringBuilder.append("<html>");
            stringBuilder.append("<head>");
            stringBuilder.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">");
            stringBuilder.append("<style type='text/css'>img { max-width: 100%; width: auto; height: auto; }body {\n" +
                    "    background-color: #fefefe;\n" +
                    "}</style>");
            stringBuilder.append("<meta http-equiv=\"REFRESH\" content=\"1800\" />");
            stringBuilder.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=yes\" />");
            stringBuilder.append("</head>");
            stringBuilder.append("<body>");
            stringBuilder.append("<p>" + _content + "</p>");
            stringBuilder.append("</body>");
            stringBuilder.append("</html>");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        restoreActionBar();
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return super.onOptionsItemSelected(item);
        }
        finish();
        return true;
    }

    public void finish() {
        setResult(0, getIntent());
        super.finish();
    }
}
