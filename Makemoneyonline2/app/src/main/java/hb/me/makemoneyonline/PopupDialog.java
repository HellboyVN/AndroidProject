package hb.me.makemoneyonline;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by nxtruong on 8/15/2017.
 */

public class PopupDialog extends Dialog implements View.OnClickListener {

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu_popup_edit:
                menuPopupLisenter.onActionOne();
                break;
            case R.id.menu_popup_delete:
                menuPopupLisenter.onActionTwo();
                break;
        }
    }

    public interface OnMenuPopup {
        void onActionTwo();

        void onActionOne();
    }

    OnMenuPopup menuPopupLisenter;

    public PopupDialog(Context context, OnMenuPopup menuPopupLisenter, String text1, String text2) {
        super(context);
        this.menuPopupLisenter = menuPopupLisenter;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_popup);
        TextView textView1 = (TextView) findViewById(R.id.menu_popup_edit);
        textView1.setText(text1);
        textView1.setOnClickListener(this);
        TextView textView2 = (TextView) findViewById(R.id.menu_popup_delete);
        textView2.setOnClickListener(this);
        textView2.setText(text2);
    }

    public void showDialog() {
        LogUtils.d( "Show dialog....");
        setCancelable(true);
        show();
    }

    public void showDropDownAtView(View v) {
        int[] loc_int = new int[2];
        try {
            v.getLocationOnScreen(loc_int);
        } catch (NullPointerException npe) {
            //Happens when the view doesn't exist on screen anymore.
        }
        Rect location = new Rect();
        location.left = loc_int[0];
        location.top = loc_int[1];

        WindowManager.LayoutParams window = this.getWindow().getAttributes();
        window.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.gravity = Gravity.TOP | Gravity.RIGHT;
        //  window.x = location.left;    //x position
        window.x = location.right;    //x position
        window.y = location.top;
        showDialog();
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public int getWidthMetric() {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

}
