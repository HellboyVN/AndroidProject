package hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.home;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.github.ksoichiro.android.observablescrollview.CacheFragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.R;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.home.model.Tabs;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.home.tabs.tabfour.Challenge30DaysFragment;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.home.tabs.tabone.LevelsFragment;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.home.tabs.tabtwo.MoreFragment;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.reminder.ReminderFragment;

public class TabsNavigationAdapter extends CacheFragmentStatePagerAdapter {
    private Context mContext;
    private int mScrollY;
    private List<Tabs> tabsList = new ArrayList();
    final int[] TabIcon =
            {R.drawable.icon_level,
             R.drawable.icon_bodyindex,
             R.drawable.icon_graph,
             R.drawable.icon_21days
            };

    public TabsNavigationAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.mContext = context;

        Tabs tabLevel = new Tabs();
        tabLevel.setName(context.getString(R.string.level));
        tabLevel.setImageResource(TabIcon[0]);

        Tabs tabRemind = new Tabs();
        tabRemind.setName(context.getString(R.string.reminder));
        tabRemind.setImageResource(TabIcon[1]);

        Tabs tabStats = new Tabs();
        tabStats.setName(context.getString(R.string.stast));
        tabStats.setImageResource(TabIcon[2]);

        Tabs tabChaleng = new Tabs();
        tabChaleng.setName(context.getString(R.string.challenge));
        tabChaleng.setImageResource(TabIcon[3]);
        this.tabsList.add(tabLevel);
        this.tabsList.add(tabChaleng);
        this.tabsList.add(tabStats);
        this.tabsList.add(tabRemind);
    }

    public void update(List<Tabs> tabsList) {
        this.tabsList = tabsList;
        notifyDataSetChanged();
    }

    public void setScrollY(int scrollY) {
        this.mScrollY = scrollY;
    }

    protected Fragment createItem(int position) {
        Fragment f = null;
        Bundle args;
        switch (position % 4) {
            case 0:
                f = new LevelsFragment();
                args = new Bundle();
                if (this.mScrollY > 0) {
                    args.putInt("ARG_INITIAL_POSITION", 1);
                }
                f.setArguments(args);
                break;
            case 1:
                f = new Challenge30DaysFragment();
                args = new Bundle();
                if (this.mScrollY > 0) {
                    args.putInt("ARG_INITIAL_POSITION", 1);
                }
                f.setArguments(args);
                break;
            case 2:
                f = new MoreFragment();
                args = new Bundle();
                if (this.mScrollY > 0) {
                    args.putInt("ARG_INITIAL_POSITION", 1);
                }
                f.setArguments(args);
                break;
            case 3:

                f = new ReminderFragment();
                args = new Bundle();
                if (this.mScrollY > 0) {
                    args.putInt("ARG_INITIAL_POSITION", 1);
                }
                f.setArguments(args);
                break;
        }
        return f;
    }

    public int getCount() {
        return this.tabsList.size();
    }

    public CharSequence getPageTitle(int position) {
        return ((Tabs) this.tabsList.get(position)).getName();
    }
    public final int getTabIcon(int position){
        return ((Tabs) this.tabsList.get(position)).getImageResource();
    }

    public int getItemPosition(Object object) {
        return -2;
    }
}
