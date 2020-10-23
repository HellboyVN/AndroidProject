package lp.me.allgifs.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import lp.me.allgifs.Admob;
import lp.me.allgifs.R;
import lp.me.allgifs.fragment.CategoryFragment;
import lp.me.allgifs.util.AppConstant;
import lp.me.allgifs.util.LogUtil;

public class CategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        String categoryName = getIntent().getStringExtra(AppConstant.BUNDLE_CATEGORY_NAME);
        getSupportActionBar().setTitle(categoryName);
        int categoryId = getCategoryIdByName(categoryName);
        LogUtil.d("Select category ID: " + categoryId);
        initAdmob();
        Bundle bundle = new Bundle();
        bundle.putInt(AppConstant.BUNDLE_CATEGORY_ID, categoryId);
        Fragment fragment = new CategoryFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.category_act, fragment).commit();


    }

    int getCategoryIdByName(String name) {
        String[] listSections = getResources().getStringArray(R.array.listExplore);
        if (listSections != null && listSections.length > 0) {
            for (int i = 0; i < listSections.length; i++) {
                if (listSections[i].equals(name)) {
                    return i + 1;
                }
            }
        }
        return 1;
    }



    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return false;
    }


    Admob admob;

    void initAdmob() {
        admob = new Admob(this);
        admob.showInterstitialAd(null);
        admob.adBanner((LinearLayout) findViewById(R.id.ad_linear));
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
