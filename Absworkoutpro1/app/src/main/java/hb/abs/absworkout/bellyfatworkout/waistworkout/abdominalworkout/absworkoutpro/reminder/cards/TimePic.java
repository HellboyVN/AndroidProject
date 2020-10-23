package hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.reminder.cards;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog.Version;

import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.R;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.reminder.data.TimeData;

public class TimePic {
    private static TimePic _instance;

    private TimePic() {
    }

    public static TimePic getInstance() {
        if (_instance == null) {
            _instance = new TimePic();
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
