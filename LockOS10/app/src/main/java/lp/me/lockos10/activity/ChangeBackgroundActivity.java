package lp.me.lockos10.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import lp.me.lockos10.R;
import lp.me.lockos10.adapter.AdapterGalery;
import lp.me.lockos10.entity.BackEntity;
import lp.me.lockos10.util.Blur;
import lp.me.lockos10.util.MyApplication;
import lp.me.lockos10.util.RealPathUtil;
import lp.me.lockos10.util.Utils;
import lp.me.lockos10.util.Values;

@SuppressLint({"InflateParams"})
public class ChangeBackgroundActivity extends Activity {
    private static final int RESULT_LOAD_IMAGE = 0;
    private int background;
    private Editor mEditor;
    private ViewGroup mViewGroup;
    private ViewHolderGetBackground mViewHolderGetBackground;
    private ArrayList<BackEntity> myListBackEntities = new ArrayList();
    private ArrayList<BackEntity> mySListBackEntities = new ArrayList();
    private SharedPreferences sharedpreference;

    class ViewHolderGetBackground {
        private Gallery galery_picture_background;
        private ImageView image_apply_choice_background;
        private ImageView kenburnsview_background_choice;
        private RelativeLayout layout_action_bottom_choice_background;
        private TextView text_choice_from_other;

        public ViewHolderGetBackground(ViewGroup mViewGroup) {
            bindView(mViewGroup);
        }

        private void bindView(ViewGroup mViewGroup) {
            this.kenburnsview_background_choice = (ImageView) mViewGroup.findViewById(R.id.kenburnsview_background_choice);
            this.layout_action_bottom_choice_background = (RelativeLayout) mViewGroup.findViewById(R.id.layout_action_bottom_choice_background);
            this.galery_picture_background = (Gallery) mViewGroup.findViewById(R.id.galery_picture_background);
            this.text_choice_from_other = (TextView) mViewGroup.findViewById(R.id.text_choice_from_other);
            this.image_apply_choice_background = (ImageView) mViewGroup.findViewById(R.id.image_apply_choice_background);
            getData();
        }

        private void getData() {
            ChangeBackgroundActivity.this.myListBackEntities.add(new BackEntity(R.drawable.b1));
            ChangeBackgroundActivity.this.myListBackEntities.add(new BackEntity(R.drawable.ic_wallpaper));
            ChangeBackgroundActivity.this.myListBackEntities.add(new BackEntity(R.drawable.b3));
            ChangeBackgroundActivity.this.myListBackEntities.add(new BackEntity(R.drawable.b4));
            ChangeBackgroundActivity.this.myListBackEntities.add(new BackEntity(R.drawable.b5));
            ChangeBackgroundActivity.this.myListBackEntities.add(new BackEntity(R.drawable.b6));
            ChangeBackgroundActivity.this.myListBackEntities.add(new BackEntity(R.drawable.b7));
            ChangeBackgroundActivity.this.myListBackEntities.add(new BackEntity(R.drawable.b8));
            ChangeBackgroundActivity.this.myListBackEntities.add(new BackEntity(R.drawable.b9));
            ChangeBackgroundActivity.this.myListBackEntities.add(new BackEntity(R.drawable.b10));
            ChangeBackgroundActivity.this.mySListBackEntities.add(new BackEntity(R.drawable.b1s));
            ChangeBackgroundActivity.this.mySListBackEntities.add(new BackEntity(R.drawable.ic_wallpaper));
            ChangeBackgroundActivity.this.mySListBackEntities.add(new BackEntity(R.drawable.b3s));
            ChangeBackgroundActivity.this.mySListBackEntities.add(new BackEntity(R.drawable.b4s));
            ChangeBackgroundActivity.this.mySListBackEntities.add(new BackEntity(R.drawable.b5s));
            ChangeBackgroundActivity.this.mySListBackEntities.add(new BackEntity(R.drawable.b6s));
            ChangeBackgroundActivity.this.mySListBackEntities.add(new BackEntity(R.drawable.b7s));
            ChangeBackgroundActivity.this.mySListBackEntities.add(new BackEntity(R.drawable.b8s));
            ChangeBackgroundActivity.this.mySListBackEntities.add(new BackEntity(R.drawable.b9s));
            ChangeBackgroundActivity.this.mySListBackEntities.add(new BackEntity(R.drawable.b10s));
        }

        private void setListener() {
            this.galery_picture_background.setAdapter(new AdapterGalery(ChangeBackgroundActivity.this, 0, ChangeBackgroundActivity.this.mySListBackEntities));
            loadImage();
            this.galery_picture_background.setSpacing(10);
            this.image_apply_choice_background.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    BackEntity mBackEntitySelected = (BackEntity) ChangeBackgroundActivity.this.myListBackEntities.get(ViewHolderGetBackground.this.galery_picture_background.getSelectedItemPosition());
                    ChangeBackgroundActivity.this.mEditor.putInt(Values.BACKGROUND_RESOURCE_ID, mBackEntitySelected.getResources());
                    ChangeBackgroundActivity.this.mEditor.putBoolean(Values.BACKGROUND_RESOUECE_BOLEAN, true);
                    ChangeBackgroundActivity.this.mEditor.commit();
                    MyApplication.blur = Blur.fastblur(ChangeBackgroundActivity.this, BitmapFactory.decodeResource(ChangeBackgroundActivity.this.getResources(), mBackEntitySelected.getResources()), 25);
                    ChangeBackgroundActivity.this.finish();
                    ChangeBackgroundActivity.this.sendBroadcast(new Intent("com.gppady.launcher.change"));
                    ChangeBackgroundActivity.this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            });
            this.text_choice_from_other.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Utils.getBackgroundEx(ChangeBackgroundActivity.this);
                }
            });
            this.galery_picture_background.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    ViewHolderGetBackground.this.kenburnsview_background_choice.setImageResource(((BackEntity) ChangeBackgroundActivity.this.myListBackEntities.get(position)).getResources());
                }
            });
        }

        private void loadImage() {
            ChangeBackgroundActivity.this.sharedpreference = PreferenceManager.getDefaultSharedPreferences(ChangeBackgroundActivity.this);
            ChangeBackgroundActivity.this.background = ChangeBackgroundActivity.this.sharedpreference.getInt(Values.BACKGROUND_RESOURCE_ID, 0);
            if (ChangeBackgroundActivity.this.background == 0) {
                this.kenburnsview_background_choice.setImageResource(R.drawable.b2);
            } else {
                this.kenburnsview_background_choice.setImageResource(ChangeBackgroundActivity.this.background);
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.sharedpreference = PreferenceManager.getDefaultSharedPreferences(this);
        this.mEditor = this.sharedpreference.edit();
        if (this.mViewGroup == null) {
            this.mViewGroup = (ViewGroup) LayoutInflater.from(getApplicationContext()).inflate(R.layout.paddy_activity_choice_background, null);
            this.mViewHolderGetBackground = new ViewHolderGetBackground(this.mViewGroup);
            this.mViewGroup.setTag(this.mViewHolderGetBackground);
        } else {
            this.mViewHolderGetBackground = (ViewHolderGetBackground) this.mViewGroup.getTag();
        }
        setContentView(this.mViewGroup);
        this.mViewHolderGetBackground.setListener();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Utils.PICK_IMAGE && resultCode == -1 && data != null) {
            String realPath = "";
            try {
                if (VERSION.SDK_INT < 19) {
                    realPath = RealPathUtil.getRealPathFromURI_API11to18(this, data.getData());
                } else {
                    try {
                        realPath = RealPathUtil.getRealPathFromURI_API19(this, data.getData());
                    } catch (Exception e) {
                        try {
                            realPath = RealPathUtil.getRealPathFromURI_API11to18(this, data.getData());
                        } catch (Exception e2) {
                        }
                    }
                }
            } catch (Exception e3) {
            }
            this.mEditor.putBoolean(Values.BACKGROUND_RESOUECE_BOLEAN, false);
            this.mEditor.putString(Values.BACKGROUND_URI, realPath);
            this.mEditor.commit();
            finish();
            MyApplication.blur = Blur.fastblur(this, ((BitmapDrawable) Drawable.createFromPath(realPath)).getBitmap(), 25);
        }
    }
}
