package hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.home.tabs.listener;

import android.view.View;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener;

public interface IWeightChang {
    void weightCalendarClick(OnDateSetListener onDateSetListener, int i, int i2, int i3);

    void weightEditTextFocus(View view);

    void weightMetricChange();
}
