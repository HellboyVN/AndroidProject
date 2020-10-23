package hb.me.homeworkout.gym.buttlegs.buttlegspro.reminder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.AdapterDataObserver;
import android.util.AttributeSet;
import android.view.View;

public class EmptySupportRecyclerView extends RecyclerView {
    private AdapterDataObserver emptyObserver = new AdapterDataObserver() {
        public void onChanged() {
            Adapter<?> adapter = EmptySupportRecyclerView.this.getAdapter();
            if (adapter != null && EmptySupportRecyclerView.this.emptyView != null) {
                if (adapter.getItemCount() == 0) {
                    EmptySupportRecyclerView.this.emptyView.setVisibility(0);
                    EmptySupportRecyclerView.this.setVisibility(8);
                    return;
                }
                EmptySupportRecyclerView.this.emptyView.setVisibility(8);
                EmptySupportRecyclerView.this.setVisibility(0);
            }
        }
    };
    private View emptyView;

    public EmptySupportRecyclerView(Context context) {
        super(context);
    }

    public EmptySupportRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EmptySupportRecyclerView(Context context, AttributeSet attrs, int defStyle) {
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
