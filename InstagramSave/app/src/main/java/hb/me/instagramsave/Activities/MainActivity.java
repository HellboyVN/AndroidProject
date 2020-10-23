package hb.me.instagramsave.Activities;

import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.NativeExpressAdView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.Drawer.OnDrawerItemClickListener;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.volokh.danylo.hashtaghelper.HashTagHelper;
import com.volokh.danylo.hashtaghelper.HashTagHelper.Creator;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;
import hb.me.instagramsave.Adapters.RecyclerViewAdapter;
import hb.me.instagramsave.Intro.IntroActivity;
import hb.me.instagramsave.Model.Files;
import hb.me.instagramsave.R;
import hb.me.instagramsave.Utils.Constant;
import hb.me.instagramsave.Utils.Constants;
import hb.me.instagramsave.Utils.SettingsActivity;
import hb.me.instagramsave.Utils.Source;

public class MainActivity extends AppCompatActivity {
    public static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 123;
    private static final String PREFS_NAME = "preferenceName";
    private static String URL = "";
    private static Bundle mBundleRecyclerViewState;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    AdRequest adRequestint;
    TextView captionText;
    private Button copyHashtagsBtn;
    private Button copyTextBtn;
    private int counter = 0;
    private String desc;
    private String description = "";
    private AlertDialog dialog;
    private Builder dialogBuilder;
    private Boolean doubleBackToExitPressedOnce = Boolean.valueOf(false);
    TextView downloadPercentage;
    private ProgressBar downloadProgress;
    String fileN = null;
    File[] files;
    private ClipData hashtagClipData;
    private ClipboardManager hashtagClipboard;
    AccountHeader headerResult;
    String htmlResponse;
    String htmlSource = "";
    private String imageUrl = "";
    Parcelable listState;
    SharedPreferences loadCounter;
    private AdView mAdView;
    private Handler mHandler = new Handler();
    InterstitialAd mInterstitialAd;
    ProgressDialog mProgressDialog;
    private int mProgressStatus;
    private HashTagHelper mTextHashTagHelper;
    Toolbar mToolbar;
    private ImageView mainImageView;
    private ClipData myClip;
    private ClipboardManager myClipboard;
    private TextView noImageText;
    String path;
    RelativeLayout placeholderLayout;
    private ImageView playIcon;
    SharedPreferences preferences;
    private ProgressBar progressBar;
    RequestQueue queue;
    SwipeRefreshLayout recyclerLayout;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private Button repost;
    String resultDesc;
    Drawer resultDrawer;
    private String resultz;
    SharedPreferences saveCounter;
    Intent serviceIntent;
    Uri uri;
    String urlDesc;
    private String videoUrl = "";

    public class DownloadTask extends AsyncTask<String, Integer, String> {
        private Context context;
        private WakeLock mWakeLock;

        public DownloadTask(Context context) {
            this.context = context;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        protected java.lang.String doInBackground(java.lang.String... f_url) {
            int count;
            OutputStream output;
            try {
                java.net.URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);
                Log.e("---LINK---",conection.getContent().toString()+"\n--URL--= "+f_url[0]
                        +"\n "+ conection.getRequestProperty("title"));
                // Output stream
                if(f_url[0].endsWith("mp4")){
                    fileN = resultDesc+"+instakeep_"+java.util.UUID.randomUUID().toString().substring(0,5)+".mp4";
                     output = new FileOutputStream(Environment
                            .getExternalStorageDirectory().toString()
                            + "/InstaKeep/"+fileN);
                }else  {
                    fileN = resultDesc+"+instakeep_"+java.util.UUID.randomUUID().toString().substring(0,5)+".jpg";
                     output = new FileOutputStream(Environment
                            .getExternalStorageDirectory().toString()
                            + "/InstaKeep/"+fileN);
                }


                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                   // publishProgress(Integer.valueOf((int) ((total * 100) / lenghtOfFile)));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            Log.e("----Levan----", f_url.toString());
            return null;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            this.mWakeLock = ((PowerManager) this.context.getSystemService(POWER_SERVICE)).newWakeLock(1, getClass().getName());
            this.mWakeLock.acquire();
            MainActivity.this.mProgressDialog.show();
        }

        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            MainActivity.this.mProgressDialog.setIndeterminate(false);
            MainActivity.this.mProgressDialog.setMax(100);
            MainActivity.this.mProgressDialog.setProgress(progress[0].intValue());
        }

        protected void onPostExecute(String result) {
            this.mWakeLock.release();
            MainActivity.this.mProgressDialog.dismiss();
            if (result != null) {
                Toast.makeText(this.context, "Download error: " + result, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this.context, "File downloaded", Toast.LENGTH_SHORT).show();
            }
            MediaScannerConnection.scanFile(MainActivity.this, new String[]{Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.FOLDER_NAME + MainActivity.this.fileN}, null, new OnScanCompletedListener() {
                public void onScanCompleted(String newpath, Uri newuri) {
                    Log.i("ExternalStorage", "Scanned " + newpath + ":");
                    Log.i("ExternalStorage", "-> uri=" + newuri);
                    MainActivity.this.path = newpath;
                    MainActivity.this.uri = newuri;
                }
            });
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        MultiDex.install(this);
        Constant.showFAd(this);
//        OneSignal.startInit(this).setNotificationOpenedHandler(new NotificationOpenedHandler() {
//            public void notificationOpened(OSNotificationOpenResult result) {
//            }
//        }).init();
        setTheme(PreferenceManager.getDefaultSharedPreferences(this).getInt("theme",Constant.theme));
        setContentView((int) R.layout.activity_main);
        if (getAppIntro(this).booleanValue()) {
            startActivity(new Intent(this, IntroActivity.class));
        }
        initComponents();
        this.mAdView = (AdView) findViewById(R.id.adView);
        this.mAdView.loadAd(new AdRequest.Builder().addTestDevice("3F90C25D51474DDEA1C2B2319E1ADE19").build());
        ((NativeExpressAdView) findViewById(R.id.nativeAD)).loadAd(new AdRequest.Builder().addTestDevice("3F90C25D51474DDEA1C2B2319E1ADE19").build());
        this.mToolbar = (Toolbar) findViewById(R.id.toolbar);
        this.mToolbar.setBackgroundColor(PreferenceManager.getDefaultSharedPreferences(this).getInt("color",Constant.color));
        setSupportActionBar(this.mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ClipData clip = ((ClipboardManager) getSystemService(CLIPBOARD_SERVICE)).getPrimaryClip();
        if (clip != null) {
            try {
                URL = Source.getURL(clip.getItemAt(0).getText().toString(), "(http(s)?:\\/\\/(.+?\\.)?[^\\s\\.]+\\.[^\\s\\/]{1,9}(\\/[^\\s]+)?)");
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("URL: ", URL);
        } else {
            Toast.makeText(this, "Empty ClipBoard!", Toast.LENGTH_SHORT).show();
        }
        this.queue = Volley.newRequestQueue(this);
        boolean result = checkPermission();
        Typeface typeface = Typeface.createFromAsset(getAssets(), "sintony-regular.otf");
        if (result) {
            checkFolder();
            if (checkForEmptyUrl(URL)) {
                viewPageSource();
                setUpRecyclerView();
            } else {
                Toast.makeText(this, "Please Copy Instagram Share URL", Toast.LENGTH_SHORT).show();
                setUpRecyclerView();
                this.placeholderLayout.setVisibility(View.INVISIBLE);
                this.recyclerLayout.setVisibility(View.VISIBLE);
            }
        }
        this.headerResult = new AccountHeaderBuilder().withActivity(this).withHeaderBackground(R.drawable.instagram_gradient_background1).withSelectionListEnabledForSingleProfile(false).withAlternativeProfileHeaderSwitching(false).withCompactStyle(false).withDividerBelowHeader(false).withProfileImagesVisible(true).withTypeface(typeface).addProfiles(new ProfileDrawerItem().withIcon((int) R.drawable.ic_icon).withName(getResources().getString(R.string.app_name)).withEmail(getResources().getString(R.string.developer_email))).build();
        this.resultDrawer = new DrawerBuilder().withActivity(this).withSelectedItem(-1).withFullscreen(true).withAccountHeader(this.headerResult).withActionBarDrawerToggle(true).withCloseOnClick(true).withMultiSelect(false).withTranslucentStatusBar(true).withToolbar(this.mToolbar).addDrawerItems((IDrawerItem) ((PrimaryDrawerItem) ((PrimaryDrawerItem) new PrimaryDrawerItem().withSelectable(false)).withName(R.string.app_name)).withTypeface(typeface), (IDrawerItem) ((PrimaryDrawerItem) ((PrimaryDrawerItem) ((PrimaryDrawerItem) ((PrimaryDrawerItem) new PrimaryDrawerItem().withSelectable(false)).withName("Gallery")).withIcon(R.drawable.ic_home_black_24dp)).withOnDrawerItemClickListener(new OnDrawerItemClickListener() {
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                if (MainActivity.this.placeholderLayout.getVisibility() == View.VISIBLE) {
                    MainActivity.this.placeholderLayout.setVisibility(View.GONE);
                    MainActivity.this.recyclerLayout.setVisibility(View.VISIBLE);
                    MainActivity.this.setUpRecyclerView();
                }
                return false;
            }
        })).withTypeface(typeface), (IDrawerItem) ((PrimaryDrawerItem) ((PrimaryDrawerItem) ((PrimaryDrawerItem) ((PrimaryDrawerItem) new PrimaryDrawerItem().withSelectable(false)).withName("Recommend to Friends")).withIcon(R.drawable.ic_share_black_24dp)).withOnDrawerItemClickListener(new OnDrawerItemClickListener() {
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                String shareappPackageName = MainActivity.this.getPackageName();
                Intent sendIntent = new Intent();
                sendIntent.setAction("android.intent.action.SEND");
                sendIntent.putExtra("android.intent.extra.TEXT", "Check out Instagram Save App at: https://play.google.com/store/apps/details?id=" + shareappPackageName);
                sendIntent.setType("text/plain");
                MainActivity.this.startActivity(sendIntent);
                return false;
            }
        })).withTypeface(typeface), (IDrawerItem) ((PrimaryDrawerItem) ((PrimaryDrawerItem) ((PrimaryDrawerItem) ((PrimaryDrawerItem) new PrimaryDrawerItem().withSelectable(false)).withName("Rate Us")).withIcon(R.drawable.ic_thumb_up_black_24dp)).withOnDrawerItemClickListener(new OnDrawerItemClickListener() {
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                String appPackageName = MainActivity.this.getPackageName();
                try {
                    MainActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + appPackageName)));
                } catch (ActivityNotFoundException e) {
                    MainActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                return false;
            }
        })).withTypeface(typeface), (IDrawerItem) ((PrimaryDrawerItem) ((PrimaryDrawerItem) ((PrimaryDrawerItem) ((PrimaryDrawerItem) new PrimaryDrawerItem().withSelectable(false)).withName("Settings")).withIcon(R.drawable.ic_settings_applications_black_24dp)).withOnDrawerItemClickListener(new OnDrawerItemClickListener() {
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                return false;
            }
        })).withTypeface(typeface), (IDrawerItem) ((PrimaryDrawerItem) ((PrimaryDrawerItem) ((PrimaryDrawerItem) ((PrimaryDrawerItem) new PrimaryDrawerItem().withSelectable(false)).withName("Feedback")).withIcon(R.drawable.ic_feedback_black_24dp)).withOnDrawerItemClickListener(new OnDrawerItemClickListener() {
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                DisplayMetrics displaymetrics = new DisplayMetrics();
                MainActivity.this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                int height = displaymetrics.heightPixels;
                int width = displaymetrics.widthPixels;
                PackageInfo info = null;
                try {
                    info = MainActivity.this.getApplicationContext().getPackageManager().getPackageInfo(MainActivity.this.getPackageName(), 0);
                } catch (NameNotFoundException e) {
                    e.printStackTrace();
                }
                String version = info.versionName;
                Intent i = new Intent("android.intent.action.SEND");
                i.setType("message/rfc822");
                i.putExtra("android.intent.extra.EMAIL", new String[]{MainActivity.this.getResources().getString(R.string.developer_email)});
                i.putExtra("android.intent.extra.SUBJECT", MainActivity.this.getResources().getString(R.string.app_name) + version);
                i.putExtra("android.intent.extra.TEXT", "\n Device :" + MainActivity.getDeviceName() + "\n System Version:" + VERSION.SDK_INT + "\n Display Height  :" + height + "px\n Display Width  :" + width + "px\n\nHave a problem? Please share it with us and we will do our best to solve it!\n");
                MainActivity.this.startActivity(Intent.createChooser(i, "Send Email"));
                return false;
            }
        })).withTypeface(typeface), (IDrawerItem) ((PrimaryDrawerItem) ((PrimaryDrawerItem) ((PrimaryDrawerItem) ((PrimaryDrawerItem) new PrimaryDrawerItem().withSelectable(false)).withName("All Gifs")).withIcon(R.drawable.ic_allgif)).withOnDrawerItemClickListener(new OnDrawerItemClickListener() {
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                try {
                    MainActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + "hb.me.giphy")));
                } catch (ActivityNotFoundException e) {
                    MainActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + "hb.me.giphy")));
                }
                return false;
            }
        })).withTypeface(typeface), (IDrawerItem) ((PrimaryDrawerItem) ((PrimaryDrawerItem) ((PrimaryDrawerItem) ((PrimaryDrawerItem) new PrimaryDrawerItem().withSelectable(false)).withName("Ielts Listening")).withIcon(R.drawable.ic_ielts)).withOnDrawerItemClickListener(new OnDrawerItemClickListener() {
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                try {
                    MainActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + "com.tlvanelearning.ielts")));
                } catch (ActivityNotFoundException e) {
                    MainActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + "com.tlvanelearning.ielts")));
                }
                return false;
            }
        })).withTypeface(typeface), (IDrawerItem) ((PrimaryDrawerItem) ((PrimaryDrawerItem) ((PrimaryDrawerItem) ((PrimaryDrawerItem) new PrimaryDrawerItem().withSelectable(false)).withName("Exit")).withIcon(R.drawable.ic_exit_to_app_black_24dp)).withOnDrawerItemClickListener(new OnDrawerItemClickListener() {
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                MainActivity.this.finish();
                return false;
            }
        })).withTypeface(typeface)).withSavedInstance(savedInstanceState).build();
        loadInterstitialAd();
    }

    private void loadInterstitialAd() {
        this.adRequestint = new AdRequest.Builder().addTestDevice("3F90C25D51474DDEA1C2B2319E1ADE19").build();
        this.mInterstitialAd = new InterstitialAd(getApplicationContext());
        this.mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_full_screen));
        this.mInterstitialAd.loadAd(this.adRequestint);
    }

    private void showInterstitial() {
        if (this.mInterstitialAd.isLoaded()) {
            this.mInterstitialAd.show();
        }
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return StringUtils.capitalize(model);
        }
        return StringUtils.capitalize(manufacturer) + StringUtils.SPACE + model;
    }

    private void setUpRecyclerView() {
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerViewAdapter = new RecyclerViewAdapter(this, getData());
        this.recyclerView.setAdapter(this.recyclerViewAdapter);
        this.recyclerViewAdapter.notifyDataSetChanged();
    }

    private ArrayList<Files> getData() {
        ArrayList<Files> filesList = new ArrayList();
        this.files = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.FOLDER_NAME).listFiles();
        if (this.files == null) {
            try {
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Arrays.sort(this.files, new Comparator() {
            public int compare(Object o1, Object o2) {
                if (((File) o1).lastModified() > ((File) o2).lastModified()) {
                    return -1;
                }
                if (((File) o1).lastModified() < ((File) o2).lastModified()) {
                    return 1;
                }
                return 0;
            }
        });
        for (int i = 0; i < this.files.length; i++) {
            File file = this.files[i];
            Files f = new Files();
            f.setName(StringUtils.substringBefore(file.getName(), "+"));
            f.setUri(Uri.fromFile(file));
            f.setFilename(this.files[i].getAbsolutePath());
            //fileN=this.files[i].getAbsolutePath();
            filesList.add(f);
        }
        return filesList;
    }

    private void getpostText(String textField) {
        if (Source.getURL(textField, "(http(s)?:\\/\\/(.+?\\.)?[^\\s\\.]+\\.[^\\s\\/]{1,9}(\\/[^\\s]+)?)").isEmpty()) {
            Log.d("incorrect", "Url is not Correct");
            return;
        }
        this.urlDesc = Source.getURL(textField, "(http(s)?:\\/\\/(.+?\\.)?[^\\s\\.]+\\.[^\\s\\/]{1,9}(\\/[^\\s]+)?)");
        AsyncHttpClient client = new AsyncHttpClient();
        client.setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.109 Safari/537.36");
        client.get(textField, new TextHttpResponseHandler() {
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                MainActivity.this.htmlResponse = responseString;
                Log.d("HtML: ", MainActivity.this.htmlResponse);
                try {
                    String starting = MainActivity.this.htmlResponse.substring(MainActivity.this.htmlResponse.indexOf("\"text\":") + 9);
                    MainActivity.this.desc = starting.substring(0, starting.indexOf("\"}}]}"));
                    MainActivity.this.resultz = StringEscapeUtils.unescapeJava(MainActivity.this.desc);
                    Log.d("End", MainActivity.this.resultz);
                    if (MainActivity.this.resultz.contains("created_at") || MainActivity.this.resultz.contains("href")) {
                        MainActivity.this.resultz = "";
                        MainActivity.this.copyTextBtn.setVisibility(View.GONE);
                    }
                    if (!MainActivity.this.resultz.contains("#")) {
                        MainActivity.this.copyHashtagsBtn.setVisibility(View.GONE);
                    }
                    try {
                        MainActivity.this.captionText.setText(MainActivity.this.resultz);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e2) {
                    MainActivity.this.desc = "";
                }
            }
        });
    }

    @TargetApi(16)
    public boolean checkPermission() {
        if (VERSION.SDK_INT < 23) {
            return true;
        }
        if (ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
            return true;
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.WRITE_EXTERNAL_STORAGE")) {
            Builder alertBuilder = new Builder(this);
            alertBuilder.setCancelable(true);
            alertBuilder.setTitle((CharSequence) "Permission necessary");
            alertBuilder.setMessage((CharSequence) "Write Storage permission is necessary to Download Images and Videos!!!");
            alertBuilder.setPositiveButton(android.R.string.yes, new OnClickListener() {
                @TargetApi(16)
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, MainActivity.MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
                }
            });
            alertBuilder.create().show();
            return false;
        }
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
        return false;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_STORAGE /*123*/:
                if (grantResults.length <= 0 || grantResults[0] != 0) {
                    checkAgain();
                    return;
                }
                checkFolder();
                if (checkForEmptyUrl(URL)) {
                    viewPageSource();
                    setUpRecyclerView();
                    return;
                }
                setUpRecyclerView();
                this.placeholderLayout.setVisibility(View.INVISIBLE);
                this.recyclerLayout.setVisibility(View.VISIBLE);
                return;
            default:
                return;
        }
    }

    public void checkAgain() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.WRITE_EXTERNAL_STORAGE")) {
            Builder alertBuilder = new Builder(this);
            alertBuilder.setCancelable(true);
            alertBuilder.setTitle((CharSequence) "Permission necessary");
            alertBuilder.setMessage((CharSequence) "Write Storage permission is necessary to Download Images and Videos!!!");
            alertBuilder.setPositiveButton(android.R.string.yes, new OnClickListener() {
                @TargetApi(16)
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, MainActivity.MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
                }
            });
            alertBuilder.create().show();
            return;
        }
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
    }

    public Boolean getAppIntro(Context context) {
        return Boolean.valueOf(context.getSharedPreferences(Constants.MyPREFERENCES, 0).getBoolean("AppIntro", true));
    }

    public void initComponents() {
        this.mainImageView = (ImageView) findViewById(R.id.imageDownloadedID);
        this.recyclerLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRecyclerView);
        this.placeholderLayout = (RelativeLayout) findViewById(R.id.relativeContent);
        this.playIcon = (ImageView) findViewById(R.id.playIcon);
        this.progressBar = (ProgressBar) findViewById(R.id.mProgressBar);
        this.recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        this.downloadPercentage = (TextView) findViewById(R.id.downloadPercent);
        this.dialogBuilder = new Builder(this);
        this.dialogBuilder.setView(getLayoutInflater().inflate(R.layout.download_dialog, null));
        this.dialog = this.dialogBuilder.create();
        this.mProgressDialog = new ProgressDialog(this);
        this.captionText = (TextView) findViewById(R.id.captionText);
        this.repost = (Button) findViewById(R.id.repostButton);
        this.copyHashtagsBtn = (Button) findViewById(R.id.copyHashtag);
        this.copyTextBtn = (Button) findViewById(R.id.copyTextButton);
        this.copyTextBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MainActivity.this.myClipboard = (ClipboardManager) MainActivity.this.getSystemService(CLIPBOARD_SERVICE);
                MainActivity.this.myClip = ClipData.newPlainText("text", MainActivity.this.captionText.getText().toString());
                MainActivity.this.myClipboard.setPrimaryClip(MainActivity.this.myClip);
                Toast.makeText(MainActivity.this.getApplicationContext(), "Text Copied", Toast.LENGTH_SHORT).show();
            }
        });
        this.mTextHashTagHelper = Creator.create(getResources().getColor(R.color.accent), null);
        this.mTextHashTagHelper.handle(this.captionText);
        this.copyHashtagsBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String tmpString2 = MainActivity.this.mTextHashTagHelper.getAllHashTags().toString().replace('[', '#').replace(']', ' ').replaceAll("\\s+", "").replaceAll(",", " #");
                Log.d("hashtags", tmpString2);
                ((ClipboardManager) MainActivity.this.getSystemService(CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("text label", tmpString2));
                Toast.makeText(MainActivity.this.getApplicationContext(), "Hashtags Copied", Toast.LENGTH_SHORT).show();
            }
        });
        this.repost.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    Uri mainUri = Uri.fromFile(new File(MainActivity.this.path));
                    Intent sharingIntent;
                    if (MainActivity.this.path.endsWith(".jpg")) {
                        sharingIntent = new Intent("android.intent.action.SEND");
                        sharingIntent.setType("image/*");
                        sharingIntent.putExtra("android.intent.extra.TEXT", "Downloaded using " + MainActivity.this.getResources().getString(R.string.app_name) + " android application");
                        sharingIntent.putExtra("android.intent.extra.STREAM", mainUri);
                        sharingIntent.addFlags(1);
                        sharingIntent.setPackage("com.instagram.android");
                        try {
                            MainActivity.this.startActivity(Intent.createChooser(sharingIntent, "Share Image using"));
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(MainActivity.this.getApplicationContext(), "No application found to open this file.", Toast.LENGTH_LONG).show();
                        }
                    } else if (MainActivity.this.path.endsWith(".mp4")) {
                        sharingIntent = new Intent("android.intent.action.SEND");
                        sharingIntent.setType("video/*");
                        sharingIntent.putExtra("android.intent.extra.TEXT", "Downloaded using " + MainActivity.this.getResources().getString(R.string.app_name) + " android application");
                        sharingIntent.putExtra("android.intent.extra.STREAM", mainUri);
                        sharingIntent.addFlags(1);
                        sharingIntent.setPackage("com.instagram.android");
                        try {
                            MainActivity.this.startActivity(Intent.createChooser(sharingIntent, "Share Video using"));
                        } catch (ActivityNotFoundException e2) {
                            Toast.makeText(MainActivity.this.getApplicationContext(), "No application found to open this file.", Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
            }
        });
        this.recyclerLayout.setOnRefreshListener(new OnRefreshListener() {
            public void onRefresh() {
                MainActivity.this.recyclerLayout.setRefreshing(true);
                MainActivity.this.setUpRecyclerView();
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        MainActivity.this.recyclerLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });
    }

    public void viewPageSource() {
        this.progressBar.setVisibility(View.VISIBLE);
        this.queue.add(new StringRequest(0, URL, new Listener<String>() {
            public void onResponse(String response) {
                Log.d("Response: ", response.toString());
                if (response != null) {
                    MainActivity.this.recyclerLayout.setVisibility(View.INVISIBLE);
                    MainActivity.this.placeholderLayout.setVisibility(View.VISIBLE);
                    MainActivity.this.htmlSource = response.toString();
                    MainActivity.this.videoUrl = Source.getURL(MainActivity.this.htmlSource, "property=\"og:video\" content=\"([^\"]+)\"");
                    MainActivity.this.imageUrl = Source.getURL(MainActivity.this.htmlSource, "property=\"og:image\" content=\"([^\"]+)\"");
                    MainActivity.this.description = Source.getURL(MainActivity.this.htmlSource, "property=\"og:description\" content=\"([^\"]+)\"");
                    Log.d("Image URl: ", MainActivity.this.imageUrl);
                    Log.d("Description: ", MainActivity.this.description);
                    Log.d("Video URL:", MainActivity.this.videoUrl);
                    try {
                        MainActivity.this.resultDesc = MainActivity.this.description.substring(MainActivity.this.description.indexOf("@") + 1, MainActivity.this.description.indexOf("Instagram") - 5);
                        Log.d("Profile Name: ", MainActivity.this.resultDesc);
                        if (MainActivity.this.resultDesc == null) {
                            MainActivity.this.resultDesc = "Profile Name";
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (MainActivity.this.videoUrl.isEmpty()) {
                        if (MainActivity.getPreference(MainActivity.this.getApplicationContext(), "link").equals(MainActivity.this.imageUrl)) {
                            MainActivity.this.recyclerLayout.setVisibility(View.VISIBLE);
                            MainActivity.this.placeholderLayout.setVisibility(View.INVISIBLE);
                            MainActivity.this.progressBar.setVisibility(View.INVISIBLE);
                            return;
                        }
                        MainActivity.this.progressBar.setVisibility(View.VISIBLE);
                        MainActivity.this.playIcon.setVisibility(View.INVISIBLE);
                        Glide.with(MainActivity.this.getApplicationContext()).load(MainActivity.this.imageUrl).into(MainActivity.this.mainImageView);
                        MainActivity.this.newDownload(MainActivity.this.imageUrl);
                        MainActivity.this.getpostText(MainActivity.URL);
                        MainActivity.setPreference(MainActivity.this.getApplicationContext(), "link", MainActivity.this.imageUrl);
                        return;
                    } else if (!MainActivity.this.videoUrl.endsWith(".mp4")) {
                        return;
                    } else {
                        if (MainActivity.getPreference(MainActivity.this.getApplicationContext(), "link").equals(MainActivity.this.videoUrl)) {
                            MainActivity.this.recyclerLayout.setVisibility(View.VISIBLE);
                            MainActivity.this.placeholderLayout.setVisibility(View.INVISIBLE);
                            MainActivity.this.progressBar.setVisibility(View.INVISIBLE);
                            return;
                        }
                        MainActivity.this.playIcon.setVisibility(View.VISIBLE);
                        MainActivity.this.progressBar.setVisibility(View.VISIBLE);
                        Glide.with(MainActivity.this.getApplicationContext()).load(MainActivity.this.imageUrl).into(MainActivity.this.mainImageView);
                        MainActivity.this.newDownload(MainActivity.this.videoUrl);
                        MainActivity.this.getpostText(MainActivity.URL);
                        MainActivity.setPreference(MainActivity.this.getApplicationContext(), "link", MainActivity.this.videoUrl);
                        return;
                    }
                }
                Toast.makeText(MainActivity.this, "Failed to Fetch Data", Toast.LENGTH_SHORT).show();
            }
        }, new ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Please Check If Your Url is Correct!", Toast.LENGTH_SHORT).show();
                MainActivity.this.progressBar.setVisibility(View.INVISIBLE);
                MainActivity.this.recyclerLayout.setVisibility(View.VISIBLE);
                MainActivity.this.placeholderLayout.setVisibility(View.INVISIBLE);
            }
        }));
    }

    private boolean checkForEmptyUrl(String url) {
        if (Pattern.compile("instagram.com").matcher(url).find()) {
            return true;
        }
        return false;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.ic_insta) {
            //Constant.showFAd(this);
            PackageManager pm = this.getPackageManager();
            boolean isInstalled = isPackageInstalled("com.instagram.android", pm);
            if(isInstalled) {
                startActivity(getPackageManager().getLaunchIntentForPackage("com.instagram.android"));
                finish();
            }else {
                Toast.makeText(getApplicationContext(),"Please Install Instagram", Toast.LENGTH_LONG).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }
    private boolean isPackageInstalled(String packagename, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packagename, 0);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }
    public void dwndImage(String url) {
        String fileName = url.substring(url.lastIndexOf(47) + 1);
        DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        Request request = new Request(Uri.parse(url));
        request.setAllowedNetworkTypes(3).setAllowedOverRoaming(false).setTitle("InstaKeep").setDescription("Downloading").setDestinationInExternalPublicDir("/InstaKeep", fileName);
        request.setNotificationVisibility(1);
        Toast.makeText(this, "Download Started", Toast.LENGTH_SHORT).show();
        dm.enqueue(request);
    }

    public void checkFolder() {
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/InstaKeep");
        boolean isDirectoryCreated = dir.exists();
        if (!isDirectoryCreated) {
            isDirectoryCreated = dir.mkdir();
        }
        if (isDirectoryCreated) {
            Log.d("Folder", "Already Created");
        }
    }

    public static boolean setPreference(Context context, String key, String value) {
        Editor editor = context.getSharedPreferences(PREFS_NAME, 0).edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public static String getPreference(Context context, String key) {
        return context.getSharedPreferences(PREFS_NAME, 0).getString(key, "defaultValue");
    }

    public void onBackPressed() {
        if (this.doubleBackToExitPressedOnce.booleanValue()) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = Boolean.valueOf(true);
        if (this.recyclerLayout.getVisibility() == View.VISIBLE) {
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        } else if (this.placeholderLayout.getVisibility() == View.VISIBLE) {
            this.placeholderLayout.setVisibility(View.GONE);
            this.recyclerLayout.setVisibility(View.VISIBLE);
            showInterstitial();
            setUpRecyclerView();
        }
        new Handler().postDelayed(new Runnable() {
            public void run() {
                MainActivity.this.doubleBackToExitPressedOnce = Boolean.valueOf(false);
            }
        }, 2000);
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    public void newDownload(String url) {
        this.mProgressDialog = new ProgressDialog(this);
        this.mProgressDialog.setMessage("Downloading..");
        this.mProgressDialog.setIndeterminate(true);
        this.mProgressDialog.setProgressStyle(1);
        this.mProgressDialog.setCancelable(false);
        new DownloadTask(this).execute(new String[]{url});
    }

    protected void onPause() {
        super.onPause();
        if (mBundleRecyclerViewState != null && this.recyclerView != null) {
            Parcelable listState = mBundleRecyclerViewState.getParcelable("recycler_state");
            if (this.recyclerView.getLayoutManager() != null) {
                this.recyclerView.getLayoutManager().onRestoreInstanceState(listState);
            }
        }
    }

    protected void onResume() {
        super.onResume();
        if (mBundleRecyclerViewState != null) {
            this.recyclerView.getLayoutManager().onRestoreInstanceState(mBundleRecyclerViewState.getParcelable("recycler_state"));
        }
    }
}
