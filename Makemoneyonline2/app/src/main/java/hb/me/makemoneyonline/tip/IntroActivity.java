package hb.me.makemoneyonline.tip;

import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

import hb.me.makemoneyonline.R;
import hb.me.makemoneyonline.Utils.Constant;


public class IntroActivity extends AppIntro {
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addSlide(AppIntroFragment.newInstance("STEP 1", "All the basic knowledge about MMO can be found here.", R.drawable.intro1, Color.parseColor("#ffa700")));
        addSlide(AppIntroFragment.newInstance("STEP 2", "You cant depend on any body but YOURSELF. Fight for your dream NOW.", R.drawable.intro2, Color.parseColor("#4caf50")));
        showSkipButton(false);
    }

    public void onSkipPressed(Fragment currentFragment) {
    }

    public void onDonePressed(Fragment currentFragment) {
        Editor editor = getSharedPreferences(Constant.MyPREFERENCES, 0).edit();
        editor.putBoolean("AppIntro", false);
        editor.commit();
        finish();
    }

    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
    }
}
