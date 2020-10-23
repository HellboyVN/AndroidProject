package com.photo.sketch.photo;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.AppConstant;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.cast.TextTrackStyle;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.hbtools.photo.sketchpencil.editer.pencilsketch.R;
import com.pencil.sketch.photo.ImageRemake;
import com.photo.sketch.gallery.BucketHomeFragmentActivity;
import com.photo.sketch.gallery.MediaChooser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class SelectActivity extends AppCompatActivity {
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_LOAD_IMAGE = 2;
    private static final int REQUEST_STORAGE = 0;
    public static int imageFrom = 0;
    protected static Uri outputFileUri = null;
    public static String selectedImagePath;
    private int MaxResolution;
    ImageButton btnstart;
    BroadcastReceiver imageBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            SelectActivity.this.imagedataSet(intent.getStringArrayListExtra("list"));
        }
    };
    private String imagePath;
    int imageheight;
    int imagwidth;
    InterstitialAd mInterstitialAd;
    private float orientation;
    private Bitmap originalBmp;
    private int widthPixel;

    public /* bridge */ /* synthetic */ View onCreateView(View view, String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(view, str, context, attributeSet);
    }

    public /* bridge */ /* synthetic */ View onCreateView(String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(str, context, attributeSet);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_select);
        MultiDex.install(this);
        MobileAds.initialize(this,
                "ca-app-pub-8468661407843417~3297941717");
        this.originalBmp = null;
        this.btnstart = (ImageButton) findViewById(R.id.PickFromGallery);
        registerReceiver(this.imageBroadcastReceiver, new IntentFilter(MediaChooser.IMAGE_SELECTED_ACTION_FROM_MEDIA_CHOOSER));
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        this.widthPixel = displaymetrics.widthPixels;
        this.MaxResolution = this.widthPixel;
        if (this.MaxResolution > 550) {
            this.MaxResolution = 550;
        }
        this.btnstart.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (SelectActivity.this.checkNeedsPermission()) {
                    SelectActivity.this.requestStoragePermission();
                    return;
                }
                MediaChooser.setSelectionLimit(20);
                SelectActivity.this.startActivity(new Intent(SelectActivity.this, BucketHomeFragmentActivity.class));
            }
        });
        this.mInterstitialAd = new InterstitialAd(this);
        this.mInterstitialAd.setAdUnitId("ca-app-pub-8468661407843417/8647359366");
        requestNewInterstitial();
    }

    private void imagedataSet(List<String> filePathList) {
        this.imagePath = (String) filePathList.get(0);
        StartImageRemaker(Uri.parse(this.imagePath));
    }

    public void GetImage() {
        try {
            this.imagePath = getIntent().getStringExtra("imagePath");
            if (this.imagePath != null) {
                this.orientation = getImageOrientation(this.imagePath);
                getAspectRatio(this.imagePath);
                this.originalBmp = getResizedOriginalBitmap();
                if (this.originalBmp == null) {
                    Toast.makeText(getApplicationContext(), "unsupported image file", 0).show();
                    finish();
                }
            }
        } catch (OutOfMemoryError e) {
            Toast.makeText(getApplicationContext(), "Insufficient memory, please free some memory space", 0).show();
            this.originalBmp = null;
            finish();
        } catch (Exception e2) {
            this.originalBmp = null;
        }
    }

    @TargetApi(16)
    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.WRITE_EXTERNAL_STORAGE")) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 0);
            return;
        }
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 0);
    }

    @TargetApi(23)
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != 0) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        } else if (grantResults.length == 1 && grantResults[0] == 0) {
            MediaChooser.setSelectionLimit(20);
            startActivity(new Intent(this, BucketHomeFragmentActivity.class));
        } else {
            Toast.makeText(this, "You canot use the application without access to external storage :/", 0).show();
        }
    }

    private float getImageOrientation(String s) {
        try {
            int i = new ExifInterface(s).getAttributeInt("Orientation", 1);
            if (i == 6) {
                return 90.0f;
            }
            if (i == 3) {
                return BitmapDescriptorFactory.HUE_CYAN;
            }
            if (i == 8) {
                return BitmapDescriptorFactory.HUE_VIOLET;
            }
            return 0.0f;
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
            return 0.0f;
        }
    }

    private boolean checkNeedsPermission() {
        return VERSION.SDK_INT >= 16 && ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") != 0;
    }

    private void getAspectRatio(String s) {
        float f2;
        float f1;
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(s, options);
        float f = ((float) options.outWidth) / ((float) options.outHeight);
        if (f > TextTrackStyle.DEFAULT_FONT_SCALE) {
            f2 = (float) this.MaxResolution;
            f1 = f2 / f;
        } else {
            f1 = (float) this.MaxResolution;
            f2 = f1 * f;
        }
        this.imagwidth = (int) f2;
        this.imageheight = (int) f1;
    }

    public Bitmap getResizedOriginalBitmap() {
        try {
            Bitmap bitmap;
            Matrix matrix;
            Options options = new Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(this.imagePath), null, options);
            int i = options.outWidth;
            int j = options.outHeight;
            int k = this.imagwidth;
            int l = this.imageheight;
            int i1 = 1;
            while (true) {
                if (i / 2 <= k) {
                    float f = ((float) k) / ((float) i);
                    float f1 = ((float) l) / ((float) j);
                    options.inJustDecodeBounds = false;
                    options.inDither = false;
                    options.inSampleSize = i1;
                    options.inScaled = false;
                    options.inPreferredConfig = Config.ARGB_8888;
                    try {
                        bitmap = BitmapFactory.decodeStream(new FileInputStream(this.imagePath), null, options);
                        matrix = new Matrix();
                        matrix.postScale(f, f1);
                        matrix.postRotate(this.orientation);
                        break;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    i /= 2;
                    j /= 2;
                    i1 *= 2;
                }
            }
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (FileNotFoundException e2) {
            return null;
        }
    }

    public void onBackPressed() {
        if (this.mInterstitialAd.isLoaded()) {
            if(!AppConstant.isRemoveAds(getApplicationContext())){
                this.mInterstitialAd.show();
            }
        } else {
            BackPressedClickOptions();
        }
        this.mInterstitialAd.setAdListener(new AdListener() {
            public void onAdClosed() {
                SelectActivity.this.requestNewInterstitial();
                SelectActivity.this.BackPressedClickOptions();
            }
        });
    }

    private void requestNewInterstitial() {
        this.mInterstitialAd.loadAd(new Builder().build());
    }

    private void StartImageRemaker(Uri uri) {
        String[] as = new String[]{"CROP"};
        Intent intent = new Intent(this, ImageRemake.class);
        intent.setData(uri);
        intent.putExtra("picresolution", this.widthPixel);
        intent.putExtra("tool_title", as);
        startActivityForResult(intent, 5);
    }

    public boolean checkOnlineState() {
        NetworkInfo NInfo = ((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo();
        if (NInfo == null || !NInfo.isConnectedOrConnecting()) {
            return false;
        }
        return true;
    }

    private void BackPressedClickOptions() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialoge);
        dialog.setTitle("Rate This Appliation");
        ((Button) dialog.findViewById(R.id.btn_yes)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setData(Uri.parse("market://details?id=" + SelectActivity.this.getPackageName()));
                SelectActivity.this.startActivity(intent);
            }
        });
        ((Button) dialog.findViewById(R.id.btn_no)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SelectActivity.this.finish();
            }
        });
        dialog.show();
    }
}
