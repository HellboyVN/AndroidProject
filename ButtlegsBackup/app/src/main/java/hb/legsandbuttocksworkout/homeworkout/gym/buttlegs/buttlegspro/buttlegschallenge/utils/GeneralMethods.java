package hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.utils;

import android.view.View;
import com.balysv.materialripple.MaterialRippleLayout;

public class GeneralMethods {
    public static void rippleEfect(View view) {
        MaterialRippleLayout.on(view).rippleDuration(600).rippleAlpha(0.1f).rippleOverlay(true).rippleColor(-1).create();
    }
}
