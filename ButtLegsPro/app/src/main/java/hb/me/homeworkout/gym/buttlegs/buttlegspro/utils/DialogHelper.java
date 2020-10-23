package hb.me.homeworkout.gym.buttlegs.buttlegspro.utils;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.Builder;
import com.afollestad.materialdialogs.MaterialDialog.ListCallbackSingleChoice;
import com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.R;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.database.RealmManager;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.database.modelweight.UserWeight;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.listeners.EventCenter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

public class DialogHelper {
    private static DialogHelper _instance;

    private DialogHelper() {
    }

    public static DialogHelper getInstance() {
        if (_instance == null) {
            _instance = new DialogHelper();
        }
        return _instance;
    }

    public void showWeightInsertDialog(final Activity context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_add_weight_dialog, null);
        final TextView dateTextView = (TextView) view.findViewById(R.id.weightDate);
        final EditText weightEditView = (EditText) view.findViewById(R.id.weight);
        final TextView weightUnitTextView = (TextView) view.findViewById(R.id.weightUnit);
        int weightMetric = RealmManager.getInstance().getUser().getWeightUnit();
        dateTextView.setText(getUnderlineContent(getDateString(Calendar.getInstance().getTimeInMillis(), context)));
        final Calendar calendar = Calendar.getInstance();
        UserWeight userWeight = RealmManager.getInstance().getWeightForTime(calendar.getTimeInMillis());
        if (userWeight != null) {
            weightEditView.setText(userWeight.getWeight(weightMetric) + "");
        }
        Builder builder = new Builder(context);
        builder.customView(view, true).positiveText((int) R.string.setting_set).negativeText((int) R.string.setting_cancel).autoDismiss(false).onPositive(new SingleButtonCallback() {
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                float weight = 0.0f;
                if (!weightEditView.getText().toString().equals("")) {
                    weight = Float.parseFloat(weightEditView.getText().toString());
                }
                if (weight <= 30.0f || weight >= 400.0f) {
                    Toast.makeText(context, context.getString(R.string.note_valid_weight), 0).show();
                    return;
                }
                RealmManager.getInstance().addWeightRecord(weight, calendar.getTimeInMillis());
                dialog.dismiss();
                EventCenter.getInstance().notifyWeightMetricChanged(RealmManager.getInstance().getUser().getWeightUnit());
            }
        }).onNegative(new SingleButtonCallback() {
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
            }
        });
        MaterialDialog dialog = builder.build();
        if (dialog != null) {
            dialog.getWindow().clearFlags(131080);
            dialog.getWindow().setSoftInputMode(4);
            dialog.show();
        }
        weightUnitTextView.setText(getUnderlineContent(weightMetric == 0 ? context.getString(R.string.kg) : context.getString(R.string.lb)));
        weightUnitTextView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                final int currentMetric = RealmManager.getInstance().getUser().getWeightUnit();
                DialogHelper.this.showWeightMetricDialog(context, new ListCallbackSingleChoice() {
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        RealmManager.getInstance().changeUserWeightUnit(which);
                        EventCenter.getInstance().notifyWeightMetricChanged(which);
                        weightUnitTextView.setText(DialogHelper.this.getUnderlineContent(which == 0 ? context.getString(R.string.kg) : context.getString(R.string.lb)));
                        if (!(currentMetric == which || weightEditView.getText().toString().equals(""))) {
                            float weight = Float.parseFloat(weightEditView.getText().toString());
                            weightEditView.setText((which == 0 ? MetricHelper.getInstance().convertLBtoKG(weight) : MetricHelper.getInstance().convertKGtoLB(weight)) + "");
                        }
                        return true;
                    }
                }, RealmManager.getInstance().getUser().getWeightUnit());
            }
        });
        final Activity activity = context;
        dateTextView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                DataPick.getInstance().showDataPicker((AppCompatActivity) activity, new OnDateSetListener() {
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(1, year);
                        calendar.set(2, monthOfYear);
                        calendar.set(5, dayOfMonth);
                        UserWeight userWeight = RealmManager.getInstance().getWeightForTime(calendar.getTimeInMillis());
                        if (userWeight != null) {
                            weightEditView.setText(userWeight.getWeight(RealmManager.getInstance().getUser().getWeightUnit()) + "");
                        } else {
                            weightEditView.setText("");
                        }
                        dateTextView.setText(DialogHelper.this.getUnderlineContent(DialogHelper.this.getDateString(calendar.getTimeInMillis(), activity)));
                    }
                }, calendar.get(1), calendar.get(2), calendar.get(5));
            }
        });
    }

    public void showWeightMetricDialog(Context context, ListCallbackSingleChoice callbackSingleChoice, int selectedMetric) {
        Collection weightList = new ArrayList();
        weightList.add(context.getString(R.string.kg).toUpperCase());
        weightList.add(context.getString(R.string.lb).toUpperCase());
        new Builder(context).title((int) R.string.weight_metrics).items(weightList).itemsCallbackSingleChoice(selectedMetric, callbackSingleChoice).positiveText((int) R.string.setting_ok).negativeText((int) R.string.setting_cancel).show();
    }

    private String getDateString(long timeInMillis, Context context) {
        Calendar calendar = Calendar.getInstance();
        int todaysIndex = (calendar.get(1) + calendar.get(2)) + calendar.get(5);
        calendar.setTimeInMillis(timeInMillis);
        if ((calendar.get(1) + calendar.get(2)) + calendar.get(5) == todaysIndex) {
            return context.getString(R.string.label_today);
        }
        return new SimpleDateFormat("MMM dd yyyy").format(calendar.getTime());
    }

    private SpannableString getUnderlineContent(String text) {
        SpannableString content = new SpannableString(text);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        return content;
    }
}
