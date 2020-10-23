package hb.homeworkout.homeworkouts.noequipment.fitnesspro.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;

import java.util.ArrayList;

import hb.homeworkout.homeworkouts.noequipment.fitnesspro.R;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.adapter.NewsNotificationListAdapter;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.constant.AppConstants;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.database.DatabaseManager;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.listener.OnListFragmentInteractionListener;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.managers.NativeAdsTaskManager;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.managers.PersistenceManager;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.modal.BaseModel;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.modal.News;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.util.AppUtil;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.util.SimpleDividerItemDecoration;

public class NewsListActivity extends BaseActivity implements OnListFragmentInteractionListener {
    public static final int NATIVE_APP_MAX_WIDTH = 1200;
    private static int availableWidthForNativeAds;
    Context context;
    private boolean isRead;
    News news;
    private ArrayList<News> newsList = new ArrayList();
    private NewsNotificationListAdapter notificationNewwsAdapter;
    private RecyclerView recyclerView;
    private TextView textViewEmpty;

    private class NotificationNewsListAsyncTask extends AsyncTask<Void, Void, Void> {
        private NotificationNewsListAsyncTask() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... voids) {
            NewsListActivity.this.prepareNotificationNewsList();
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            NewsListActivity.this.notificationNewwsAdapter.setRowItemList(NativeAdsTaskManager.prepareNativeAdsInNewsNotificationRowItemList(NewsListActivity.this, NewsListActivity.this.newsList));
            NewsListActivity.this.notificationNewwsAdapter.notifyDataSetChanged();
            if (NewsListActivity.this.newsList.isEmpty()) {
                NewsListActivity.this.textViewEmpty.setVisibility(View.VISIBLE);
            } else {
                NewsListActivity.this.textViewEmpty.setVisibility(View.GONE);
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_notification_list);
        loadBannerAdvertisement(this);
        availableWidthForNativeAds = calculateAvailableWidthForNativeAdView();
        getData();
        initializeView();
    }

    private void getData() {
        this.news = (News) getIntent().getParcelableExtra("PLAN_OBJECT");
    }

    private void initializeView() {
        this.context = this;
        setToolbar("Notifications", true);
        this.recyclerView = (RecyclerView) findViewById(R.id.newslistrecyclerView);
        this.textViewEmpty = (TextView) findViewById(R.id.textViewEmptyNotifications);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        this.notificationNewwsAdapter = new NewsNotificationListAdapter(this, this);
        this.recyclerView.setAdapter(this.notificationNewwsAdapter);
    }

    public void onResume() {
        super.onResume();
        new NotificationNewsListAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    private void prepareNotificationNewsList() {
        ArrayList<News> newsArrayList = DatabaseManager.getInstance(this).getNotificationNewsList();
        this.newsList.clear();
        this.newsList.addAll(newsArrayList);
    }

    public int getAvailableWidthForNativeAds() {
        return availableWidthForNativeAds;
    }

    public int getAvailableHeightForNativeAds() {
        return 140;
    }

    public int calculateAvailableWidthForNativeAdView() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int deviceWidthInDp = (int) (((float) displayMetrics.widthPixels) / displayMetrics.density);
        if (deviceWidthInDp > 1200) {
            deviceWidthInDp = 1200;
        }
        return (deviceWidthInDp - ((int) AppUtil.getDimenstionInDp(this, R.dimen.plan_workout_list_native_ads_margin))) - ((int) AppUtil.getDimenstionInDp(this, R.dimen.plan_workout_list_native_ads_margin));
    }

    public void onListFragmentInteraction(BaseModel model, int position) {
        if (model instanceof News) {
            final News news = (News) model;
            boolean showInterstitialAd = ((long) PersistenceManager.getNotificationActivityOpenCount()) % ((long) PersistenceManager.getNotification_interstitial_ads_interval()) == 0;
            if (this.interstitialAd != null && this.interstitialAd.isLoaded() && showInterstitialAd) {
                this.interstitialAd.setAdListener(new AdListener() {
                    public void onAdClosed() {
                        super.onAdClosed();
                        NewsListActivity.this.openNotificationActivity(news);
                    }
                });
                return;
            }
            openNotificationActivity(news);
        }
    }

    private void openNotificationActivity(News news) {
        Intent intent = new Intent(this, NotificationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(AppConstants.NOTIFICATION_DATE, news.getDate());
        bundle.putString(AppConstants.NOTIFICATION_BODY, news.getBody());
        bundle.putString(AppConstants.NOTIFICATION_IMAGE, news.getImage());
        bundle.putString("title", news.getTitle());
        bundle.putBoolean(AppConstants.NOTIFICATION_READ, news.isRead());
        bundle.putBoolean(AppConstants.NOTIFICATION_SEEN, news.isSeen());
        bundle.putBoolean("isComingFromNewsListActivity", true);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void onBackPressed() {
        try {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onBackPressed();
    }
}
