package hb.me.homeworkout.gym.buttlegs.buttlegspro.home;

import android.content.Context;
import android.support.v4.app.Fragment;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.R;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.fragments.Challenge30DaysFragment;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.fragments.TrainAtHomeFragment;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.fragments.TreadmillFragment;

public class TabManager {
    public static Fragment get(int menuItemId, boolean isReselection, Context context) {
        switch (menuItemId) {
            case R.id.action_challenge:
                return new Challenge30DaysFragment();
            case R.id.action_treadmill:
                return new TreadmillFragment();
            default:
                return new TrainAtHomeFragment();
        }
    }
}
