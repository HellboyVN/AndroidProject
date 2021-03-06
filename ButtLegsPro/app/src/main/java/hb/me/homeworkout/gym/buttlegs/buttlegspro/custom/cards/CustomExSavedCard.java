package hb.me.homeworkout.gym.buttlegs.buttlegspro.custom.cards;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.R;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.custom.IPreviousWorkoutAactions;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.type.ExerciseType;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.models.Exercise;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.SharedPrefsService;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.TypeFaceService;

public class CustomExSavedCard extends CustomExAbstractCard {
    @BindView(R.id.cCardName)
    TextView cCardName;
    @BindView(R.id.mCardBegin)
    TextView mCardBegin;
    @BindView(R.id.mCardItemLayout)
    FrameLayout mCardItemLayout;
    @BindView(R.id.mCardLayout)
    CardView mCardLayout;
    @BindView(R.id.mCardRestore)
    TextView mCardRestore;
    Context mContext;
    @BindView(R.id.duration)
    TextView mDuration;
    private ExerciseType mExType;
    @BindView(R.id.exersiceCount)
    TextView mExersiceCount;
    private IPreviousWorkoutAactions mListener;
    private List<Exercise> mSavedWorkout;

    public CustomExSavedCard(Context context, ViewGroup parent) {
        this(context, LayoutInflater.from(context).inflate(R.layout.preview_custom_saved, parent, false));
    }

    public CustomExSavedCard(Context context, View view) {
        super(view, context);
        this.mContext = context;
        try {
            ButterKnife.bind((Object) this, view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setExType(ExerciseType type) {
        this.mExType = type;
    }

    public void setActionsListener(IPreviousWorkoutAactions listener) {
        this.mListener = listener;
    }

    public void bind(Object data) {
        this.mSavedWorkout = SharedPrefsService.getInstance().getCustomWorkoutData(this.mContext, this.mExType);
        int count = 0;
        int duration = 0;
        for (Exercise exercise : this.mSavedWorkout) {
            duration += exercise.getDuration().intValue();
            count++;
        }
        this.mExersiceCount.setText("x" + count);
        this.mDuration.setText(constructTime(duration));
        this.mCardBegin.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (CustomExSavedCard.this.mListener != null) {
                    CustomExSavedCard.this.mListener.onPreview(CustomExSavedCard.this.mSavedWorkout);
                }
            }
        });
        this.mCardRestore.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (CustomExSavedCard.this.mListener != null) {
                    CustomExSavedCard.this.mListener.onRestore();
                }
            }
        });
        this.mCardBegin.setTypeface(TypeFaceService.getInstance().getRobotoRegular(this.context));
        this.mCardRestore.setTypeface(TypeFaceService.getInstance().getRobotoRegular(this.context));
        this.cCardName.setTypeface(TypeFaceService.getInstance().getRobotoRegular(this.context));
    }

    private String constructTime(int duration) {
        int hour = (duration / 1000) / 60;
        int minute = (duration / 1000) % 60;
        return (hour < 10 ? "0" + hour : Integer.valueOf(hour)) + ":" + (minute < 10 ? "0" + minute : Integer.valueOf(minute)) + this.mContext.getResources().getString(R.string.minute_short);
    }
}
