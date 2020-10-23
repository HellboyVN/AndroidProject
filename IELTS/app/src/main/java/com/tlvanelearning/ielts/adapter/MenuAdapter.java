package com.tlvanelearning.ielts.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tlvanelearning.ielts.R;
import com.tlvanelearning.ielts.common.EntryItem;
import com.tlvanelearning.ielts.common.Item;
import com.tlvanelearning.ielts.common.SectionItem;
import java.util.ArrayList;

public class MenuAdapter extends ArrayAdapter<Item> {
    private ArrayList<Item> items;
    private Context mContext;
    private Typeface typeface;
    private LayoutInflater vi;

    public MenuAdapter(Context context, ArrayList<Item> items) {
        super(context, 0, items);
        this.mContext = context;
        this.items = items;
        this.vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        Item i = (Item) this.items.get(position);
        if (i == null) {
            return rowView;
        }
        if (i.isSection()) {
            SectionItem si = (SectionItem) i;
            rowView = this.vi.inflate(R.layout.fragment_navigation_section, null);
            rowView.setOnClickListener(null);
            rowView.setOnLongClickListener(null);
            rowView.setLongClickable(false);
            TextView sectionView = (TextView) rowView.findViewById(R.id.list_item_section_text);
            this.typeface = Typeface.createFromAsset(this.mContext.getAssets(), "fonts/Roboto-Bold-Italic.ttf");
            sectionView.setTypeface(this.typeface);
            sectionView.setText(si.getTitle());
            return rowView;
        }
        EntryItem ei = (EntryItem) i;
        rowView = this.vi.inflate(R.layout.fragment_navigation_row, null);
        this.typeface = Typeface.createFromAsset(this.mContext.getAssets(), "fonts/Roboto-Bold.ttf");
        TextView title = (TextView) rowView.findViewById(R.id.title);
        title.setTypeface(this.typeface);
        TextView subtitle = (TextView) rowView.findViewById(R.id.subtitle);
        this.typeface = Typeface.createFromAsset(this.mContext.getAssets(), "fonts/Roboto-Light.ttf");
        subtitle.setTypeface(this.typeface);
        title.setText(ei.getTitle());
        if (TextUtils.isEmpty(ei.getSubtitle())) {
            subtitle.setVisibility(View.GONE);
            return rowView;
        }
        subtitle.setVisibility(View.VISIBLE);
        subtitle.setText(ei.getSubtitle());
        return rowView;
    }
}
