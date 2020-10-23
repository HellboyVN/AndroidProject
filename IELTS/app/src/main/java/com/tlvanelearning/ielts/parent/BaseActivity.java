package com.tlvanelearning.ielts.parent;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.tlvanelearning.ielts.TabActivity;

public class BaseActivity extends ADSActivity implements ObservableScrollViewCallbacks {
    protected Context context = this;

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                if (!(this instanceof TabActivity)) {
                    finish();
                    break;
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onScrollChanged(int i, boolean b, boolean b1) {
    }

    public void onDownMotionEvent() {
    }

    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        ActionBar ab = getSupportActionBar();
        if (scrollState == ScrollState.UP) {
            if (ab.isShowing()) {
                ab.hide();
            }
        } else if (scrollState == ScrollState.DOWN && !ab.isShowing()) {
            ab.show();
        }
    }
}
