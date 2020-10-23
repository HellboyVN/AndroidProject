package hb.me.homeworkout.gym.buttlegs.buttlegspro.home.cards;

import android.content.Context;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.ads.listener.IAdCardClicked;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.lisener.IBeginChallenge;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.lisener.IWeightChang;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.listeners.IRateClicked;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.reminder.listener.ITimeItemClick;

public abstract class AbstractCard extends ViewHolder {
    private IBeginChallenge beginChallenge;
    protected Context context;
    private IAdCardClicked iAdCardClicked;
    private IRateClicked iRateClicked;
    private ITimeItemClick iTimeItemClick;
    private IWeightChang iWeightChang;
    private boolean isForFinish;
    protected int position;

    public abstract void bind(Object obj);

    public void setRateListener(IRateClicked iRateClicked) {
        this.iRateClicked = iRateClicked;
    }

    protected void rateClicked(int position) {
        if (this.iRateClicked != null) {
            this.iRateClicked.rateClicked(position);
        }
    }

    protected void removeAdClicked(int position) {
        if (this.iAdCardClicked != null) {
            this.iAdCardClicked.removeAdClicked(position);
        }
    }

    public void setiAdCardClicked(IAdCardClicked iAdCardClicked) {
        this.iAdCardClicked = iAdCardClicked;
    }

    public void setBeginChallenge(IBeginChallenge beginChallenge) {
        this.beginChallenge = beginChallenge;
    }

    protected void beginChallegeClicked(int position) {
        if (this.beginChallenge != null) {
            this.beginChallenge.beginChallenge();
        }
    }

    public Context getContext() {
        return this.context;
    }

    public AbstractCard(View cardView, Context context) {
        super(cardView);
        this.context = context;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getCardPosition() {
        return this.position;
    }

    public boolean isForFinish() {
        return this.isForFinish;
    }

    public void setForFinish(boolean forFinish) {
        this.isForFinish = forFinish;
    }

    public void setiTimerCardClicked(ITimeItemClick iTimeItemClick) {
        this.iTimeItemClick = iTimeItemClick;
    }

    protected void timeChange(int position) {
        if (this.iTimeItemClick != null) {
            this.iTimeItemClick.onTimeChange(position);
        }
    }

    public void timeChange(ITimeItemClick iTimeItemClick) {
        this.iTimeItemClick = iTimeItemClick;
    }

    protected void weightMetricChange() {
        if (this.iWeightChang != null) {
            this.iWeightChang.weightMetricChange();
        }
    }

    protected void weightEditTextFocus(View view) {
        if (this.iWeightChang != null) {
            this.iWeightChang.weightEditTextFocus(view);
        }
    }

    protected void weightCalendarClick() {
        if (this.iWeightChang != null) {
            this.iWeightChang.weightCalendarClick();
        }
    }

    public void setweightChange(IWeightChang iWeightChang) {
        this.iWeightChang = iWeightChang;
    }
}
