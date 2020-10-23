package hb.me.homeworkout.gym.buttlegs.buttlegspro.finish;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.R;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.ads.listener.IAdCardClicked;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.finish.model.Finish;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.type.ChallengeDaysType;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.type.ExerciseType;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.listeners.IRateClicked;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.models.LevelData;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.reminder.cards.TimePicker;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.reminder.data.TimeData;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.reminder.listener.ITimeItemClick;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.ConsKeys;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.SharedPrefsService;

//import com.google.firebase.perf.metrics.AppStartTrace;

public class FinishActivity extends AppCompatActivity implements OnTimeSetListener, IAdCardClicked, ITimeItemClick, IRateClicked {
    @BindView(R.id.finish)
    AppCompatTextView finish;
    List<Finish> finishList;
    @BindView(R.id.finishToolbar)
    Toolbar finishToolbar;
    FinishRecycleAdapter mAdapter;
    LevelData mData;
    private ExerciseType mExType;
    @BindView(R.id.finishRecycler)
    RecyclerView mRecyclerView;
    private TimeData timeData;

    protected void onResume() {
//        AppStartTrace.setLauncherActivityOnResumeTime("hb.me.homeworkout.gym.buttlegs.buttlegspro.finish.FinishActivity");
        super.onResume();
    }

    protected void onStart() {
//        AppStartTrace.setLauncherActivityOnStartTime("hb.me.homeworkout.gym.buttlegs.buttlegspro.finish.FinishActivity");
        super.onStart();
    }

    protected void onCreate(Bundle bundle) {
//        AppStartTrace.setLauncherActivityOnCreateTime("hb.me.homeworkout.gym.buttlegs.buttlegspro.finish.FinishActivity");
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_finish);
        ButterKnife.bind((Activity) this);
        SharedPrefsService.getInstance().setFirstTime(this, false);
        this.mData = (LevelData) getIntent().getSerializableExtra(ConsKeys.EXERCISE_DATA_KEY);
        this.mExType = ExerciseType.getByType(getIntent().getIntExtra(ConsKeys.EXERCISE_DATA_TYPE_KEY, 0));
        if (this.mData != null) {
            if (getIntent() != null && getIntent().hasExtra(ConsKeys.EXERCISE_DATA_KEY)) {
                SharedPrefsService.getInstance().setLevelPassed(this, this.mData.getLevel().intValue());
            }
            if (this.mExType == ExerciseType.CHALLENGE) {
                ChallengeDaysType type = ChallengeDaysType.getByType(SharedPrefsService.getInstance().getChallengeType(this));
                if (this.mData.getLevel().intValue() > SharedPrefsService.getInstance().getChallengeCurrentDay(this, type.getType())) {
                    SharedPrefsService.getInstance().setChallengeCurrentDay(this, this.mData.getLevel().intValue(), type.getType());
                }
            }
        }
        try {
            this.finishList = (List) new Gson().fromJson(new InputStreamReader(getAssets().open("json/finish.json")), new TypeToken<List<Finish>>() {
            }.getType());
            Iterator<Finish> iter = this.finishList.iterator();
            while (iter.hasNext()) {
                Finish next = (Finish) iter.next();
                if (next != null) {
                    switch (next.getViewType().intValue()) {
                        case 0:
                            if (this.mData == null) {
                                break;
                            }
                            next.setDuration(this.mData.getDurationFormatted());
                            next.setLevel(this.mData.getLevel().intValue());
                            next.setExType(this.mExType);
                            break;
                        case 1:
                            if (!SharedPrefsService.getInstance().getRatedStatus(this)) {
                                break;
                            }
                            iter.remove();
                            break;
                        case 2:
                            if (!SharedPrefsService.getInstance().isHaveAlarm(this)) {
                                break;
                            }
                            iter.remove();
                            break;
                        default:
                            break;
                    }
                }
            }
            setupList(this.finishList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.finish.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                FinishActivity.this.doFinish();
            }
        });
    }

    public void onBackPressed() {
        doFinish();
    }

    private void doFinish() {
        //RestartAppModel.getInstance().settingsChanged(true);
        finish();
    }

    private void setupList(List<Finish> dataList) {
        this.mRecyclerView.setHasFixedSize(true);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.mAdapter = new FinishRecycleAdapter(this, dataList, this, this, this);
        this.timeData = this.mAdapter.crateTimData();
        this.mRecyclerView.setAdapter(this.mAdapter);
    }

    public void removeAdClicked(int position) {
        this.mAdapter.removeAtPosition(position);
    }

    public void onTimeChange(int position) {
        TimePicker.getInstance().showTimePicker(this, this, this.timeData);
    }

    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        this.timeData.getTime().setHour(hourOfDay);
        this.timeData.getTime().setMinute(minute);
        this.mAdapter.changeTimeData(this.timeData);
        this.mAdapter.notifyDataSetChanged();
    }

    public void rateClicked(int position) {
        this.mAdapter.removeAtPosition(position);
    }
}
