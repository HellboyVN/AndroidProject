package hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.reminder;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.R;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.base.BackBaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ReminderActivity extends BackBaseActivity {
    @BindView(R.id.toolbarReminder)
    Toolbar toolbarReminder;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_remind);
        ButterKnife.bind((Activity) this);
        setSupportActionBar(this.toolbarReminder);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}
