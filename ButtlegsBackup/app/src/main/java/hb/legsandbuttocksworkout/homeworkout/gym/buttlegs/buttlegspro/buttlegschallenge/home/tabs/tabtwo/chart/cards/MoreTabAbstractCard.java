package hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.home.tabs.tabtwo.chart.cards;

import android.content.Context;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;

public abstract class MoreTabAbstractCard extends ViewHolder {
    protected Context context;

    public abstract void bind(Object obj);

    public MoreTabAbstractCard(View cardView, Context context) {
        super(cardView);
        this.context = context;
    }
}
