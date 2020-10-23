package lp.me.allgifs.activity;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.folderselector.FolderChooserDialog;
import com.afollestad.materialdialogs.folderselector.FolderChooserDialog.FolderCallback;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.body.StringBody;
import com.koushikdutta.ion.Ion;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.Drawer.OnDrawerItemClickListener;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import lp.me.allgifs.Admob;
import lp.me.allgifs.R;
import lp.me.allgifs.fragment.AboutFragment;
import lp.me.allgifs.fragment.ExploreFragment;
import lp.me.allgifs.fragment.FavoriteFragment;
import lp.me.allgifs.fragment.SettingsFragment;
import lp.me.allgifs.fragment.TrendingFragment;
import lp.me.allgifs.fragment.VarietyFragment;
import lp.me.allgifs.util.AppConstant;
import lp.me.allgifs.util.AppUtils;
import lp.me.allgifs.util.LogUtil;

public class AGActivity extends AppCompatActivity implements FolderCallback {
    private boolean mIsSelected;
    private int mCurrentItem = -1;
    private AccountHeader mHeaderResult = null;
    public Drawer mDrawer = null;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        printHashKey();
        initAdmob();
        buildHeader(false, savedInstanceState);
        mDrawer = new DrawerBuilder().withActivity(this).withToolbar(toolbar)
                .withAccountHeader(mHeaderResult)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(getString(R.string.sec_all)).withIcon(R.drawable.sec_trends).withIconTintingEnabled(true).withIdentifier(AppConstant.ID_TRENDING_ITEM),
                        new PrimaryDrawerItem().withName(getString(R.string.sec_explore)).withIcon(R.drawable.sec_actions).withIconTintingEnabled(true).withIdentifier(AppConstant.ID_EXPLORE_ITEM),
                        new PrimaryDrawerItem().withName(getString(R.string.sec_variety)).withIcon(R.drawable.sec_variety).withIconTintingEnabled(true).withIdentifier(AppConstant.ID_VARIETY_ITEM),
                        new PrimaryDrawerItem().withName(getString(R.string.sec_favorite)).withIcon(R.drawable.favorite_white).withIconTintingEnabled(true).withIdentifier(AppConstant.ID_FAVORITE_ITEM),
                        new PrimaryDrawerItem().withName(getString(R.string.settings)).withIcon(R.drawable.sec_settings).withIconTintingEnabled(true).withIdentifier(AppConstant.ID_SETTING_ITEM),
                        new PrimaryDrawerItem().withName(getString(R.string.sec_about)).withIcon(R.drawable.sec_about).withIconTintingEnabled(true).withIdentifier(AppConstant.ID_ABOUT_ITEM),
                        new PrimaryDrawerItem().withName(getString(R.string.sec_app_fbdownloader)).withIcon(R.drawable.ic_app_downloader).withIconTintingEnabled(true).withIdentifier(AppConstant.ID_APP_FB_DOWNLOADER),
                        new PrimaryDrawerItem().withName(getString(R.string.sec_app_fblite)).withIcon(R.drawable.ic_app_fb_lite).withIconTintingEnabled(true).withIdentifier(AppConstant.ID_APP_FB_LITE)
                ).withOnDrawerItemClickListener(new OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            mIsSelected = true;
                            int index = (int) drawerItem.getIdentifier();
                            switch (index) {
                                case AppConstant.ID_TRENDING_ITEM:
                                    AGActivity.this.switchFragment(AppConstant.ID_TRENDING_ITEM, AGActivity.this.getString(R.string.sec_all));
                                    break;
                                case AppConstant.ID_EXPLORE_ITEM:
                                    AGActivity.this.switchFragment(AppConstant.ID_EXPLORE_ITEM, AGActivity.this.getString(R.string.sec_explore));
                                    break;
                                case AppConstant.ID_VARIETY_ITEM:
                                    AGActivity.this.switchFragment(AppConstant.ID_VARIETY_ITEM, AGActivity.this.getString(R.string.sec_variety));
                                    break;
                                case AppConstant.ID_FAVORITE_ITEM:
                                    AGActivity.this.switchFragment(AppConstant.ID_FAVORITE_ITEM, AGActivity.this.getString(R.string.sec_favorite));
                                    break;
                                case AppConstant.ID_SETTING_ITEM:
                                    AGActivity.this.switchFragment(AppConstant.ID_SETTING_ITEM, AGActivity.this.getString(R.string.settings));
                                    break;
                                case AppConstant.ID_ABOUT_ITEM:
                                    AGActivity.this.switchFragment(AppConstant.ID_ABOUT_ITEM, AGActivity.this.getString(R.string.sec_about));
                                    break;
                                case AppConstant.ID_APP_FB_DOWNLOADER:
                                    try {
                                        startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=lp.me.videodownloader")));
                                    } catch (ActivityNotFoundException e3) {
                                        Toast.makeText(AGActivity.this, getResources().getString(R.string.market_error), Toast.LENGTH_SHORT).show();
                                    }
                                    if (mDrawer.isDrawerOpen()) {
                                        mDrawer.closeDrawer();
                                    }
                                    break;
                                case AppConstant.ID_APP_FB_LITE:
                                    try {
                                        startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=little.star.fblite")));
                                    } catch (ActivityNotFoundException e3) {
                                        Toast.makeText(AGActivity.this, getResources().getString(R.string.market_error), Toast.LENGTH_SHORT).show();
                                    }
                                    if (mDrawer.isDrawerOpen()) {
                                        mDrawer.closeDrawer();
                                    }
                                    break;
                            }
                        } else {
                            mIsSelected = false;
                        }
                        return mIsSelected;
                    }
                })
                .withSavedInstance(savedInstanceState).build();
        if (savedInstanceState == null) {
            mCurrentItem = -1;
            mDrawer.setSelection(1);
        }

    }

    private void buildHeader(boolean compact, Bundle savedInstanceState) {
        mHeaderResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.bg_profile)
                .withSelectionFirstLine(getString(R.string.app_name))
                .withSelectionSecondLine("Havinat Studio")
                .withCompactStyle(compact)
                .withTranslucentStatusBar(true)
                .withSavedInstance(savedInstanceState)
                .build();
    }

    public void switchFragment(int itemId, String title) {
        LogUtil.d("Switch FRAGMENT.....");
        if (mCurrentItem != itemId) {
            Fragment fragment;
            mCurrentItem = itemId;
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(title);
            }

            switch (itemId) {
                case AppConstant.ID_TRENDING_ITEM:
                    fragment = new TrendingFragment();
                    break;
                case AppConstant.ID_EXPLORE_ITEM:
                    fragment = new ExploreFragment();
                    break;
                case AppConstant.ID_VARIETY_ITEM:
                    fragment = new VarietyFragment();
                    break;
                case AppConstant.ID_FAVORITE_ITEM:
                    fragment = new FavoriteFragment();
                    break;
                case AppConstant.ID_SETTING_ITEM:
                    fragment = new SettingsFragment();
                    break;
                case AppConstant.ID_ABOUT_ITEM:
                    fragment = new AboutFragment();
                    break;
                default:
                    fragment = new TrendingFragment();
                    break;
            }
            if (mDrawer.isDrawerOpen()) {
                mDrawer.closeDrawer();
            }


            final Fragment keyFragment = fragment;
            Runnable mPendingRunnable = new Runnable() {
                @Override
                public void run() {
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                            .replace(R.id.main, keyFragment).commitAllowingStateLoss();
                }
            };
            if(admob!= null){
                if (AppUtils.isMoreLessShowAd()) {
                    admob.showInterstitialAd(() -> {
                                new Handler().postDelayed(mPendingRunnable, 300);
                            }
                    );
                } else {
                    new Handler().postDelayed(mPendingRunnable, 300);
                }
            }
        }
    }

    public void onBackPressed() {
        if (mDrawer != null && mDrawer.isDrawerOpen()) {
            mDrawer.closeDrawer();
        } else if (this.mDrawer != null && this.mCurrentItem != 1) {
            mDrawer.setSelection(1);
        } else if (this.mDrawer != null) {
            super.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setQueryHint(getString(R.string.search_actionbar));
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        initSearchView(searchView);
        return super.onCreateOptionsMenu(menu);
    }

    void initSearchView(final SearchView searchView) {
        final StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        final CursorAdapter suggestionAdapter = new SimpleCursorAdapter(this,
                R.layout.item_search_suggestion,
                null,
                new String[]{SearchManager.SUGGEST_COLUMN_TEXT_1},
                new int[]{R.id.text1},
                0);
        final List<String> suggestions = new ArrayList<>();
        searchView.setSuggestionsAdapter(suggestionAdapter);
        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                Log.d("LAMPARD", "Click on item: " + position + " value: " + suggestions.size());
                if (position < suggestions.size()) {
                    String tag = suggestions.get(position);
                    tag = tag.substring(1, tag.length() - 1);
                    Log.d("LAMPARD", "CLICK TAG VALUEL " + tag);
                    searchView.setQuery(tag, true);
                    searchView.clearFocus();
                }
                return true;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText.trim())) {
                    String url = "https://api.tenor.com/v1/search_suggestions?tag=" + newText.trim() + "&key=5BTCZNSMAV3J&limit=10";
                    LogUtil.d("Request API to suggestion...." + newText);
                    Future<JsonObject> jsonTask = Ion.with(AGActivity.this).load(url).asJsonObject();
                    jsonTask.setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            if (e == null) {
                                suggestions.clear();
                                Log.d("LEGEND", "Data is: " + result.toString());
                                for (int i = 0; i < result.getAsJsonArray("results").size(); i++) {
                                    suggestions.add(result.getAsJsonArray("results").get(i).toString());
                                }
                                //update mData
                                if (suggestions.size() > 0) {
                                    final MatrixCursor cursor = new MatrixCursor(new String[]{BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1});
                                    int i = 0;
                                    for (String temp : suggestions) {
                                        cursor.addRow(new Object[]{i, temp});
                                        i++;
                                    }
                                    suggestionAdapter.changeCursor(cursor);
                                }
                            }
                        }
                    });

                }
                return true;
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_share) {
            Intent sharingIntent = new Intent("android.intent.action.SEND");
            sharingIntent.setType(StringBody.CONTENT_TYPE);
            sharingIntent.putExtra("android.intent.extra.SUBJECT", getResources().getString(R.string.app_name));
            sharingIntent.putExtra("android.intent.extra.TEXT", getResources().getString(R.string.share_disc) + "\n" + "https://play.google.com/store/apps/details?id=lp.me.allgifs");
            startActivity(sharingIntent);
        } else if (id == R.id.action_rate) {
            try {
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=lp.me.allgifs")));
            } catch (Exception e) {
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=lp.me.allgifs")));
            }
        }
        if (id == R.id.action_search || super.onOptionsItemSelected(item)) {
            return true;
        }
        return false;
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(mHeaderResult.saveInstanceState(mDrawer.saveInstanceState(outState)));
    }

    public void onFolderSelection(@NonNull FolderChooserDialog dialog, @NonNull File folder) {
        Editor path = getSharedPreferences("settings_data", 0).edit();
        path.putString("folder_chooser", folder.getPath());
        path.apply();
        Toast.makeText(this, getString(R.string.folder_chooser_change) + folder.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFolderChooserDismissed(@NonNull FolderChooserDialog dialog) {

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != KeyEvent.KEYCODE_BACK) {
            return super.onKeyDown(keyCode, event);
        }
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(getString(R.string.app_name));
        alert.setIcon(R.mipmap.ic_launcher);
        alert.setMessage("Are You Sure You Want To Quit?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                finish();
            }
        });
        alert.setNegativeButton("Rate App", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String appName = getPackageName();
                try {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + appName)));
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=" + appName)));
                }
            }
        });
        alert.show();
        return true;
    }

    Admob admob;
    InterstitialAd interstitialAd;
    void initAdmob() {
        admob = new Admob(this);
        admob.showInterstitialAd(null);
        admob.adBanner((LinearLayout) findViewById(R.id.ad_linear));
        showFAd();

    }

    void showFAd() {
        interstitialAd = new InterstitialAd(this, "634855396706847_707949006064152");
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
                interstitialAd.show();
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



    @Override
    public void onPause() {
        if (admob != null) {
            admob.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (admob != null) {
            admob.resume();
        }
    }


    @Override
    public void onDestroy() {
        if (admob != null) {
            admob.destroy();
        }
        if(interstitialAd != null){
            interstitialAd.destroy();
        }
        super.onDestroy();
    }
    public void printHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("lp.me.allgifs", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                LogUtil.d("printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            LogUtil.d("printHashKey()" + e.getMessage());
        } catch (Exception e) {
            LogUtil.d( "printHashKey()" + e.getMessage());
        }
    }
}
