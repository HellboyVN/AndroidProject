package lp.me.allgifs.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

import lp.me.allgifs.Admob;
import lp.me.allgifs.R;
import lp.me.allgifs.adapter.RecyclerViewDataAdapter;
import lp.me.allgifs.model.SectionDataModel;
import lp.me.allgifs.model.SingleItemModel;
import lp.me.allgifs.util.AppConstant;
import lp.me.allgifs.util.AppUtils;
import lp.me.allgifs.util.LogUtil;


public class ExploreFragment extends Fragment {

    ArrayList<SectionDataModel> allSampleData;
    int mCategoryId = 0;
    String[] listSections = null;
    boolean isFirst = true;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_explore, container, false);
        allSampleData = new ArrayList<SectionDataModel>();

        createDummyData();
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        RecyclerViewDataAdapter adapter = new RecyclerViewDataAdapter(getContext(), allSampleData);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        initAdmob();

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(isFirst){
            isFirst = false;
            if (AppUtils.isShowAdnative()) {
                admob.showNativeAd((LinearLayout) getView().findViewById(R.id.native_ad_admob_1));
            }
        }

    }

    public void createDummyData() {
        listSections = getResources().getStringArray(R.array.listExplore);
        if (listSections != null && listSections.length > 0) {
            for (int i = 0; i < listSections.length; i++) {
                mCategoryId++;
                SectionDataModel dm = new SectionDataModel();
                dm.setHeaderTitle(listSections[i]);
                ArrayList<SingleItemModel> singleItem = new ArrayList<SingleItemModel>();
                String[] links = getLinkByCategoryId();
                String[] tags = getTagByCategoryId();

                if (links != null && tags != null) {
                    int size = (tags.length > 10) ? 10 : tags.length;
                    for (int j = 0; j < size; j++) {
                        SingleItemModel item = new SingleItemModel();
                        item.setName(tags[j]);
                        item.setUrl(links[j]);
                        singleItem.add(item);
                    }
                }

                dm.setAllItemsInSection(singleItem);
                allSampleData.add(dm);
            }
        }


    }

    String[] getLinkByCategoryId() {
        String[] data = null;
        switch (mCategoryId) {
            case AppConstant.ID_ACTION_CATEGORY:
                data = getResources().getStringArray(R.array.actions_links);
                break;
            case AppConstant.ID_ANIMAL_CATEGORY:
                data = getResources().getStringArray(R.array.animals_links);
                break;
            case AppConstant.ID_ART_CATEGORY:
                data = getResources().getStringArray(R.array.art_links);
                break;
            case AppConstant.ID_CARTOON_CATEGORY:
                data = getResources().getStringArray(R.array.cartoons_links);
                break;
            case AppConstant.ID_FUNNY_CATEGORY:
                data = getResources().getStringArray(R.array.funny_links);
                break;
            case AppConstant.ID_FILM_CATEGORY:
                data = getResources().getStringArray(R.array.film_links);
                break;
            case AppConstant.ID_GAME_CATEGORY:
                data = getResources().getStringArray(R.array.games_links);
                break;
            case AppConstant.ID_ISLAMIC_CATEGORY:
                data = getResources().getStringArray(R.array.islamic_links);
                break;
            case AppConstant.ID_NATURE_CATEGORY:
                data = getResources().getStringArray(R.array.nature_links);
                break;
            case AppConstant.ID_NEWS_CATEGORY:
                data = getResources().getStringArray(R.array.news_links);
                break;
            case AppConstant.ID_SCIENCE_CATEGORY:
                data = getResources().getStringArray(R.array.science_links);
                break;
            case AppConstant.ID_SPORT_CATEGORY:
                data = getResources().getStringArray(R.array.sport_links);
                break;
            default:
                break;
        }
        LogUtil.d("Data length: " + data.length);
        return data;
    }

    String[] getTagByCategoryId() {
        String[] data = null;
        switch (mCategoryId) {
            case AppConstant.ID_ACTION_CATEGORY:
                data = getResources().getStringArray(R.array.actions_tags);
                break;
            case AppConstant.ID_ANIMAL_CATEGORY:
                data = getResources().getStringArray(R.array.animals_tags);
                break;
            case AppConstant.ID_ART_CATEGORY:
                data = getResources().getStringArray(R.array.art_tags);
                break;
            case AppConstant.ID_CARTOON_CATEGORY:
                data = getResources().getStringArray(R.array.cartoons_tags);
                break;
            case AppConstant.ID_FUNNY_CATEGORY:
                data = getResources().getStringArray(R.array.funny_tags);
                break;
            case AppConstant.ID_FILM_CATEGORY:
                data = getResources().getStringArray(R.array.film_tags);
                break;
            case AppConstant.ID_GAME_CATEGORY:
                data = getResources().getStringArray(R.array.games_tags);
                break;
            case AppConstant.ID_ISLAMIC_CATEGORY:
                data = getResources().getStringArray(R.array.islamic_tags);
                break;
            case AppConstant.ID_NATURE_CATEGORY:
                data = getResources().getStringArray(R.array.nature_tags);
                break;
            case AppConstant.ID_NEWS_CATEGORY:
                data = getResources().getStringArray(R.array.news_tags);
                break;
            case AppConstant.ID_SCIENCE_CATEGORY:
                data = getResources().getStringArray(R.array.science_tags);
                break;
            case AppConstant.ID_SPORT_CATEGORY:
                data = getResources().getStringArray(R.array.sport_tags);
                break;
            default:
                break;
        }
        LogUtil.d("Data length TAG: " + data.length);
        return data;
    }


    Admob admob;

    void initAdmob() {
        admob = new Admob(getContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (admob != null) {
            admob.pause();
        }
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