package com.tlvanelearning.ielts;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.tlvanelearning.ielts.common.Config;
import com.tlvanelearning.ielts.database.DatabaseIO;
import com.tlvanelearning.ielts.parent.BaseActivity;
import com.tlvanelearning.ielts.player.PlayerListener.PlayerViewServiceListener;
import com.tlvanelearning.ielts.player.PlayerService;
import com.tlvanelearning.ielts.util.FileUtil;
import com.tlvanelearning.ielts.util.MUtil;
import com.tlvanelearning.ielts.util.NetworkUtil;
import com.tlvanelearning.ielts.util.PrefUtil;
import com.tlvanelearning.ielts.util.UIUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends BaseActivity implements InterstitialAdListener {
    @BindView(R.id.adsView)
    LinearLayout adsView;
    @BindView(R.id.btn_player_download)
    ImageButton btnDown;
    @BindView(R.id.btn_player_favorite)
    ImageButton btnFavor;
    @BindView(R.id.btn_player_next)
    ImageButton btnNext;
    @BindView(R.id.btn_player_play)
    ImageButton btnPlay;
    @BindView(R.id.btn_player_prev)
    ImageButton btnPrev;
    @BindView(R.id.btn_player_repeat)
    ImageButton btnRepeat;
    @BindView(R.id.btn_player_shuffle)
    ImageButton btnShuffle;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    private int mPosition;
    private InterstitialAd interstitialAd;
    private NativeAd nativeAd;
    private LinearLayout  nativeAdContainer;
    private LinearLayout  adView;
    private void loadInterstitialAd() {
        interstitialAd = new InterstitialAd(this, "2001659010117857_2011796662437425");
        interstitialAd.setAdListener(this);
        interstitialAd.loadAd();
    }
    private OnSeekBarChangeListener onSeekBarChangeListener = new OnSeekBarChangeListener() {
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser && playerService != null) {
                playerService.seekTo(progress);
            }
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    };
    private PlayerService playerService = PlayerService.getInstance();
    PlayerViewServiceListener playerViewServiceListener = new PlayerViewServiceListener() {
        public void updateData(int position) {
            if (position != mPosition) {
                mPosition = position;
                fillData();
                if (playerService != null) {
                    playerService.play(mPosition);
                }
            }
        }

        public void onBufferingUpdate(int percent) {
            seekBar.setSecondaryProgress(percent);
        }

        public void onPreparedAudio(final int duration) {
            dismissProgressBar();
            resetPlayerInfo();
            seekBar.setMax(duration);
            txtDuration.post(new Runnable() {
                public void run() {
                    txtDuration.setText(UIUtil.toTimeAudio((long) duration));
                }
            });
        }

        public void onDuration(final int duration) {
            seekBar.setMax(duration);
            txtDuration.post(new Runnable() {
                public void run() {
                    txtDuration.setText(UIUtil.toTimeAudio((long) duration));
                }
            });
        }

        public void onCompletedAudio() {
            resetPlayerInfo();
        }

        public void onPaused() {
            UIUtil.changeBackgroundView(btnPlay, R.drawable.btn_play_selector);
            btnPlay.setTag(Integer.valueOf(R.drawable.ic_play_normal));
        }

        public void onContinueAudio() {
            dismissProgressBar();
        }

        public void onPlaying() {
            UIUtil.changeBackgroundView(btnPlay, R.drawable.btn_pause_selector);
            btnPlay.setTag(Integer.valueOf(R.drawable.ic_pause_normal));
        }

        public void onTimeChanged(final long currentPosition) {
            seekBar.post(new Runnable() {
                public void run() {
                    seekBar.setProgress((int) currentPosition);
                }
            });
            txtProgress.post(new Runnable() {
                public void run() {
                    txtProgress.setText(String.valueOf(UIUtil.toTimeAudio(currentPosition)));
                }
            });
        }

        public void onPrepared() {
            if (NetworkUtil.isOnline(context) && MUtil.isStoragePermissionGranted(DetailActivity.this) && playerService != null) {
                playerService.download(false);
            }
        }

        public void onProgressBar(final boolean show) {
            progressPlay.post(new Runnable() {
                public void run() {
                    progressPlay.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
            btnDown.post(new Runnable() {
                public void run() {
                    btnDown.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });
        }

        public void onDownloadSuccess() {
            btnDown.post(new Runnable() {
                public void run() {
                    UIUtil.changeBackgroundView(btnDown, R.drawable.ic_download_done_normal);
                    btnDown.setClickable(false);
                }
            });
        }

        public void onKill() {
            finish();
        }
    };
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.player_progress)
    ProgressBar progressPlay;
    private boolean repeatTrack;
    @BindView(R.id.player_seekbar)
    SeekBar seekBar;
    private boolean shuffle;
    private int tabPos;
    @BindView(R.id.txt_player_duration)
    TextView txtDuration;
    @BindView(R.id.txt_player_progress)
    TextView txtProgress;
    @BindView(R.id.webView)
    WebView webView;

    @Override
    public void onError(Ad ad, AdError adError) {

    }

    @Override
    public void onAdLoaded(Ad ad) {
        Random rand = new Random();
        int n = rand.nextInt(2)+1;
        Log.e("levan_new","show + n= "+ String.valueOf(n));
        if(n==1){
            interstitialAd.show();

        }

    }

    @Override
    public void onAdClicked(Ad ad) {

    }

    @Override
    public void onLoggingImpression(Ad ad) {

    }

    @Override
    public void onInterstitialDisplayed(Ad ad) {

    }

    @Override
    public void onInterstitialDismissed(Ad ad) {

    }
    private void showNativeAd(){
        Log.e("NativeADS","----HERE----");
        nativeAd = new NativeAd(this, "2001659010117857_2014121425538282");
        nativeAd.setAdListener(new AdListener() {

            @Override
            public void onError(Ad ad, AdError error) {

            }

            @Override
            public void onAdLoaded(Ad ad) {
                Log.e("NativeADSLoaded","----HERE----");
                adsView.setVisibility(View.VISIBLE);
                if (nativeAd != null) {
                    nativeAd.unregisterView();
                }

                // Add the Ad view into the ad container.
                nativeAdContainer = (LinearLayout) findViewById(R.id.adsView);
                LayoutInflater inflater = LayoutInflater.from(DetailActivity.this);
                // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
                adView = (LinearLayout) inflater.inflate(R.layout.native_ad_layout, nativeAdContainer, false);
                nativeAdContainer.addView(adView);

                // Create native UI using the ad metadata.
                ImageView nativeAdIcon = (ImageView) adView.findViewById(R.id.native_ad_icon);
                TextView nativeAdTitle = (TextView) adView.findViewById(R.id.native_ad_title);
                MediaView nativeAdMedia = (MediaView) adView.findViewById(R.id.native_ad_media);
                TextView nativeAdSocialContext = (TextView) adView.findViewById(R.id.native_ad_social_context);
                TextView nativeAdBody = (TextView) adView.findViewById(R.id.native_ad_body);
                Button nativeAdCallToAction = (Button) adView.findViewById(R.id.native_ad_call_to_action);

                // Set the Text.
                nativeAdTitle.setText(nativeAd.getAdTitle());
                nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
                nativeAdBody.setText(nativeAd.getAdBody());
                nativeAdCallToAction.setText(nativeAd.getAdCallToAction());

                // Download and display the ad icon.
                NativeAd.Image adIcon = nativeAd.getAdIcon();
                NativeAd.downloadAndDisplayImage(adIcon, nativeAdIcon);

                // Download and display the cover image.
                nativeAdMedia.setNativeAd(nativeAd);

                // Add the AdChoices icon
                LinearLayout adChoicesContainer = (LinearLayout) findViewById(R.id.ad_choices_container);
                AdChoicesView adChoicesView = new AdChoicesView(DetailActivity.this, nativeAd, true);
                adChoicesContainer.addView(adChoicesView);

                // Register the Title and CTA button to listen for clicks.
                List<View> clickableViews = new ArrayList<>();
                clickableViews.add(nativeAdTitle);
                clickableViews.add(nativeAdCallToAction);
                nativeAd.registerViewForInteraction(nativeAdContainer,clickableViews);
            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        });

        nativeAd.loadAd(NativeAd.MediaCacheFlag.ALL);
    }


    private class GetDataAsynTask extends AsyncTask<Boolean, Void, Boolean> {
        private GetDataAsynTask() {
        }

        protected Boolean doInBackground(Boolean... bool) {
            return bool[0];
        }

        protected void onPreExecute() {
            super.onPreExecute();
            fab.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }

        protected void onPostExecute(Boolean hasExtra) {
            super.onPostExecute(hasExtra);
            try {
                if (playerService != null) {
                    playerService.registerServicePlayerListener(playerViewServiceListener);
                }
                if (hasExtra.booleanValue()) {
                    playObject();
                } else {
                    fillData();
                }
                if (tabPos != 4) {
                    fab.setVisibility(View.VISIBLE);
                    btnFavor.setVisibility(View.GONE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_detail);
        ButterKnife.bind((Activity) this);
        this.seekBar.setOnSeekBarChangeListener(this.onSeekBarChangeListener);
        //this.webView.setScrollViewCallbacks(this);
        if (!PrefUtil.getBooleanValue(this, Config.ITEM_SKU) && NetworkUtil.isOnline(context)){
            loadInterstitialAd();
            showNativeAd();
        }

        this.webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }
        });
        this.fab.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                try {
                    if (playerService != null) {
                        showFacebookInterstitialAd();
                        Intent intent = new Intent(context, TestActivity.class);
                        intent.putExtra("question", ((JSONObject) playerService.getPlaylist().get(mPosition)).getString("question"));
                        startActivity(intent);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        this.repeatTrack = PrefUtil.getRepeatValue(this, "REPEATE");
        UIUtil.changeBackgroundView(this.btnRepeat, this.repeatTrack ? R.drawable.btn_repeat_one_selector : R.drawable.btn_repeat_all_selector);
        this.shuffle = PrefUtil.getBooleanValue(this, "PREFS_SHUFFLE");
        UIUtil.changeBackgroundView(this.btnShuffle, this.shuffle ? R.drawable.btn_shuffle_selector : R.drawable.btn_shuffle_off_selector);
    }

    protected void onPostCreate(Bundle savedInstanceState) {
        boolean z = false;
        super.onPostCreate(savedInstanceState);
        try {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                this.mPosition = extras.getInt("EXTRAS_POS");
                this.tabPos = extras.getInt("EXTRAS_TAB");
            } else {
                this.mPosition = this.playerService.getTrackPosition();
                this.tabPos = PrefUtil.getIntValue(this, "EXTRAS_TAB");
            }
            GetDataAsynTask getDataAsynTask = new GetDataAsynTask();
            Boolean[] boolArr = new Boolean[1];
            if (extras != null) {
                z = true;
            }
            boolArr[0] = Boolean.valueOf(z);
            getDataAsynTask.execute(boolArr);
            //admobBanner(this, this.adsView);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void onPause() {
        super.onPause();
        if (this.playerService != null) {
            this.playerService.createNotificationPlayer();
        }
    }

    private void playObject() {
        fillData();
        if (this.playerService != null) {
            this.playerService.play(this.mPosition);
        }
    }

    private void fillData() {
        try {
            if (this.playerService != null && this.playerService.getPlaylist().size() != 0) {
                JSONObject jsonObject = (JSONObject) this.playerService.getPlaylist().get(this.mPosition);
                if (jsonObject != null) {
                    setTitle(jsonObject.getString("title"));
                    this.webView.loadDataWithBaseURL(null, processHtmlWithObject(DatabaseIO.database(this.context).getContentWithId(jsonObject.getInt("id"), 0 - this.tabPos)), "text/html", "UTF-8", null);
                    if (this.tabPos == 4) {
                        UIUtil.changeBackgroundView(this.btnFavor, jsonObject.getString("favorite").equals("1") ? R.drawable.btn_favorited_selector : R.drawable.btn_favorite_selector);
                    }
                    if (FileUtil.fileExists(this, jsonObject.getString("audio"))) {
                        UIUtil.changeBackgroundView(this.btnDown, R.drawable.ic_download_done_normal);
                    } else {
                        UIUtil.changeBackgroundView(this.btnDown, R.drawable.btn_download_selector);
                    }
                }
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
            stringBuilder.append("<p style=\"color:#3b5998;\"><b>" + _content + "</b></p>");
            stringBuilder.append("</body>");
            stringBuilder.append("</html>");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public void btnFavoriteOnClick(View view) {
        try {
            if (this.tabPos == 4 && this.playerService != null) {
                JSONObject jsonObject = (JSONObject) this.playerService.getPlaylist().get(this.mPosition);
                if (jsonObject != null) {
                    boolean favorite = jsonObject.getString("favorite").equals("1");
                    if (DatabaseIO.database(this.context).updateFavorite(jsonObject.getInt("id"), favorite, 0 - this.tabPos) != -1) {
                        ((JSONObject) this.playerService.getPlaylist().get(this.mPosition)).put("favorite", favorite ? "0" : "1");
                        UIUtil.changeBackgroundView(this.btnFavor, favorite ? R.drawable.btn_favorite_selector : R.drawable.btn_favorited_selector);
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    public void btnShuffleOnClick(View view) {
        try {
            this.shuffle = !this.shuffle;
            PrefUtil.saveBoolean(this, "PREFS_SHUFFLE", this.shuffle);
            UIUtil.changeBackgroundView(this.btnShuffle, this.shuffle ? R.drawable.btn_shuffle_selector : R.drawable.btn_shuffle_off_selector);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void btnPrevOnClick(View view) {
        if (this.playerService != null) {
            resetPlayerInfo();
            showProgressBar();
            try {
                this.playerService.previousAudio();
            } catch (Exception e) {
                dismissProgressBar();
                e.printStackTrace();
            }
        }
    }

    public void btnNextOnClick(View view) {
        if (this.playerService != null) {
            resetPlayerInfo();
            showProgressBar();
            try {
                this.playerService.nextAudio();
            } catch (Exception e) {
                dismissProgressBar();
                e.printStackTrace();
            }
        }
    }

    public void btnPlayOnClick(View view) {
        try {
            if (this.playerService != null) {
                if (this.playerService.isPlaying()) {
                    this.playerService.pauseAudio();
                } else {
                    this.playerService.play(this.mPosition);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void btnRepeatOnClick(View view) {
        try {
            this.repeatTrack = !this.repeatTrack;
            PrefUtil.saveBoolean(this.context, "REPEATE", this.repeatTrack);
            UIUtil.changeBackgroundView(this.btnRepeat, this.repeatTrack ? R.drawable.btn_repeat_one_selector : R.drawable.btn_repeat_all_selector);
        } catch (Exception e) {
        }
    }

    public void btnDownloadOnClick(View view) {
        if (!NetworkUtil.isOnline(this.context)) {
            NetworkUtil.toastMessage(this.context);
        } else if (MUtil.isStoragePermissionGranted(this) && this.playerService != null) {
            this.playerService.download(false);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults != null && grantResults.length != 0 && grantResults[0] == 0 && this.playerService != null) {
            this.playerService.download(false);
        }
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

    protected void onDestroy() {
        super.onDestroy();
        try {
            PrefUtil.saveInt(this.context, "EXTRAS_TAB", this.tabPos);
            if (this.playerService != null) {
                this.playerService.unRegisterServicePlayerListener();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void finish() {
        setResult(-1, getIntent());
        super.finish();
    }

    private void showProgressBar() {
        this.progressPlay.post(new Runnable() {
            public void run() {
                progressPlay.setVisibility(View.VISIBLE);
            }
        });
        this.btnDown.post(new Runnable() {
            public void run() {
                btnDown.setVisibility(View.GONE);
            }
        });
        this.btnPlay.setClickable(false);
        this.btnDown.setClickable(false);
        this.btnNext.setClickable(false);
        this.btnPrev.setClickable(false);
    }

    private void dismissProgressBar() {
        this.progressPlay.post(new Runnable() {
            public void run() {
                progressPlay.setVisibility(View.GONE);
            }
        });
        this.btnDown.post(new Runnable() {
            public void run() {
                btnDown.setVisibility(View.VISIBLE);
            }
        });
        this.btnPlay.setClickable(true);
        this.btnDown.setClickable(true);
        this.btnNext.setClickable(true);
        this.btnPrev.setClickable(true);
    }

    private void resetPlayerInfo() {
        this.seekBar.setProgress(0);
        this.txtProgress.setText(getString(R.string.zero_progress));
        this.txtDuration.setText(getString(R.string.zero_progress));
    }
}
