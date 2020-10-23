package com.workout.workout.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import com.workout.workout.fragment.FavouriteFragment;
import com.workout.workout.fragment.MoreFragment;
import com.workout.workout.fragment.PlanFragment;
import com.workout.workout.fragment.TrainingFragment;
import com.workout.workout.fragment.UtilityFragment;
import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList();

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return TrainingFragment.newInstance();
            case 1:
                return PlanFragment.newInstance();
            case 2:
                return UtilityFragment.newInstance();
            case 3:
                return FavouriteFragment.newInstance();
            case 4:
                return MoreFragment.newInstance();
            default:
                Log.w("ViewPagerAdapter", "Invalid position " + position);
                return null;
        }
    }

    public int getCount() {
        return 5;
    }

    public void addFragment(Fragment fragment) {
        this.mFragmentList.add(fragment);
    }
}
