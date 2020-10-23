package hb.me.homeworkout.gym.buttlegs.buttlegspro.reminder;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

//import com.google.firebase.perf.metrics.AppStartTrace;

import butterknife.BindView;
import butterknife.ButterKnife;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.R;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.base.BackBaseActivity;

public class ReminderActivity extends BackBaseActivity {
    @BindView(R.id.toolbarReminder)
    Toolbar toolbarReminder;

    protected void onResume() {
//        AppStartTrace.setLauncherActivityOnResumeTime("hb.me.homeworkout.gym.buttlegs.buttlegspro.reminder.ReminderActivity");
        super.onResume();
    }

    protected void onStart() {
//        AppStartTrace.setLauncherActivityOnStartTime("hb.me.homeworkout.gym.buttlegs.buttlegspro.reminder.ReminderActivity");
        super.onStart();
    }

    protected void onCreate(Bundle bundle) {
//        AppStartTrace.setLauncherActivityOnCreateTime("hb.me.homeworkout.gym.buttlegs.buttlegspro.reminder.ReminderActivity");
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_reminder);
        ButterKnife.bind((Activity) this);
        setSupportActionBar(this.toolbarReminder);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}
