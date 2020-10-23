package hb.me.homeworkout.gym.buttlegs.buttlegspro.statistic;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

//import com.google.firebase.perf.metrics.AppStartTrace;

import butterknife.BindView;
import butterknife.ButterKnife;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.R;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.base.BackBaseActivity;

public class StatisticActivity extends BackBaseActivity {
    @BindView(R.id.toolbarStatistic)
    Toolbar toolbarStatistic;

    protected void onResume() {
//        AppStartTrace.setLauncherActivityOnResumeTime("hb.me.homeworkout.gym.buttlegs.buttlegspro.statistic.StatisticActivity");
        super.onResume();
    }

    protected void onStart() {
//        AppStartTrace.setLauncherActivityOnStartTime("hb.me.homeworkout.gym.buttlegs.buttlegspro.statistic.StatisticActivity");
        super.onStart();
    }

    protected void onCreate(Bundle bundle) {
//        AppStartTrace.setLauncherActivityOnCreateTime("hb.me.homeworkout.gym.buttlegs.buttlegspro.statistic.StatisticActivity");
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_statistic);
        ButterKnife.bind((Activity) this);
        setSupportActionBar(this.toolbarStatistic);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}
