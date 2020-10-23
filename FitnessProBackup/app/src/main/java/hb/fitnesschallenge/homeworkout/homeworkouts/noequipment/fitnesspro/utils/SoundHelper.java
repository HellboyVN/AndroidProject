package hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

public class SoundHelper {
    private static SoundHelper INSTANCE = null;
    private final Context context;
    private final String packageName;

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

        public static NumberSounds getCountdouwnStart(int value) {
            if (value > 10) {
                return NUMBER_10;
            }
            if (value < 1) {
                return null;
            }
            return getByNumber(value);
        }

        public NumberSounds getPrevSound() {
            return getByNumber(this.number - 1);
        }
    }

    public static void initialize(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new SoundHelper(context);
        }
    }

    public static SoundHelper getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        throw new IllegalStateException("service not initialized");
    }

    private SoundHelper(Context context) {
        this.context = context;
        this.packageName = context.getApplicationInfo().packageName;
    }

    public String getString(String property) {
        int nameResourceID = this.context.getResources().getIdentifier(property, "string", this.packageName);
        return nameResourceID == 0 ? null : this.context.getString(nameResourceID);
    }

    public Integer getSound(String resource) {
        int nameResourceID = this.context.getResources().getIdentifier(resource, "raw", this.packageName);
        return nameResourceID == 0 ? null : Integer.valueOf(nameResourceID);
    }

    public void playSound(String resource) {
        MediaPlayer mp = MediaPlayer.create(this.context, getSound(resource).intValue());
        mp.setOnCompletionListener(new OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mp.reset();
                mp.release();
            }
        });
        mp.start();
    }
}
