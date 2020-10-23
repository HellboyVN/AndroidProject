package hb.homeworkout.homeworkouts.noequipment.fitnesspro.adapter;

import android.content.Context;
import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.Recycler;
import android.support.v7.widget.RecyclerView.SmoothScroller.ScrollVectorProvider;
import android.support.v7.widget.RecyclerView.State;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;


public class StickyHeaderGridLayoutManager extends LayoutManager implements ScrollVectorProvider {
    private static final int DEFAULT_ROW_COUNT = 16;
    public static final String TAG = "StickyLayoutManager";
    private StickyHeaderGridAdapter mAdapter;
    private AnchorPosition mAnchor = new AnchorPosition();
    private int mAverageHeaderHeight;
    private final FillResult mFillResult = new FillResult();
    private View[] mFillViewSet;
    private int mFloatingHeaderPosition;
    private View mFloatingHeaderView;
    private int mHeaderOverlapMargin;
    private HeaderStateChangeListener mHeaderStateListener;
    private int mHeadersStartPosition;
    private ArrayList<LayoutRow> mLayoutRows = new ArrayList(16);
    private SavedState mPendingSavedState;
    private int mPendingScrollPosition = -1;
    private int mPendingScrollPositionOffset;
    private int mSpanCount;
    private SpanSizeLookup mSpanSizeLookup = new DefaultSpanSizeLookup();
    private int mStickOffset;
    private HeaderState mStickyHeadeState;
    private int mStickyHeaderSection = -1;
    private View mStickyHeaderView;

    private static class AnchorPosition {
        private int item;
        private int offset;
        private int section;

        public AnchorPosition() {
            reset();
        }

        public void reset() {
            this.section = -1;
            this.item = 0;
            this.offset = 0;
        }
    }

    public static abstract class SpanSizeLookup {
        public abstract int getSpanSize(int i, int i2);

        public int getSpanIndex(int section, int position, int spanCount) {
            int positionSpanSize = getSpanSize(section, position);
            if (positionSpanSize >= spanCount) {
                return 0;
            }
            int spanIndex = 0;
            for (int i = 0; i < position; i++) {
                int spanSize = getSpanSize(section, i);
                spanIndex += spanSize;
                if (spanIndex == spanCount) {
                    spanIndex = 0;
                } else if (spanIndex > spanCount) {
                    spanIndex = spanSize;
                }
            }
            if (spanIndex + positionSpanSize > spanCount) {
                return 0;
            }
            return spanIndex;
        }
    }

    public static final class DefaultSpanSizeLookup extends SpanSizeLookup {
        public int getSpanSize(int section, int position) {
            return 1;
        }

        public int getSpanIndex(int section, int position, int spanCount) {
            return position % spanCount;
        }
    }

    private static class FillResult {
        private int adapterPosition;
        private View edgeView;
        private int height;
        private int length;

        private FillResult() {
        }
    }

    public enum HeaderState {
        NORMAL,
        STICKY,
        PUSHED
    }

    public interface HeaderStateChangeListener {
        void onHeaderStateChanged(int i, View view, HeaderState headerState, int i2);
    }

    public static class LayoutParams extends RecyclerView.LayoutParams {
        public static final int INVALID_SPAN_ID = -1;
        private int mSpanIndex = -1;
        private int mSpanSize = 0;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(RecyclerView.LayoutParams source) {
            super(source);
        }

        public int getSpanIndex() {
            return this.mSpanIndex;
        }

        public int getSpanSize() {
            return this.mSpanSize;
        }
    }

    private static class LayoutRow {
        private int adapterPosition;
        private int bottom;
        private boolean header;
        private View headerView;
        private int length;
        private int top;

        public LayoutRow(int adapterPosition, int length, int top, int bottom) {
            this.header = false;
            this.headerView = null;
            this.adapterPosition = adapterPosition;
            this.length = length;
            this.top = top;
            this.bottom = bottom;
        }

        public LayoutRow(View headerView, int adapterPosition, int length, int top, int bottom) {
            this.header = true;
            this.headerView = headerView;
            this.adapterPosition = adapterPosition;
            this.length = length;
            this.top = top;
            this.bottom = bottom;
        }

        int getHeight() {
            return this.bottom - this.top;
        }
    }

    public static class SavedState implements Parcelable {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        private int mAnchorItem;
        private int mAnchorOffset;
        private int mAnchorSection;

        SavedState(Parcel in) {
            this.mAnchorSection = in.readInt();
            this.mAnchorItem = in.readInt();
            this.mAnchorOffset = in.readInt();
        }
        public SavedState(){

        }
        public SavedState(SavedState other) {
            this.mAnchorSection = other.mAnchorSection;
            this.mAnchorItem = other.mAnchorItem;
            this.mAnchorOffset = other.mAnchorOffset;
        }

        boolean hasValidAnchor() {
            return this.mAnchorSection >= 0;
        }

        void invalidateAnchor() {
            this.mAnchorSection = -1;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.mAnchorSection);
            dest.writeInt(this.mAnchorItem);
            dest.writeInt(this.mAnchorOffset);
        }
    }

    public StickyHeaderGridLayoutManager(int spanCount) {
        this.mSpanCount = spanCount;
        this.mFillViewSet = new View[spanCount];
        this.mHeaderOverlapMargin = 0;
        if (spanCount < 1) {
            throw new IllegalArgumentException("Span count should be at least 1. Provided " + spanCount);
        }
    }

    public void setSpanSizeLookup(SpanSizeLookup spanSizeLookup) {
        this.mSpanSizeLookup = spanSizeLookup;
        if (this.mSpanSizeLookup == null) {
            this.mSpanSizeLookup = new DefaultSpanSizeLookup();
        }
    }

    public SpanSizeLookup getSpanSizeLookup() {
        return this.mSpanSizeLookup;
    }

    public HeaderStateChangeListener getHeaderStateChangeListener() {
        return this.mHeaderStateListener;
    }

    public void setHeaderStateChangeListener(HeaderStateChangeListener listener) {
        this.mHeaderStateListener = listener;
    }

    public void setHeaderBottomOverlapMargin(int bottomMargin) {
        this.mHeaderOverlapMargin = bottomMargin;
    }

    public void onAdapterChanged(Adapter oldAdapter, Adapter newAdapter) {
        super.onAdapterChanged(oldAdapter, newAdapter);
        try {
            this.mAdapter = (StickyHeaderGridAdapter) newAdapter;
            removeAllViews();
            clearState();
        } catch (ClassCastException e) {
            throw new ClassCastException("Adapter used with StickyHeaderGridLayoutManager must be kind of StickyHeaderGridAdapter");
        }
    }

    public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);
        try {
            this.mAdapter = (StickyHeaderGridAdapter) view.getAdapter();
        } catch (ClassCastException e) {
            throw new ClassCastException("Adapter used with StickyHeaderGridLayoutManager must be kind of StickyHeaderGridAdapter");
        }
    }

    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-1, -2);
    }

    public RecyclerView.LayoutParams generateLayoutParams(Context c, AttributeSet attrs) {
        return new LayoutParams(c, attrs);
    }

    public RecyclerView.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams lp) {
        if (lp instanceof MarginLayoutParams) {
            return new LayoutParams((MarginLayoutParams) lp);
        }
        return new LayoutParams(lp);
    }

    public Parcelable onSaveInstanceState() {
        if (this.mPendingSavedState != null) {
            return new SavedState(this.mPendingSavedState);
        }
        SavedState state = new SavedState();
        if (getChildCount() > 0) {
            state.mAnchorSection = this.mAnchor.section;
            state.mAnchorItem = this.mAnchor.item;
            state.mAnchorOffset = this.mAnchor.offset;
            return state;
        }
        state.invalidateAnchor();
        return state;
    }

    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof SavedState) {
            this.mPendingSavedState = (SavedState) state;
            requestLayout();
            return;
        }
        Log.d(TAG, "invalid saved state class");
    }

    public boolean checkLayoutParams(RecyclerView.LayoutParams lp) {
        return lp instanceof LayoutParams;
    }

    public boolean canScrollVertically() {
        return true;
    }

    public void scrollToPosition(int position) {
        if (position < 0 || position > getItemCount()) {
            throw new IndexOutOfBoundsException("adapter position out of range");
        }
        this.mPendingScrollPosition = position;
        this.mPendingScrollPositionOffset = 0;
        if (this.mPendingSavedState != null) {
            this.mPendingSavedState.invalidateAnchor();
        }
        requestLayout();
    }

    private int getExtraLayoutSpace(State state) {
        if (state.hasTargetScrollPosition()) {
            return getHeight();
        }
        return 0;
    }

    public void smoothScrollToPosition(RecyclerView recyclerView, State state, int position) {
        LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {
            public int calculateDyToMakeVisible(View view, int snapPreference) {
                LayoutManager layoutManager = getLayoutManager();
                if (layoutManager == null || !layoutManager.canScrollVertically()) {
                    return 0;
                }
                int topOffset = StickyHeaderGridLayoutManager.this.getPositionSectionHeaderHeight(StickyHeaderGridLayoutManager.this.getPosition(view));
                return calculateDtToFit(layoutManager.getDecoratedTop(view), layoutManager.getDecoratedBottom(view), layoutManager.getPaddingTop() + topOffset, layoutManager.getHeight() - layoutManager.getPaddingBottom(), snapPreference);
            }
        };
        linearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(linearSmoothScroller);
    }

    public PointF computeScrollVectorForPosition(int targetPosition) {
        if (getChildCount() == 0) {
            return null;
        }
        LayoutRow firstRow = getFirstVisibleRow();
        if (firstRow != null) {
            return new PointF(0.0f, (float) (targetPosition - firstRow.adapterPosition));
        }
        return null;
    }

    private int getAdapterPositionFromAnchor(AnchorPosition anchor) {
        if (anchor.section < 0 || anchor.section >= this.mAdapter.getSectionCount()) {
            anchor.reset();
            return -1;
        } else if (anchor.item >= 0 && anchor.item < this.mAdapter.getSectionItemCount(anchor.section)) {
            return this.mAdapter.getSectionItemPosition(anchor.section, anchor.item);
        } else {
            anchor.offset = 0;
            return this.mAdapter.getSectionHeaderPosition(anchor.section);
        }
    }

    private int getAdapterPositionChecked(int section, int offset) {
        if (section < 0 || section >= this.mAdapter.getSectionCount()) {
            return -1;
        }
        if (offset < 0 || offset >= this.mAdapter.getSectionItemCount(section)) {
            return this.mAdapter.getSectionHeaderPosition(section);
        }
        return this.mAdapter.getSectionItemPosition(section, offset);
    }

    public void onLayoutChildren(Recycler recycler, State state) {
        if (this.mAdapter == null || state.getItemCount() == 0) {
            removeAndRecycleAllViews(recycler);
            clearState();
            return;
        }
        int pendingAdapterPosition;
        int pendingAdapterOffset;
        if (this.mPendingScrollPosition >= 0) {
            pendingAdapterPosition = this.mPendingScrollPosition;
            pendingAdapterOffset = this.mPendingScrollPositionOffset;
        } else if (this.mPendingSavedState == null || !this.mPendingSavedState.hasValidAnchor()) {
            pendingAdapterPosition = getAdapterPositionFromAnchor(this.mAnchor);
            pendingAdapterOffset = this.mAnchor.offset;
        } else {
            pendingAdapterPosition = getAdapterPositionChecked(this.mPendingSavedState.mAnchorSection, this.mPendingSavedState.mAnchorItem);
            pendingAdapterOffset = this.mPendingSavedState.mAnchorOffset;
            this.mPendingSavedState = null;
        }
        if (pendingAdapterPosition < 0 || pendingAdapterPosition >= state.getItemCount()) {
            pendingAdapterPosition = 0;
            pendingAdapterOffset = 0;
            this.mPendingScrollPosition = -1;
        }
        if (pendingAdapterOffset > 0) {
            pendingAdapterOffset = 0;
        }
        detachAndScrapAttachedViews(recycler);
        clearState();
        pendingAdapterPosition = findFirstRowItem(pendingAdapterPosition);
        int left = getPaddingLeft();
        int right = getWidth() - getPaddingRight();
        int recyclerBottom = getHeight() - getPaddingBottom();
        int adapterPosition = pendingAdapterPosition;
        int top = getPaddingTop() + pendingAdapterOffset;
        while (adapterPosition < state.getItemCount()) {
            int bottom;
            if (this.mAdapter.getItemViewInternalType(adapterPosition) == 0) {
                int margin;
                View v = recycler.getViewForPosition(adapterPosition);
                addView(v);
                measureChildWithMargins(v, 0, 0);
                int height = getDecoratedMeasuredHeight(v);
                if (height >= this.mHeaderOverlapMargin) {
                    margin = this.mHeaderOverlapMargin;
                } else {
                    margin = height;
                }
                bottom = top + height;
                layoutDecorated(v, left, top, right, bottom);
                bottom -= margin;
                height -= margin;
                this.mLayoutRows.add(new LayoutRow(v, adapterPosition, 1, top, bottom));
                adapterPosition++;
                this.mAverageHeaderHeight = height;
            } else {
                FillResult result = fillBottomRow(recycler, state, adapterPosition, top);
                bottom = top + result.height;
                this.mLayoutRows.add(new LayoutRow(result.adapterPosition, result.length, top, bottom));
                adapterPosition += result.length;
            }
            top = bottom;
            if (bottom >= getExtraLayoutSpace(state) + recyclerBottom) {
                break;
            }
        }
        if (getBottomRow().bottom < recyclerBottom) {
            scrollVerticallyBy(getBottomRow().bottom - recyclerBottom, recycler, state);
        } else {
            clearViewsAndStickHeaders(recycler, state, false);
        }
        if (this.mPendingScrollPosition >= 0) {
            this.mPendingScrollPosition = -1;
            int topOffset = getPositionSectionHeaderHeight(pendingAdapterPosition);
            if (topOffset != 0) {
                scrollVerticallyBy(-topOffset, recycler, state);
            }
        }
    }

    public void onLayoutCompleted(State state) {
        super.onLayoutCompleted(state);
        this.mPendingSavedState = null;
    }

    private int getPositionSectionHeaderHeight(int adapterPosition) {
        int section = this.mAdapter.getAdapterPositionSection(adapterPosition);
        if (section < 0 || !this.mAdapter.isSectionHeaderSticky(section) || this.mAdapter.getItemSectionOffset(section, adapterPosition) < 0) {
            return 0;
        }
        int headerAdapterPosition = this.mAdapter.getSectionHeaderPosition(section);
        if (this.mFloatingHeaderView != null && headerAdapterPosition == this.mFloatingHeaderPosition) {
            return Math.max(0, getDecoratedMeasuredHeight(this.mFloatingHeaderView) - this.mHeaderOverlapMargin);
        }
        LayoutRow header = getHeaderRow(headerAdapterPosition);
        if (header != null) {
            return header.getHeight();
        }
        return this.mAverageHeaderHeight;
    }

    private int findFirstRowItem(int adapterPosition) {
        int section = this.mAdapter.getAdapterPositionSection(adapterPosition);
        int sectionPosition = this.mAdapter.getItemSectionOffset(section, adapterPosition);
        while (sectionPosition > 0 && this.mSpanSizeLookup.getSpanIndex(section, sectionPosition, this.mSpanCount) != 0) {
            sectionPosition--;
            adapterPosition--;
        }
        return adapterPosition;
    }

    private int getSpanWidth(int recyclerWidth, int spanIndex, int spanSize) {
        int spanWidth = recyclerWidth / this.mSpanCount;
        return (spanWidth * spanSize) + Math.min(Math.max(0, (recyclerWidth - (this.mSpanCount * spanWidth)) - spanIndex), spanSize);
    }

    private int getSpanLeft(int recyclerWidth, int spanIndex) {
        int spanWidth = recyclerWidth / this.mSpanCount;
        return (spanWidth * spanIndex) + Math.min(recyclerWidth - (this.mSpanCount * spanWidth), spanIndex);
    }

    private FillResult fillBottomRow(Recycler recycler, State state, int position, int top) {
        int recyclerWidth = (getWidth() - getPaddingLeft()) - getPaddingRight();
        int section = this.mAdapter.getAdapterPositionSection(position);
        int adapterPosition = position;
        int sectionPosition = this.mAdapter.getItemSectionOffset(section, adapterPosition);
        int spanSize = this.mSpanSizeLookup.getSpanSize(section, sectionPosition);
        int spanIndex = this.mSpanSizeLookup.getSpanIndex(section, sectionPosition, this.mSpanCount);
        int count = 0;
        int maxHeight = 0;
        Arrays.fill(this.mFillViewSet, null);
        while (spanIndex < this.mSpanCount) {
            int spanWidth = getSpanWidth(recyclerWidth, spanIndex, spanSize);
            View v = recycler.getViewForPosition(adapterPosition);
            LayoutParams params = (LayoutParams) v.getLayoutParams();
            params.mSpanIndex = spanIndex;
            params.mSpanSize = spanSize;
            addView(v, this.mHeadersStartPosition);
            this.mHeadersStartPosition++;
            measureChildWithMargins(v, recyclerWidth - spanWidth, 0);
            this.mFillViewSet[count] = v;
            count++;
            int height = getDecoratedMeasuredHeight(v);
            if (maxHeight < height) {
                maxHeight = height;
            }
            adapterPosition++;
            sectionPosition++;
            if (sectionPosition >= this.mAdapter.getSectionItemCount(section)) {
                break;
            }
            spanIndex += spanSize;
            spanSize = this.mSpanSizeLookup.getSpanSize(section, sectionPosition);
        }
        int left = getPaddingLeft();
        for (int i = 0; i < count; i++) {
            View v = this.mFillViewSet[i];
            int height = getDecoratedMeasuredHeight(v);
            int width = getDecoratedMeasuredWidth(v);

            layoutDecorated(v, left, top, left + width, top + height);
            left += width;
        }
        this.mFillResult.edgeView = this.mFillViewSet[count - 1];
        this.mFillResult.adapterPosition = position;
        this.mFillResult.length = count;
        this.mFillResult.height = maxHeight;
        return this.mFillResult;
    }

    private FillResult fillTopRow(Recycler recycler, State state, int position, int top) {
        int recyclerWidth = (getWidth() - getPaddingLeft()) - getPaddingRight();
        int section = this.mAdapter.getAdapterPositionSection(position);
        int adapterPosition = position;
        int sectionPosition = this.mAdapter.getItemSectionOffset(section, adapterPosition);
        int spanSize = this.mSpanSizeLookup.getSpanSize(section, sectionPosition);
        int spanIndex = this.mSpanSizeLookup.getSpanIndex(section, sectionPosition, this.mSpanCount);
        int count = 0;
        int maxHeight = 0;
        Arrays.fill(this.mFillViewSet, null);
        while (spanIndex >= 0) {
            int spanWidth = getSpanWidth(recyclerWidth, spanIndex, spanSize);
            View v = recycler.getViewForPosition(adapterPosition);
            LayoutParams params = (LayoutParams) v.getLayoutParams();
            params.mSpanIndex = spanIndex;
            params.mSpanSize = spanSize;
            addView(v, 0);
            this.mHeadersStartPosition++;
            measureChildWithMargins(v, recyclerWidth - spanWidth, 0);
            this.mFillViewSet[count] = v;
            count++;
            int height = getDecoratedMeasuredHeight(v);
            if (maxHeight < height) {
                maxHeight = height;
            }
            adapterPosition--;
            sectionPosition--;
            if (sectionPosition < 0) {
                break;
            }
            spanSize = this.mSpanSizeLookup.getSpanSize(section, sectionPosition);
            spanIndex -= spanSize;
        }
        int left = getPaddingLeft();
        for (int i = count - 1; i >= 0; i--) {
            View v = this.mFillViewSet[i];
            int height = getDecoratedMeasuredHeight(v);
            int width = getDecoratedMeasuredWidth(v);
            layoutDecorated(v, left, top - maxHeight, left + width, top - (maxHeight - height));
            left += width;
        }
        this.mFillResult.edgeView = this.mFillViewSet[count - 1];
        this.mFillResult.adapterPosition = adapterPosition + 1;
        this.mFillResult.length = count;
        this.mFillResult.height = maxHeight;
        return this.mFillResult;
    }

    private void clearHiddenRows(Recycler recycler, State state, boolean top) {
        if (this.mLayoutRows.size() > 0) {
            int recyclerTop = getPaddingTop();
            int recyclerBottom = getHeight() - getPaddingBottom();
            LayoutRow row;
            int i;
            if (top) {
                row = getTopRow();
                while (true) {
                    if (row.bottom < recyclerTop - getExtraLayoutSpace(state) || row.top > recyclerBottom) {
                        if (row.header) {
                            removeAndRecycleViewAt((this.mFloatingHeaderView != null ? 1 : 0) + this.mHeadersStartPosition, recycler);
                        } else {
                            for (i = 0; i < row.length; i++) {
                                removeAndRecycleViewAt(0, recycler);
                                this.mHeadersStartPosition--;
                            }
                        }
                        this.mLayoutRows.remove(0);
                        row = getTopRow();
                    } else {
                        return;
                    }
                }
            }
            row = getBottomRow();
            while (true) {
                if (row.bottom < recyclerTop || row.top > getExtraLayoutSpace(state) + recyclerBottom) {
                    if (row.header) {
                        removeAndRecycleViewAt(getChildCount() - 1, recycler);
                    } else {
                        for (i = 0; i < row.length; i++) {
                            removeAndRecycleViewAt(this.mHeadersStartPosition - 1, recycler);
                            this.mHeadersStartPosition--;
                        }
                    }
                    this.mLayoutRows.remove(this.mLayoutRows.size() - 1);
                    row = getBottomRow();
                } else {
                    return;
                }
            }
        }
    }

    private void clearViewsAndStickHeaders(Recycler recycler, State state, boolean top) {
        clearHiddenRows(recycler, state, top);
        if (getChildCount() > 0) {
            stickTopHeader(recycler);
        }
        updateTopPosition();
    }

    private LayoutRow getBottomRow() {
        return (LayoutRow) this.mLayoutRows.get(this.mLayoutRows.size() - 1);
    }

    private LayoutRow getTopRow() {
        return (LayoutRow) this.mLayoutRows.get(0);
    }

    private void offsetRowsVertical(int offset) {
        Iterator it = this.mLayoutRows.iterator();
        while (it.hasNext()) {
            LayoutRow row = (LayoutRow) it.next();
            row.top = row.top + offset;
            row.bottom = row.bottom + offset;
        }
        offsetChildrenVertical(offset);
    }

    private void addRow(Recycler recycler, State state, boolean isTop, int adapterPosition, int top) {
        int left = getPaddingLeft();
        int right = getWidth() - getPaddingRight();
        if (isTop && this.mFloatingHeaderView != null && adapterPosition == this.mFloatingHeaderPosition) {
            removeFloatingHeader(recycler);
        }
        if (this.mAdapter.getItemViewInternalType(adapterPosition) == 0) {
            int margin;
            View v = recycler.getViewForPosition(adapterPosition);
            if (isTop) {
                addView(v, this.mHeadersStartPosition);
            } else {
                addView(v);
            }
            measureChildWithMargins(v, 0, 0);
            int height = getDecoratedMeasuredHeight(v);
            if (height >= this.mHeaderOverlapMargin) {
                margin = this.mHeaderOverlapMargin;
            } else {
                margin = height;
            }
            if (isTop) {
                layoutDecorated(v, left, (top - height) + margin, right, top + margin);
                this.mLayoutRows.add(0, new LayoutRow(v, adapterPosition, 1, (top - height) + margin, top));
            } else {
                layoutDecorated(v, left, top, right, top + height);
                this.mLayoutRows.add(new LayoutRow(v, adapterPosition, 1, top, (top + height) - margin));
            }
            this.mAverageHeaderHeight = height - margin;
        } else if (isTop) {
           FillResult result = fillTopRow(recycler, state, adapterPosition, top);
            this.mLayoutRows.add(0, new LayoutRow(result.adapterPosition, result.length, top - result.height, top));
        } else {
            FillResult result = fillBottomRow(recycler, state, adapterPosition, top);
            this.mLayoutRows.add(new LayoutRow(result.adapterPosition, result.length, top, result.height + top));
        }
    }

    private void addOffScreenRows(Recycler recycler, State state, int recyclerTop, int recyclerBottom, boolean bottom) {
        int adapterPosition;
        if (bottom) {
            while (true) {
                LayoutRow bottomRow = getBottomRow();
                adapterPosition = bottomRow.adapterPosition + bottomRow.length;
                if (bottomRow.bottom < getExtraLayoutSpace(state) + recyclerBottom && adapterPosition < state.getItemCount()) {
                    addRow(recycler, state, false, adapterPosition, bottomRow.bottom);
                } else {
                    return;
                }
            }
        }
        while (true) {
            LayoutRow topRow = getTopRow();
            adapterPosition = topRow.adapterPosition - 1;
            if (topRow.top < recyclerTop - getExtraLayoutSpace(state)) {
                return;
            }
            if (adapterPosition >= 0) {
                addRow(recycler, state, true, adapterPosition, topRow.top);
            } else {
                return;
            }
        }
    }

    public int scrollVerticallyBy(int dy, Recycler recycler, State state) {
        if (getChildCount() == 0) {
            return 0;
        }
        boolean z;
        int scrolled = 0;
        int left = getPaddingLeft();
        int right = getWidth() - getPaddingRight();
        int recyclerTop = getPaddingTop();
        int recyclerBottom = getHeight() - getPaddingBottom();
        int firstHeader = getFirstVisibleSectionHeader();
        if (firstHeader != -1) {
            ((LayoutRow) this.mLayoutRows.get(firstHeader)).headerView.offsetTopAndBottom(-this.mStickOffset);
        }
        int scrollChunk;
        int adapterPosition;
        if (dy < 0) {
            while (scrolled > dy) {
                LayoutRow topRow = getTopRow();
                scrollChunk = Math.min(Math.max((-topRow.top) + recyclerTop, 0), scrolled - dy);
                offsetRowsVertical(scrollChunk);
                scrolled -= scrollChunk;
                adapterPosition = topRow.adapterPosition - 1;
                if (scrolled <= dy || adapterPosition >= state.getItemCount()) {
                    break;
                } else if (adapterPosition < 0) {
                    break;
                } else {
                    addRow(recycler, state, true, adapterPosition, topRow.top);
                }
            }
        } else {
            while (scrolled < dy) {
                LayoutRow bottomRow = getBottomRow();
                scrollChunk = -Math.min(Math.max(bottomRow.bottom - recyclerBottom, 0), dy - scrolled);
                offsetRowsVertical(scrollChunk);
                scrolled -= scrollChunk;
                adapterPosition = bottomRow.adapterPosition + bottomRow.length;
                if (scrolled >= dy || adapterPosition >= state.getItemCount()) {
                    break;
                }
                addRow(recycler, state, false, adapterPosition, bottomRow.bottom);
            }
        }
        if (scrolled == dy) {
            addOffScreenRows(recycler, state, recyclerTop, recyclerBottom, dy >= 0);
        }
        if (dy >= 0) {
            z = true;
        } else {
            z = false;
        }
        clearViewsAndStickHeaders(recycler, state, z);
        return scrolled;
    }

    public int getFirstVisibleItemPosition(boolean visibleTop) {
        return getFirstVisiblePosition(1, visibleTop);
    }

    public int getLastVisibleItemPosition() {
        return getLastVisiblePosition(1);
    }

    public int getFirstVisibleHeaderPosition(boolean visibleTop) {
        return getFirstVisiblePosition(0, visibleTop);
    }

    public int getLastVisibleHeaderPosition() {
        return getLastVisiblePosition(0);
    }

    private int getFirstVisiblePosition(int type, boolean visibleTop) {
        if (type == 1 && this.mHeadersStartPosition <= 0) {
            return -1;
        }
        if (type == 0 && this.mHeadersStartPosition >= getChildCount()) {
            return -1;
        }
        int viewFrom = type == 1 ? 0 : this.mHeadersStartPosition;
        int viewTo = type == 1 ? this.mHeadersStartPosition : getChildCount();
        int recyclerTop = getPaddingTop();
        for (int i = viewFrom; i < viewTo; i++) {
            View v = getChildAt(i);
            int adapterPosition = getPosition(v);
            int headerHeight = getPositionSectionHeaderHeight(adapterPosition);
            int top = getDecoratedTop(v);
            int bottom = getDecoratedBottom(v);
            if (visibleTop) {
                if (top >= recyclerTop + headerHeight) {
                    return adapterPosition;
                }
            } else if (bottom >= recyclerTop + headerHeight) {
                return adapterPosition;
            }
        }
        return -1;
    }

    private int getLastVisiblePosition(int type) {
        if (type == 1 && this.mHeadersStartPosition <= 0) {
            return -1;
        }
        if (type == 0 && this.mHeadersStartPosition >= getChildCount()) {
            return -1;
        }
        int viewFrom = type == 1 ? this.mHeadersStartPosition - 1 : getChildCount() - 1;
        int viewTo = type == 1 ? 0 : this.mHeadersStartPosition;
        int recyclerBottom = getHeight() - getPaddingBottom();
        for (int i = viewFrom; i >= viewTo; i--) {
            View v = getChildAt(i);
            if (getDecoratedTop(v) < recyclerBottom) {
                return getPosition(v);
            }
        }
        return -1;
    }

    private LayoutRow getFirstVisibleRow() {
        int recyclerTop = getPaddingTop();
        Iterator it = this.mLayoutRows.iterator();
        while (it.hasNext()) {
            LayoutRow row = (LayoutRow) it.next();
            if (row.bottom > recyclerTop) {
                return row;
            }
        }
        return null;
    }

    private int getFirstVisibleSectionHeader() {
        int recyclerTop = getPaddingTop();
        int header = -1;
        int n = this.mLayoutRows.size();
        for (int i = 0; i < n; i++) {
            LayoutRow row = (LayoutRow) this.mLayoutRows.get(i);
            if (row.header) {
                header = i;
            }
            if (row.bottom > recyclerTop) {
                return header;
            }
        }
        return -1;
    }

    private LayoutRow getNextVisibleSectionHeader(int headerFrom) {
        int n = this.mLayoutRows.size();
        for (int i = headerFrom + 1; i < n; i++) {
            LayoutRow row = (LayoutRow) this.mLayoutRows.get(i);
            if (row.header) {
                return row;
            }
        }
        return null;
    }

    private LayoutRow getHeaderRow(int adapterPosition) {
        int n = this.mLayoutRows.size();
        for (int i = 0; i < n; i++) {
            LayoutRow row = (LayoutRow) this.mLayoutRows.get(i);
            if (row.header && row.adapterPosition == adapterPosition) {
                return row;
            }
        }
        return null;
    }

    private void removeFloatingHeader(Recycler recycler) {
        if (this.mFloatingHeaderView != null) {
            View view = this.mFloatingHeaderView;
            this.mFloatingHeaderView = null;
            this.mFloatingHeaderPosition = -1;
            removeAndRecycleView(view, recycler);
        }
    }

    private void onHeaderChanged(int section, View view, HeaderState state, int pushOffset) {
        if (!(this.mStickyHeaderSection == -1 || section == this.mStickyHeaderSection)) {
            onHeaderUnstick();
        }
        boolean headerStateChanged = (this.mStickyHeaderSection == section && this.mStickyHeadeState.equals(state) && !state.equals(HeaderState.PUSHED)) ? false : true;
        this.mStickyHeaderSection = section;
        this.mStickyHeaderView = view;
        this.mStickyHeadeState = state;
        if (headerStateChanged && this.mHeaderStateListener != null) {
            this.mHeaderStateListener.onHeaderStateChanged(section, view, state, pushOffset);
        }
    }

    private void onHeaderUnstick() {
        if (this.mStickyHeaderSection != -1) {
            if (this.mHeaderStateListener != null) {
                this.mHeaderStateListener.onHeaderStateChanged(this.mStickyHeaderSection, this.mStickyHeaderView, HeaderState.NORMAL, 0);
            }
            this.mStickyHeaderSection = -1;
            this.mStickyHeaderView = null;
            this.mStickyHeadeState = HeaderState.NORMAL;
        }
    }

    private void stickTopHeader(Recycler recycler) {
        int firstHeader = getFirstVisibleSectionHeader();
        int top = getPaddingTop();
        int left = getPaddingLeft();
        int right = getWidth() - getPaddingRight();
        HeaderState notifyState = HeaderState.NORMAL;
        int section;
        int offset;
        if (firstHeader != -1) {
            removeFloatingHeader(recycler);
            LayoutRow firstHeaderRow = (LayoutRow) this.mLayoutRows.get(firstHeader);
            section = this.mAdapter.getAdapterPositionSection(firstHeaderRow.adapterPosition);
            if (this.mAdapter.isSectionHeaderSticky(section)) {
                HeaderState headerState;
                LayoutRow nextHeaderRow = getNextVisibleSectionHeader(firstHeader);
                offset = 0;
                if (nextHeaderRow != null) {
                    int height = firstHeaderRow.getHeight();
                    offset = Math.min(Math.max(top - nextHeaderRow.top, -height) + height, height);
                }
                this.mStickOffset = (top - firstHeaderRow.top) - offset;
                firstHeaderRow.headerView.offsetTopAndBottom(this.mStickOffset);
                View access$1900 = firstHeaderRow.headerView;
                if (offset == 0) {
                    headerState = HeaderState.STICKY;
                } else {
                    headerState = HeaderState.PUSHED;
                }
                onHeaderChanged(section, access$1900, headerState, offset);
                return;
            }
            onHeaderUnstick();
            this.mStickOffset = 0;
            return;
        }
        LayoutRow firstVisibleRow = getFirstVisibleRow();
        if (firstVisibleRow != null) {
            section = this.mAdapter.getAdapterPositionSection(firstVisibleRow.adapterPosition);
            if (this.mAdapter.isSectionHeaderSticky(section)) {
                int headerPosition = this.mAdapter.getSectionHeaderPosition(section);
                if (this.mFloatingHeaderView == null || this.mFloatingHeaderPosition != headerPosition) {
                    removeFloatingHeader(recycler);
                    View v = recycler.getViewForPosition(headerPosition);
                    addView(v, this.mHeadersStartPosition);
                    measureChildWithMargins(v, 0, 0);
                    this.mFloatingHeaderView = v;
                    this.mFloatingHeaderPosition = headerPosition;
                }
                int height = getDecoratedMeasuredHeight(this.mFloatingHeaderView);
                offset = 0;
                if (getChildCount() - this.mHeadersStartPosition > 1) {
                    View nextHeader = getChildAt(this.mHeadersStartPosition + 1);
                    int contentHeight = Math.max(0, height - this.mHeaderOverlapMargin);
                    offset = Math.max(top - getDecoratedTop(nextHeader), -contentHeight) + contentHeight;
                }
                layoutDecorated(this.mFloatingHeaderView, left, top - offset, right, (top + height) - offset);
                onHeaderChanged(section, this.mFloatingHeaderView, offset == 0 ? HeaderState.STICKY : HeaderState.PUSHED, offset);
                return;
            }
            onHeaderUnstick();
            return;
        }
        onHeaderUnstick();
    }

    private void updateTopPosition() {
        if (getChildCount() == 0) {
            this.mAnchor.reset();
        }
        LayoutRow firstVisibleRow = getFirstVisibleRow();
        if (firstVisibleRow != null) {
            this.mAnchor.section = this.mAdapter.getAdapterPositionSection(firstVisibleRow.adapterPosition);
            this.mAnchor.item = this.mAdapter.getItemSectionOffset(this.mAnchor.section, firstVisibleRow.adapterPosition);
            this.mAnchor.offset = Math.min(firstVisibleRow.top - getPaddingTop(), 0);
        }
    }

    private int getViewType(View view) {
        return getItemViewType(view) & 255;
    }

    private int getViewType(int position) {
        return this.mAdapter.getItemViewType(position) & 255;
    }

    private void clearState() {
        this.mHeadersStartPosition = 0;
        this.mStickOffset = 0;
        this.mFloatingHeaderView = null;
        this.mFloatingHeaderPosition = -1;
        this.mAverageHeaderHeight = 0;
        this.mLayoutRows.clear();
        if (this.mStickyHeaderSection != -1) {
            if (this.mHeaderStateListener != null) {
                this.mHeaderStateListener.onHeaderStateChanged(this.mStickyHeaderSection, this.mStickyHeaderView, HeaderState.NORMAL, 0);
            }
            this.mStickyHeaderSection = -1;
            this.mStickyHeaderView = null;
            this.mStickyHeadeState = HeaderState.NORMAL;
        }
    }

    public int computeVerticalScrollExtent(State state) {
        if (this.mHeadersStartPosition == 0 || state.getItemCount() == 0) {
            return 0;
        }
        View startChild = getChildAt(0);
        View endChild = getChildAt(this.mHeadersStartPosition - 1);
        if (startChild == null || endChild == null) {
            return 0;
        }
        return Math.abs(getPosition(startChild) - getPosition(endChild)) + 1;
    }

    public int computeVerticalScrollOffset(State state) {
        if (this.mHeadersStartPosition == 0 || state.getItemCount() == 0) {
            return 0;
        }
        View startChild = getChildAt(0);
        View endChild = getChildAt(this.mHeadersStartPosition - 1);
        if (startChild == null || endChild == null) {
            return 0;
        }
        if (Math.max((-getTopRow().top) + getPaddingTop(), 0) == 0) {
            return 0;
        }
        int minPosition = Math.min(getPosition(startChild), getPosition(endChild));
        int maxPosition = Math.max(getPosition(startChild), getPosition(endChild));
        return Math.max(0, minPosition);
    }

    public int computeVerticalScrollRange(State state) {
        if (this.mHeadersStartPosition == 0 || state.getItemCount() == 0) {
            return 0;
        }
        View startChild = getChildAt(0);
        View endChild = getChildAt(this.mHeadersStartPosition - 1);
        if (startChild == null || endChild == null) {
            return 0;
        }
        return state.getItemCount();
    }
}
