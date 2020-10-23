package hb.me.homeworkout.gym.buttlegs.buttlegspro.reminder.cards;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog.Version;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.R;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.reminder.data.TimeData;

public class TimePicker {
    private static TimePicker _instance;

    private TimePicker() {
    }

    public static TimePicker getInstance() {
        if (_instance == null) {
            _instance = new TimePicker();
        }
        return _instance;
    }

    public void showTimePicker(AppCompatActivity activity, OnTimeSetListener callback, TimeData data) {
        TimePickerDialog tpd = TimePickerDialog.newInstance(callback, data.getTime().getHour(), data.getTime().getMinute(), 0, false);
        tpd.setVersion(Version.VERSION_1);
        tpd.setAccentColor(ContextCompat.getColor(activity, R.color.mdtp_accent_color_dark));
        tpd.show(activity.getFragmentManager(), "time");
    }
}
