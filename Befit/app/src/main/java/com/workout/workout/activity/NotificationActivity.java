package com.workout.workout.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.crashlytics.android.answers.ShareEvent;
import com.google.android.gms.ads.AdListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.workout.workout.R;
import com.workout.workout.constant.AppConstants;
import com.workout.workout.database.DatabaseManager;
import java.io.File;
import java.io.FileOutputStream;

public class NotificationActivity extends BaseActivity {
    String body;
    String date;
    TextView descriptionTextView;
    String image;
    ImageView imageView;
    private boolean isComingFromNewsListActivity;
    String link;
    TextView linkTextView;
    private ProgressBar progressBar;
    TextView progymwebsite;
    String title;
    TextView titleTextView;

    private class NotificationNewsListAsyncTask extends AsyncTask<Void, Void, Void> {
        private NotificationNewsListAsyncTask() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... voids) {
            if (NotificationActivity.this.link == null || NotificationActivity.this.link.isEmpty()) {
                NotificationActivity.this.getThisNotificationSource(NotificationActivity.this.date);
            }
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (NotificationActivity.this.link == null || NotificationActivity.this.link.isEmpty()) {
                NotificationActivity.this.linkTextView.setVisibility(View.GONE);
                return;
            }
            NotificationActivity.this.linkTextView.setMovementMethod(LinkMovementMethod.getInstance());
            NotificationActivity.this.linkTextView.setText(Html.fromHtml(NotificationActivity.this.link));
        }
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_notification);
        if (getIntent() == null) {
            finish();
        }
        if (!isFinishing()) {
            Bundle b = getIntent().getExtras();
            if (b != null) {
                this.title = (String) b.get("title");
                this.body = (String) b.get(AppConstants.NOTIFICATION_BODY);
                this.image = (String) b.get(AppConstants.NOTIFICATION_IMAGE);
                this.link = (String) b.get("link");
                this.date = (String) b.get(AppConstants.NOTIFICATION_DATE);
                this.isComingFromNewsListActivity = b.getBoolean("isComingFromNewsListActivity");
                try {
                    Answers.getInstance().logContentView((ContentViewEvent) new ContentViewEvent().putContentName("Notification View").putCustomAttribute("Notification Title", this.title));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    DatabaseManager.getInstance(this).changeNewsReadStatus(this.date);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                if (FirebaseRemoteConfig.getInstance().getBoolean("notification_activity_banner_ads_enable")) {
                    loadBannerAdvertisement(this, AppConstants.ADMOB_NOTIFICATION_BANNER_AD_ID);
                }
                loadInterstitialAds(AppConstants.ADMOB_NOTIFICATION_INTERSTITIAL_AD_ID);
                initializeView();
                markThisNotificationRead();
            }
        }
    }

    private void markThisNotificationRead() {
    }

    private void initializeView() {
        setToolbar(this.title, true);
        initializeButtonToToolbar();
        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
        this.progressBar.setVisibility(View.VISIBLE);
        this.descriptionTextView = (TextView) findViewById(R.id.notificationText);
        this.progymwebsite = (TextView) findViewById(R.id.progymwebsite);
        this.linkTextView = (TextView) findViewById(R.id.linkTextView);
        this.titleTextView = (TextView) findViewById(R.id.notificationTitle);
        this.imageView = (ImageView) findViewById(R.id.notificationImage);
        if (!(this.link == null || this.link.isEmpty())) {
            this.linkTextView.setText(Html.fromHtml(this.link));
            this.linkTextView.setMovementMethod(LinkMovementMethod.getInstance());
        }
        new NotificationNewsListAsyncTask().execute(new Void[0]);
        this.progymwebsite.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                try {
                    NotificationActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://progymworkout.com")));
                } catch (Exception e) {
                    Toast.makeText(NotificationActivity.this, "Unknown Error Occured", Toast.LENGTH_SHORT).show();
                }
            }
        });
        this.body = this.body.replace("\n", "<br>");
        this.descriptionTextView.setText(Html.fromHtml(this.body));
        this.descriptionTextView.setMovementMethod(LinkMovementMethod.getInstance());
        this.titleTextView.setText(this.title);
        Glide.with((FragmentActivity) this).load(this.image).into(this.imageView);
        Glide.with((FragmentActivity) this).load(this.image).fitCenter().diskCacheStrategy(DiskCacheStrategy.ALL).listener(new RequestListener<String, GlideDrawable>() {
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                NotificationActivity.this.progressBar.setVisibility(View.GONE);
                return false;
            }

            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                NotificationActivity.this.progressBar.setVisibility(View.GONE);
                return false;
            }
        }).into(this.imageView);
    }

    private void initializeButtonToToolbar() {
        ((Button) findViewById(R.id.notificationsharebutton)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                try {
                    NotificationActivity.this.getWindow().getDecorView().findViewById(R.id.scrollviewnotification).setDrawingCacheEnabled(true);
                    ScrollView iv = (ScrollView) NotificationActivity.this.findViewById(R.id.scrollviewnotification);
                    Bitmap bitmap = Bitmap.createBitmap(iv.getChildAt(0).getWidth(), iv.getChildAt(0).getHeight(), Config.ARGB_8888);
                    iv.getChildAt(0).draw(new Canvas(bitmap));
                    NotificationActivity.this.share(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void share(Bitmap bitmap) {
        File dir = new File(getCacheDir(), "images");
        dir.mkdirs();
        File file = new File(dir, "share.png");
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", file);
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        intent.addFlags(1);
        intent.setType("image/*");
        intent.setDataAndType(uri, getContentResolver().getType(uri));
        intent.putExtra("android.intent.extra.SUBJECT", this.title);
        intent.putExtra("android.intent.extra.TEXT", this.title);
        intent.putExtra("android.intent.extra.STREAM", uri);
        try {
            Answers.getInstance().logShare((ShareEvent) new ShareEvent().putMethod("Notification share").putCustomAttribute("Title", this.title));
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        try {
            startActivity(Intent.createChooser(intent, "Share Image"));
        } catch (ActivityNotFoundException e3) {
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onBackPressed() {
        if (this.interstitialAd == null || !this.interstitialAd.isLoaded() || this.isComingFromNewsListActivity) {
            handleNotificationBack();
        } else {
            showInterstitialAds(AppConstants.ADMOB_NOTIFICATION_INTERSTITIAL_AD_ID);
            this.interstitialAd.setAdListener(new AdListener() {
                public void onAdClosed() {
                    super.onAdClosed();
                    NotificationActivity.this.loadInterstitialAds(AppConstants.ADMOB_NOTIFICATION_INTERSTITIAL_AD_ID);
                    NotificationActivity.this.handleNotificationBack();
                }
            });
        }
        super.onBackPressed();
    }

    private void handleNotificationBack() {
        startActivity(new Intent(this, NewsListActivity.class));
        finish();
    }

    private void getThisNotificationSource(String date) {
        this.link = DatabaseManager.getInstance(this).getNotificationSource(date);
    }
}
