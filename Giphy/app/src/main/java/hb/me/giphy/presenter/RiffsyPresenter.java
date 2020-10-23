package hb.me.giphy.presenter;

import android.content.Context;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

import hb.me.giphy.model.GifItem;
import hb.me.giphy.util.AppUtils;
import hb.me.giphy.util.LogUtil;


/**
 * Created by nxtruong on 7/12/2017.
 */

public class RiffsyPresenter extends BasePresenter {
    public interface IPageView {

        void showProgress();

        void hideProgress();

        void onError(Exception e);

        void onChangeData(List<GifItem> data, boolean isFirst);
    }

    private int mTemp = 0;
    private int mResolution;
    private String mTag;
    private String mNext = "";
    private String mRiffsyAPILink;
    private IPageView mView;

    public RiffsyPresenter(IPageView view, Context context, String query) {
        super(context);
        this.mView = view;
        if (context.getSharedPreferences("settings_data", 0).getString("resolution_pref", "").equals("normal")) {
            this.mResolution = 1;
        } else {
            this.mResolution = 0;
        }
        mTag = query.replace(" ", "+");
        mRiffsyAPILink = AppUtils.getFirstLinkRiffsy(mTag);
    }

    public RiffsyPresenter(IPageView view, Context context) {
        super(context);
        this.mView = view;
        if (context.getSharedPreferences("settings_data", 0).getString("resolution_pref", "").equals("normal")) {
            this.mResolution = 1;
        } else {
            this.mResolution = 0;
        }
    }

    public void loadTrending() {
        mRiffsyAPILink = AppUtils.getTrendingRiffsy();
        loadRiffy();
    }

    public void loadMoreTrending() {
        mRiffsyAPILink = AppUtils.getMoreTrendingLink(mNext);
        loadMore();
    }

    public void loadRiffy() {
        try {
            Ion.with(mContext).load(this.mRiffsyAPILink).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
                public void onCompleted(Exception e, JsonObject result) {
                    if (e == null) {
                        if (mView != null) mView.hideProgress();

                        LogUtil.d("Result : " + result);
                        mNext = result.getAsJsonObject().get("next").getAsString();
                        JsonArray results = result.getAsJsonArray("results");
                        if (results.size() < 50) {
                            mTemp = 1;
                        }
                        List<GifItem> gifItemList = new ArrayList<GifItem>();

                        for (int i = 0; i < results.size(); i++) {
                            JsonObject element = results.get(i).getAsJsonObject();
                            GifItem item = new GifItem();
                            item.setTitle(element.get("title").getAsString());

                            JsonObject media = element.getAsJsonArray("media").get(0).getAsJsonObject();
                            item.setTinyGifUrl(media.get("tinygif").getAsJsonObject().get("url").getAsString());
                            if (mResolution == 0) {
                                item.setKeyGifUrl(media.get("tinygif").getAsJsonObject().get("url").getAsString());
                            } else if (mResolution == 1) {
                                item.setKeyGifUrl(media.get("gif").getAsJsonObject().get("url").getAsString());
                            }
                            if (media.has("loopedmp4")) {
                                item.setLoopedMp4Url(media.get("loopedmp4").getAsJsonObject().get("url").getAsString());
                            }

                            item.setWidthTinyGif(media.get("tinygif").getAsJsonObject().getAsJsonArray("dims").get(0).getAsString());
                            item.setHeightTinyGif(media.get("tinygif").getAsJsonObject().getAsJsonArray("dims").get(1).getAsString());

                            int size = element.getAsJsonArray("tags").size();
                            String tag = "";
                            for (int s = 0; s < size; s++) {
                                StringBuilder stringBuilder = new StringBuilder();
                                tag = stringBuilder.append(tag)
                                        .append("#").append(element.getAsJsonArray("tags").get(s).getAsString())
                                        .append(" ").toString();
                            }
                            item.setTags(tag);
                            item.setUrl(element.get("url").getAsString());
                            gifItemList.add(item);
                        }
                        LogUtil.d("Size of response: " + mNext);

                        if (mView != null) {
                            mView.onChangeData(gifItemList, true);
                        }
                        return;
                    }


                    if (mView != null) {
                        mView.onError(e);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void loadMoreRiffy() {
        this.mRiffsyAPILink = AppUtils.getLoadMoreLinkRiffsy(mNext, mTag);
        LogUtil.d("Riffsy link: " + mRiffsyAPILink);
        loadMore();
    }

    void loadMore() {
        if (!this.mNext.equals("0")) {
            if (mView != null) {
                mView.showProgress();
            }
            try {
                Ion.with(mContext).load(this.mRiffsyAPILink).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e == null) {
                            if (mView != null) mView.hideProgress();
                            mNext = result.getAsJsonObject().get("next").getAsString();
                            JsonArray results = result.getAsJsonArray("results");
                            if (results.size() < 50) {
                                mTemp = 1;
                            }
                            List<GifItem> gifItemList = new ArrayList<GifItem>();

                            for (int i = 0; i < results.size(); i++) {
                                JsonObject element = results.get(i).getAsJsonObject();
                                GifItem item = new GifItem();
                                item.setTitle(element.get("title").getAsString());

                                JsonObject media = element.getAsJsonArray("media").get(0).getAsJsonObject();
                                item.setTinyGifUrl(media.get("tinygif").getAsJsonObject().get("url").getAsString());
                                if (mResolution == 0) {
                                    item.setKeyGifUrl(media.get("tinygif").getAsJsonObject().get("url").getAsString());
                                } else if (mResolution == 1) {
                                    item.setKeyGifUrl(media.get("gif").getAsJsonObject().get("url").getAsString());
                                }
                                if (media.has("loopedmp4")) {
                                    item.setLoopedMp4Url(media.get("loopedmp4").getAsJsonObject().get("url").getAsString());
                                }

                                item.setWidthTinyGif(media.get("tinygif").getAsJsonObject().getAsJsonArray("dims").get(0).getAsString());
                                item.setHeightTinyGif(media.get("tinygif").getAsJsonObject().getAsJsonArray("dims").get(1).getAsString());

                                int size = element.getAsJsonArray("tags").size();
                                String tag = "";
                                for (int s = 0; s < size; s++) {
                                    StringBuilder stringBuilder = new StringBuilder();
                                    tag = stringBuilder.append(tag)
                                            .append("#").append(element.getAsJsonArray("tags").get(s).getAsString())
                                            .append(" ").toString();
                                }
                                item.setTags(tag);
                                item.setUrl(element.get("url").getAsString());
                                gifItemList.add(item);
                            }
                            LogUtil.d("Size of response: " + mNext);

                            if (mView != null) {
                                mView.onChangeData(gifItemList, false);
                            }
                            return;
                        }


                        if (mView != null) {
                            mView.onError(e);
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
