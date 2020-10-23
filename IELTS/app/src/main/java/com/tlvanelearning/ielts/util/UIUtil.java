package com.tlvanelearning.ielts.util;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.widget.ImageView;

import com.tlvanelearning.ielts.R;

public class UIUtil {

    public interface UIUtilCallback {
        void onCallback(int i);
    }

    public static void changeBackgroundView(View button, int id) {
        try {
            button.setBackgroundResource(id);
        } catch (Exception e) {
        }
    }

    public static String toTimeAudio(long time) {
        long aux = time / 1000;
        int minutes = (int) (aux / 60);
        int seconds = (int) (aux % 60);
        return String.valueOf((minutes < 10 ? "0" + minutes : minutes + "") + ":" + (seconds < 10 ? "0" + seconds : seconds + ""));
    }

    public static void changeImageSource(ImageView button, int id) {
        button.setImageResource(id);
    }

    public static AlertDialog createDialog(Context context, String title, String message, final UIUtilCallback callback) {
        Builder builder = new Builder(context);
        builder.setMessage(message).setTitle(title);
        builder.setPositiveButton(R.string.again, new OnClickListener() {
            public void onClick(DialogInterface dialog, int position) {
                if (callback != null) {
                    callback.onCallback(position);
                }
            }
        });
        builder.setNegativeButton(R.string.answers, new OnClickListener() {
            public void onClick(DialogInterface dialog, int position) {
                if (callback != null) {
                    callback.onCallback(position);
                }
            }
        });
        builder.setNeutralButton("OK", new OnClickListener() {
            public void onClick(DialogInterface dialog, int position) {
                dialog.dismiss();
            }
        });
        return builder.create();
    }
}
