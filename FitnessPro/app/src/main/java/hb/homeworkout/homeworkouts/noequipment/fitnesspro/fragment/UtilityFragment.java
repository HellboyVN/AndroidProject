package hb.homeworkout.homeworkouts.noequipment.fitnesspro.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;

import hb.homeworkout.homeworkouts.noequipment.fitnesspro.R;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.activity.BMIActivity;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.activity.CalorieActivity;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.activity.FatCalculator;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.activity.ProteinActivity;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.adapter.UtilityAdapter;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.listener.OnListFragmentInteractionListener;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.managers.NativeAdsTaskManager;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.modal.BaseModel;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.modal.Utility;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.util.AppUtil;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.util.SimpleDividerItemDecoration;

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
        loadBannerAdvertisement(view);
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
        utility.setName("Protein Body Index");
        utility.setImage_url("");
        utility.setImage_name("@drawable/protein_calc");
        this.utilityList.add(utility);
        utility = new Utility();
        utility.setName("Fat Body Index");
        utility.setImage_url("");
        utility.setImage_name("@drawable/fat");
        this.utilityList.add(utility);
        utility = new Utility();
        utility.setName("BMI Body Index");
        utility.setImage_name("@drawable/bmi_calc");
        utility.setImage_url("");
        this.utilityList.add(utility);
        utility = new Utility();
        utility.setName("Calories Body Index");
        utility.setImage_url("");
        utility.setImage_name("@drawable/calories");
        this.utilityList.add(utility);
        this.utilityAdapter.setRowItemList(NativeAdsTaskManager.prepareNativeAdsInUtilityRowItemList(getActivity(), this.utilityList));
        this.utilityAdapter.notifyDataSetChanged();
    }

    public void onListFragmentInteraction(BaseModel model, int position) {
//        try {
//            Answers.getInstance().logContentView(new ContentViewEvent().putContentName(((Utility) model).getName()));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        if ((model instanceof Utility) && ((Utility) model).getName().equals("BMI Body Index")) {
            getActivity().startActivity(new Intent(getActivity(), BMIActivity.class));
        } else if ((model instanceof Utility) && ((Utility) model).getName().equals("Protein Body Index")) {
            getActivity().startActivity(new Intent(getActivity(), ProteinActivity.class));
        } else if ((model instanceof Utility) && ((Utility) model).getName().equals("Fat Body Index")) {
            getActivity().startActivity(new Intent(getActivity(), FatCalculator.class));
        } else if ((model instanceof Utility) && ((Utility) model).getName().equals("Calories Body Index")) {
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
