package com.tlvanelearning.ielts.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tlvanelearning.ielts.R;
import com.tlvanelearning.ielts.database.DatabaseIO;
import com.tlvanelearning.ielts.database.KEYConstants;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

public class ItemAdapter extends ArrayAdapter<JSONObject> implements KEYConstants {
    public ArrayList<JSONObject> filterList;
    public ArrayList<JSONObject> jsonObjects;
    private Context mContext;
    private int tableIndex = 1;
    private Typeface typeface;

    private static class Holder {
        private ImageButton favoriteButton;
        private TextView subtitle;
        private TextView title;

        private Holder() {
        }
    }

    public ItemAdapter(Context context, int resource, ArrayList<JSONObject> jsonObjects) {
        super(context, resource, jsonObjects);
        this.mContext = context;
        this.typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
        this.jsonObjects = jsonObjects;
        this.filterList = new ArrayList();
    }

    public int getCount() {
        return this.jsonObjects.size();
    }

    public JSONObject getItem(int position) {
        return (JSONObject) this.jsonObjects.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder;
        final JSONObject jsonObject = getItem(position);
        if (convertView == null) {
            holder = new Holder();
            convertView = ((LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.fragment_main_row, parent, false);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            this.typeface = Typeface.createFromAsset(this.mContext.getAssets(), "fonts/Roboto-Bold.ttf");
            holder.title.setTypeface(this.typeface);
            holder.subtitle = (TextView) convertView.findViewById(R.id.subtitle);
            this.typeface = Typeface.createFromAsset(this.mContext.getAssets(), "fonts/Roboto-Light.ttf");
            holder.favoriteButton = (ImageButton) convertView.findViewById(R.id.favoriteButton);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        try {
            holder.title.setText(jsonObject.getString("title").toString());
            if (jsonObject.has("desc")) {
                holder.subtitle.setVisibility(View.VISIBLE);
                holder.subtitle.setText(jsonObject.getString("desc").toString());
            } else {
                holder.subtitle.setVisibility(View.GONE);
            }
            if (jsonObject.has("favorite")) {
                holder.favoriteButton.setVisibility(View.VISIBLE);
                if (jsonObject.getString("favorite").equals("1")) {
                    holder.favoriteButton.setBackgroundResource(R.drawable.btn_favorited_selector);
                } else {
                    holder.favoriteButton.setBackgroundResource(R.drawable.btn_favorite_selector);
                }
                holder.favoriteButton.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        try {
                            boolean favorite = jsonObject.getString("favorite").equals("1");
                            if (DatabaseIO.database(mContext).updateFavoriteItem(jsonObject.getInt("id"), favorite, tableIndex) != -1) {
                                holder.favoriteButton.setBackgroundResource(favorite ? R.drawable.btn_favorite_selector : R.drawable.btn_favorited_selector);
                                getItem(position).put("favorite", favorite ? "0" : "1");
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            } else {
                holder.favoriteButton.setVisibility(View.GONE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return convertView;
    }

    public void filter(String newText) {
        try {
            newText = newText.toLowerCase(Locale.getDefault());
            this.jsonObjects.clear();
            if (newText.length() == 0) {
                this.jsonObjects.addAll(this.filterList);
            } else {
                Iterator it = this.filterList.iterator();
                while (it.hasNext()) {
                    JSONObject jsonObject = (JSONObject) it.next();
                    if (jsonObject.getString("title").toLowerCase(Locale.getDefault()).contains(newText)) {
                        this.jsonObjects.add(jsonObject);
                    }
                }
            }
            notifyDataSetChanged();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setTableIndex(int tableIndex) {
        this.tableIndex = tableIndex;
    }
}
