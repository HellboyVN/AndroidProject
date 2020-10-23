package hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.preview.cards;

import android.content.Context;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;

public abstract class PreviewAbstractCard extends ViewHolder {
    protected Context context;
    private CardListener listener;
    protected int position;

    public interface CardListener {
        void requestListUpdate(int i, int i2);
    }

    public abstract void bind(Object obj);

    public void setListener(CardListener listener) {
        this.listener = listener;
    }

    protected void requestListUpdate(int position, int action) {
        if (this.listener != null) {
            this.listener.requestListUpdate(position, action);
        }
    }

    public PreviewAbstractCard(View cardView, Context context) {
        super(cardView);
        this.context = context;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getCardPosition() {
        return this.position;
    }
}
