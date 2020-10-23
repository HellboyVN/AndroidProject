package com.photo.sketch.gallery;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.aerotools.photo.sketch.maker.editor.R;
import com.photo.sketch.gallery.ImageFragment.OnImageSelectedListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeFragmentActivity extends FragmentActivity implements OnImageSelectedListener {
    private static Uri fileUri;
    OnClickListener clickListener = new OnClickListener() {
        public void onClick(View view) {
            if (view == HomeFragmentActivity.this.headerBarBack) {
                HomeFragmentActivity.this.finish();
            }
        }
    };
    private final Handler handler = new Handler();
    private ImageView headerBarBack;
    private FragmentTabHost mTabHost;

    public /* bridge */ /* synthetic */ View onCreateView(View view, String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(view, str, context, attributeSet);
    }

    public /* bridge */ /* synthetic */ View onCreateView(String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(str, context, attributeSet);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(R.layout.activity_home_media_chooser);
        this.headerBarBack = (ImageView) findViewById(R.id.backArrowImageViewFromMediaChooserHeaderView);
        this.mTabHost = (FragmentTabHost) findViewById(16908306);
        this.mTabHost.setup(this, getSupportFragmentManager(), R.id.realTabcontent);
        this.headerBarBack.setOnClickListener(this.clickListener);
        if (getIntent() == null || !getIntent().getBooleanExtra("isFromBucket", false)) {
            if (MediaChooserConstants.showImage) {
                this.mTabHost.addTab(this.mTabHost.newTabSpec("tab1").setIndicator(new StringBuilder(String.valueOf(getResources().getString(R.string.images_tab)))), ImageFragment.class, null);
            }
        } else if (getIntent().getBooleanExtra("image", false)) {
            Bundle bundle = new Bundle();
            bundle.putString("name", getIntent().getStringExtra("name"));
            this.mTabHost.addTab(this.mTabHost.newTabSpec("tab1").setIndicator(new StringBuilder(String.valueOf(getResources().getString(R.string.images_tab)))), ImageFragment.class, bundle);
        } else {
            new Bundle().putString("name", getIntent().getStringExtra("name"));
        }
        this.mTabHost.getTabWidget().setBackgroundResource(R.drawable.tab_icon);
        for (int i = 0; i < this.mTabHost.getTabWidget().getChildCount(); i++) {
            TextView textView = (TextView) this.mTabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            if (textView.getLayoutParams() instanceof LayoutParams) {
                LayoutParams params = (LayoutParams) textView.getLayoutParams();
                params.addRule(14);
                params.addRule(15);
                params.height = -1;
                params.width = -2;
                textView.setLayoutParams(params);
            } else if (textView.getLayoutParams() instanceof LinearLayout.LayoutParams) {
                LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) textView.getLayoutParams();
                params2.gravity = 17;
                textView.setLayoutParams(params2);
            }
            textView.setTextColor(getResources().getColor(R.color.black));
            textView.setTextSize((float) convertDipToPixels(10.0f));
        }
        if (this.mTabHost.getTabWidget().getChildAt(0) != null) {
            ((TextView) this.mTabHost.getTabWidget().getChildAt(0).findViewById(16908310)).setTextColor(-1);
        }
        if (this.mTabHost.getTabWidget().getChildAt(1) != null) {
            ((TextView) this.mTabHost.getTabWidget().getChildAt(1).findViewById(16908310)).setTextColor(getResources().getColor(R.color.white));
        }
        this.mTabHost.setOnTabChangedListener(new OnTabChangeListener() {
            public void onTabChanged(String tabId) {
                FragmentManager fragmentManager = HomeFragmentActivity.this.getSupportFragmentManager();
                ImageFragment imageFragment = (ImageFragment) fragmentManager.findFragmentByTag("tab1");
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if (tabId.equalsIgnoreCase("tab1")) {
                    if (imageFragment != null) {
                        fragmentTransaction.show(imageFragment);
                    }
                    ((TextView) HomeFragmentActivity.this.mTabHost.getTabWidget().getChildAt(0).findViewById(16908310)).setTextColor(HomeFragmentActivity.this.getResources().getColor(R.color.transparent));
                    ((TextView) HomeFragmentActivity.this.mTabHost.getTabWidget().getChildAt(1).findViewById(16908310)).setTextColor(-1);
                } else {
                    ((TextView) HomeFragmentActivity.this.mTabHost.getTabWidget().getChildAt(0).findViewById(16908310)).setTextColor(-1);
                    ((TextView) HomeFragmentActivity.this.mTabHost.getTabWidget().getChildAt(1).findViewById(16908310)).setTextColor(HomeFragmentActivity.this.getResources().getColor(R.color.transparent));
                }
                fragmentTransaction.commit();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();

        for(int i=0;i<this.mTabHost.getTabWidget().getChildCount();i++)
        {
            this.mTabHost.getTabWidget().getChildAt(this.mTabHost.getCurrentTab()).setBackgroundColor(Color.parseColor("#00000000"));
            TextView tv = (TextView) this.mTabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(Color.parseColor("#000000"));
        }
    }
    private Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), MediaChooserConstants.folderName);
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            return null;
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        if (type == 1) {
            return new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        }
        if (type == 2) {
            return new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
        }
        return null;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == -1) {
            sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", fileUri));
            final AlertDialog alertDialog = MediaChooserConstants.getDialog(this).create();
            alertDialog.show();
            this.handler.postDelayed(new Runnable() {
                public void run() {
                    String fileUriString = HomeFragmentActivity.fileUri.toString().replaceFirst("file:///", "/").trim();
                    ImageFragment imageFragment = (ImageFragment) HomeFragmentActivity.this.getSupportFragmentManager().findFragmentByTag("tab1");
                    if (imageFragment == null) {
                        new ImageFragment().addItem(fileUriString);
                    } else {
                        imageFragment.addItem(fileUriString);
                    }
                    alertDialog.cancel();
                }
            }, 5000);
        }
    }

    public void onImageSelected(int count) {
        if (this.mTabHost.getTabWidget().getChildAt(1) != null) {
            if (count != 0) {
                ((TextView) this.mTabHost.getTabWidget().getChildAt(1).findViewById(16908310)).setText(new StringBuilder(String.valueOf(getResources().getString(R.string.images_tab))).append("  ").append(count).toString());
            } else {
                ((TextView) this.mTabHost.getTabWidget().getChildAt(1).findViewById(16908310)).setText(getResources().getString(R.string.image));
            }
        } else if (count != 0) {
            ((TextView) this.mTabHost.getTabWidget().getChildAt(0).findViewById(16908310)).setText(new StringBuilder(String.valueOf(getResources().getString(R.string.images_tab))).append("  ").append(count).toString());
        } else {
            ((TextView) this.mTabHost.getTabWidget().getChildAt(0).findViewById(16908310)).setText(getResources().getString(R.string.image));
        }
    }

    @SuppressLint({"NewApi"})
    private void setHeaderBarCameraBackground(Drawable drawable) {
        int sdk = VERSION.SDK_INT;
    }

    public int convertDipToPixels(float dips) {
        return (int) ((getResources().getDisplayMetrics().density * dips) + 0.5f);
    }
}
