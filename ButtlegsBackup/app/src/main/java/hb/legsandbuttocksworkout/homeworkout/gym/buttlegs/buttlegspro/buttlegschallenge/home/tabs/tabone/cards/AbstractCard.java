package hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.home.tabs.tabone.cards;

import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.ads.listener.IAdCardClicked;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.home.tabs.listener.IWeightChang;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.home.tabs.tabfour.liseners.IBeginChallenge;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.listeners.IRateClicked;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.reminder.listener.ITimeItemClik;
import android.content.Context;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener;

public abstract class AbstractCard extends ViewHolder {
    private IBeginChallenge beginChallenge;
    protected Context context;
    private IAdCardClicked iAdCardClicked;
    public IRateClicked iRateClicked;
    private ITimeItemClik iTimeItemClik;
    private IWeightChang iWeightChang;
    private boolean isForFinish;

    public abstract void bind(Object obj);

    public void rateClicked(int adapterPosition) {
        if (this.iRateClicked != null) {
            this.iRateClicked.rateClicked(adapterPosition);
        }
    }

    public void setiRateClicked(IRateClicked iRateClicked) {
        this.iRateClicked = iRateClicked;
    }

    public AbstractCard(View cardView, Context context) {
        super(cardView);
        this.context = context;
    }

    protected void removeAdClicked(int position) {
        if (this.iAdCardClicked != null) {
            this.iAdCardClicked.removeAdClicked(position);
        }
    }

    protected void whyAdsClicked() {
        if (this.iAdCardClicked != null) {
            this.iAdCardClicked.whySeeAd();
        }
    }

    public void setiAdCardClicked(IAdCardClicked iAdCardClicked) {
        this.iAdCardClicked = iAdCardClicked;
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

    protected void weightCalendarClick(OnDateSetListener listener, int year, int monthOfYear, int dayOfMonth) {
        if (this.iWeightChang != null) {
            this.iWeightChang.weightCalendarClick(listener, year, monthOfYear, dayOfMonth);
        }
    }

    public void setweightChange(IWeightChang iWeightChang) {
        this.iWeightChang = iWeightChang;
    }

    public boolean isForFinish() {
        return this.isForFinish;
    }

    public void setForFinish(boolean forFinish) {
        this.isForFinish = forFinish;
    }

    public void setiTimerCardClicked(ITimeItemClik iTimeItemClik) {
        this.iTimeItemClik = iTimeItemClik;
    }

    protected void timeChange(int position) {
        if (this.iTimeItemClik != null) {
            this.iTimeItemClik.timeChange(position);
        }
    }

    protected void removeClick(int position) {
        if (this.iTimeItemClik != null) {
            this.iTimeItemClik.removeClick(position);
        }
    }

    public void timeChange(ITimeItemClik iTimeItemClik) {
        this.iTimeItemClik = iTimeItemClik;
    }

    public void setBeginChallenge(IBeginChallenge beginChallenge) {
        this.beginChallenge = beginChallenge;
    }

    protected void beginChallegeClicked(int position) {
        if (this.beginChallenge != null) {
            this.beginChallenge.beginChallenge();
        }
    }
}
