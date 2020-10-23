package com.pencil.sketch.photo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aerotools.photo.sketch.maker.editor.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.cast.TextTrackStyle;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.pencil.sketch.photo.GPUImageFilterTools.OnGpuImageFilterChosenListener;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

public class PhotoShare_Activity extends Activity {
    String Imagepath;
    int MaxResolution;
    ImageButton blue_Button;
    LinearLayout color_layout;
    int currentapiVersion;
    ImageButton doneButton;
    ImageButton effect_Button;
    ImageButton fb_btn;
    private String foldername = null;
    ImageButton green_Button;
    int imageheight;
    int imagewidth;
    public GPUImageFilter mFilter;
    public GPUImage mGPUImage;
    InterstitialAd mInterstitialAd;
    ImageView pic_imageview;
    ImageButton red_Button;
    ImageButton reset_Button;
    ImageButton save;
    public Boolean savedok = Boolean.valueOf(false);
    int screenwidth;
    ImageButton share;
    Bitmap share_bitmap;
    Uri shareuri = null;
    private String tempimagepath = null;
    ImageButton txt_i;

    public class EffectAsnyTask extends AsyncTask<Integer, Void, Void> {
        Bitmap eff;

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Integer... params) {
            PhotoShare_Activity.this.Image_effect(params[0].intValue());
            return null;
        }

        protected void onPostExecute(Void result) {
            Bitmap bitmap = this.eff;
            try {
                this.eff = PhotoShare_Activity.this.mGPUImage.getBitmapWithFilterApplied(PhotoShare_Activity.this.share_bitmap);
                PhotoShare_Activity.this.pic_imageview.setImageBitmap(this.eff);
            } catch (NullPointerException e) {
                PhotoShare_Activity.this.pic_imageview.setImageBitmap(PhotoShare_Activity.this.share_bitmap);
                PhotoShare_Activity.this.mGPUImage.setImage(PhotoShare_Activity.this.share_bitmap);
            } catch (OutOfMemoryError e2) {
                PhotoShare_Activity.this.pic_imageview.setImageBitmap(PhotoShare_Activity.this.share_bitmap);
                PhotoShare_Activity.this.mGPUImage.setImage(PhotoShare_Activity.this.share_bitmap);
                System.gc();
            } catch (Exception e3) {
            }
            if (!(bitmap == null || bitmap.isRecycled())) {
                bitmap.recycle();
                System.gc();
            }
            super.onPostExecute(result);
        }
    }

    public class LoadImageAsycTask extends AsyncTask<Void, Void, Void> {
        Float Orientation;
        ProgressDialog dialog;
        Boolean getimage;

        protected void onPreExecute() {
            this.dialog = ProgressDialog.show(PhotoShare_Activity.this, "", "Loading...");
            this.dialog.setCancelable(false);
            PhotoShare_Activity.this.getIntentExtra();
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            if (PhotoShare_Activity.this.shareuri == null) {
                try {
                    this.getimage = Boolean.valueOf(false);
                } catch (OutOfMemoryError e) {
                    this.getimage = Boolean.valueOf(false);
                } catch (NullPointerException e2) {
                    this.getimage = Boolean.valueOf(false);
                } catch (Exception e3) {
                    this.getimage = Boolean.valueOf(false);
                }
                this.getimage = Boolean.valueOf(false);
            }
            PhotoShare_Activity.this.Imagepath = PhotoShare_Activity.this.getRealPathFromURI(PhotoShare_Activity.this.shareuri);
            if ((PhotoShare_Activity.this.Imagepath != null && PhotoShare_Activity.this.Imagepath.endsWith(".png")) || PhotoShare_Activity.this.Imagepath.endsWith(".jpg") || PhotoShare_Activity.this.Imagepath.endsWith(".jpeg") || PhotoShare_Activity.this.Imagepath.endsWith(".bmp")) {
                this.Orientation = Float.valueOf(PhotoShare_Activity.this.getImageOrientation(PhotoShare_Activity.this.Imagepath));
                PhotoShare_Activity.this.getAspectRatio(PhotoShare_Activity.this.Imagepath, (float) PhotoShare_Activity.this.MaxResolution);
                PhotoShare_Activity.this.share_bitmap = PhotoShare_Activity.this.getResizedOriginalBitmap(PhotoShare_Activity.this.Imagepath, this.Orientation.floatValue());
                this.getimage = Boolean.valueOf(true);
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            if (!this.getimage.booleanValue()) {
                Toast.makeText(PhotoShare_Activity.this.getApplicationContext(), "Unsupported media file.", 0).show();
                PhotoShare_Activity.this.finish();
            } else if (PhotoShare_Activity.this.share_bitmap == null || PhotoShare_Activity.this.share_bitmap.getHeight() <= 5 || PhotoShare_Activity.this.share_bitmap.getWidth() <= 5) {
                Toast.makeText(PhotoShare_Activity.this.getApplicationContext(), "Image Format not supported .", 0).show();
                PhotoShare_Activity.this.finish();
            } else {
                PhotoShare_Activity.this.pic_imageview.setImageBitmap(PhotoShare_Activity.this.share_bitmap);
                PhotoShare_Activity.this.mGPUImage.setImage(PhotoShare_Activity.this.share_bitmap);
            }
            this.dialog.dismiss();
            super.onPostExecute(result);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photoshare_activity);
        this.mInterstitialAd = new InterstitialAd(this);
        this.mInterstitialAd.setAdUnitId("ca-app-pub-5153360694838747/5102960711");
        requestNewInterstitial();
        this.color_layout = (LinearLayout) findViewById(R.id.top_color_button);
        this.effect_Button = (ImageButton) findViewById(R.id.effect_button);
        this.red_Button = (ImageButton) findViewById(R.id.red_button);
        this.blue_Button = (ImageButton) findViewById(R.id.blue_button);
        this.green_Button = (ImageButton) findViewById(R.id.green_button);
        this.doneButton = (ImageButton) findViewById(R.id.done_button);
        this.reset_Button = (ImageButton) findViewById(R.id.reset_button);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.mGPUImage = new GPUImage(this);
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        this.screenwidth = displaymetrics.widthPixels;
        this.MaxResolution = this.screenwidth;
        this.currentapiVersion = VERSION.SDK_INT;
        this.pic_imageview = (ImageView) findViewById(R.id.finalimg);
        this.save = (ImageButton) findViewById(R.id.save_btn);
        this.share = (ImageButton) findViewById(R.id.share_btn);
        this.fb_btn = (ImageButton) findViewById(R.id.fb);
        this.txt_i = (ImageButton) findViewById(R.id.insta);
        new LoadImageAsycTask().execute(new Void[0]);
        this.green_Button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                new EffectAsnyTask().execute(new Integer[]{Integer.valueOf(0)});
                PhotoShare_Activity.this.doneButton.setVisibility(View.VISIBLE);
                PhotoShare_Activity.this.reset_Button.setVisibility(View.VISIBLE);
            }
        });
        this.blue_Button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                new EffectAsnyTask().execute(new Integer[]{Integer.valueOf(1)});
                PhotoShare_Activity.this.doneButton.setVisibility(View.VISIBLE);
                PhotoShare_Activity.this.reset_Button.setVisibility(View.VISIBLE);
            }
        });
        this.red_Button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                new EffectAsnyTask().execute(new Integer[]{Integer.valueOf(2)});
                PhotoShare_Activity.this.doneButton.setVisibility(View.VISIBLE);
                PhotoShare_Activity.this.reset_Button.setVisibility(View.VISIBLE);
            }
        });
        this.reset_Button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                new LoadImageAsycTask().execute(new Void[0]);
                PhotoShare_Activity.this.color_layout.setVisibility(4);
            }
        });
        this.doneButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                PhotoShare_Activity.this.color_layout.setVisibility(4);
            }
        });
        this.effect_Button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                PhotoShare_Activity.this.color_layout.setVisibility(View.VISIBLE);
            }
        });
        this.save.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                PhotoShare_Activity.this.saveImage(PhotoShare_Activity.this.mGPUImage.getBitmapWithFilterApplied());
            }
        });
        this.share.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                File file = null;
                try {
                    file = new File(PhotoShare_Activity.this.saveImageFoShare(PhotoShare_Activity.this.foldername, 100, PhotoShare_Activity.this.mGPUImage.getBitmapWithFilterApplied()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                PhotoShare_Activity.this.shareuri = Uri.parse("file:" + file);
                Intent intent = new Intent("android.intent.action.SEND");
                intent.putExtra("sms_body", PhotoShare_Activity.this.foldername);
                intent.setType("image/png");
                intent.putExtra("android.intent.extra.SUBJECT", PhotoShare_Activity.this.foldername);
                intent.putExtra("android.intent.extra.STREAM", PhotoShare_Activity.this.shareuri);
                PhotoShare_Activity.this.startActivity(Intent.createChooser(intent, "Share image by..."));
            }
        });
        this.fb_btn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (PhotoShare_Activity.this.appInstalledOrNot("com.facebook.katana")) {
                    File file = null;
                    try {
                        file = new File(PhotoShare_Activity.this.saveImageFoShare(PhotoShare_Activity.this.foldername, 100, PhotoShare_Activity.this.mGPUImage.getBitmapWithFilterApplied()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    PhotoShare_Activity.this.shareuri = Uri.parse("file:" + file);
                    PhotoShare_Activity.this.share("com.facebook.katana", PhotoShare_Activity.this.shareuri);
                    return;
                }
                Toast.makeText(PhotoShare_Activity.this, "Facebook is not Installed", 0).show();
            }
        });
        this.txt_i.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (PhotoShare_Activity.this.appInstalledOrNot("com.instagram.android")) {
                    File file = null;
                    try {
                        file = new File(PhotoShare_Activity.this.saveImageFoShare(PhotoShare_Activity.this.foldername, 100, PhotoShare_Activity.this.mGPUImage.getBitmapWithFilterApplied()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    PhotoShare_Activity.this.shareuri = Uri.parse("file:" + file);
                    PhotoShare_Activity.this.share("com.instagram.android", PhotoShare_Activity.this.shareuri);
                    return;
                }
                Toast.makeText(PhotoShare_Activity.this, "Instagram is not Installed", 0).show();
            }
        });
    }

    private void getIntentExtra() {
        Intent intent = getIntent();
        this.shareuri = intent.getData();
        this.MaxResolution = intent.getIntExtra("picresolution", this.screenwidth);
        this.foldername = intent.getStringExtra("foldername");
    }

    private String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) {
            return uri.getPath();
        }
        cursor.moveToFirst();
        String s = cursor.getString(cursor.getColumnIndex("_data"));
        cursor.close();
        return s;
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

    private void getAspectRatio(String s, float f) {
        float f3;
        float f2;
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(s, options);
        float f1 = ((float) options.outWidth) / ((float) options.outHeight);
        if (f1 > TextTrackStyle.DEFAULT_FONT_SCALE) {
            f3 = f;
            f2 = f3 / f1;
        } else {
            f2 = f;
            f3 = f2 * f1;
        }
        this.imagewidth = (int) f3;
        this.imageheight = (int) f2;
    }

    public Bitmap getResizedOriginalBitmap(String s, float f2) {
        try {
            Bitmap bitmap;
            Matrix matrix;
            Options options = new Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(s), null, options);
            int i = options.outWidth;
            int j = options.outHeight;
            int k = this.imagewidth;
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
                        bitmap = BitmapFactory.decodeStream(new FileInputStream(s), null, options);
                        matrix = new Matrix();
                        matrix.postScale(f, f1);
                        matrix.postRotate(f2);
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

    public static String encodeString(String string) {
        if (string != null) {
            return string.replace("/", "-").replace(" ", "_").replace(":", "-").replace("&", "-");
        }
        return string;
    }

    public void saveImage(Bitmap bitmap) {
        String second;
        String myDir = Environment.getExternalStorageDirectory() + "/Sketch Photo";
        Calendar c = Calendar.getInstance();
        int month = (c.get(2) + 1);
        int day = c.get(5);
        int year = c.get(1);
        int hour = c.get(11);
        int minute = c.get(12);
        int seconds = c.get(13);
        if (seconds < 10) {
            second = "0" + seconds;
        } else {
            second = String.valueOf(seconds);
        }
        File file = new File(myDir+"/"+encodeString("Sketch-" + hour + ":" + minute + ":" + second + "/" + month + "-" + day + "-" + year + ".jpg"));
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            String[] paths = new String[]{file.toString()};
            String[] mimeTypes = new String[]{"/image/jpeg"};
            Toast.makeText(getApplicationContext(), "Image Saved to" + paths[0], 1).show();
            MediaScannerConnection.scanFile(this, paths, mimeTypes, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onDestroy() {
        if (this.savedok.booleanValue()) {
            if (!(this.share_bitmap == null || this.share_bitmap.isRecycled())) {
                this.share_bitmap.recycle();
                this.share_bitmap = null;
                System.gc();
            }
            this.savedok = Boolean.valueOf(false);
            this.shareuri = null;
        } else if (this.shareuri == null) {
            if (!(this.share_bitmap == null || this.share_bitmap.isRecycled())) {
                this.share_bitmap.recycle();
                this.share_bitmap = null;
                System.gc();
            }
            this.savedok = Boolean.valueOf(false);
            this.shareuri = null;
        } else {
            File file = new File(this.shareuri.getPath());
            if (file.exists()) {
                file.delete();
            }
            if (this.currentapiVersion > 18) {
                scanFile(this.shareuri.getPath(), true);
            }
            if (this.currentapiVersion <= 18) {
                try {
                    Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                    intent.setData(Uri.fromFile(new File(this.shareuri.getPath().toString())));
                    sendBroadcast(intent);
                } catch (NullPointerException nullpointerexception) {
                    nullpointerexception.printStackTrace();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
        super.onDestroy();
    }

    private void scanFile(String s, final boolean isDelete) {
        try {
            MediaScannerConnection.scanFile(getApplicationContext(), new String[]{s}, null, new OnScanCompletedListener() {
                public void onScanCompleted(String s1, Uri uri) {
                    if (isDelete && uri != null) {
                        PhotoShare_Activity.this.getApplicationContext().getContentResolver().delete(uri, null, null);
                    }
                }
            });
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private String saveImageFoShare(String s, int i, Bitmap bitmap) throws IOException {
        String s1 = Environment.getExternalStorageDirectory() + File.separator + s + File.separator;
        new File(s1).mkdirs();
        new Options().inSampleSize = 5;
        this.tempimagepath = new StringBuilder(String.valueOf(s1)).append(UUID.randomUUID().toString()).append(".jpg").toString();
        File file = new File(this.tempimagepath);
        String[] as = null;
        if (file.exists()) {
            try {
                file.delete();
                file.createNewFile();
            } catch (NullPointerException e) {
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        } else {
            file.createNewFile();
        }
        BufferedOutputStream bufferedoutputstream = new BufferedOutputStream(new FileOutputStream(file));
        bitmap.compress(CompressFormat.JPEG, i, bufferedoutputstream);
        bufferedoutputstream.flush();
        bufferedoutputstream.close();
        as = new String[]{file.toString()};
        MediaScannerConnection.scanFile(this, as, null, new OnScanCompletedListener() {
            public void onScanCompleted(String s1, Uri uri) {
            }
        });
        return this.tempimagepath;
    }

    public void share(String s, Uri shareuri2) {
        try {
            ArrayList<Intent> arraylist = new ArrayList();
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("image/jpeg");
            List<?> list = getPackageManager().queryIntentActivities(intent, 0);
            if (!list.isEmpty()) {
                Iterator<?> iterator = list.iterator();
                while (iterator.hasNext()) {
                    ResolveInfo resolveinfo = (ResolveInfo) iterator.next();
                    Intent intent2 = new Intent("android.intent.action.SEND");
                    intent2.setType("image/jpeg");
                    if (resolveinfo.activityInfo.packageName.toLowerCase().contains(s) || resolveinfo.activityInfo.name.toLowerCase().contains(s)) {
                        intent2.putExtra("android.intent.extra.SUBJECT", "Sample Photo");
                        intent2.putExtra("android.intent.extra.TEXT", "This photo is created by App Name");
                        intent2.putExtra("android.intent.extra.STREAM", shareuri2);
                        intent2.setPackage(resolveinfo.activityInfo.packageName);
                        arraylist.add(intent2);
                    }
                }
                Intent intent1 = Intent.createChooser((Intent) arraylist.remove(0), "Select app to share");
                intent1.putExtra("android.intent.extra.INITIAL_INTENTS", (Parcelable[]) arraylist.toArray(new Parcelable[0]));
                startActivity(intent1);
            }
        } catch (Exception exception) {
            Log.v("VM", "Exception while sending image on" + s + " " + exception.getMessage());
        }
    }

    protected void Image_effect(int i) {
        GPUImageFilterTools.Applyeffects(i, this, new OnGpuImageFilterChosenListener() {
            public void onGpuImageFilterChosenListener(GPUImageFilter gpuimagefilter) {
                PhotoShare_Activity.this.switchFilterTo(gpuimagefilter);
                PhotoShare_Activity.this.mGPUImage.requestRender();
            }
        });
    }

    public void switchFilterTo(GPUImageFilter gpuimagefilter) {
        if (this.mFilter == null || gpuimagefilter != null) {
            this.mFilter = gpuimagefilter;
            this.mGPUImage.setFilter(this.mFilter);
        }
    }

    public void onBackPressed() {
        if (this.mInterstitialAd.isLoaded()) {
            this.mInterstitialAd.show();
        } else {
            finish();
        }
        this.mInterstitialAd.setAdListener(new AdListener() {
            public void onAdClosed() {
                PhotoShare_Activity.this.finish();
            }
        });
    }

    private void requestNewInterstitial() {
        this.mInterstitialAd.loadAd(new Builder().build());
    }

    private boolean appInstalledOrNot(String uri) {
        try {
            getPackageManager().getPackageInfo(uri, 1);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }
}
