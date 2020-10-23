package hb.giphy.allgif.giffree.activity;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.ButtonCallback;
import com.afollestad.materialdialogs.MaterialDialog.ListCallback;
import com.afollestad.materialdialogs.folderselector.FolderChooserDialog;
import com.afollestad.materialdialogs.folderselector.FolderChooserDialog.FolderCallback;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListAdapter;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.facebook.CallbackManager;
import com.facebook.CallbackManager.Factory;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.messenger.MessengerUtils;
import com.facebook.messenger.ShareToMessengerParams;
import com.facebook.share.Sharer.Result;
import com.facebook.share.internal.ShareConstants;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.koushikdutta.ion.builder.Builders.Any.B;
import com.rey.material.widget.FloatingActionButton;
import com.rey.material.widget.ProgressView;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import hb.giphy.allgif.giffree.Admob;
import hb.giphy.allgif.giffree.R;
import hb.giphy.allgif.giffree.model.FavoriteItemRealm;
import hb.giphy.allgif.giffree.util.AppConstant;
import hb.giphy.allgif.giffree.util.Event;
import hb.giphy.allgif.giffree.util.LogUtil;
import io.realm.Realm;
import io.realm.RealmResults;

public class DetailGifActivity extends AppCompatActivity implements FolderCallback {
    private String mAppPackage;
    private CallbackManager mCallbackManager;
    private String mExtension;
    private String mFolderPath;
    private MaterialDialog mMaterialDialog;
    private ShareDialog mShareDialog;
    private int mType;

    private String mGifUrl;
    private String title;
    private String gifPreview;
    private String mpfourLink;
    private boolean isFavorite = false;
    private NativeAd nativeAd;
    private LinearLayout nativeAdContainer;
    private LinearLayout  adView;
    LinearLayout adsView;
    private InterstitialAd interstitialAd;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_gif);
        adsView =(LinearLayout)findViewById(R.id.native_ad_admob_1);
        showNativeAd();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        View messenger_send = findViewById(R.id.messenger_button_send);
        ImageView image_gif = (ImageView) findViewById(R.id.image_gif);
        final ProgressView progressView = (ProgressView) findViewById(R.id.share_progress_view);
        FloatingActionButton facebook_flat = (FloatingActionButton) findViewById(R.id.facebook_float);
        FloatingActionButton whatsapp_flat = (FloatingActionButton) findViewById(R.id.whatsapp_float);
        FloatingActionButton snapchat_flat = (FloatingActionButton) findViewById(R.id.snapchat_float);
        FloatingActionButton download_flat = (FloatingActionButton) findViewById(R.id.download_float);
        FloatingActionButton share_flat = (FloatingActionButton) findViewById(R.id.share_float);
        View title_divider = findViewById(R.id.title_dividr);
        TextView gif_title = (TextView) findViewById(R.id.gif_title);
        CardView tags_cardview = (CardView) findViewById(R.id.tags_card_view);
        TextView tags_textview = (TextView) findViewById(R.id.tags_textview);
        final CardView powered_cardview = (CardView) findViewById(R.id.powered_card_view);
        final ImageView powered_imageview = (ImageView) findViewById(R.id.powered_imageview);
        info_notify();
        this.mGifUrl = getIntent().getStringExtra("gif_link");
        this.title = getIntent().getStringExtra("title");
        this.gifPreview = getIntent().getStringExtra("gif_preview");
        this.mpfourLink = getIntent().getStringExtra("gif_mp4");
        if (getIntent().hasExtra("gif_id")) {
            isFavorite = true;
        }

        LogUtil.d("Gif url in detail activity " + mGifUrl);
        if (TextUtils.isEmpty(mGifUrl)) {
            this.mGifUrl = "not thing";
            progressView.stop();
        }
        LogUtil.d("GIF URL : " + mGifUrl);
        if (this.mGifUrl.substring(this.mGifUrl.length() - 3, this.mGifUrl.length()).equals("gif")
                || this.mGifUrl.substring(this.mGifUrl.length() - 3, this.mGifUrl.length()).equals("raw")) {
            Glide.with(this).load(mGifUrl).asGif().placeholder(R.drawable.loading_2).dontAnimate().error(R.drawable.error_2)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE).listener(new RequestListener<String, GifDrawable>() {
                public boolean onException(Exception e, String model, Target<GifDrawable> target, boolean isFirstResource) {
                    LogUtil.d("Exception occurred: " + e.getMessage());
                    SnackBarNetwork();
                    progressView.stop();
                    progressView.setVisibility(View.GONE);
                    return false;
                }

                public boolean onResourceReady(GifDrawable resource, String model, Target<GifDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    LogUtil.d("Load GIF success");
                    progressView.stop();
                    progressView.setVisibility(View.GONE);
                    if (!mGifUrl.contains("tumblr")) {
                        powered_cardview.setVisibility(View.VISIBLE);
                        powered_imageview.setImageResource(R.drawable.riffsy);
                    }
                    return false;
                }
            }).into(image_gif);

            this.mExtension = ".gif";
        } else {
            Glide.with(this).load(this.mGifUrl).asBitmap().error(R.drawable.error_2).diskCacheStrategy(DiskCacheStrategy.SOURCE).listener(new RequestListener<String, Bitmap>() {
                public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                    LogUtil.d("Exception occurred: " + e.getMessage());
                    e.printStackTrace();
                    SnackBarNetwork();
                    progressView.stop();
                    progressView.setVisibility(View.GONE);
                    return false;
                }

                public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    LogUtil.d("Load GIF success");
                    progressView.stop();
                    progressView.setVisibility(View.GONE);
                    return false;
                }
            }).into(image_gif);
            this.mExtension = ".jpg";
        }
        String title = getIntent().getStringExtra(ShareConstants.WEB_DIALOG_PARAM_TITLE);
        if (title != null) {
            Log.d("LAMPARD", "mTag is null");
            if (!title.isEmpty()) {
                gif_title.setVisibility(View.VISIBLE);
                gif_title.setText(title);
                title_divider.setVisibility(View.VISIBLE);
            }
            final String tags = getIntent().getStringExtra("tags");
            if (!(tags == null || tags.isEmpty())) {
                tags_cardview.setVisibility(View.VISIBLE);
                if (tags.length() > 31) {
                    tags_textview.setText(tags.substring(0, 30) + "...");
                } else {
                    tags_textview.setText(tags);
                }
                tags_textview.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        new MaterialDialog.Builder(DetailGifActivity.this).title("Hashtags").items(tags.split(" ")).contentColorRes(R.color.primary).itemsCallback(new ListCallback() {
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                Intent i = new Intent(DetailGifActivity.this, SearchViewActivity.class);
                                i.putExtra("tag", text);
                                startActivity(i);
                            }
                        }).show();
                    }
                });
            }
            this.mCallbackManager = Factory.create();
            this.mShareDialog = new ShareDialog(this);
            this.mShareDialog.registerCallback(this.mCallbackManager, new FacebookCallback<Result>() {
                public void onSuccess(Result result) {
                }

                public void onCancel() {
                }

                public void onError(FacebookException e) {
                }
            });
            facebook_flat.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    if (mExtension.equals(".jpg")) {
                        final File k = new File(Environment.getExternalStorageDirectory().getPath() + "/GIFs.jpg");
                        showPrepareProgressDialog();
                        (Ion.with(getBaseContext()).load(mGifUrl)).write(k).setCallback(new FutureCallback<File>() {
                            public void onCompleted(Exception e, File result) {
                                dismissProgressDialog();
                                if (e != null) {
                                    SnackBarFail();
                                } else if (ShareDialog.canShow(ShareLinkContent.class)) {
                                    mShareDialog.show(new SharePhotoContent.Builder().addPhoto(new SharePhoto.Builder().setBitmap(BitmapFactory.decodeFile(k.getPath())).build()).build());
                                }
                            }
                        });
                    } else if (ShareDialog.canShow(ShareLinkContent.class)) {
                        if (getIntent().hasExtra("facebook_link")) {
                            mShareDialog.show((new ShareLinkContent.Builder().setContentUrl(Uri.parse(getIntent().getStringExtra("facebook_link")))).setContentTitle(getString(R.string.facebook_disc)).setContentDescription(getString(R.string.facebook_disc)).build());
                            Toast.makeText(getBaseContext(), getString(R.string.facebook_gif_remember), Toast.LENGTH_LONG).show();
                        } else {
                            mShareDialog.show((new ShareLinkContent.Builder().setContentUrl(Uri.parse(mGifUrl))).setContentTitle(getString(R.string.facebook_disc)).setContentDescription(getString(R.string.facebook_disc)).build());
                            Toast.makeText(getBaseContext(), getString(R.string.facebook_gif_remember), Toast.LENGTH_LONG).show();

                        }
                    }
                }
            });
            messenger_send.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    boolean isConnected;
                    NetworkInfo activeNetwork = ((ConnectivityManager) getBaseContext().getSystemService(CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
                    if (activeNetwork == null || !activeNetwork.isConnectedOrConnecting()) {
                        isConnected = false;
                    } else {
                        isConnected = true;
                    }
                    if (!isConnected) {
                        SnackBarNetwork();
                    } else if (DetailGifActivity.CheckAndroidVersion()) {
                        if (checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1988);
                        }
                        if (checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED) {
                            File f = new File(Environment.getExternalStorageDirectory().getPath() + "/GIFs.gif");
                            showPrepareProgressDialog();
                            ((B) Ion.with(getBaseContext()).load(mGifUrl)).write(f).setCallback(new FutureCallback<File>() {
                                public void onCompleted(Exception e, File result) {
                                    dismissProgressDialog();
                                    if (e == null) {
                                        MessengerUtils.shareToMessenger(DetailGifActivity.this, 1, ShareToMessengerParams.newBuilder(FileProvider.getUriForFile(getBaseContext(), getApplicationContext().getPackageName() + ".provider", result), "image/gif").setMetaData("{ \"image\" : \"tree\" }").build());
                                        return;
                                    }
                                    SnackBarFail();
                                }
                            });
                        }
                    } else {
                        File f = new File(Environment.getExternalStorageDirectory().getPath() + "/GIFs.gif");
                        showPrepareProgressDialog();
                        ((B) Ion.with(getBaseContext()).load(mGifUrl)).write(f).setCallback(new FutureCallback<File>() {
                            public void onCompleted(Exception e, File result) {
                                dismissProgressDialog();
                                if (e == null) {
                                    MessengerUtils.shareToMessenger(DetailGifActivity.this, 1, ShareToMessengerParams.newBuilder(FileProvider.getUriForFile(getBaseContext(), getApplicationContext().getPackageName() + ".provider", result), "image/gif").setMetaData("{ \"image\" : \"tree\" }").build());
                                    return;
                                }
                                SnackBarFail();
                            }
                        });
                    }
                }
            });
            download_flat.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    boolean isConnected;
                    NetworkInfo activeNetwork = ((ConnectivityManager) getBaseContext().getSystemService(CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
                    if (activeNetwork == null || !activeNetwork.isConnectedOrConnecting()) {
                        isConnected = false;
                    } else {
                        isConnected = true;
                    }
                    if (!isConnected) {
                        SnackBarNetwork();
                    } else if (DetailGifActivity.CheckAndroidVersion()) {
                        MarshmallowPermissionRequest("nothing", "download");
                    } else {
                        SharedPreferences sharedPreferences = getSharedPreferences("settings_data", 0);
                        mFolderPath = sharedPreferences.getString("folder_chooser", Environment.getExternalStorageDirectory().getPath());
                        if (mFolderPath.equals(Environment.getExternalStorageDirectory().getPath())) {
                            new FolderChooserDialog.Builder(DetailGifActivity.this).chooseButton(R.string.folder_choose).cancelButton(R.string.folder_cancel).initialPath(mFolderPath).show();
                        } else {
                            downloadGIF();
                        }
                    }
                }
            });
            share_flat.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    MaterialSimpleListAdapter adapter = new MaterialSimpleListAdapter(new MaterialSimpleListAdapter.Callback() {
                        @Override
                        public void onMaterialListItemSelected(MaterialDialog dialog, int index, MaterialSimpleListItem item) {
                            int which = index;
                            if (which == 1 || which == 6 || which == 7) {
                                mType = 1;
                                if (which == 1) {
                                    dialog.dismiss();
                                    if (DetailGifActivity.CheckAndroidVersion()) {
                                        mAppPackage = "com.instagram.android";
                                        MarshmallowPermissionRequest("com.instagram.android", Event.SHARE);
                                        // requestPermission("com.instagram.android", Event.SHARE);
                                        return;
                                    }
                                    ShareasVideo("com.instagram.android");
                                    return;
                                } else if (which == 6) {
                                    dialog.dismiss();
                                    if (DetailGifActivity.CheckAndroidVersion()) {
                                        mAppPackage = "com.sgiggle.production";
                                        MarshmallowPermissionRequest("com.sgiggle.production", Event.SHARE);
                                        return;
                                    }
                                    ShareasVideo("com.sgiggle.production");
                                    return;
                                } else {
                                    dialog.dismiss();
                                    if (DetailGifActivity.CheckAndroidVersion()) {
                                        mAppPackage = "com.bbm";
                                        MarshmallowPermissionRequest("com.bbm", Event.SHARE);
                                        return;
                                    }
                                    ShareasVideo("com.bbm");
                                    return;
                                }
                            }
                            mType = 2;
                            if (which == 2) {
                                dialog.dismiss();
                                if (DetailGifActivity.CheckAndroidVersion()) {
                                    mAppPackage = "com.twitter.android";
                                    MarshmallowPermissionRequest("com.twitter.android", Event.SHARE);
                                    return;
                                }
                                ShareasGIF("com.twitter.android");
                            } else if (which == 3) {
                                dialog.dismiss();
                                if (DetailGifActivity.CheckAndroidVersion()) {
                                    mAppPackage = "org.telegram.messenger";
                                    MarshmallowPermissionRequest("org.telegram.messenger", Event.SHARE);
                                    return;
                                }
                                ShareasVideo("org.telegram.messenger");
                            } else if (which == 4) {
                                dialog.dismiss();
                                if (DetailGifActivity.CheckAndroidVersion()) {
                                    mAppPackage = "com.google.android.apps.plus";
                                    MarshmallowPermissionRequest("com.google.android.apps.plus", Event.SHARE);
                                    return;
                                }
                                ShareasGIF("com.google.android.apps.plus");
                            } else if (which == 5) {
                                dialog.dismiss();
                                if (DetailGifActivity.CheckAndroidVersion()) {
                                    mAppPackage = "com.viber.voip";
                                    MarshmallowPermissionRequest("com.viber.voip", Event.SHARE);
                                    return;
                                }
                                ShareasGIF("com.viber.voip");
                            } else if (which == 8) {
                                dialog.dismiss();
                                if (DetailGifActivity.CheckAndroidVersion()) {
                                    mAppPackage = "com.google.android.talk";
                                    MarshmallowPermissionRequest("com.google.android.talk", Event.SHARE);
                                    return;
                                }
                                ShareasGIF("com.google.android.talk");
                            } else if (which == 9) {
                                dialog.dismiss();
                                if (DetailGifActivity.CheckAndroidVersion()) {
                                    mAppPackage = "com.google.android.apps.messaging";
                                    MarshmallowPermissionRequest("com.google.android.apps.messaging", Event.SHARE);
                                    return;
                                }
                                ShareasGIF("com.google.android.apps.messaging");
                            } else if (which == 0) {
                                dialog.dismiss();
                                copy_url();
                            } else if (which == 10) {
                                dialog.dismiss();
                                ShareOther();
                            }
//                            }
                        }
                    });
                    adapter.add(new MaterialSimpleListItem.Builder(DetailGifActivity.this).content(R.string.copy_link).icon(R.drawable.copy_btnarrow).backgroundColor(-1).build());
                    adapter.add(new MaterialSimpleListItem.Builder(DetailGifActivity.this).content(R.string.instagram).icon(R.drawable.instagram).build());
                    adapter.add(new MaterialSimpleListItem.Builder(DetailGifActivity.this).content(R.string.twitter).icon(R.drawable.twitter).backgroundColor(-1).build());
                    adapter.add(new MaterialSimpleListItem.Builder(DetailGifActivity.this).content(R.string.Telegram).icon(R.drawable.telegram).build());
                    adapter.add(new MaterialSimpleListItem.Builder(DetailGifActivity.this).content(R.string.google_plus).icon(R.drawable.google_plus).build());
                    adapter.add(new MaterialSimpleListItem.Builder(DetailGifActivity.this).content(R.string.viber).icon(R.drawable.viber).backgroundColor(-1).build());
                    adapter.add(new MaterialSimpleListItem.Builder(DetailGifActivity.this).content(R.string.tango).icon(R.drawable.tango).build());
                    adapter.add(new MaterialSimpleListItem.Builder(DetailGifActivity.this).content(R.string.bbm).icon(R.drawable.bbm).backgroundColor(-1).build());
                    adapter.add(new MaterialSimpleListItem.Builder(DetailGifActivity.this).content(R.string.hangouts).icon(R.drawable.hangouts).backgroundColor(-1).build());
                    adapter.add(new MaterialSimpleListItem.Builder(DetailGifActivity.this).content(R.string.google_messenger).icon(R.drawable.google_messenger).backgroundColor(-1).build());
                    adapter.add(new MaterialSimpleListItem.Builder(DetailGifActivity.this).content(R.string.share_other).icon(R.drawable.share_icon).backgroundColor(-1).build());
                    new MaterialDialog.Builder(DetailGifActivity.this).title(R.string.share_by).contentColor(ViewCompat.MEASURED_STATE_MASK).adapter(adapter, new LinearLayoutManager(DetailGifActivity.this)).show();
                }
            });

            whatsapp_flat.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    if (DetailGifActivity.CheckAndroidVersion()) {
                        mType = 2;
                        mAppPackage = "com.whatsapp";
                        MarshmallowPermissionRequest("com.whatsapp", Event.SHARE);
                        return;
                    }
                    ShareasGIF("com.whatsapp");
                }
            });
            snapchat_flat.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    if (DetailGifActivity.CheckAndroidVersion()) {
                        mType = 2;
                        mAppPackage = "com.snapchat.android";
                        MarshmallowPermissionRequest("com.snapchat.android", Event.SHARE);
                        return;
                    }
                    ShareasGIF("com.snapchat.android");
                }
            });
        }
        initAdmob();
//        if(AppUtils.isShowAdnative()){
//            admob.showNativeAd((LinearLayout) findViewById(R.id.native_ad_admob_1));
//        }
    }

    private void showNativeAd(){
        if(!AppConstant.isRemoveAds(getApplicationContext())) {
            Log.e("NativeADS", "----HERE----");
            nativeAd = new NativeAd(this, "169523040462460_169526760462088");
            nativeAd.setAdListener(new AdListener() {

                @Override
                public void onError(Ad ad, AdError error) {

                }

                @Override
                public void onAdLoaded(Ad ad) {
                    Log.e("NativeADSLoaded", "----HERE----");
                    adsView.setVisibility(View.VISIBLE);
                    if (nativeAd != null) {
                        nativeAd.unregisterView();
                    }

                    // Add the Ad view into the ad container.
                    nativeAdContainer = (LinearLayout) findViewById(R.id.native_ad_admob_1);
                    LayoutInflater inflater = LayoutInflater.from(DetailGifActivity.this);
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
                    AdChoicesView adChoicesView = new AdChoicesView(DetailGifActivity.this, nativeAd, true);
                    adChoicesContainer.addView(adChoicesView);

                    // Register the Title and CTA button to listen for clicks.
                    List<View> clickableViews = new ArrayList<>();
                    clickableViews.add(nativeAdTitle);
                    clickableViews.add(nativeAdCallToAction);
                    nativeAd.registerViewForInteraction(nativeAdContainer, clickableViews);
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
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    public void ShareasVideo(final String app_package) {
        NetworkInfo activeNetwork = ((ConnectivityManager) getBaseContext().getSystemService(CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            File f = new File(Environment.getExternalStorageDirectory().getPath() + "/GIFs.mp4");
            if (!getIntent().getStringExtra("gif_mp4").equals("")) {
                showPrepareProgressDialog();
                ((B) ((B) Ion.with(getBaseContext()).load(getIntent().getExtras().getString("gif_mp4"))).progressHandler(new ProgressCallback() {
                    public void onProgress(long downloaded, long total) {
                        mMaterialDialog.setContent("loading " + (((downloaded / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) * 100) / (total / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID)) + "%");
                    }
                })).write(f).setCallback(new FutureCallback<File>() {
                    public void onCompleted(Exception e, File result) {
                        dismissProgressDialog();
                        if (e == null) {
                            try {
                                Intent sendIntent = new Intent();
                                sendIntent.setAction("android.intent.action.SEND");
                                sendIntent.setType("video/mp4");
                                sendIntent.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(getBaseContext(), getApplicationContext().getPackageName() + ".provider", result));
                                sendIntent.setPackage(app_package);
                                startActivity(sendIntent);
                                return;
                            } catch (ActivityNotFoundException e2) {
                                Toast.makeText(DetailGifActivity.this, getString(R.string.activity_exception), Toast.LENGTH_LONG).show();
                                return;
                            }
                        }
                        SnackBarFail();
                    }
                });
            } else if (!this.mGifUrl.contains(".jpg") && !this.mGifUrl.contains(".png")) {
                SnackBarMPtoAppFail();
            } else if (this.mGifUrl.contains(".jpg")) {
                File k = new File(Environment.getExternalStorageDirectory().getPath() + "/GIFs.jpeg");
                showPrepareProgressDialog();
                ((B) ((B) Ion.with(getBaseContext()).load(this.mGifUrl)).progressHandler(new ProgressCallback() {
                    public void onProgress(long downloaded, long total) {
                        mMaterialDialog.setContent("loading " + (((downloaded / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) * 100) / (total / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID)) + "%");
                    }
                })).write(k).setCallback(new FutureCallback<File>() {
                    public void onCompleted(Exception e, File result) {
                        dismissProgressDialog();
                        if (e == null) {
                            try {
                                Intent sendIntent = new Intent();
                                sendIntent.setAction("android.intent.action.SEND");
                                sendIntent.setType("image/jpeg");
                                sendIntent.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(getBaseContext(), getApplicationContext().getPackageName() + ".provider", result));
                                sendIntent.setPackage(app_package);
                                startActivity(sendIntent);
                                return;
                            } catch (ActivityNotFoundException e2) {
                                Toast.makeText(DetailGifActivity.this, getString(R.string.activity_exception), Toast.LENGTH_LONG).show();
                                return;
                            }
                        }
                        SnackBarFail();
                    }
                });
            } else {
                File k = new File(Environment.getExternalStorageDirectory().getPath() + "/GIFs.png");
                showPrepareProgressDialog();
                ((B) ((B) Ion.with(getBaseContext()).load(this.mGifUrl)).progressHandler(new ProgressCallback() {
                    public void onProgress(long downloaded, long total) {
                        mMaterialDialog.setContent("loading " + (((downloaded / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) * 100) / (total / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID)) + "%");
                    }
                })).write(k).setCallback(new FutureCallback<File>() {
                    public void onCompleted(Exception e, File result) {
                        dismissProgressDialog();
                        if (e == null) {
                            try {
                                Intent sendIntent = new Intent();
                                sendIntent.setAction("android.intent.action.SEND");
                                sendIntent.setType("image/png");
                                sendIntent.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(getBaseContext(), getApplicationContext().getPackageName() + ".provider", result));
                                sendIntent.setPackage(app_package);
                                startActivity(sendIntent);
                                return;
                            } catch (ActivityNotFoundException e2) {
                                Toast.makeText(DetailGifActivity.this, getString(R.string.activity_exception), Toast.LENGTH_LONG).show();
                                return;
                            }
                        }
                        SnackBarFail();
                    }
                });
            }
        } else {
            SnackBarNetwork();
        }
    }

    public void ShareasGIF(final String app_package) {
        NetworkInfo activeNetwork = ((ConnectivityManager) getBaseContext().getSystemService(CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (!isConnected) {
            SnackBarNetwork();
        } else if (!this.mGifUrl.contains(".jpg") && !this.mGifUrl.contains(".png")) {
            File f = new File(Environment.getExternalStorageDirectory().getPath() + "/GIFs.gif");
            showPrepareProgressDialog();
            ((B) ((B) Ion.with(getBaseContext()).load(this.mGifUrl)).progressHandler(new ProgressCallback() {
                public void onProgress(long downloaded, long total) {
                    mMaterialDialog.setContent("loading " + (((downloaded / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) * 100) / (total / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID)) + "%");
                }
            })).write(f).setCallback(new FutureCallback<File>() {
                public void onCompleted(Exception e, File result) {
                    dismissProgressDialog();
                    if (e == null) {
                        try {
                            Intent share = new Intent("android.intent.action.SEND");
                            share.setType("image/gif");
                            share.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(getBaseContext(), getApplicationContext().getPackageName() + ".provider", result));
                            share.setPackage(app_package);
                            startActivity(share);
                            return;
                        } catch (ActivityNotFoundException e2) {
                            Toast.makeText(DetailGifActivity.this, getString(R.string.activity_exception), Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    SnackBarFail();
                }
            });
        } else if (this.mGifUrl.contains(".jpg")) {
            File k = new File(Environment.getExternalStorageDirectory().getPath() + "/GIFs.jpeg");
            showPrepareProgressDialog();
            ((B) ((B) Ion.with(getBaseContext()).load(this.mGifUrl)).progressHandler(new ProgressCallback() {
                public void onProgress(long downloaded, long total) {
                    mMaterialDialog.setContent("loading " + (((downloaded / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) * 100) / (total / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID)) + "%");
                }
            })).write(k).setCallback(new FutureCallback<File>() {
                public void onCompleted(Exception e, File result) {
                    dismissProgressDialog();
                    if (e == null) {
                        try {
                            Intent sendIntent = new Intent();
                            sendIntent.setAction("android.intent.action.SEND");
                            sendIntent.setType("image/jpeg");
                            sendIntent.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(getBaseContext(), getApplicationContext().getPackageName() + ".provider", result));
                            sendIntent.setPackage(app_package);
                            startActivity(sendIntent);
                            return;
                        } catch (ActivityNotFoundException e2) {
                            Toast.makeText(DetailGifActivity.this, getString(R.string.activity_exception), Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    SnackBarFail();
                }
            });
        } else {
            File k = new File(Environment.getExternalStorageDirectory().getPath() + "/GIFs.png");
            showPrepareProgressDialog();
            ((B) ((B) Ion.with(getBaseContext()).load(this.mGifUrl)).progressHandler(new ProgressCallback() {
                public void onProgress(long downloaded, long total) {
                    mMaterialDialog.setContent("loading " + (((downloaded / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) * 100) / (total / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID)) + "%");
                }
            })).write(k).setCallback(new FutureCallback<File>() {
                public void onCompleted(Exception e, File result) {
                    dismissProgressDialog();
                    if (e == null) {
                        try {
                            Intent sendIntent = new Intent();
                            sendIntent.setAction("android.intent.action.SEND");
                            sendIntent.setType("image/png");
                            sendIntent.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(getBaseContext(), getApplicationContext().getPackageName() + ".provider", result));
                            sendIntent.setPackage(app_package);
                            startActivity(sendIntent);
                            return;
                        } catch (ActivityNotFoundException e2) {
                            Toast.makeText(DetailGifActivity.this, getString(R.string.activity_exception), Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    SnackBarFail();
                }
            });
        }
    }

    public void ShareOther() {
        boolean isConnected;
        NetworkInfo activeNetwork = ((ConnectivityManager) getBaseContext().getSystemService(CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (activeNetwork == null || !activeNetwork.isConnectedOrConnecting()) {
            isConnected = false;
        } else {
            isConnected = true;
        }
        if (!isConnected) {
            SnackBarNetwork();
        } else if (CheckAndroidVersion()) {
            if (checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1988);
            }
            if (checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED) {
                if (this.mExtension.equals(".jpg")) {
                    File f = new File(Environment.getExternalStorageDirectory().getPath() + "/GIFs.jpg");
                    showPrepareProgressDialog();
                    ((B) ((B) Ion.with(getBaseContext()).load(this.mGifUrl)).progressHandler(new ProgressCallback() {
                        public void onProgress(long downloaded, long total) {
                            mMaterialDialog.setContent("loading " + (((downloaded / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) * 100) / (total / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID)) + "%");
                        }
                    })).write(f).setCallback(new FutureCallback<File>() {
                        public void onCompleted(Exception e, File result) {
                            dismissProgressDialog();
                            if (e == null) {
                                try {
                                    Intent share = new Intent("android.intent.action.SEND");
                                    share.setType("image/jpeg");
                                    share.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(getBaseContext(), getApplicationContext().getPackageName() + ".provider", result));
                                    if (share.resolveActivity(getPackageManager()) != null) {
                                        startActivity(Intent.createChooser(share, "Share"));
                                        return;
                                    }
                                    return;
                                } catch (ActivityNotFoundException e2) {
                                    Toast.makeText(DetailGifActivity.this, getString(R.string.activity_exception), Toast.LENGTH_LONG).show();
                                    return;
                                }
                            }
                            SnackBarFail();
                        }
                    });
                } else {
                    File f = new File(Environment.getExternalStorageDirectory().getPath() + "/GIFs.gif");
                    showPrepareProgressDialog();
                    ((B) ((B) Ion.with(getBaseContext()).load(this.mGifUrl)).progressHandler(new ProgressCallback() {
                        public void onProgress(long downloaded, long total) {
                            mMaterialDialog.setContent("loading " + (((downloaded / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) * 100) / (total / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID)) + "%");
                        }
                    })).write(f).setCallback(new FutureCallback<File>() {
                        public void onCompleted(Exception e, File result) {
                            dismissProgressDialog();
                            if (e == null) {
                                try {
                                    Intent share = new Intent("android.intent.action.SEND");
                                    share.setType("image/gif");
                                    share.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(getBaseContext(), getApplicationContext().getPackageName() + ".provider", result));
                                    if (share.resolveActivity(getPackageManager()) != null) {
                                        startActivity(Intent.createChooser(share, "Share"));
                                        return;
                                    }
                                    return;
                                } catch (ActivityNotFoundException e2) {
                                    Toast.makeText(DetailGifActivity.this, getString(R.string.activity_exception), Toast.LENGTH_LONG).show();
                                    return;
                                }
                            }
                            SnackBarFail();
                        }
                    });
                }
            }
        } else if (this.mExtension.equals(".jpg")) {
            File f = new File(Environment.getExternalStorageDirectory().getPath() + "/GIFs.jpg");
            showPrepareProgressDialog();
            ((B) ((B) Ion.with(getBaseContext()).load(this.mGifUrl)).progressHandler(new ProgressCallback() {
                public void onProgress(long downloaded, long total) {
                    mMaterialDialog.setContent("loading " + (((downloaded / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) * 100) / (total / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID)) + "%");
                }
            })).write(f).setCallback(new FutureCallback<File>() {
                public void onCompleted(Exception e, File result) {
                    dismissProgressDialog();
                    if (e == null) {
                        try {
                            Intent share = new Intent("android.intent.action.SEND");
                            share.setType("image/jpeg");
                            share.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(getBaseContext(), getApplicationContext().getPackageName() + ".provider", result));
                            if (share.resolveActivity(getPackageManager()) != null) {
                                startActivity(Intent.createChooser(share, "Share"));
                                return;
                            }
                            return;
                        } catch (ActivityNotFoundException e2) {
                            Toast.makeText(DetailGifActivity.this, getString(R.string.activity_exception), Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    SnackBarFail();
                }
            });
        } else {
            File f = new File(Environment.getExternalStorageDirectory().getPath() + "/GIFs.gif");
            showPrepareProgressDialog();
            ((Ion.with(getBaseContext()).load(this.mGifUrl)).progressHandler(new ProgressCallback() {
                public void onProgress(long downloaded, long total) {
                    mMaterialDialog.setContent("loading " + (((downloaded / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) * 100) / (total / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID)) + "%");
                }
            })).write(f).setCallback(new FutureCallback<File>() {
                public void onCompleted(Exception e, File result) {
                    dismissProgressDialog();
                    if (e == null) {
                        try {
                            Intent share = new Intent("android.intent.action.SEND");
                            share.setType("image/gif");
                            share.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(getBaseContext(), getApplicationContext().getPackageName() + ".provider", result));
                            if (share.resolveActivity(getPackageManager()) != null) {
                                startActivity(Intent.createChooser(share, "Share"));
                                return;
                            }
                            return;
                        } catch (ActivityNotFoundException e2) {
                            Toast.makeText(DetailGifActivity.this, getString(R.string.activity_exception), Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    SnackBarFail();
                }
            });
        }
    }

    public void downloadGIF() {
        Calendar c = Calendar.getInstance();
        int seconds = c.get(Calendar.SECOND);
        int minute = c.get(Calendar.MINUTE);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        String time_name = String.format("%s", new Object[]{Integer.valueOf(year)}) + String.format("%s", new Object[]{Integer.valueOf(month)}) + String.format("%s", new Object[]{Integer.valueOf(day)}) + String.format("%s", new Object[]{Integer.valueOf(hour)}) + String.format("%s", new Object[]{Integer.valueOf(minute)}) + String.format("%s", new Object[]{Integer.valueOf(seconds)});
        if (CheckAndroidVersion()) {
            this.mFolderPath = getSharedPreferences("settings_data", 0).getString("folder_chooser", Environment.getExternalStorageDirectory().getPath());
        }
        File directory = new File(this.mFolderPath, "GIFs");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        final File path = new File(directory + File.separator + "GIFs" + time_name + this.mExtension);
        showDownloadProgressDialog();
        ((B) ((B) Ion.with(getBaseContext()).load(this.mGifUrl)).progressHandler(new ProgressCallback() {
            public void onProgress(long downloaded, long total) {
                mMaterialDialog.setContent((downloaded / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) + "KB / " + (total / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) + "KB");
            }
        })).write(path).setCallback(new FutureCallback<File>() {
            public void onCompleted(Exception e, File result) {
                dismissProgressDialog();
                if (e == null) {
                    SnackBarComplete();
                    if (mExtension.equals(".jpg")) {
                        MediaScannerConnection.scanFile(DetailGifActivity.this, new String[]{path.getPath()}, new String[]{"image/jpeg"}, null);
                        return;
                    }
                    MediaScannerConnection.scanFile(DetailGifActivity.this, new String[]{path.getPath()}, new String[]{"image/gif"}, null);
                    return;
                }
                SnackBarFail();
                Toast.makeText(DetailGifActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void copy_url() {
        if (VERSION.SDK_INT < 11) {
            ((ClipboardManager) getSystemService(CLIPBOARD_SERVICE)).setText(this.mGifUrl);
            return;
        }
        ((android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("Copied Text", this.mGifUrl));
        Toast.makeText(this, "Link Copied", Toast.LENGTH_LONG).show();
    }

    private void showDownloadProgressDialog() {
        this.mMaterialDialog = new MaterialDialog.Builder(this).title(getString(R.string.gif_download)).content(getString(R.string.p_wait)).progress(true, 0).progressIndeterminateStyle(true).show();
    }

    private void showPrepareProgressDialog() {
        this.mMaterialDialog = new MaterialDialog.Builder(this).title(getString(R.string.gif_prepare)).content(getString(R.string.p_wait)).progress(true, 0).show();
    }

    private void dismissProgressDialog() {
        if (this.mMaterialDialog.isShowing()) {
            this.mMaterialDialog.dismiss();
        }
    }

    private void SnackBarComplete() {
        Toast.makeText(this, R.string.gif_download_done, Toast.LENGTH_LONG).show();
    }

    private void SnackBarNetwork() {
        Toast.makeText(this, R.string.network_error, Toast.LENGTH_LONG).show();
    }

    private void SnackBarFail() {
        Toast.makeText(this, R.string.operation_fail, Toast.LENGTH_LONG).show();
    }

    private void SnackBarMPtoAppFail() {
        Toast.makeText(this, R.string.tumblr_to_mp4, Toast.LENGTH_LONG).show();
    }

    public static boolean CheckAndroidVersion() {
        return VERSION.SDK_INT >= 23;
    }

    @TargetApi(23)
    public void MarshmallowPermissionRequest(String s, String operation) {
        if (operation.equals(Event.SHARE)) {
            if (checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED) {
                if (this.mType == 1) {
                    ShareasVideo(s);
                } else {
                    ShareasGIF(s);
                }
            } else if (shouldShowRequestPermissionRationale("android.permission.WRITE_EXTERNAL_STORAGE")) {
                new MaterialDialog.Builder(this).title(R.string.app_name).content(R.string.permission_request_explain).positiveText(R.string.ok).positiveColorRes(R.color.primary).contentColor(ViewCompat.MEASURED_STATE_MASK).callback(new ButtonCallback() {
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        dialog.dismiss();
                        requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1988);
                    }
                }).show();
            } else {
                requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1988);
            }
        } else if (!operation.equals("download")) {
        } else {
            if (checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED) {
                downloadGIF();
            } else if (shouldShowRequestPermissionRationale("android.permission.WRITE_EXTERNAL_STORAGE")) {
                new MaterialDialog.Builder(this).title(R.string.app_name).content(R.string.permission_request_explain).positiveText(R.string.ok).positiveColorRes(R.color.primary).contentColor(ViewCompat.MEASURED_STATE_MASK).callback(new ButtonCallback() {
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        dialog.dismiss();
                        requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 2016);
                    }
                }).show();
            } else {
                requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 2016);
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1988) {
            if (grantResults[0] != 0) {
                Toast.makeText(this, "Permission request denied. Share fail", Toast.LENGTH_LONG).show();
            } else if (this.mType == 1) {
                ShareasVideo(this.mAppPackage);
            } else {
                ShareasGIF(this.mAppPackage);
            }
        } else if (requestCode != 2016) {
        } else {
            if (grantResults[0] == 0) {
                downloadGIF();
            } else {
                Toast.makeText(this, "Permission request denied. Downloading fail", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void info_notify() {
        try {
            SharedPreferences prefs = getSharedPreferences("info_notify", 0);
            if (!prefs.getBoolean("dontshowagain", false)) {
                final Editor editor = prefs.edit();
                long launch_count = prefs.getLong("launch_count", 0) + 1;
                editor.putLong("launch_count", launch_count);
                if (launch_count >= ((long) 1)) {
                    new MaterialDialog.Builder(this).title(R.string.app_name).content(R.string.info_notify).positiveText(R.string.ok).positiveColorRes(R.color.primary).contentColor(ViewCompat.MEASURED_STATE_MASK).cancelable(true).callback(new ButtonCallback() {
                        public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);
                            editor.putBoolean("dontshowagain", true);
                            editor.apply();
                            dialog.dismiss();
                        }
                    }).show();
                }
                editor.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_show, menu);
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem fave = menu.findItem(R.id.action_favorite);
        if (isFavorite) {
            if (VERSION.SDK_INT >= 21) {
                fave.setIcon(ContextCompat.getDrawable(this, R.drawable.favorite_white));
            } else {
                fave.setIcon(getResources().getDrawable(R.drawable.favorite_white));
            }
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_share:
                ShareOther();
                return true;
            case R.id.action_favorite:
                if (isFavorite) {
                    if (VERSION.SDK_INT >= 21) {
                        item.setIcon(ContextCompat.getDrawable(this, R.drawable.favorite_border));
                    } else {
                        item.setIcon(getResources().getDrawable(R.drawable.favorite_border));
                    }
                    removeFavorite();
                    isFavorite = false;
                } else {
                    if (VERSION.SDK_INT >= 21) {
                        item.setIcon(ContextCompat.getDrawable(this, R.drawable.favorite_white));
                    } else {
                        item.setIcon(getResources().getDrawable(R.drawable.favorite_white));
                    }
                    addFavorite();
                    isFavorite = true;
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void removeFavorite() {

        Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<FavoriteItemRealm> items = realm.where(FavoriteItemRealm.class)
                        .equalTo("gifLink", getIntent().getStringExtra("gif_link")).findAll();

                if (items != null && items.size() > 0) {
                    items.deleteAllFromRealm();
                }


            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(DetailGifActivity.this, R.string.fav_delete, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addFavorite() {
        Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                FavoriteItemRealm item = new FavoriteItemRealm(mGifUrl, mpfourLink, title, gifPreview);
                realm.insertOrUpdate(item);


            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(DetailGifActivity.this, R.string.fav_add, Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void onFolderSelection(@NonNull FolderChooserDialog dialog, @NonNull File folder) {
        Editor path = getSharedPreferences("settings_data", 0).edit();
        path.putString("folder_chooser", folder.getPath());
        path.apply();
        mFolderPath = folder.getPath();
        downloadGIF();
    }

    @Override
    public void onFolderChooserDismissed(@NonNull FolderChooserDialog dialog) {

    }


    Admob admob;

    void initAdmob() {
        admob = new Admob(this);
        admob.showInterstitialAd(null);
        showFAd();
    }
    void showFAd() {
        if(!AppConstant.isRemoveAds(getApplicationContext())) {
            interstitialAd = new InterstitialAd(this, "169523040462460_169526347128796");
            interstitialAd.setAdListener(new InterstitialAdListener() {
                @Override
                public void onInterstitialDisplayed(Ad ad) {
                    // Interstitial displayed callback
                }

                @Override
                public void onInterstitialDismissed(Ad ad) {
                    // Interstitial dismissed callback
                }

                @Override
                public void onError(Ad ad, AdError adError) {
                    // Ad error callback
                    LogUtil.d("Error: " + adError.getErrorMessage());
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    // Show the ad when it's done loading.
                    Random rand = new Random();
                    int n = rand.nextInt(4) + 1;
                    Log.e("levan_new", "show + n= " + String.valueOf(n));
                    if (n == 2) {
                        interstitialAd.show();
                    }
                }

                @Override
                public void onAdClicked(Ad ad) {
                    // Ad clicked callback
                }

                @Override
                public void onLoggingImpression(Ad ad) {
                    // Ad impression logged callback
                }
            });

            // For auto play video ads, it's recommended to load the ad
            // at least 30 seconds before it is shown
            interstitialAd.loadAd();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (admob != null) {
            admob.pause();
        }
        LogUtil.d( "onResume() PageFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        if (admob != null) {
            admob.resume();
        }
        LogUtil.d("onPAUSE() PageFragment");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (admob != null) {
            admob.destroy();
        }
    }
}
