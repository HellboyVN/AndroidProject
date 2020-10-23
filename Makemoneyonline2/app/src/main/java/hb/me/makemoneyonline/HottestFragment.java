package hb.me.makemoneyonline;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by tlvan on 11/24/2017.
 */

public class HottestFragment extends Fragment {


    WebView hottestWebview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hottest, container, false);
        hottestWebview = (WebView) view.findViewById(R.id.hottestWebView);
        startWebView("file:///android_asset/pagehottest"+"/index.html",view);
        return  view;
    }
    private void startWebView(String url, View viewall) {
        hottestWebview.setWebViewClient(new WebViewClient() {

            //If you will not use this method mEditUrl links are opeen in new brower not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);
                return true;
            }

            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                //method to handle PAGE NOT AVAILABLE
                if (description.equals("net::ERR_NAME_NOT_RESOLVED")) {
                    Snackbar.make(viewall.findViewById(android.R.id.content), "Check the URL", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }

                if (description.equals("net::ERR_INTERNET_DISCONNECTED")) {
                    Snackbar.make(viewall.findViewById(android.R.id.content), "Check Your Internet Connection", Snackbar.LENGTH_LONG).setAction("Action", null).show();
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

            }
        });

        hottestWebview.setDrawingCacheBackgroundColor(0);
        hottestWebview.setFocusableInTouchMode(true);
        hottestWebview.setFocusable(true);
        hottestWebview.setDrawingCacheEnabled(true);
        hottestWebview.setWillNotCacheDrawing(false);
        hottestWebview.setScrollbarFadingEnabled(true);
        hottestWebview.setHorizontalScrollBarEnabled(false);
        WebSettings settings = hottestWebview.getSettings();
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

        hottestWebview.loadUrl(url);
    }
}