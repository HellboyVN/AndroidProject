package hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.reminder.cards;

import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.R;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.home.tabs.tabone.cards.AbstractCard;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.reminder.data.TimeData;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.reminder.receivers.AlarmUtil;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.utils.SharedPrefsService;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.utils.TypeFaceService;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.views.SetTimeWeeksView;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
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

public class SetTimeCard extends AbstractCard {
    private Context mContext;
    @BindView(R.id.reminder_ok)
    TextView ok;
    @BindView(R.id.reminder_question)
    TextView question;
    @BindView(R.id.reminder_time)
    TextView reminderTime;
    @BindView(R.id.remove_reminder)
    AppCompatImageView remove;
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
        int i3 = 0;
        this.timeData = (TimeData) data;
        TextView textView = this.question;
        if (isForFinish()) {
            i = 0;
        } else {
            i = 8;
        }
        textView.setVisibility(i);
        textView = this.ok;
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
        this.ok.setTypeface(TypeFaceService.getInstance().getRobotoRegular(this.context));
        final int pos = getAdapterPosition();
        timeTextDesign(this.timeData.getTime().getHour(), this.timeData.getTime().getMinute(), pos);
        if (VERSION.SDK_INT > 19) {
            this.switchOn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    SetTimeCard.this.timeData.setActive(isChecked);
                    SetTimeCard.this.remove.setVisibility(isChecked ? 4 : 0);
                }
            });
            this.switchOn.setChecked(this.timeData.isActive());
            if (isForFinish()) {
                this.remove.setVisibility(4);
            } else if (this.timeData.isActive()) {
                this.remove.setVisibility(4);
            } else {
                this.remove.setVisibility(0);
            }
        } else {
            AppCompatImageView appCompatImageView = this.remove;
            if (isForFinish()) {
                i3 = 4;
            }
            appCompatImageView.setVisibility(i3);
        }
        this.setTimeView.setInitialData(this.timeData.getListDay());
        this.ok.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SetTimeCard.this.timeData.setActive(true);
                SharedPrefsService.getInstance().addTimeData(SetTimeCard.this.timeData, SetTimeCard.this.context);
                AlarmUtil.getInstance().updateAlarm(SetTimeCard.this.context);
                SetTimeCard.this.rateClicked(pos);
            }
        });
        this.remove.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SetTimeCard.this.removeClick(SetTimeCard.this.getAdapterPosition());
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
