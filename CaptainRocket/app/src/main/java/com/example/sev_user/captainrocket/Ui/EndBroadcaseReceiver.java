package com.example.sev_user.captainrocket.Ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by sev_user on 8/11/2016.
 */
public class EndBroadcaseReceiver extends BroadcastReceiver {

    private static boolean isEndGame = false;

    @Override
    public void onReceive(Context context, Intent intent) {
//        Toast.makeText(context, "Receiver end game", Toast.LENGTH_SHORT).show();
        isEndGame = true;
    }

    public boolean endGame() {
        return this.isEndGame;
    }
}
