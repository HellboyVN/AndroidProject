package hb.me.homeworkout.gym.buttlegs.buttlegspro.reminder.cards;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.R;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.cards.AbstractCard;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.reminder.data.TimeData;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.reminder.receivers.AlarmUtil;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.ConsKeys;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.SharedPrefsService;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.TypeFaceService;

public class SetTimeCard extends AbstractCard {
    private Context mContext;
    @BindView(R.id.reminder_question)
    TextView question;
    @BindView(R.id.reminder_time)
    TextView reminderTime;
    @BindView(R.id.reminder_ok)
    TextView setReminder;
    @BindView(R.id.setTimeView)
    SetTimeWeeksView setTimeView;
    @BindView(R.id.switchOn)
    SwitchCompat switchOn;
    @BindView(R.id.timeCardLayout)
    CardView timeCardLayout;
    TimeData timeData;
    @BindView(R.id.timeList)
    FrameLayout timeList;

    public SetTimeCard(Context context, ViewGroup parent) {
        this(context, LayoutInflater.from(context).inflate(R.layout.reminder_list, parent, false));
    }

    public SetTimeCard(Context context, View view) {
        super(view, context);
        ButterKnife.bind((Object) this, view);
        this.mContext = context;
    }

    @TargetApi(23)
    @RequiresApi(api = 23)
    public void bind(Object data) {
        int i;
        int i2 = 8;
        this.timeData = (TimeData) data;
        TextView textView = this.question;
        if (isForFinish()) {
            i = 0;
        } else {
            i = 8;
        }
        textView.setVisibility(i);
        textView = this.setReminder;
        if (isForFinish()) {
            i = 0;
        } else {
            i = 8;
        }
        textView.setVisibility(i);
        SwitchCompat switchCompat = this.switchOn;
        if (!isForFinish()) {
            i2 = 0;
        }
        switchCompat.setVisibility(i2);
        this.question.setTypeface(TypeFaceService.getInstance().getRobotoRegular(this.context));
        this.reminderTime.setTypeface(TypeFaceService.getInstance().getRobotoMedium(this.context));
        this.setReminder.setTypeface(TypeFaceService.getInstance().getRobotoRegular(this.context));
        final int pos = getAdapterPosition();
        timeTextDesign(this.timeData.getTime().getHour(), this.timeData.getTime().getMinute(), pos);
        this.switchOn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SetTimeCard.this.timeData.setActive(isChecked);
            }
        });
        this.switchOn.setChecked(this.timeData.isActive());
        this.setTimeView.setInitialData(this.timeData.getListDay());
        this.setReminder.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SetTimeCard.this.timeData.setActive(true);
                SharedPrefsService.getInstance().addTimeData(SetTimeCard.this.timeData, SetTimeCard.this.context);
                AlarmUtil.getInstance().updateAlarm(SetTimeCard.this.context);
                SetTimeCard.this.rateClicked(pos);
                Bundle bundle = new Bundle();
                bundle.putString("item_name", "set_reminder");
            }
        });
    }

    private void timeTextDesign(int hour, int minute, final int posi) {
        StringBuilder stringBuilder = new StringBuilder();
        int r3 = hour > 12 ? hour - 12 : hour == 0 ? 12 : hour;
        this.reminderTime.setText(stringBuilder.append(r3).append("").toString() + ":" + ((minute <= 9 ? "0" : "") + minute) + "  " + this.context.getResources().getString(hour >= 12 ? R.string.pm : R.string.am));
        this.reminderTime.setTypeface(TypeFaceService.getInstance().getRobotoMedium(this.context));
        this.reminderTime.setTextColor(ContextCompat.getColor(this.context, R.color.mdtp_accent_color_dark));
        this.reminderTime.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SetTimeCard.this.timeChange(posi);
            }
        });
    }
}
