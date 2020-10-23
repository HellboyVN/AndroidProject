package hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.finish.cards;

import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.R;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.finish.model.Finish;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.home.tabs.tabone.cards.AbstractCard;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.utils.EmailHelper;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.utils.RateHelper;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.utils.SharedPrefsService;
import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RateQuestionCard extends AbstractCard {
    @BindView(R.id.rateCardLayout)
    CardView rateCardLayout;
    @BindView(R.id.rateNegative)
    AppCompatTextView rateNegative;
    @BindView(R.id.ratePositive)
    AppCompatTextView ratePositive;
    @BindView(R.id.rateQuestion)
    TextView rateQuestion;

    public enum RateQuestionStates {
        STATE_LIKE,
        STATE_RATE,
        STATE_FEEDBACK;

        public RateQuestionStates getNextState(boolean answer) {
            switch (this) {
                case STATE_LIKE:
                    if (answer) {
                        return STATE_RATE;
                    }
                    return STATE_FEEDBACK;
                default:
                    return STATE_FEEDBACK;
            }
        }
    }

    public RateQuestionCard(Context context, ViewGroup parent) {
        this(LayoutInflater.from(context).inflate(R.layout.rate_question_card, parent, false), context);
    }

    public RateQuestionCard(View cardView, Context context) {
        super(cardView, context);
        ButterKnife.bind((Object) this, cardView);
    }

    public void bind(Object data) {
        final Finish finishData = new Finish();
        this.rateQuestion.setText(getQuestion(finishData.getRateQuestionState()));
        this.rateNegative.setText(this.context.getString(R.string.rate1_negativ));
        this.ratePositive.setText(this.context.getString(R.string.rate1_positiv));
        this.rateNegative.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                RateQuestionCard.this.proceedAnswer(finishData, false, RateQuestionCard.this.context.getString(R.string.thank_you));
            }
        });
        this.ratePositive.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                RateQuestionCard.this.proceedAnswer(finishData, true, RateQuestionCard.this.context.getString(R.string.thank_you));
            }
        });
    }

    private void proceedAnswer(Finish finishData, boolean answer, String rateMessage) {
        switch (finishData.getRateQuestionState()) {
            case STATE_LIKE:
                this.rateNegative.setText(this.context.getString(R.string.rate2_negativ));
                this.ratePositive.setText(this.context.getString(R.string.rate2_positiv));
                finishData.setRateQuestionState(finishData.getRateQuestionState().getNextState(answer));
                break;
            case STATE_RATE:
                if (answer) {
                    openGoogle(rateMessage);
                }
                closeRateCard();
                break;
            case STATE_FEEDBACK:
                if (answer) {
                    openEmail();
                }
                closeRateCard();
                break;
        }
        this.rateQuestion.setText(getQuestion(finishData.getRateQuestionState()));
    }

    private void openGoogle(String message) {
        RateHelper.openPlayStore(this.context, this.context.getPackageName(), message);
        SharedPrefsService.getInstance().setRatedStatus(this.context, true);
    }

    private void openEmail() {
        SharedPrefsService.getInstance().setRatedStatus(this.context, true);
        EmailHelper.getInstance().sendEmail("support@fitfit.am", this.context.getResources().getString(R.string.app_name) + " " + this.context.getResources().getString(R.string.feedback), "");
    }

    private void closeRateCard() {
        rateClicked(getAdapterPosition());
    }

    public String getQuestion(RateQuestionStates rateQuestionStates) {
        switch (rateQuestionStates) {
            case STATE_LIKE:
                return this.context.getString(R.string.question_like) + " " + this.context.getString(R.string.app_name) + "?";
            case STATE_RATE:
                return this.context.getString(R.string.give_5_title);
            case STATE_FEEDBACK:
                return this.context.getString(R.string.question_feedback);
            default:
                return "";
        }
    }
}
