package com.workout.workout.adapter;

import android.support.v7.widget.RecyclerView.Adapter;
import android.view.View;
import android.view.ViewGroup;

import java.security.InvalidParameterException;
import java.util.ArrayList;

public abstract class StickyHeaderGridAdapter extends Adapter<StickyHeaderGridAdapter.ViewHolder> {
    public static final String TAG = "StickyHeaderGridAdapter";
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;
    private int[] mSectionIndices;
    private ArrayList<Section> mSections;
    private int mTotalItemNumber;

    public static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }

        public boolean isHeader() {
            return false;
        }

        public int getSectionItemViewType() {
            return StickyHeaderGridAdapter.externalViewType(getItemViewType());
        }
    }

    public static class HeaderViewHolder extends ViewHolder {
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }

        public boolean isHeader() {
            return true;
        }
    }

    public static class ItemViewHolder extends ViewHolder {
        public ItemViewHolder(View itemView) {
            super(itemView);
        }
    }

    private static class Section {
        private int itemNumber;
        private int length;
        private int position;

        private Section() {
        }
    }

    public abstract void onBindHeaderViewHolder(HeaderViewHolder headerViewHolder, int i);

    public abstract void onBindItemViewHolder(ItemViewHolder itemViewHolder, int i, int i2);

    public abstract HeaderViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup, int i);

    public abstract ItemViewHolder onCreateItemViewHolder(ViewGroup viewGroup, int i);

    private void calculateSections() {
        int s;
        this.mSections = new ArrayList();
        int total = 0;
        int ns = getSectionCount();
        for (s = 0; s < ns; s++) {
            Section section = new Section();
            section.position = total;
            section.itemNumber = getSectionItemCount(s);
            section.length = section.itemNumber + 1;
            this.mSections.add(section);
            total += section.length;
        }
        this.mTotalItemNumber = total;
        total = 0;
        this.mSectionIndices = new int[this.mTotalItemNumber];
        ns = getSectionCount();
        for (s = 0; s < ns; s++) {
            Section section = (Section) this.mSections.get(s);
            for (int i = 0; i < section.length; i++) {
                this.mSectionIndices[total + i] = s;
            }
            total += section.length;
        }
    }

    protected int getItemViewInternalType(int position) {
        int section = getAdapterPositionSection(position);
        return getItemViewInternalType(section, position - ((Section) this.mSections.get(section)).position);
    }

    private int getItemViewInternalType(int section, int position) {
        return position == 0 ? 0 : 1;
    }

    private static int internalViewType(int type) {
        return type & 255;
    }

    private static int externalViewType(int type) {
        return type >> 8;
    }

    public final int getItemCount() {
        if (this.mSections == null) {
            calculateSections();
        }
        return this.mTotalItemNumber;
    }

    public final ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int internalType = internalViewType(viewType);
        int externalType = externalViewType(viewType);
        switch (internalType) {
            case 0:
                return onCreateHeaderViewHolder(parent, externalType);
            case 1:
                return onCreateItemViewHolder(parent, externalType);
            default:
                throw new InvalidParameterException("Invalid viewType: " + viewType);
        }
    }

    public final void onBindViewHolder(ViewHolder holder, int position) {
        if (this.mSections == null) {
            calculateSections();
        }
        int section = this.mSectionIndices[position];
        int internalType = internalViewType(holder.getItemViewType());
        int externalType = externalViewType(holder.getItemViewType());
        switch (internalType) {
            case 0:
                onBindHeaderViewHolder((HeaderViewHolder) holder, section);
                return;
            case 1:
                ItemViewHolder itemHolder = (ItemViewHolder) holder;
                onBindItemViewHolder((ItemViewHolder) holder, section, getItemSectionOffset(section, position));
                return;
            default:
                throw new InvalidParameterException("invalid viewType: " + internalType);
        }
    }

    public final int getItemViewType(int position) {
        int section = getAdapterPositionSection(position);
        int sectionPosition = position - ((Section) this.mSections.get(section)).position;
        int internalType = getItemViewInternalType(section, sectionPosition);
        int externalType = 0;
        switch (internalType) {
            case 0:
                externalType = getSectionHeaderViewType(section);
                break;
            case 1:
                externalType = getSectionItemViewType(section, sectionPosition - 1);
                break;
        }
        return ((externalType & 255) << 8) | (internalType & 255);
    }

    private int getItemSectionHeaderPosition(int position) {
        return getSectionHeaderPosition(getAdapterPositionSection(position));
    }

    private int getAdapterPosition(int section, int offset) {
        if (this.mSections == null) {
            calculateSections();
        }
        if (section < 0) {
            throw new IndexOutOfBoundsException("section " + section + " < 0");
        } else if (section < this.mSections.size()) {
            return ((Section) this.mSections.get(section)).position + offset;
        } else {
            throw new IndexOutOfBoundsException("section " + section + " >=" + this.mSections.size());
        }
    }

    public int getItemSectionOffset(int section, int position) {
        if (this.mSections == null) {
            calculateSections();
        }
        if (section < 0) {
            throw new IndexOutOfBoundsException("section " + section + " < 0");
        } else if (section >= this.mSections.size()) {
            throw new IndexOutOfBoundsException("section " + section + " >=" + this.mSections.size());
        } else {
            Section sectionObject = (Section) this.mSections.get(section);
            int localPosition = position - sectionObject.position;
            if (localPosition < sectionObject.length) {
                return localPosition - 1;
            }
            throw new IndexOutOfBoundsException("localPosition: " + localPosition + " >=" + sectionObject.length);
        }
    }

    public int getAdapterPositionSection(int position) {
        if (this.mSections == null) {
            calculateSections();
        }
        if (getItemCount() == 0) {
            return -1;
        }
        if (position < 0) {
            throw new IndexOutOfBoundsException("position " + position + " < 0");
        } else if (position < getItemCount()) {
            return this.mSectionIndices[position];
        } else {
            throw new IndexOutOfBoundsException("position " + position + " >=" + getItemCount());
        }
    }

    public int getSectionHeaderPosition(int section) {
        return getAdapterPosition(section, 0);
    }

    public int getSectionItemPosition(int section, int position) {
        return getAdapterPosition(section, position + 1);
    }

    public int getSectionCount() {
        return 0;
    }

    public int getSectionItemCount(int section) {
        return 0;
    }

    public int getSectionHeaderViewType(int section) {
        return 0;
    }

    public int getSectionItemViewType(int section, int offset) {
        return 0;
    }

    public boolean isSectionHeaderSticky(int section) {
        return true;
    }

    public void notifyAllSectionsDataSetChanged() {
        calculateSections();
        notifyDataSetChanged();
    }

    public void notifySectionDataSetChanged(int section) {
        calculateSections();
        if (this.mSections == null) {
            notifyAllSectionsDataSetChanged();
            return;
        }
        Section sectionObject = (Section) this.mSections.get(section);
        notifyItemRangeChanged(sectionObject.position, sectionObject.length);
    }

    public void notifySectionHeaderChanged(int section) {
        calculateSections();
        if (this.mSections == null) {
            notifyAllSectionsDataSetChanged();
        } else {
            notifyItemRangeChanged(((Section) this.mSections.get(section)).position, 1);
        }
    }

    public void notifySectionItemChanged(int section, int position) {
        calculateSections();
        if (this.mSections == null) {
            notifyAllSectionsDataSetChanged();
            return;
        }
        Section sectionObject = (Section) this.mSections.get(section);
        if (position >= sectionObject.itemNumber) {
            throw new IndexOutOfBoundsException("Invalid index " + position + ", size is " + sectionObject.itemNumber);
        }
        notifyItemChanged((sectionObject.position + position) + 1);
    }

    public void notifySectionInserted(int section) {
        calculateSections();
        if (this.mSections == null) {
            notifyAllSectionsDataSetChanged();
            return;
        }
        Section sectionObject = (Section) this.mSections.get(section);
        notifyItemRangeInserted(sectionObject.position, sectionObject.length);
    }

    public void notifySectionItemInserted(int section, int position) {
        calculateSections();
        if (this.mSections == null) {
            notifyAllSectionsDataSetChanged();
            return;
        }
        Section sectionObject = (Section) this.mSections.get(section);
        if (position < 0 || position >= sectionObject.itemNumber) {
            throw new IndexOutOfBoundsException("Invalid index " + position + ", size is " + sectionObject.itemNumber);
        }
        notifyItemInserted((sectionObject.position + position) + 1);
    }

    public void notifySectionItemRangeInserted(int section, int position, int count) {
        calculateSections();
        if (this.mSections == null) {
            notifyAllSectionsDataSetChanged();
            return;
        }
        Section sectionObject = (Section) this.mSections.get(section);
        if (position < 0 || position >= sectionObject.itemNumber) {
            throw new IndexOutOfBoundsException("Invalid index " + position + ", size is " + sectionObject.itemNumber);
        } else if (position + count > sectionObject.itemNumber) {
            throw new IndexOutOfBoundsException("Invalid index " + (position + count) + ", size is " + sectionObject.itemNumber);
        } else {
            notifyItemRangeInserted((sectionObject.position + position) + 1, count);
        }
    }

    public void notifySectionRemoved(int section) {
        if (this.mSections == null) {
            calculateSections();
            notifyAllSectionsDataSetChanged();
            return;
        }
        Section sectionObject = (Section) this.mSections.get(section);
        calculateSections();
        notifyItemRangeRemoved(sectionObject.position, sectionObject.length);
    }

    public void notifySectionItemRemoved(int section, int position) {
        if (this.mSections == null) {
            calculateSections();
            notifyAllSectionsDataSetChanged();
            return;
        }
        Section sectionObject = (Section) this.mSections.get(section);
        if (position < 0 || position >= sectionObject.itemNumber) {
            throw new IndexOutOfBoundsException("Invalid index " + position + ", size is " + sectionObject.itemNumber);
        }
        calculateSections();
        notifyItemRemoved((sectionObject.position + position) + 1);
    }

    private void notifySectionItemRangeRemoved(int section, int position, int count) {
        if (this.mSections == null) {
            calculateSections();
            notifyAllSectionsDataSetChanged();
            return;
        }
        Section sectionObject = (Section) this.mSections.get(section);
        if (position < 0 || position >= sectionObject.itemNumber) {
            throw new IndexOutOfBoundsException("Invalid index " + position + ", size is " + sectionObject.itemNumber);
        } else if (position + count > sectionObject.itemNumber) {
            throw new IndexOutOfBoundsException("Invalid index " + (position + count) + ", size is " + sectionObject.itemNumber);
        } else {
            calculateSections();
            notifyItemRangeRemoved((sectionObject.position + position) + 1, count);
        }
    }
}
