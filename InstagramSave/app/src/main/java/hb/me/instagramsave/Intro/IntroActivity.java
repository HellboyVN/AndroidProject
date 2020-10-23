package hb.me.instagramsave.Intro;

import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

import hb.me.instagramsave.R;
import hb.me.instagramsave.Utils.Constants;

public class IntroActivity extends AppIntro {
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addSlide(AppIntroFragment.newInstance("STEP 1", "Tap to open instagram.", R.drawable.instaintro1, Color.parseColor("#ffa700")));
        addSlide(AppIntroFragment.newInstance("STEP 2", "Tap on more option icon over instagram post.", R.drawable.instaintro2, Color.parseColor("#4caf50")));
        addSlide(AppIntroFragment.newInstance("STEP 3", "Tap on 'Copy Link' (or 'Copy Share URL') option.\n NOTE : If users profile is private 'Copy share URL' option is not available", R.drawable.instaintro3, Color.parseColor("#fe5757")));
        addSlide(AppIntroFragment.newInstance("STEP 4", "Yeah! Photo or Video is Automatically Saved to your device.", R.drawable.instaintro4, Color.parseColor("#9c27b0")));
        showSkipButton(false);
    }

    public void onSkipPressed(Fragment currentFragment) {
    }

    public void onDonePressed(Fragment currentFragment) {
        Editor editor = getSharedPreferences(Constants.MyPREFERENCES, 0).edit();
        editor.putBoolean("AppIntro", false);
        editor.commit();
        finish();
    }

    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
    }
}
