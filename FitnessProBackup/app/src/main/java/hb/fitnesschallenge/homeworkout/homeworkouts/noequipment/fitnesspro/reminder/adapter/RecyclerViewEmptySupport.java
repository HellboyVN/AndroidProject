package hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.reminder.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.AdapterDataObserver;
import android.util.AttributeSet;
import android.view.View;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;

public class RecyclerViewEmptySupport extends ObservableRecyclerView {
    private AdapterDataObserver emptyObserver = new AdapterDataObserver() {
        public void onChanged() {
            Adapter<?> adapter = RecyclerViewEmptySupport.this.getAdapter();
            if (adapter != null && RecyclerViewEmptySupport.this.emptyView != null) {
                if (adapter.getItemCount() == 1) {
                    RecyclerViewEmptySupport.this.emptyView.setVisibility(0);
                    RecyclerViewEmptySupport.this.setVisibility(8);
                    return;
                }
                RecyclerViewEmptySupport.this.emptyView.setVisibility(8);
                RecyclerViewEmptySupport.this.setVisibility(0);
            }
        }
    };
    private View emptyView;

    public RecyclerViewEmptySupport(Context context) {
        super(context);
    }

    public RecyclerViewEmptySupport(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewEmptySupport(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(this.emptyObserver);
        }
        this.emptyObserver.onChanged();
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
    }
}
