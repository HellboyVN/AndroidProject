package hb.homeworkout.homeworkouts.noequipment.fitnesspro.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView.Adapter;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import hb.homeworkout.homeworkouts.noequipment.fitnesspro.R;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.listener.OnListFragmentInteractionListener;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.modal.News;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.modal.RowItem;

public class NewsNotificationListAdapter extends Adapter<NewsNotificationListAdapter.ViewHolder> {
    public static final int VIEW_TYPE_NATIVE_APP = 1;
    public static final int VIEW_TYPE_WORKOUT = 0;
    private Context context;
    private final OnListFragmentInteractionListener listClickListener;
    private List<RowItem> rowItemList;

    public class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        private final TextView body;
        private final TextView date;
        private final ImageView image;
        private final View mView;
        private News news;
        private final ProgressBar progressBar;
        private final TextView title;

        ViewHolder(View view) {
            super(view);
            this.mView = view;
            this.title = (TextView) view.findViewById(R.id.newsTitle);
            this.body = (TextView) view.findViewById(R.id.newsBody);
            this.image = (ImageView) view.findViewById(R.id.newsImage);
            this.progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            this.date = (TextView) view.findViewById(R.id.newsDate);
        }
    }

    public NewsNotificationListAdapter(Context context, OnListFragmentInteractionListener listClickListener) {
        this.context = context;
        this.listClickListener = listClickListener;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list_row, parent, false);
        return new ViewHolder(view);
    }

    public static String parseDate(String timeAtMiliseconds) {
        if (timeAtMiliseconds.equalsIgnoreCase("")) {
            return "";
        }
        String result = " ";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String todayDate = simpleDateFormat.format(new Date());
        Calendar calendar = Calendar.getInstance();
        long dayagolong = Long.valueOf(timeAtMiliseconds).longValue();
        calendar.setTimeInMillis(dayagolong);
        String agoformater = simpleDateFormat.format(calendar.getTime());
        try {
            long different = Math.abs(simpleDateFormat.parse(todayDate).getTime() - simpleDateFormat.parse(agoformater).getTime());
            long minutesInMilli = 1000 * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;
            long elapsedDays = different / daysInMilli;
            different %= daysInMilli;
            long elapsedHours = different / hoursInMilli;
            different %= hoursInMilli;
            long elapsedMinutes = different / minutesInMilli;
            different %= minutesInMilli;
            long elapsedSeconds = different / 1000;
            different %= 1000;
            if (elapsedDays == 0) {
                if (elapsedHours != 0) {
                    return String.valueOf(elapsedHours) + "h ago";
                }
                if (elapsedMinutes != 0) {
                    return String.valueOf(elapsedMinutes) + "m ago";
                }
                if (elapsedSeconds < 0) {
                    return "0 s";
                }
                if (elapsedSeconds < 59) {
                    return elapsedSeconds + "s ago";
                }
                return result;
            } else if (elapsedDays <= 29) {
                return String.valueOf(elapsedDays) + "d ago";
            } else {
                if (elapsedDays > 29 && elapsedDays <= 58) {
                    return "1Mth ago";
                }
                if (elapsedDays > 58 && elapsedDays <= 87) {
                    return "2Mth ago";
                }
                if (elapsedDays > 87 && elapsedDays <= 116) {
                    return "3Mth ago";
                }
                if (elapsedDays > 116 && elapsedDays <= 145) {
                    return "4Mth ago";
                }
                if (elapsedDays > 145 && elapsedDays <= 174) {
                    return "5Mth ago";
                }
                if (elapsedDays > 174 && elapsedDays <= 203) {
                    return "6Mth ago";
                }
                if (elapsedDays > 203 && elapsedDays <= 232) {
                    return "7Mth ago";
                }
                if (elapsedDays > 232 && elapsedDays <= 261) {
                    return "8Mth ago";
                }
                if (elapsedDays > 261 && elapsedDays <= 290) {
                    return "9Mth ago";
                }
                if (elapsedDays > 290 && elapsedDays <= 319) {
                    return "10Mth ago";
                }
                if (elapsedDays > 319 && elapsedDays <= 348) {
                    return "11Mth ago";
                }
                if (elapsedDays > 348 && elapsedDays <= 360) {
                    return "12Mth ago";
                }
                if (elapsedDays > 360 && elapsedDays <= 720) {
                    return "1 year ago";
                }
                if (elapsedDays <= 720) {
                    return result;
                }
                simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                Calendar calendarYear = Calendar.getInstance();
                calendarYear.setTimeInMillis(dayagolong);
                return simpleDateFormat.format(calendarYear.getTime()) + "";
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return result;
        }
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        if (getItemViewType(position) == 0) {
            final News news = (News) this.rowItemList.get(position);
            String date = news.getDate();
            String image = news.getImage();
            String title = news.getTitle();
            String body = news.getBody();
            boolean read = news.isRead();
            boolean seen = news.isSeen();
            holder.title.setText(title);
            if (Long.parseLong(date) <= 6) {
                try {
                    holder.date.setText("");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    holder.date.setText(parseDate(date));
                } catch (NumberFormatException e2) {
                    e2.printStackTrace();
                }
            }
            holder.body.setText(Html.fromHtml(body));
            if (read) {
                holder.title.setTextColor(ContextCompat.getColor(this.context, R.color.notification_title_read_color));
                holder.body.setTextColor(ContextCompat.getColor(this.context, R.color.notification_title_read_color));
                holder.date.setTextColor(ContextCompat.getColor(this.context, R.color.notification_title_read_color));
                holder.body.setTypeface(null, 0);
                holder.date.setTypeface(null, 0);
                holder.title.setTypeface(null, 0);
            } else {
                holder.title.setTextColor(ContextCompat.getColor(this.context, R.color.notification_title_unread_color));
                holder.body.setTextColor(ContextCompat.getColor(this.context, R.color.notification_title_unread_color));
                holder.date.setTextColor(ContextCompat.getColor(this.context, R.color.notification_title_unread_color));
                holder.body.setTypeface(null, 1);
                holder.date.setTypeface(null, 1);
                holder.title.setTypeface(null, 1);
            }
            holder.progressBar.setVisibility(View.VISIBLE);
            final ViewHolder viewHolder = holder;
            Glide.with(this.context).load(image).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).listener(new RequestListener<String, GlideDrawable>() {
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    viewHolder.progressBar.setVisibility(View.GONE);
                    return false;
                }

                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    viewHolder.progressBar.setVisibility(View.GONE);
                    return false;
                }
            }).into(holder.image);
            final int i = position;
            holder.mView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (NewsNotificationListAdapter.this.listClickListener != null) {
                        NewsNotificationListAdapter.this.listClickListener.onListFragmentInteraction(news, i);
                    }
                }
            });
            return;
        }
    }

    public int getItemCount() {
        if (this.rowItemList != null) {
            return this.rowItemList.size();
        }
        return 0;
    }

    public void setRowItemList(List<RowItem> rowItemList) {
        this.rowItemList = rowItemList;
        notifyDataSetChanged();
    }

    public int getItemViewType(int position) {
        if (((RowItem) this.rowItemList.get(position)) instanceof News) {
            return 0;
        }
        return 0;
    }




}
