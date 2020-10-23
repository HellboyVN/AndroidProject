package hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.reminder;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.R;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.base.BackBaseActivity;
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
