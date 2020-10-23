package lp.me.allgifs.view;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.StaggeredGridLayoutManager;

public abstract class EndlessRecyclerViewScrollListener extends OnScrollListener {
    private int currentPage = 0;
    private boolean loading = true;
    LayoutManager mLayoutManager;
    private int previousTotalItemCount = 0;
    private int startingPageIndex = 0;
    private int visibleThreshold = 5;

    public abstract void onLoadMore(int i, int i2, RecyclerView recyclerView);

    public EndlessRecyclerViewScrollListener(LinearLayoutManager layoutManager) {
         mLayoutManager = layoutManager;
    }

    public EndlessRecyclerViewScrollListener(GridLayoutManager layoutManager) {
         mLayoutManager = layoutManager;
         visibleThreshold *= layoutManager.getSpanCount();
    }

    public EndlessRecyclerViewScrollListener(StaggeredGridLayoutManager layoutManager) {
         mLayoutManager = layoutManager;
         visibleThreshold *= layoutManager.getSpanCount();
    }

    public int getLastVisibleItem(int[] lastVisibleItemPositions) {
        int maxSize = 0;
        for (int i = 0; i < lastVisibleItemPositions.length; i++) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i];
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i];
            }
        }
        return maxSize;
    }

    public void onScrolled(RecyclerView view, int dx, int dy) {
        int lastVisibleItemPosition = 0;
        int totalItemCount =  mLayoutManager.getItemCount();
        if ( mLayoutManager instanceof StaggeredGridLayoutManager) {
            lastVisibleItemPosition = getLastVisibleItem(((StaggeredGridLayoutManager)  mLayoutManager).findLastVisibleItemPositions(null));
        } else if ( mLayoutManager instanceof GridLayoutManager) {
            lastVisibleItemPosition = ((GridLayoutManager)  mLayoutManager).findLastVisibleItemPosition();
        } else if ( mLayoutManager instanceof LinearLayoutManager) {
            lastVisibleItemPosition = ((LinearLayoutManager)  mLayoutManager).findLastVisibleItemPosition();
        }
        if (totalItemCount <  previousTotalItemCount) {
             currentPage =  startingPageIndex;
             previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                 loading = true;
            }
        }
        if ( loading && totalItemCount >  previousTotalItemCount) {
             loading = false;
             previousTotalItemCount = totalItemCount;
        }
        if (! loading &&  visibleThreshold + lastVisibleItemPosition > totalItemCount) {
             currentPage++;
            onLoadMore( currentPage, totalItemCount, view);
             loading = true;
        }
    }

    public void resetState() {
         currentPage =  startingPageIndex;
         previousTotalItemCount = 0;
         loading = true;
    }
}
