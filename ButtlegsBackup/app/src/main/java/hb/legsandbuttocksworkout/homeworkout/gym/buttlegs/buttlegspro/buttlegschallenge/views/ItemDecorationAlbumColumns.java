package hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.views;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.RecyclerView.LayoutParams;
import android.support.v7.widget.RecyclerView.State;
import android.view.View;

public class ItemDecorationAlbumColumns extends ItemDecoration {
    private int mGridSize;
    private boolean mNeedLeftSpacing = false;
    private int mSizeGridSpacingPx;

    public ItemDecorationAlbumColumns(int gridSpacingPx, int gridSize) {
        this.mSizeGridSpacingPx = gridSpacingPx;
        this.mGridSize = gridSize;
    }

    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
        int padding = (parent.getWidth() / this.mGridSize) - ((int) ((((float) parent.getWidth()) - (((float) this.mSizeGridSpacingPx) * ((float) (this.mGridSize - 1)))) / ((float) this.mGridSize)));
        int itemPosition = ((LayoutParams) view.getLayoutParams()).getViewAdapterPosition();
        if (itemPosition < this.mGridSize) {
            outRect.top = 0;
        } else {
            outRect.top = this.mSizeGridSpacingPx;
        }
        if (itemPosition % this.mGridSize == 0) {
            outRect.left = 0;
            outRect.right = padding;
            this.mNeedLeftSpacing = true;
        } else if ((itemPosition + 1) % this.mGridSize == 0) {
            this.mNeedLeftSpacing = false;
            outRect.right = 0;
            outRect.left = padding;
        } else if (this.mNeedLeftSpacing) {
            this.mNeedLeftSpacing = false;
            outRect.left = this.mSizeGridSpacingPx - padding;
            if ((itemPosition + 2) % this.mGridSize == 0) {
                outRect.right = this.mSizeGridSpacingPx - padding;
            } else {
                outRect.right = this.mSizeGridSpacingPx / 2;
            }
        } else if ((itemPosition + 2) % this.mGridSize == 0) {
            this.mNeedLeftSpacing = false;
            outRect.left = this.mSizeGridSpacingPx / 2;
            outRect.right = this.mSizeGridSpacingPx - padding;
        } else {
            this.mNeedLeftSpacing = false;
            outRect.left = this.mSizeGridSpacingPx / 2;
            outRect.right = this.mSizeGridSpacingPx / 2;
        }
        outRect.bottom = 0;
    }
}
