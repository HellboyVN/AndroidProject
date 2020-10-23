package hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.utils;

import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.R;
import android.content.Context;
import com.afollestad.materialdialogs.MaterialDialog.Builder;
import com.afollestad.materialdialogs.MaterialDialog.ListCallbackSingleChoice;
import java.util.ArrayList;
import java.util.Collection;

public class WeightMetricDialog {
    private static WeightMetricDialog _instance;

    private WeightMetricDialog() {
    }

    public static WeightMetricDialog getInstance() {
        if (_instance == null) {
            _instance = new WeightMetricDialog();
        }
        return _instance;
    }

    public void show(Context context, ListCallbackSingleChoice callbackSingleChoice) {
        int position = SharedPrefsService.getInstance().getWeightMetric(context);
        Collection weightList = new ArrayList();
        weightList.add(context.getString(R.string.kg).toUpperCase());
        weightList.add(context.getString(R.string.lb).toUpperCase());
        new Builder(context).title((int) R.string.weight_metrics).items(weightList).itemsCallbackSingleChoice(position, callbackSingleChoice).positiveText((int) R.string.setting_ok).negativeText((int) R.string.setting_cancel).show();
    }
}
