package hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.finish;

import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.R;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.database.RealmManager;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.finish.model.Finish;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.home.tabs.tabfour.type.ChallengeDaysType;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.home.tabs.tabfour.type.ExerciseType;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.listeners.IRateClicked;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.models.LevelData;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.reminder.cards.TimePic;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.reminder.data.TimeData;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.reminder.listener.ITimeItemClik;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.utils.ConsKeys;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.utils.RestartAppModel;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.utils.SharedPrefsService;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

public class FinishActivity extends AppCompatActivity implements OnTimeSetListener, IRateClicked, ITimeItemClik {
    @BindView(R.id.finish)
    AppCompatTextView finish;
    private List<Finish> finishList;
    @BindView(R.id.finishToolbar)
    Toolbar finishToolbar;
    FinishRecycleAdapter mAdapter;
    LevelData mData;
    private ExerciseType mExType;
    @BindView(R.id.finishRecycler)
    RecyclerView mRecyclerView;
    private TimeData timeData;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_finish);
        ButterKnife.bind((Activity) this);
        SharedPrefsService.getInstance().setFirstTime(this, false);
        this.mData = (LevelData) getIntent().getSerializableExtra(ConsKeys.EXERCISE_DATA_KEY);
        this.mExType = ExerciseType.getByType(getIntent().getIntExtra(ConsKeys.EXERCISE_DATA_TYPE_KEY, 0));
        if (this.mData != null) {
            if (getIntent() != null && getIntent().hasExtra(ConsKeys.EXERCISE_DATA_KEY)) {
                RealmManager.getInstance().addWorkout(this, this.mData.getLevel().intValue(), this.mData.getDuration());
                RealmManager.getInstance().addExercise(this, this.mData.getExercisesForRealm(this));
                SharedPrefsService.getInstance().setLevelPassed(this, this.mData.getLevel().intValue());
            }
            int gender = SharedPrefsService.getInstance().getGender(this);
            if (this.mExType == ExerciseType.CHALLENGE) {
                ChallengeDaysType type = ChallengeDaysType.getByType(SharedPrefsService.getInstance().getChallengeType(this, gender));
                if (this.mData.getLevel().intValue() > SharedPrefsService.getInstance().getChallengeCurrentDay(this, type.getType(), gender)) {
                    SharedPrefsService.getInstance().setChallengeCurrentDay(this, this.mData.getLevel().intValue(), type.getType(), gender);
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
                            continue;
                        case 3:
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
        finish();
        RestartAppModel.getInstance().settingsChanged(true);
    }

    private void setupList(List<Finish> dataList) {
        this.mRecyclerView.setHasFixedSize(true);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.mAdapter = new FinishRecycleAdapter(getApplicationContext(), dataList, this, this);
        this.timeData = this.mAdapter.crateTimData();
        this.mRecyclerView.setAdapter(this.mAdapter);
    }

    public void rateClicked(int position) {
        this.mAdapter.removeAt(position);
    }

    public void timeChange(int position) {
        TimePic.getInstance().showTimePicker(this, this, this.timeData);
    }

    public void removeClick(int position) {
    }

    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        this.timeData.getTime().setHour(hourOfDay);
        this.timeData.getTime().setMinute(minute);
        this.mAdapter.chengeTimeData(this.timeData);
        this.mAdapter.notifyDataSetChanged();
    }
}
