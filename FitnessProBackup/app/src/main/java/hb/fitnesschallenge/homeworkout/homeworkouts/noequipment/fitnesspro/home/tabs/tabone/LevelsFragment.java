package hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.home.tabs.tabone;

import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.R;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.ads.listener.IAdCardClicked;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.base.BackBaseActivity;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.custom.CustomPreviewActivity;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.home.MainActivity;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.home.tabs.BaseFragment;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.home.tabs.listener.IWeightChang;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.home.tabs.listener.MIAPListener;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.listeners.EventCenter;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.listeners.IRateClicked;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.models.LevelData;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.preview.LevelPreviewActivity;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.utils.ConsKeys;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.utils.DataPick;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.utils.RecyclerItemClickListener;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.utils.RecyclerItemClickListener.OnItemClickListener;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.utils.SharedPrefsService;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.utils.TypeFaceService;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.utils.WeightMetricDialog;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.ListCallbackSingleChoice;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.google.android.gms.ads.AdView;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener;
import java.util.List;

public class LevelsFragment extends BaseFragment implements IRateClicked, MIAPListener, IAdCardClicked, IWeightChang, ListCallbackSingleChoice {
    static final /* synthetic */ boolean $assertionsDisabled = (!LevelsFragment.class.desiredAssertionStatus());
    public static final String ARG_INITIAL_POSITION = "ARG_INITIAL_POSITION";
    List<LevelData> dataList;
    private View headerView;
    private MRecyclerAdapter mAdapter;
    private MainActivity mainActivity;
    private Typeface rBold;
    private Typeface rLight;
    @BindView(R.id.scrollable)
    ObservableRecyclerView recyclerView;
    private ViewPager viewPager;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventCenter.getInstance().addIAPListener(this);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_level, container, false);
        ButterKnife.bind((Object) this, view);
        this.headerView = LayoutInflater.from(getActivity()).inflate(R.layout.padding, null);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this.mainActivity));
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
        this.dataList = this.mainActivity.getDatas().getData();
        setupList(this.dataList);
        if (this.mainActivity instanceof ObservableScrollViewCallbacks) {
            Bundle args = getArguments();
            if (args != null && args.containsKey("ARG_INITIAL_POSITION")) {
                final int initialPosition = args.getInt("ARG_INITIAL_POSITION", 0);
                ScrollUtils.addOnGlobalLayoutListener(this.recyclerView, new Runnable() {
                    public void run() {
                        LevelsFragment.this.recyclerView.scrollVerticallyToPosition(initialPosition);
                    }
                });
            }
            this.recyclerView.setTouchInterceptionViewGroup((ViewGroup) this.mainActivity.findViewById(R.id.root));
            this.recyclerView.setScrollViewCallbacks(this.mainActivity);
        }
        return view;
    }

    private void setupList(List<LevelData> levelDatas) {
        this.dataList = collectData(levelDatas);
        this.recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this.mainActivity, this.recyclerView, new OnItemClickListener() {
            public void onItemClick(View view, int position) {
                if (position - 1 >= 0) {
                    switch (((LevelData) LevelsFragment.this.dataList.get(position - 1)).getViewType()) {
                        case 3:
                            Intent intentCustom = new Intent(LevelsFragment.this.mainActivity, CustomPreviewActivity.class);
                            intentCustom.putExtra(ConsKeys.All_EXERCISE_KEY, LevelsFragment.this.mainActivity.getDatas().getAllExercises());
                            LevelsFragment.this.startActivityForResult(intentCustom, BackBaseActivity.REQUEST_LEVEL_PREVIEW);
                            return;
                        case 4:
                        case 5:
                        case 6:
                        case 8:
                            return;
                        case 7:
                            LevelsFragment.this.openReminderFragment();
                            LevelsFragment.this.removeReminderCard();
                            return;
                        default:
                            LevelData exercise = (LevelData) LevelsFragment.this.dataList.get(position - 1);
                            Intent intent = new Intent(LevelsFragment.this.mainActivity, LevelPreviewActivity.class);
                            intent.putExtra(ConsKeys.All_EXERCISE_KEY, LevelsFragment.this.mainActivity.getDatas().getAllExercises());
                            intent.putExtra(ConsKeys.EXERCISE_DATA_KEY, exercise);
                            LevelsFragment.this.startActivityForResult(intent, BackBaseActivity.REQUEST_LEVEL_PREVIEW);
                            return;
                    }
                }
            }

            public void onItemLongClick(View view, int position) {
            }
        }));
        this.mAdapter = new MRecyclerAdapter(this.mainActivity, this.dataList, this.headerView, this, this, this);
        this.recyclerView.setAdapter(this.mAdapter);
    }

    private void openReminderFragment() {
        this.viewPager = (ViewPager) this.mainActivity.findViewById(R.id.pager);
        this.viewPager.postDelayed(new Runnable() {
            public void run() {
                LevelsFragment.this.viewPager.setCurrentItem(LevelsFragment.this.viewPager.getCurrentItem() + 3, true);
            }
        }, 100);
    }

    public void rateClicked(int position) {
        this.mAdapter.removeAt(position > 0 ? position - 1 : 0);
    }

    private void removeReminderCard() {
        SharedPrefsService.getInstance().setRecommendCardShow(this.mainActivity, true);
        this.mAdapter.removeAt(identifyRemovableCard(7));
    }

    private LevelData identifyRemovableCard(int viewType) {
        LevelData levelData = new LevelData();
        for (LevelData levelData1 : this.dataList) {
            if (levelData1.getViewType() == viewType) {
                levelData = levelData1;
            }
        }
        return levelData;
    }

    private List<LevelData> collectData(List<LevelData> dataList) {
        LevelData label = new LevelData();
        label.setNameKey("label_workout");
        label.setViewType(5);
        LevelData customCard = new LevelData();
        customCard.setViewType(3);
        LevelData rateCard = new LevelData();
        rateCard.setViewType(4);
        LevelData reminderCard = new LevelData();
        reminderCard.setViewType(7);
        dataList.add(0, label);
        dataList.add(1, customCard);
        if (!SharedPrefsService.getInstance().isRecommendCardShowed(this.mainActivity)) {
            dataList.add(1, reminderCard);
        }
        if (!(SharedPrefsService.getInstance().getRatedStatus(getContext()) || SharedPrefsService.getInstance().getFirstTime(getContext()))) {
            dataList.add(1, rateCard);
        }
        if (!this.mainActivity.isProPackagePurchased()) {
            for (int i = 0; i < dataList.size(); i += 6) {
                LinearLayout adView = new LinearLayout(getActivity());
                LevelData adCard = new LevelData();
                adCard.setViewType(6);
                adCard.setAdView(adView);
                dataList.add(i, adCard);
            }
            this.mainActivity.setUpAndLoadNativeExpressAds(0, this.recyclerView, dataList,getActivity());
        }
        return dataList;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.mainActivity = (MainActivity) context;
        }
    }

    public void updateRemoveAdsUI() {
        if (this.mAdapter != null) {
            this.mAdapter.removeAds();
        }
    }

    public void setWaitScreen(boolean set) {
    }

    public void iabSetupFailed() {
    }

    public void whyAdDialog(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(1);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.unlock_dialog);
        dialog.setCancelable(true);
        dialog.show();
        Window window = dialog.getWindow();
        if ($assertionsDisabled || window != null) {
            window.setLayout(-1, -2);
            AppCompatButton unlockNow = (AppCompatButton) dialog.findViewById(R.id.dUnlockNow);
            AppCompatTextView descByRemoveAds = (AppCompatTextView) dialog.findViewById(R.id.descByRemoveAds);
            AppCompatTextView descYouHelpUs = (AppCompatTextView) dialog.findViewById(R.id.descYouHelpUs);
            AppCompatTextView descPresent = (AppCompatTextView) dialog.findViewById(R.id.descPresent);
            ((AppCompatTextView) dialog.findViewById(R.id.dialogTitle)).setTypeface(TypeFaceService.getInstance().getRobotoRegular(activity));
            descByRemoveAds.setTypeface(TypeFaceService.getInstance().getRobotoBold(activity));
            descYouHelpUs.setTypeface(TypeFaceService.getInstance().getRobotoBold(activity));
            unlockNow.setTypeface(TypeFaceService.getInstance().getRobotoRegular(activity));
            descPresent.setTypeface(TypeFaceService.getInstance().getRobotoRegular(activity));
            unlockNow.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    LevelsFragment.this.removeAdClicked(0);
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                }
            });
            return;
        }
        throw new AssertionError();
    }

    public void onDestroy() {
        super.onDestroy();
        EventCenter.getInstance().removeIAPListener(this);
    }

    public void removeAdClicked(int position) {
        this.mainActivity.onPurchaseClick();
    }

    public void whySeeAd() {
        whyAdDialog(this.mainActivity);
    }

    public void weightMetricChange() {
        WeightMetricDialog.getInstance().show(this.mainActivity, this);
    }

    public void weightEditTextFocus(View view) {
        ((InputMethodManager) this.mainActivity.getSystemService("input_method")).hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void weightCalendarClick(OnDateSetListener listener, int year, int monthOfYear, int dayOfMonth) {
        DataPick.getInstance().showDataPicker(this.mainActivity, listener, year, monthOfYear, dayOfMonth);
    }

    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
        SharedPrefsService.getInstance().setWeightMetric(getContext(), which);
        this.mAdapter.notifyDataSetChanged();
        return true;
    }
}
