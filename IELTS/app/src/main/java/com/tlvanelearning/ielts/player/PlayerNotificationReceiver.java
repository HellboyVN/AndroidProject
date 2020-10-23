package com.tlvanelearning.ielts.player;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PlayerNotificationReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        String action = "";
        if (intent.hasExtra("ACTION")) {
            action = intent.getStringExtra("ACTION");
        }
        PlayerService playerService = PlayerService.getInstance();
        switch (action) {
            case "PLAY":
                if (playerService != null) {
                    try {
                        playerService.continueAudio();
                        playerService.updateNotification();
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                }
                break;
            case "PAUSE":
                if (playerService != null) {
                    try {
                        playerService.pauseAudio();
                        playerService.updateNotification();
                        return;
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        return;
                    }
                }
                break;
            case "NEXT":
                if (playerService != null) {
                    try {
                        playerService.nextAudio();
                        return;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        return;
                    }
                }
                break;
            case "PREVIOUS":
                if (playerService != null) {
                    try {
                        playerService.previousAudio();
                        return;
                    } catch (Exception ex2) {
                        ex2.printStackTrace();
                        return;
                    }
                }
                break;
            case "KILL":
                if (playerService != null) {
                    try {
                        playerService.kill();
                        return;
                    } catch (Exception e3) {
                        return;
                    }
                }
                break;
            default:
                break;
        }
    }
}
