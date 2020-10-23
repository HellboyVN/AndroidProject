package hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.views;

import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.R;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.utils.SharedPrefsService;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.utils.TypeFaceService;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetTimeWeeksView extends LinearLayout implements OnClickListener {
    Context context;
    List<Boolean> selectedDays;
    Map<String, Boolean> weekDaysMap;
    int weekStartPosition;

    public SetTimeWeeksView(Context context) {
        super(context);
        init(context);
    }

    public SetTimeWeeksView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SetTimeWeeksView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        int i;
        this.context = context;
        this.weekDaysMap = new HashMap();
        String[] weekDaysArray = context.getResources().getStringArray(R.array.week_days_short);
        this.weekStartPosition = SharedPrefsService.getInstance().getWeekStart(context);
        if (this.weekStartPosition == 0) {
            String tmp = weekDaysArray[weekDaysArray.length - 1];
            for (i = weekDaysArray.length - 1; i > 0; i--) {
                weekDaysArray[i] = weekDaysArray[i - 1];
            }
            weekDaysArray[0] = tmp;
        }
        for (i = 0; i < weekDaysArray.length; i++) {
            this.weekDaysMap.put(weekDaysArray[i], Boolean.valueOf(false));
            addWeekDayView(weekDaysArray[i], i);
        }
    }

    private void addWeekDayView(String name, int index) {
        Button weekDayBtn = (Button) LayoutInflater.from(this.context).inflate(R.layout.btn_set_week, null);
        weekDayBtn.setId(index);
        boolean isSelected = ((Boolean) this.weekDaysMap.get(name)).booleanValue();
        weekDayBtn.setTag(Boolean.valueOf(Boolean.parseBoolean(isSelected + "")));
        weekDayBtn.setOnClickListener(this);
        weekDayBtn.setText(name);
        weekDayBtn.setTypeface(TypeFaceService.getInstance().getRobotoRegular(this.context));
        addView(weekDayBtn);
        ((LayoutParams) weekDayBtn.getLayoutParams()).weight = 1.0f;
        updateButtonState(weekDayBtn, isSelected);
    }

    public void onClick(View v) {
        Button btn = (Button) v;
        boolean isSelected = !((Boolean) btn.getTag()).booleanValue();
        btn.setTag(Boolean.valueOf(isSelected));
        updateButtonState(btn, isSelected);
        int index = btn.getId();
        if (this.weekStartPosition == 1) {
            index = (index + 1) % 7;
        }
        this.selectedDays.set(index, Boolean.valueOf(isSelected));
    }

    private void updateButtonState(Button btn, boolean isSelected) {
        btn.setBackgroundColor(ContextCompat.getColor(this.context, isSelected ? R.color.mdtp_accent_color_dark : R.color.mdtp_accent_color));
        btn.setTextColor(ContextCompat.getColor(this.context, isSelected ? R.color.white : R.color.on_color));
    }

    public void setInitialData(List<Boolean> selectedDays) {
        this.selectedDays = selectedDays;
        for (int i = 0; i < selectedDays.size(); i++) {
            int offset = 0;
            if (this.weekStartPosition == 1) {
                if (i == 0) {
                    offset = 6;
                } else {
                    offset = -1;
                }
            }
            Button btn = (Button) findViewById(i + offset);
            btn.setTag(selectedDays.get(i));
            updateButtonState(btn, ((Boolean) selectedDays.get(i)).booleanValue());
        }
    }
}
