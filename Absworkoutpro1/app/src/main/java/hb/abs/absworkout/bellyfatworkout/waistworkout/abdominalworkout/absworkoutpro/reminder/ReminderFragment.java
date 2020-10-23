package hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.reminder;

import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.R;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.home.MainActivity;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.home.tabs.BaseFragment;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.reminder.adapter.RecyclerViewEmptySupport;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.reminder.adapter.ReminderRecyclerAdapter;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.reminder.cards.TimePic;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.reminder.data.Time;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.reminder.data.TimeData;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.reminder.listener.ITimeItemClik;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.reminder.receivers.AlarmUtil;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.utils.SharedPrefsService;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.utils.TypeFaceService;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class ReminderFragment extends BaseFragment implements OnTimeSetListener, ITimeItemClik {
    public static final String ARG_INITIAL_POSITION = "ARG_INITIAL_POSITION";
    private MainActivity activity;
    private ReminderRecyclerAdapter adapter;
    private List<TimeData> dataList;
    @BindView(R.id.emptyLayout)
    public LinearLayout emptyLayout;
    @BindView(R.id.floatingActionButton)
    FloatingActionButton floatingActionButton;
    private View headerView;
    @BindView(R.id.scrollable)
    public RecyclerViewEmptySupport mRecyclerView;
    private int positionList;
    private TimeData timeData;
    @BindView(R.id.btn_male)
    public RadioButton btn_male;
    @BindView(R.id.btn_female)
    public RadioButton btn_female;
    @BindView(R.id.ed_weight)
    public EditText edWeight;
    @BindView(R.id.ed_height)
    public EditText edHeight;
    @BindView(R.id.seekbar_bmi)
    public SeekBar seekbarBmi;
    @BindView(R.id.result_bmi)
    public TextView resultBmi;
    @BindView(R.id.btn_caculate)
    public ImageButton btncaculate;
    @BindView(R.id.radiogroup)
    public RadioGroup radioGroup;
    @BindView(R.id.text_weight)
    public TextView tvWeight;
    @BindView(R.id.text_height)
    public TextView tvHeight;
    Float weight = 0.0f;Float height = 0.0f;

    void newReminder() {
        this.timeData = new TimeData();
        List<Boolean> listDay = new ArrayList();
        for (int i = 0; i < 7; i++) {
            listDay.add(Boolean.valueOf(false));
        }
        listDay.set(Calendar.getInstance().get(7) - 1, Boolean.valueOf(true));
        this.timeData.setTime(new Time(Calendar.getInstance().get(11), Calendar.getInstance().get(12)));
        this.timeData.setListDay(listDay);
        this.positionList = -2;
        TimePic.getInstance().showTimePicker(this.activity, this, this.timeData);
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_remind, container, false);
        ButterKnife.bind((Object) this, view);
        this.headerView = LayoutInflater.from(getActivity()).inflate(R.layout.padding, null);
//        this.emptyTextResult.setTypeface(TypeFaceService.getInstance().getRobotoMedium(this.activity));
//        this.emptyText.setTypeface(TypeFaceService.getInstance().getRobotoRegular(this.activity));
//        this.emptyText.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                ReminderFragment.this.newReminder();
//            }
//        });
        this.floatingActionButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ReminderFragment.this.newReminder();
            }
        });
        this.dataList = new ArrayList();
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        this.mRecyclerView.setHasFixedSize(true);
        this.mRecyclerView.setEmptyView(this.emptyLayout);
        this.adapter = new ReminderRecyclerAdapter(this.activity.getApplicationContext(), this.dataList, this.headerView, this);
        this.mRecyclerView.setAdapter(this.adapter);
        if (SharedPrefsService.getInstance().getReminderData(inflater.getContext()) != null) {
            for (TimeData data : SharedPrefsService.getInstance().getReminderData(inflater.getContext())) {
                this.dataList.add(data);
            }
            this.adapter.notifyDataSetChanged();
        }
        if (this.activity instanceof ObservableScrollViewCallbacks) {
            Bundle args = getArguments();
            if (args != null && args.containsKey("ARG_INITIAL_POSITION")) {
                final int initialPosition = args.getInt("ARG_INITIAL_POSITION", 0);
                ScrollUtils.addOnGlobalLayoutListener(this.mRecyclerView, new Runnable() {
                    public void run() {
                        ReminderFragment.this.mRecyclerView.scrollVerticallyToPosition(initialPosition);
                    }
                });
            }
            this.mRecyclerView.setTouchInterceptionViewGroup((ViewGroup) this.activity.findViewById(R.id.root));
            this.mRecyclerView.setScrollViewCallbacks(this.activity);
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()){
                    case R.id.btn_male:
                        tvWeight.setText("Weight (Kg)");
                        tvHeight.setText("Height (cm)");
                        break;
                    case R.id.btn_female:
                        tvWeight.setText("Weight (lbs)");
                        tvHeight.setText("Height (in)");
                        break;
                }
            }
        });

        btncaculate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                weight = Float.valueOf(edWeight.getText().toString());
                height = Float.valueOf(edHeight.getText().toString());
                float result = 0;
                if (btn_male.isChecked()){
                    btn_female.setChecked(false);
                    result = BmiIndex(weight,height,1);
                    resultBmi.setText(String.format("%.1f", result)+"/40");
                    seekbarBmi.setProgress((int)result);
                }else if( btn_female.isChecked()){
                    btn_male.setChecked(false);
                    result = BmiIndex(weight,height,0);
                    resultBmi.setText(String.format("%.1f", result)+"/40");
                    seekbarBmi.setProgress((int)result);
                }
            }
        });

        return view;
    }
    public float BmiIndex(float w, float h,int gender){
        if(gender == 0/*female*/){
         return (float) 703*w/(h*h);
        }else{
         return (float) w/(h*h/10000);
        }
    }
    public void onPause() {
        super.onPause();
        SharedPrefsService.getInstance().saveReminderData(this.activity.getApplicationContext(), this.dataList);
        AlarmUtil.getInstance().updateAlarm(this.activity.getApplicationContext());
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.activity = (MainActivity) context;
        }
    }

    public void updateAdapter() {
        Collections.sort(this.dataList);
        this.adapter.notifyDataSetChanged();
    }

    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        this.timeData.getTime().setHour(hourOfDay);
        this.timeData.getTime().setMinute(minute);
        if (this.positionList == -2) {
            this.timeData.setActive(true);
            this.dataList.add(this.timeData);
        } else {
            this.dataList.set(this.positionList, this.timeData);
        }
        updateAdapter();
    }

    public void timeChange(int position) {
        int realPosition = 0;
        if (position > 0) {
            realPosition = position - 1;
        }
        this.positionList = realPosition;
        this.timeData = (TimeData) this.dataList.get(realPosition);
        TimePic.getInstance().showTimePicker(this.activity, this, this.timeData);
    }

    public void removeClick(int position) {
        int realPosition = 0;
        if (position > 0) {
            realPosition = position - 1;
        }
        this.dataList.remove(realPosition);
        this.adapter.notifyItemRemoved(realPosition);
        updateAdapter();
    }
}
