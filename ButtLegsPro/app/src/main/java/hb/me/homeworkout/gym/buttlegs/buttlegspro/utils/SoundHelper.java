package hb.me.homeworkout.gym.buttlegs.buttlegspro.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

public class SoundHelper {
    private static SoundHelper INSTANCE = null;

    public enum NumberSounds {
        NUMBER_1(1),
        NUMBER_2(2),
        NUMBER_3(3),
        NUMBER_4(4),
        NUMBER_5(5),
        NUMBER_6(6),
        NUMBER_7(7),
        NUMBER_8(8),
        NUMBER_9(9),
        NUMBER_10(10),
        NUMBER_15(15),
        NUMBER_20(20),
        NUMBER_25(25),
        NUMBER_30(30),
        NUMBER_40(40),
        NUMBER_50(50);
        
        int number;

        private NumberSounds(int number) {
            this.number = number;
        }

        public int getNumber() {
            return this.number;
        }

        public String getResource() {
            return "number_" + this.number;
        }

        private static NumberSounds getByNumber(int number) {
            for (NumberSounds numberSounds : values()) {
                if (numberSounds.getNumber() == number) {
                    return numberSounds;
                }
            }
            return null;
        }

        public static NumberSounds findNearest(int value) {
            if (value <= 10) {
                return null;
            }
            int diff = Integer.MAX_VALUE;
            for (NumberSounds numberSounds : values()) {
                if (Math.abs(numberSounds.getNumber() - value) < Math.abs(diff)) {
                    diff = numberSounds.getNumber() - value;
                }
            }
            return getByNumber(diff + value);
        }

        public static NumberSounds getCountdouwnStart(Context context, int value) {
            int cdStart = SharedPrefsService.getInstance().getCDVoiceStart(context);
            if (value > 10) {
                value = 10;
            }
            if (value < 1 || cdStart == 0) {
                return null;
            }
            if (value > cdStart) {
                value = cdStart;
            }
            return getByNumber(value);
        }

        public NumberSounds getPrevSound() {
            return getByNumber(this.number - 1);
        }
    }

    public static SoundHelper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SoundHelper();
        }
        return INSTANCE;
    }

    private SoundHelper() {
    }

    public String getString(Context context, String property) {
        int nameResourceID = context.getResources().getIdentifier(property, "string", context.getApplicationInfo().packageName);
        return nameResourceID == 0 ? null : context.getString(nameResourceID);
    }

    public Integer getSound(Context context, String resource) {
        int nameResourceID = context.getResources().getIdentifier(resource, "raw", context.getApplicationInfo().packageName);
        return nameResourceID == 0 ? null : Integer.valueOf(nameResourceID);
    }

    public void playSound(Context context, String resource) {
        MediaPlayer mp = MediaPlayer.create(context, getSound(context, resource).intValue());
        if (mp != null) {
            mp.setOnCompletionListener(new OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    mp.release();
                }
            });
            mp.start();
        }
    }
}
