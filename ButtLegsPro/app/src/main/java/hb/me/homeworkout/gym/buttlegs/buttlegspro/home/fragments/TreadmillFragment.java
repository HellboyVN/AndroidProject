package hb.me.homeworkout.gym.buttlegs.buttlegspro.home.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.ListCallbackSingleChoice;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.R;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.ads.listener.IAdCardClicked;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.base.BackBaseActivity;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.custom.CustomPreviewActivity;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.MRecyclerAdapter;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.MainActivity;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.type.ExerciseType;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.listeners.EventCenter;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.listeners.EventsListener;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.listeners.IRateClicked;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.models.LevelData;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.models.MData;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.preview.LevelPreviewActivity;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.reminder.ReminderActivity;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.ConsKeys;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.LevelUtils;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.RecyclerItemClickListener;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.RecyclerItemClickListener.OnItemClickListener;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.SharedPrefsService;

public class TreadmillFragment extends Fragment implements IRateClicked, IAdCardClicked, EventsListener, ListCallbackSingleChoice, OnDateSetListener {
    MRecyclerAdapter mAdapter;
    MData mData;
    List<LevelData> mLevelDataList;
    MainActivity mParentActivity;
    @BindView(R.id.recyclerViewHome)
    RecyclerView mRecyclerView;
    private InterstitialAd interstitialAd;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tm, container, false);
        ButterKnife.bind((Object) this, view);
        EventCenter.getInstance().addIAPListener(this);
        this.mData = LevelUtils.getLevelsData(this.mParentActivity, 21);
        this.mData.setDataType(ConsKeys.EXERCISE_TM_TYPE);
        setupList(this.mData.getData());
        return view;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.mParentActivity = (MainActivity) context;
        }
    }

    private void setupList(final List<LevelData> dataList) {
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this.mParentActivity));
        this.mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        this.mLevelDataList = collectData(dataList);
        this.mAdapter = new MRecyclerAdapter(this.mParentActivity, this.mLevelDataList, this, this);
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this.mParentActivity, this.mRecyclerView, new OnItemClickListener() {
            public void onItemClick(View view, int position) {
                if (position >= 0 && position <= TreadmillFragment.this.mLevelDataList.size() - 1) {
                    LevelData levelData = (LevelData) TreadmillFragment.this.mLevelDataList.get(position);
                    switch (((LevelData) TreadmillFragment.this.mLevelDataList.get(position)).getViewType()) {
                        case 0:
                        case 2:
                        case 4:
                        case 8:
                            return;
                        case 3:
                            Intent intentCustom = new Intent(TreadmillFragment.this.mParentActivity, CustomPreviewActivity.class);
                            intentCustom.putExtra(ConsKeys.Oll_EXERCISE_KEY, TreadmillFragment.this.mData.getAllExercises());
                            intentCustom.putExtra(ConsKeys.EXERCISE_DATA_TYPE_KEY, ExerciseType.TREADMILL.getType());
                            TreadmillFragment.this.startActivityForResult(intentCustom, BackBaseActivity.REQUEST_LEVEL_TRAIN);
                            return;
                        case 7:
                            TreadmillFragment.this.startActivity(new Intent(TreadmillFragment.this.mParentActivity, ReminderActivity.class));
                            TreadmillFragment.this.removeReminderCard();
                            return;
                        default:
                            LevelData exercise = (LevelData) dataList.get(position);
                            Intent intent = new Intent(TreadmillFragment.this.mParentActivity, LevelPreviewActivity.class);
                            intent.putExtra(ConsKeys.EXERCISE_DATA_KEY, exercise);
                            intent.putExtra(ConsKeys.Oll_EXERCISE_KEY, TreadmillFragment.this.mData.getAllExercises());
                            TreadmillFragment.this.startActivityForResult(intent, ConsKeys.REQUEST_LEVEL_PREVIEW);
                            Random rand = new Random();
                            int n = rand.nextInt(3)+1;
                            Log.e("levan_new","show + n= "+ String.valueOf(n));
                            if(n==2) {
                                showInterFacebook(getActivity());
                            }
                            return;
                    }
                }
            }

            public void onItemLongClick(View view, int position) {
            }
        }));
    }
    protected void showInterFacebook(Context context){
        if(!SharedPrefsService.getInstance().getPurchasedRemoveAds(context)){
            interstitialAd = new InterstitialAd(context, "141466806566650_141467579899906");
            interstitialAd.setAdListener(new InterstitialAdListener() {
                @Override
                public void onInterstitialDisplayed(Ad ad) {

                }

                @Override
                public void onInterstitialDismissed(Ad ad) {

                }

                @Override
                public void onError(Ad ad, AdError adError) {

                }

                @Override
                public void onAdLoaded(Ad ad) {
                    interstitialAd.show();
                }

                @Override
                public void onAdClicked(Ad ad) {

                }

                @Override
                public void onLoggingImpression(Ad ad) {

                }
            });
            interstitialAd.loadAd();
        }
    }

    private LevelData identifyRemovableCard(int viewType) {
        LevelData levelData = new LevelData();
        for (LevelData levelData1 : this.mLevelDataList) {
            if (levelData1.getViewType() == viewType) {
                levelData = levelData1;
            }
        }
        return levelData;
    }

    private void removeReminderCard() {
        SharedPrefsService.getInstance().setRecommendCardShow(this.mParentActivity, true);
        this.mAdapter.removeAt(identifyRemovableCard(7));
    }

    private List<LevelData> collectData(List<LevelData> dataList) {
        LevelData label = new LevelData();
        label.setNameKey("label_workout");
        label.setViewType(0);
        dataList.add(0, label);
        LevelData customCard = new LevelData();
        customCard.setViewType(3);
        dataList.add(1, customCard);
        LevelData rateCard = new LevelData();
        rateCard.setViewType(2);
        LevelData reminderCard = new LevelData();
        reminderCard.setViewType(7);
        new LevelData().setViewType(8);
        if (SharedPrefsService.getInstance().getLastTab(this.mParentActivity) == 21) {
            if (!SharedPrefsService.getInstance().isRecommendCardShowed(this.mParentActivity)) {
                dataList.add(1, reminderCard);
            }
            if (!(SharedPrefsService.getInstance().getRatedStatus(this.mParentActivity) || SharedPrefsService.getInstance().getFirstTime(this.mParentActivity))) {
                dataList.add(1, rateCard);
            }
        }
//        if (!this.mParentActivity.isRemoveAdsPurchased()) {
//            for (int i = 0; i <= dataList.size(); i += 5) {
//                NativeExpressAdView adView = new NativeExpressAdView(this.mParentActivity);
//                LevelData adCard = new LevelData();
//                adCard.setViewType(4);
//                adCard.setAdView(adView);
//                dataList.add(i, adCard);
//            }
////            this.mParentActivity.setUpAndLoadNativeExpressAds(0, this.mRecyclerView, dataList);
//        }
        return dataList;
    }

    public void rateClicked(int position) {
        this.mAdapter.removeAt(position);
    }

    public void removeAdClicked(int position) {
        this.mParentActivity.onPurchaseClick();
    }

    public void updateRemoveAdsUI() {
        if (this.mAdapter != null) {
            this.mAdapter.removeAds();
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
    }

    public void onDestroy() {
        super.onDestroy();
        EventCenter.getInstance().removeIAPListener(this);
    }

    public void setWaitScreen(boolean set) {
    }

    public void iabSetupFailed() {
    }

    public void notifyAdapterUpdate() {
        this.mAdapter.notifyDataSetChanged();
    }

    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
        SharedPrefsService.getInstance().setWeightMetric(getContext(), which);
        this.mAdapter.notifyDataSetChanged();
        return true;
    }

    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Log.e("Data Weight", dayOfMonth + "/" + monthOfYear + "/" + year);
    }
}
