package com.tlvanelearning.ielts.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.tlvanelearning.ielts.R;
import com.tlvanelearning.ielts.common.AnswerItem;
import java.util.HashMap;
import java.util.List;

public class QuizAdapter extends BaseExpandableListAdapter {
    private Context _context;
    private HashMap<String, List<AnswerItem>> _listDataChild;
    private List<String> _listDataHeader;
    private boolean compare;
    private boolean quiz;

    public QuizAdapter(Context context, List<String> listDataHeader, HashMap<String, List<AnswerItem>> listChildData, boolean quiz) {
        _context = context;
        _listDataHeader = listDataHeader;
        _listDataChild = listChildData;
        this.quiz = quiz;
    }

    public Object getChild(int groupPosition, int childPosititon) {
        if (_listDataChild.containsKey(_listDataHeader.get(groupPosition))) {
            return ((List) _listDataChild.get(_listDataHeader.get(groupPosition))).get(childPosititon);
        }
        return null;
    }

    public long getChildId(int groupPosition, int childPosition) {
        return (long) childPosition;
    }

    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        AnswerItem answerItem = (AnswerItem) getChild(groupPosition, childPosition);
        if (convertView == null) {
            convertView = ((LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.test_list_item, null);
        }
        TextView answerText = (TextView) convertView.findViewById(R.id.list_item_text);
        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.list_item_cb);
        checkBox.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                onChilcClickListener(groupPosition, childPosition);
            }
        });
        answerText.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                onChilcClickListener(groupPosition, childPosition);
            }
        });
        checkBox.setChecked(answerItem.isChecked());
        answerText.setText(answerItem.getTitle());
        answerText.setTextColor(ContextCompat.getColor(_context, android.R.color.black));
        if (compare) {
            if (answerItem.compareValue(quiz)) {
                answerText.setTextColor(ContextCompat.getColor(_context, R.color.accent));
            } else if (answerItem.isChecked()) {
                answerText.setTextColor(ContextCompat.getColor(_context, R.color.accentColor));
            }
        }
        return convertView;
    }

    private void onChilcClickListener(int groupPosition, int childPosition) {
        try {
            List<AnswerItem> entryItems = _listDataChild.get(_listDataHeader.get(groupPosition));
            int i = 0;
            while (i < entryItems.size()) {
                (_listDataChild.get(_listDataHeader.get(groupPosition))).get(i).setChecked(i == childPosition);
                i++;
            }
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getChildrenCount(int groupPosition) {
        if (_listDataChild.containsKey(_listDataHeader.get(groupPosition))) {
            return ((List) _listDataChild.get(_listDataHeader.get(groupPosition))).size();
        }
        return 0;
    }

    public Object getGroup(int groupPosition) {
        return _listDataHeader.get(groupPosition);
    }

    public int getGroupCount() {
        return _listDataHeader.size();
    }

    public long getGroupId(int groupPosition) {
        return (long) groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        try {
            ((ExpandableListView) parent).expandGroup(groupPosition);
        } catch (Exception e) {
        }
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            convertView = ((LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.test_list_group, null);
        }
        TextView lblListHeader = (TextView) convertView.findViewById(R.id.list_group_text);
        lblListHeader.setTypeface(null, 1);
        lblListHeader.setText(headerTitle);
        return convertView;
    }

    public boolean hasStableIds() {
        return false;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void setCompare(boolean compare) {
        this.compare = compare;
    }
}
