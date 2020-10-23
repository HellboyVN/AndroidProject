package hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.utils;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog.Version;

import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.R;

public class DataPick {
    private static DataPick _instance;

    private DataPick() {
    }

    public static DataPick getInstance() {
        if (_instance == null) {
            _instance = new DataPick();
        }
        return _instance;
    }

    public void showDataPicker(AppCompatActivity activity, OnDateSetListener callback, int year, int monthOfYear, int dayOfMonth) {
        DatePickerDialog dpd = DatePickerDialog.newInstance(callback, year, monthOfYear, dayOfMonth);
        dpd.setVersion(Version.VERSION_1);
        dpd.setAccentColor(ContextCompat.getColor(activity, R.color.mdtp_accent_color_dark));
        dpd.show(activity.getFragmentManager(), ConsKeys.LIST_KEY);
    }
}
