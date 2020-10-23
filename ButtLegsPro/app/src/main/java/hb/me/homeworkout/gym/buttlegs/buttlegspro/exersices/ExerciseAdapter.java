package hb.me.homeworkout.gym.buttlegs.buttlegspro.exersices;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.models.Exercise;
import java.util.List;

public class ExerciseAdapter extends FragmentPagerAdapter {
    List<Exercise> exersiceList;
    int level;

    public ExerciseAdapter(FragmentManager fm, List<Exercise> exersiceList, int level) {
        super(fm);
        this.exersiceList = exersiceList;
        this.level = level;
    }

    public int getCount() {
        return this.exersiceList.size();
    }

    public Fragment getItem(int position) {
        Log.d("Adapter Page Count", position + "");
        int realPos = position;
        return ExerciseFragment.newInstance(realPos, realPos == getCount() + -1, (Exercise) this.exersiceList.get(position), this.level);
    }
}
