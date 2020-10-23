package com.photo.sketch.gallery;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.AppConstant;
import com.hbtools.photo.sketchpencil.editer.pencilsketch.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class BucketHomeFragmentActivity extends FragmentActivity {
    private static Uri fileUri;
    OnClickListener clickListener = new OnClickListener() {
        public void onClick(View view) {
            if (view == BucketHomeFragmentActivity.this.headerBarBack) {
                BucketHomeFragmentActivity.this.finish();
            }
        }
    };
    private final Handler handler = new Handler();
    private ImageView headerBarBack;
    private ArrayList<String> mSelectedImage = new ArrayList();
    private FragmentTabHost mTabHost;
    private RelativeLayout adsBaner;
    public /* bridge */ /* synthetic */ View onCreateView(View view, String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(view, str, context, attributeSet);
    }

    public /* bridge */ /* synthetic */ View onCreateView(String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(str, context, attributeSet);
    }

    @SuppressLint({"ResourceAsColor"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(R.layout.activity_home_media_chooser);
        adsBaner = (RelativeLayout)findViewById(R.id.banner_photoshare_ads);
        AppConstant appConstant = new AppConstant(getApplicationContext());
        appConstant.adBanner(adsBaner);
        this.headerBarBack = (ImageView) findViewById(R.id.backArrowImageViewFromMediaChooserHeaderView);
        this.mTabHost = (FragmentTabHost) findViewById(16908306);
        this.headerBarBack.setOnClickListener(this.clickListener);
        this.mTabHost.setup(this, getSupportFragmentManager(), R.id.realTabcontent);
        if (MediaChooserConstants.showImage) {
            this.mTabHost.addTab(this.mTabHost.newTabSpec("tab1").setIndicator("GALLERY"), BucketImageFragment.class, null);
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
        ((TextView) this.mTabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title)).setTextColor(-1);
        this.mTabHost.setOnTabChangedListener(new OnTabChangeListener() {
            public void onTabChanged(String tabId) {
                FragmentManager fragmentManager = BucketHomeFragmentActivity.this.getSupportFragmentManager();
                BucketImageFragment imageFragment = (BucketImageFragment) fragmentManager.findFragmentByTag("tab1");
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if (tabId.equalsIgnoreCase("tab1")) {
                    if (imageFragment == null) {
                        fragmentTransaction.add(R.id.realTabcontent, new BucketImageFragment(), "tab1");
                    } else {
                        fragmentTransaction.show(imageFragment);
                    }
                    ((TextView) BucketHomeFragmentActivity.this.mTabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title)).setTextColor(BucketHomeFragmentActivity.this.getResources().getColor(R.color.transparent));
                } else {
                    ((TextView) BucketHomeFragmentActivity.this.mTabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title)).setTextColor(-1);
                    ((TextView) BucketHomeFragmentActivity.this.mTabHost.getTabWidget().getChildAt(1).findViewById(android.R.id.title)).setTextColor(BucketHomeFragmentActivity.this.getResources().getColor(R.color.transparent));
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
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != -1) {
            return;
        }
        if (requestCode == 1000) {
            addMedia(this.mSelectedImage, data.getStringArrayListExtra("list"));
        } else if (requestCode == 100) {
            sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", fileUri));
            final AlertDialog alertDialog = MediaChooserConstants.getDialog(this).create();
            alertDialog.show();
            this.handler.postDelayed(new Runnable() {
                public void run() {
                    String fileUriString = BucketHomeFragmentActivity.fileUri.toString().replaceFirst("file:///", "/").trim();
                    BucketImageFragment bucketImageFragment = (BucketImageFragment) BucketHomeFragmentActivity.this.getSupportFragmentManager().findFragmentByTag("tab1");
                    if (bucketImageFragment != null) {
                        bucketImageFragment.getAdapter().addLatestEntry(fileUriString);
                        bucketImageFragment.getAdapter().notifyDataSetChanged();
                    }
                    alertDialog.dismiss();
                }
            }, 5000);
        }
    }

    private void addMedia(ArrayList<String> list, ArrayList<String> input) {
        Iterator it = input.iterator();
        while (it.hasNext()) {
            list.add((String) it.next());
        }
    }

    public int convertDipToPixels(float dips) {
        return (int) ((getResources().getDisplayMetrics().density * dips) + 0.5f);
    }
}
