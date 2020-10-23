package hb.me.makemoneyonline;


import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.google.android.gms.ads.formats.NativeAd;
import com.turkialkhateeb.materialcolorpicker.ColorChooserDialog;
import com.turkialkhateeb.materialcolorpicker.ColorListener;

import hb.me.makemoneyonline.Utils.Constant;
import hb.me.makemoneyonline.Utils.Methods;
import hb.me.makemoneyonline.tip.IntroActivity;


public class SettingsFragment extends Fragment {
    int appColor;
    int appTheme;
    SharedPreferences app_preferences;
    Button button;
    Constant constant;
    SharedPreferences.Editor editor;
    LinearLayout llHelp;
    LinearLayout llprivacyPolicy;
    Methods methods;
    SharedPreferences sharedPreferences;
    int themeColor;
    private NativeAd nativeAd;
    private LinearLayout nativeAdContainer;
    private LinearLayout adView;
    LinearLayout adsView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        this.app_preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        this.appColor = this.app_preferences.getInt("color", 0);
        this.appTheme = this.app_preferences.getInt("theme", 0);
        this.themeColor = this.appColor;

//        if (this.themeColor == 0) {
//            setTheme(Constant.theme);
//        } else if (this.appTheme == 0) {
//            setTheme(Constant.theme);
//        } else {
//            view.setTheme(this.appTheme);
//        }

        adsView =(LinearLayout)view.findViewById(R.id.native_ad_admob_1);
        //showNativeAd();
        this.llHelp = (LinearLayout) view.findViewById(R.id.llhelp);
        this.llprivacyPolicy = (LinearLayout) view.findViewById(R.id.llprivacypolicy);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar_setting);
        toolbar.setTitle((CharSequence) "Settings");
        toolbar.setBackgroundColor(PreferenceManager.getDefaultSharedPreferences(getActivity()).getInt("color",Constant.color));
        toolbar.setTitleTextColor(-1);
        getContext().getTheme().applyStyle(Constant.theme, true);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.methods = new Methods();
        this.button = (Button) view.findViewById(R.id.button_color);
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        this.editor = this.sharedPreferences.edit();
        colorize();
        this.button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ColorChooserDialog dialog = new ColorChooserDialog(getActivity());
                dialog.setTitle("Select");
                dialog.setColorListener(new ColorListener() {
                    public void OnColorClick(View v, int color) {
                        colorize();
                        Constant.color = color;
                        methods.setColorTheme();
                        editor.putInt("color", Constant.color);
                        editor.putInt("theme", Constant.theme);
                        editor.commit();
                        getContext().getTheme().applyStyle(Constant.theme, true);
                        Intent intent = new Intent(getActivity(), MoneyActivity.class);
                        intent.setFlags(67108864);
                        startActivity(intent);
                    }
                });
                dialog.show();
            }
        });
        this.llHelp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               startActivity(new Intent(getActivity(), IntroActivity.class));
                //Toast.makeText(getActivity(),"haha",Toast.LENGTH_SHORT).show();
            }
        });
        this.llprivacyPolicy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextView textView = (TextView) new AlertDialog.Builder(getActivity()).setTitle((CharSequence) "PRIVACY POLICY").setMessage((int) R.string.privacy_message).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setIcon((int) R.drawable.ic_info_black_24dp).show().findViewById(android.R.id.message);
                textView.setScroller(new Scroller(getActivity()));
                textView.setVerticalScrollBarEnabled(true);
                textView.setMovementMethod(new ScrollingMovementMethod());
            }
        });
        return view;
    }
    @TargetApi(16)
    private void colorize() {
        ShapeDrawable d = new ShapeDrawable(new OvalShape());
        d.setBounds(58, 58, 58, 58);
        d.getPaint().setStyle(Paint.Style.FILL);
        d.getPaint().setColor(PreferenceManager.getDefaultSharedPreferences(getActivity()).getInt("color",Constant.color));
        this.button.setBackground(d);
    }
}
