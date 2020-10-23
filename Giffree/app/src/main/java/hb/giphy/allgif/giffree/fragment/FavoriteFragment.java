package hb.giphy.allgif.giffree.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import hb.giphy.allgif.giffree.Admob;
import hb.giphy.allgif.giffree.R;
import hb.giphy.allgif.giffree.activity.DetailGifActivity;
import hb.giphy.allgif.giffree.adapter.FavoriteRecycleAdapter;
import hb.giphy.allgif.giffree.model.FavoriteItemRealm;
import hb.giphy.allgif.giffree.util.LogUtil;
import io.realm.Realm;
import io.realm.RealmResults;

public class FavoriteFragment extends Fragment {
    private List<FavoriteItemRealm> mData = new ArrayList<>();
    private LinearLayout mLinearLayout;
    private FavoriteRecycleAdapter mAdapter;
    private GridView mGridview;
    private boolean mIsInit = false;
    boolean isFirst = true;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.favorite_fragment, container, false);
        mLinearLayout = (LinearLayout) root.findViewById(R.id.linear_no_gif);
        mGridview = (GridView) root.findViewById(R.id.grid_favorite);
        init();
        mAdapter = new FavoriteRecycleAdapter(getActivity(), mData);
        mGridview.setAdapter(mAdapter);
        mGridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FavoriteItemRealm item = mData.get(i);
                Intent intent = new Intent(getContext(), DetailGifActivity.class);
                intent.putExtra("gif_id", "");
                intent.putExtra("title", item.getTitle());
                intent.putExtra("gif_preview", item.getGifPreview());
                intent.putExtra("gif_link", item.getGifLink());
                intent.putExtra("gif_mp4", item.getMpfourLink());
                intent.putExtra("tags", "");
                startActivity(intent);
            }
        });
        if (mData.size() == 0) {
            mGridview.setVisibility(View.GONE);
            mLinearLayout.setVisibility(View.VISIBLE);
        }
        initAdmob();
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(isFirst){
            isFirst = false;
//            if (AppUtils.isShowAdnative()) {
//                admob.showNativeAd((LinearLayout) getView().findViewById(R.id.native_ad_admob_1));
//            }
        }

    }


    void init() {
        RealmResults<FavoriteItemRealm> results = Realm.getDefaultInstance().where(FavoriteItemRealm.class).findAll();
        for (FavoriteItemRealm temp : results) {
            mData.add(temp.clone());
        }
        LogUtil.d("Size of data: " + mData.size());
        mIsInit = true;
    }

    void refresh() {

        mData.clear();
        RealmResults<FavoriteItemRealm> results = Realm.getDefaultInstance().where(FavoriteItemRealm.class).findAll();
        for (FavoriteItemRealm temp : results) {
            mData.add(temp.clone());
        }

        LogUtil.d("Size of data: 2 " + mData.size());

        mAdapter.notifyDataSetChanged();
        if (mData.size() == 0) {
            mGridview.setVisibility(View.GONE);
            mLinearLayout.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (!mIsInit) {
            refresh();
        } else {
            mIsInit = false;
        }
        if (admob != null) {
            admob.pause();
        }
    }


    Admob admob;

    void initAdmob() {
        admob = new Admob(getContext());
    }

    @Override
    public void onPause() {
        super.onPause();
        if (admob != null) {
            admob.resume();
        }
        LogUtil.d("onPAUSE() PageFragment");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (admob != null) {
            admob.destroy();
        }
    }

}
