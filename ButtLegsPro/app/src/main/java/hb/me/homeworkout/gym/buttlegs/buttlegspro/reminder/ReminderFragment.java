package hb.me.homeworkout.gym.buttlegs.buttlegspro.reminder;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.support.v7.widget.helper.ItemTouchHelper.SimpleCallback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.R;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.reminder.adapter.ReminderRecyclerAdapter;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.reminder.cards.TimePicker;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.reminder.data.Time;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.reminder.data.TimeData;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.reminder.listener.ITimeItemClick;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.reminder.receivers.AlarmUtil;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.SharedPrefsService;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.TypeFaceService;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class ReminderFragment extends Fragment implements OnTimeSetListener, ITimeItemClick {
    private ReminderActivity activity;
    private ReminderRecyclerAdapter adapter;
    private List<TimeData> dataList;
    @BindView(R.id.emptyText)
    public TextView emptyText;
    @BindView(R.id.reminder_list)
    public EmptySupportRecyclerView mRecyclerView;
    private int positionList;
    private TimeData timeData;

    @RequiresApi(api = 23)
    @OnClick({R.id.floatingActionButton})
    void userClicked(FloatingActionButton floatingActionButton) {
        this.timeData = new TimeData();
        List<Boolean> listDay = new ArrayList();
        for (int i = 0; i < 7; i++) {
            listDay.add(Boolean.valueOf(false));
        }
        listDay.set(Calendar.getInstance().get(7) - 1, Boolean.valueOf(true));
        this.timeData.setTime(new Time(Calendar.getInstance().get(11), Calendar.getInstance().get(12)));
        this.timeData.setListDay(listDay);
        this.positionList = -1;
        TimePicker.getInstance().showTimePicker(this.activity, this, this.timeData);
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_remind, container, false);
        ButterKnife.bind((Object) this, view);
        this.emptyText.setTypeface(TypeFaceService.getInstance().getRobotoRegular(this.activity));
        this.dataList = new ArrayList();
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        this.mRecyclerView.setHasFixedSize(true);
        this.mRecyclerView.setEmptyView(this.emptyText);
        this.adapter = new ReminderRecyclerAdapter(this.activity.getApplicationContext(), this.dataList, this);
        this.mRecyclerView.setAdapter(this.adapter);
        if (SharedPrefsService.getInstance().getReminderData(inflater.getContext()) != null) {
            for (TimeData data : SharedPrefsService.getInstance().getReminderData(inflater.getContext())) {
                this.dataList.add(data);
            }
            this.adapter.notifyDataSetChanged();
        }
        itemRemoveToSwiped();
        return view;
    }

    public void onPause() {
        super.onPause();
        SharedPrefsService.getInstance().seveReminderData(this.activity.getApplicationContext(), this.dataList);
        AlarmUtil.getInstance().updateAlarm(this.activity.getApplicationContext());
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.activity = (ReminderActivity) context;
        }
    }

    void itemRemoveToSwiped() {
        new ItemTouchHelper(new SimpleCallback(12, 12) {
            public boolean onMove(RecyclerView recyclerView, ViewHolder viewHolder, ViewHolder target) {
                return false;
            }

            public void onSwiped(ViewHolder viewHolder, int direction) {
                ReminderFragment.this.dataList.remove(viewHolder.getAdapterPosition());
                ReminderFragment.this.adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                ReminderFragment.this.adapterRefres();
            }
        }).attachToRecyclerView(this.mRecyclerView);
    }

    public void adapterRefres() {
        Collections.sort(this.dataList);
        this.adapter.notifyDataSetChanged();
    }

    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        this.timeData.getTime().setHour(hourOfDay);
        this.timeData.getTime().setMinute(minute);
        if (this.positionList == -1) {
            this.timeData.setActive(true);
            this.dataList.add(this.timeData);
        } else {
            this.dataList.set(this.positionList, this.timeData);
        }
        adapterRefres();
    }

    public void onTimeChange(int position) {
        this.positionList = position;
        this.timeData = (TimeData) this.dataList.get(position);
        TimePicker.getInstance().showTimePicker(this.activity, this, this.timeData);
    }
}
