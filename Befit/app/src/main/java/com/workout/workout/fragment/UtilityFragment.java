package com.workout.workout.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.workout.workout.R;
import com.workout.workout.activity.BMIActivity;
import com.workout.workout.activity.CalorieActivity;
import com.workout.workout.activity.FatCalculator;
import com.workout.workout.activity.ProteinActivity;
import com.workout.workout.adapter.UtilityAdapter;
import com.workout.workout.constant.AppConstants;
import com.workout.workout.listener.OnListFragmentInteractionListener;
import com.workout.workout.managers.NativeAdsTaskManager;
import com.workout.workout.modal.BaseModel;
import com.workout.workout.modal.Utility;
import com.workout.workout.util.AppUtil;
import com.workout.workout.util.SimpleDividerItemDecoration;
import java.util.ArrayList;

public class UtilityFragment extends BaseFragment implements OnListFragmentInteractionListener {
    public static final int NATIVE_APP_MAX_WIDTH = 1200;
    private static int availableWidthForNativeAds;
    private RecyclerView recyclerView;
    private UtilityAdapter utilityAdapter;
    private ArrayList<Utility> utilityList = new ArrayList();

    public static UtilityFragment newInstance() {
        UtilityFragment fragment = new UtilityFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_utility, container, false);
        if (FirebaseRemoteConfig.getInstance().getBoolean("utility_banner_ads_enable")) {
            loadBannerAdvertisement(view, AppConstants.ADMOB_UTILITY_BANNER_AD_ID);
        }
        availableWidthForNativeAds = calculateAvailableWidthForNativeAdView();
        Context context = view.getContext();
        this.recyclerView = (RecyclerView) view.findViewById(R.id.list);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        this.recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        this.utilityAdapter = new UtilityAdapter(getActivity(), this.utilityList, this, this);
        this.recyclerView.setAdapter(this.utilityAdapter);
        prepareUtilityList();
        return view;
    }

    private void prepareUtilityList() {
        Utility utility = new Utility();
        utility.setName("BMI Calculator");
        utility.setImage_name("@drawable/bmi_calc");
        utility.setImage_url("");
        this.utilityList.add(utility);
        utility = new Utility();
        utility.setName("Protein Calculator");
        utility.setImage_url("");
        utility.setImage_name("@drawable/protein_calc");
        this.utilityList.add(utility);
        utility = new Utility();
        utility.setName("Fat Calculator");
        utility.setImage_url("");
        utility.setImage_name("@drawable/fat");
        this.utilityList.add(utility);
        utility = new Utility();
        utility.setName("Calories Calculator");
        utility.setImage_url("");
        utility.setImage_name("@drawable/calories");
        this.utilityList.add(utility);
        this.utilityAdapter.setRowItemList(NativeAdsTaskManager.prepareNativeAdsInUtilityRowItemList(getActivity(), this.utilityList));
        this.utilityAdapter.notifyDataSetChanged();
    }

    public void onListFragmentInteraction(BaseModel model, int position) {
        try {
            Answers.getInstance().logContentView(new ContentViewEvent().putContentName(((Utility) model).getName()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if ((model instanceof Utility) && ((Utility) model).getName().equals("BMI Calculator")) {
            getActivity().startActivity(new Intent(getActivity(), BMIActivity.class));
        } else if ((model instanceof Utility) && ((Utility) model).getName().equals("Protein Calculator")) {
            getActivity().startActivity(new Intent(getActivity(), ProteinActivity.class));
        } else if ((model instanceof Utility) && ((Utility) model).getName().equals("Fat Calculator")) {
            getActivity().startActivity(new Intent(getActivity(), FatCalculator.class));
        } else if ((model instanceof Utility) && ((Utility) model).getName().equals("Calories Calculator")) {
            getActivity().startActivity(new Intent(getActivity(), CalorieActivity.class));
        }
    }

    public int getAvailableWidthForNativeAds() {
        return availableWidthForNativeAds;
    }

    public int getAvailableHeightForNativeAds() {
        return 140;
    }

    public int calculateAvailableWidthForNativeAdView() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int deviceWidthInDp = (int) (((float) displayMetrics.widthPixels) / displayMetrics.density);
        if (deviceWidthInDp > 1200) {
            deviceWidthInDp = 1200;
        }
        return (deviceWidthInDp - ((int) AppUtil.getDimenstionInDp(getActivity(), R.dimen.training_native_ads_margin))) - ((int) AppUtil.getDimenstionInDp(getActivity(), R.dimen.training_native_ads_margin));
    }
}
