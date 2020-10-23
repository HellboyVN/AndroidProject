package hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.option;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.R;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.home.MainActivity;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.utils.AnimationService;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.utils.SharedPrefsService;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.utils.TypeFaceService;

public class OptionsActivity extends AppCompatActivity {
    @BindView(R.id.beginWorkout)
    Button beginWorkout;
    @BindView(R.id.genderRGroup)
    RadioGroup genderRGroup;
    @BindView(R.id.rFemale)
    RadioButton rFemale;
    Typeface rLight;
    @BindView(R.id.rMale)
    RadioButton rMale;
    Typeface rRegular;
    @BindView(R.id.userImgBoy)
    AppCompatImageView userImgBoy;
    @BindView(R.id.userImgGirl)
    AppCompatImageView userImgGirl;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPrefsService.getInstance().setGender(this, 0);
        setContentView((int) R.layout.activity_launch);
        Log.e("hellboy","Oncreate Launch");
        ButterKnife.bind((Activity) this);
        initFonts();
        ArrayAdapter<String> adapter = new ArrayAdapter(this, 17367050, getResources().getStringArray(R.array.languages));
        this.beginWorkout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (OptionsActivity.this.genderRGroup.getCheckedRadioButtonId() == R.id.rMale) {
                    SharedPrefsService.getInstance().setGender(OptionsActivity.this, 1);
                } else {
                    SharedPrefsService.getInstance().setGender(OptionsActivity.this, 2);
                }
                OptionsActivity.this.startActivity(new Intent(OptionsActivity.this, MainActivity.class));
                OptionsActivity.this.finish();
            }
        });
        this.genderRGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rFemale:
                        OptionsActivity.this.setImage(false);
                        return;
                    case R.id.rMale:
                        OptionsActivity.this.setImage(true);
                        return;
                    default:
                        return;
                }
            }
        });
    }

    private void initFonts() {
        this.rLight = TypeFaceService.getInstance().getRobotoLight(this);
        this.rRegular = TypeFaceService.getInstance().getRobotoRegular(this);
        setFonts();
    }

    private void setFonts() {
        this.rFemale.setTypeface(this.rRegular);
        this.rMale.setTypeface(this.rRegular);
        this.beginWorkout.setTypeface(this.rRegular);
    }

    private void setImage(boolean isBoy) {
        Animation goRight;
        int i;
        int i2 = 0;
        AppCompatImageView appCompatImageView = this.userImgGirl;
        if (isBoy) {
            goRight = AnimationService.getInstance().getGoRight();
        } else {
            goRight = AnimationService.getInstance().getRight();
        }
        appCompatImageView.startAnimation(goRight);
        appCompatImageView = this.userImgGirl;
        if (isBoy) {
            i = 8;
        } else {
            i = 0;
        }
        appCompatImageView.setVisibility(i);
        appCompatImageView = this.userImgBoy;
        if (isBoy) {
            goRight = AnimationService.getInstance().getLeft();
        } else {
            goRight = AnimationService.getInstance().getGoLeft();
        }
        appCompatImageView.startAnimation(goRight);
        AppCompatImageView appCompatImageView2 = this.userImgBoy;
        if (!isBoy) {
            i2 = 8;
        }
        appCompatImageView2.setVisibility(i2);
    }

    protected void onPause() {
        super.onPause();
        this.userImgGirl.clearAnimation();
        this.userImgBoy.clearAnimation();
    }
}
