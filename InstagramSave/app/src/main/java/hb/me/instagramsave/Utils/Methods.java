package hb.me.instagramsave.Utils;

import android.support.v4.view.ViewCompat;

import hb.me.instagramsave.R;


public class Methods {
    public void setColorTheme() {
        switch (Constant.color) {
            case ViewCompat.MEASURED_STATE_MASK /*-16777216*/:
                Constant.theme = R.style.AppTheme_black;
                return;
            case -16738680:
                Constant.theme = R.style.AppTheme_teal;
                return;
            case -16728876:
                Constant.theme = R.style.AppTheme_cyan;
                return;
            case -16537100:
                Constant.theme = R.style.AppTheme_skyblue;
                return;
            case -14575885:
                Constant.theme = R.style.AppTheme_bluee;
                return;
            case -12627531:
                Constant.theme = R.style.AppTheme_blue;
                return;
            case -11751600:
                Constant.theme = R.style.AppTheme_green;
                return;
            case -10453621:
                Constant.theme = R.style.AppTheme_gray;
                return;
            case -10011977:
                Constant.theme = R.style.AppTheme_violet;
                return;
            case -8825528:
                Constant.theme = R.style.AppTheme_brown;
                return;
            case -7617718:
                Constant.theme = R.style.AppTheme_lgreen;
                return;
            case -6543440:
                Constant.theme = R.style.AppTheme_darpink;
                return;
            case -6381922:
                Constant.theme = R.style.AppTheme_grey;
                return;
            case -3285959:
                Constant.theme = R.style.AppTheme_lime;
                return;
            case -1499549:
                Constant.theme = R.style.AppTheme_pink;
                return;
            case -769226:
                Constant.theme = R.style.AppTheme_red;
                return;
            case -43230:
                Constant.theme = R.style.AppTheme_dorange;
                return;
            case -26624:
                Constant.theme = R.style.AppTheme_orange;
                return;
            case -16121:
                Constant.theme = R.style.AppTheme_amber;
                return;
            case -5317:
                Constant.theme = R.style.AppTheme_yellow;
                return;
            default:
                Constant.theme = R.style.AppTheme;
                return;
        }
    }
}
