package hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.home.tabs.tabfour;

import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.R;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.ads.listener.IAdCardClicked;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.home.MainActivity;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.home.tabs.listener.MIAPListener;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.home.tabs.tabfour.adapter.ChallengeRecyclerAdapter;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.home.tabs.tabfour.liseners.IBeginChallenge;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.home.tabs.tabfour.liseners.IDaySelect;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.home.tabs.tabfour.model.ChallengeDay;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.home.tabs.tabfour.model.ChallengeItem;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.home.tabs.tabfour.type.ChallengeDaysType;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.home.tabs.tabfour.type.ExerciseType;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.listeners.EventCenter;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.listeners.IRateClicked;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.models.LevelData;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.models.Levels;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.preview.LevelPreviewActivity;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.utils.ConsKeys;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.utils.LevelUtils;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.utils.RateHelper;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.utils.RecyclerItemClickListener;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.utils.RecyclerItemClickListener.OnItemClickListener;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.utils.SharedPrefsService;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.utils.TypeFaceService;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.views.DialogService;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;

import java.util.ArrayList;
import java.util.List;

public class Challenge30DaysFragment extends Fragment implements OnClickListener, IDaySelect, IBeginChallenge, IAdCardClicked, MIAPListener, IRateClicked {
    public static final String ARG_INITIAL_POSITION = "ARG_INITIAL_POSITION";
    @BindView(R.id.cLFirstDesc)
    AppCompatTextView cLFirstDesc;
    @BindView(R.id.cLFirstLayout)
    LinearLayout cLFirstLayout;
    @BindView(R.id.cLFirstTitle)
    AppCompatTextView cLFirstTitle;
    @BindView(R.id.cLLayout)
    ScrollView cLLayout;
    @BindView(R.id.cLSecondDesc)
    AppCompatTextView cLSecondDesc;
    @BindView(R.id.cLSecondLayout)
    LinearLayout cLSecondLayout;
    @BindView(R.id.cLSecondTitle)
    AppCompatTextView cLSecondTitle;
    @BindView(R.id.cLThirdDesc)
    AppCompatTextView cLThirdDesc;
    @BindView(R.id.cLThirdLayout)
    LinearLayout cLThirdLayout;
    @BindView(R.id.cLThirdTitle)
    AppCompatTextView cLThirdTitle;
    private int challengeCurrentDay = 0;
    @BindView(R.id.chooseLevel)
    LinearLayout chooseLevel;
    @BindView(R.id.chooseLevel_icone)
    AppCompatImageView chooseLevelIcone;
    @BindView(R.id.chooseLevelTitle)
    AppCompatTextView chooseLevelTitle;
    @BindView(R.id.clOr)
    AppCompatTextView clOr;
    @BindView(R.id.clTitle)
    AppCompatTextView clTitle;
    private View headerView;
    private ChallengeRecyclerAdapter mChallengeRecyclerAdapter;
    private ChallengeDaysType mChallengeType = ChallengeDaysType.NONE;
    private Levels mData;
    private int mGender;
    private List<ChallengeItem> mListData;
    private MainActivity mParentActivity;
    @BindView(R.id.scrollable)
    ObservableRecyclerView mRecyclerView;
    private ViewPager viewPager;

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_challenge, container, false);
        ButterKnife.bind((Object) this, view);
        EventCenter.getInstance().addIAPListener(this);
        setFonts();
        return view;
    }

    private void setFonts() {
        this.clTitle.setTypeface(TypeFaceService.getInstance().getRobotoRegular(this.mParentActivity));
        this.cLFirstTitle.setTypeface(TypeFaceService.getInstance().getRobotoBold(this.mParentActivity));
        this.cLSecondTitle.setTypeface(TypeFaceService.getInstance().getRobotoBold(this.mParentActivity));
        this.cLThirdTitle.setTypeface(TypeFaceService.getInstance().getRobotoBold(this.mParentActivity));
        this.cLFirstDesc.setTypeface(TypeFaceService.getInstance().getRobotoRegular(this.mParentActivity));
        this.cLSecondDesc.setTypeface(TypeFaceService.getInstance().getRobotoRegular(this.mParentActivity));
        this.cLThirdDesc.setTypeface(TypeFaceService.getInstance().getRobotoRegular(this.mParentActivity));
        this.clOr.setTypeface(TypeFaceService.getInstance().getRobotoMedium(this.mParentActivity));
        this.chooseLevelTitle.setTypeface(TypeFaceService.getInstance().getRobotoRegular(this.mParentActivity));
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.cLFirstLayout.setOnClickListener(this);
        this.cLSecondLayout.setOnClickListener(this);
        this.cLThirdLayout.setOnClickListener(this);
        this.chooseLevel.setOnClickListener(this);
        openPrefferedApp(SharedPrefsService.getInstance().getGender(this.mParentActivity));
        this.mGender = SharedPrefsService.getInstance().getGender(getContext());
        this.mChallengeType = ChallengeDaysType.getByType(SharedPrefsService.getInstance().getChallengeType(getContext(), this.mGender));
        if (this.mChallengeType == ChallengeDaysType.NONE) {
            showChooseLevelLayout();
        } else {
            showChallengeLayout();
        }
    }

    private void openPrefferedApp(int gender) {
        if (gender == 1) {
            this.chooseLevelTitle.setText(this.mParentActivity.getString(R.string.rec_app_push_abs_name));
            this.chooseLevelIcone.setImageDrawable(ContextCompat.getDrawable(this.mParentActivity, R.drawable.ic_pushups));
            if (RateHelper.appInstalledOrNot(getContext(), ConsKeys.RECOMMENDED_APP_PUSH_UPS)) {
                this.chooseLevel.setVisibility(8);
                this.clOr.setVisibility(8);
                return;
            }
            return;
        }
        this.chooseLevelTitle.setText(this.mParentActivity.getString(R.string.rec_app_but_name));
        this.chooseLevelIcone.setImageDrawable(ContextCompat.getDrawable(this.mParentActivity, R.drawable.ic_lose_weight));
        if (RateHelper.appInstalledOrNot(getContext(), ConsKeys.RECOMMENDED_LOSE_WEIGHT)) {
            this.chooseLevel.setVisibility(8);
            this.clOr.setVisibility(8);
        }
    }

    private void showChooseLevelLayout() {
        this.mRecyclerView.setVisibility(8);
        this.cLLayout.setVisibility(0);
    }

    private void showChallengeLayout() {
        if (this.mData == null) {
            this.mData = LevelUtils.getChallenfData(this.mParentActivity);
            this.mData.setType(null);
        }
        this.challengeCurrentDay = SharedPrefsService.getInstance().getChallengeCurrentDay(this.mParentActivity, this.mChallengeType.getType(), this.mGender);
        setupList();
        this.mRecyclerView.setVisibility(0);
        this.cLLayout.setVisibility(8);
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.mParentActivity = (MainActivity) context;
        }
    }

    private void setupList() {
        this.mListData = new ArrayList();
        ChallengeItem titleItemCard = new ChallengeItem();
        titleItemCard.setViewType(10);
        this.mListData.add(titleItemCard);
        ChallengeItem challengeCard = new ChallengeItem();
        challengeCard.setViewType(11);
        this.mListData.add(challengeCard);
        if (!this.mParentActivity.isProPackagePurchased()) {
            LinearLayout adView = new LinearLayout(this.mParentActivity);
            ChallengeItem adCard = new ChallengeItem();
            adCard.setViewType(6);
            adCard.setAdView(adView);
            this.mListData.add(0, adCard);
            this.mParentActivity.setUpAndLoadNativeExpressAds(0, this.mRecyclerView, this.mListData,getActivity());
        }
        this.headerView = LayoutInflater.from(getActivity()).inflate(R.layout.padding, null);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this.mParentActivity));
        this.mChallengeRecyclerAdapter = new ChallengeRecyclerAdapter(this.mParentActivity, this.mListData, this.headerView, this, this, this, this);
        this.mRecyclerView.setAdapter(this.mChallengeRecyclerAdapter);
        if (this.mParentActivity instanceof ObservableScrollViewCallbacks) {
            Bundle args = getArguments();
            if (args != null && args.containsKey("ARG_INITIAL_POSITION")) {
                final int initialPosition = args.getInt("ARG_INITIAL_POSITION", 0);
                ScrollUtils.addOnGlobalLayoutListener(this.mRecyclerView, new Runnable() {
                    public void run() {
                        Challenge30DaysFragment.this.mRecyclerView.scrollVerticallyToPosition(initialPosition);
                    }
                });
            }
            this.mRecyclerView.setTouchInterceptionViewGroup((ViewGroup) this.mParentActivity.findViewById(R.id.root));
            this.mRecyclerView.setScrollViewCallbacks(this.mParentActivity);
        }
        this.mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this.mParentActivity, this.mRecyclerView, new OnItemClickListener() {
            public void onItemClick(View view, int position) {
                if (position >= 0 && position <= Challenge30DaysFragment.this.mListData.size() - 1) {
                    ChallengeItem item = (ChallengeItem) Challenge30DaysFragment.this.mListData.get(position);
                    switch (item.getViewType()) {
                        case 4:
                        case 6:
                        case 10:
                            return;
                        case 7:
                            Challenge30DaysFragment.this.openReminderFragment();
                            SharedPrefsService.getInstance().setRecommendCardShow(Challenge30DaysFragment.this.mParentActivity, true);
                            Challenge30DaysFragment.this.removeCard(item.getViewType());
                            return;
                        default:
                            return;
                    }
                }
            }

            public void onItemLongClick(View view, int position) {
            }
        }));
    }

    private void removeCard(int type) {
        SharedPrefsService.getInstance().setRecommendCardShow(this.mParentActivity, true);
        this.mChallengeRecyclerAdapter.removeAt(identifyRemovableCard(type));
    }

    private ChallengeItem identifyRemovableCard(int viewType) {
        for (ChallengeItem challengeItem : this.mListData) {
            if (challengeItem.getViewType() == viewType) {
                return challengeItem;
            }
        }
        return new ChallengeItem();
    }

    public void onDestroyView() {
        super.onDestroyView();
        EventCenter.getInstance().removeIAPListener(this);
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onDayClick(ChallengeDay challengeDay) {
        if (challengeDay == null) {
            this.challengeCurrentDay = 0;
            SharedPrefsService.getInstance().setChallengeCurrentDay(this.mParentActivity, this.challengeCurrentDay, this.mChallengeType.getType(), this.mGender);
            this.mChallengeRecyclerAdapter.notifyDataSetChanged();
        } else if (challengeDay.getDay() <= this.challengeCurrentDay) {
            startWorkout(this.mData.getDataByDay(challengeDay.getDay(), this.mChallengeType.getScaleFactor()));
        } else if (challengeDay.getDay() == this.challengeCurrentDay + 1) {
            startWorkout(this.mData.getDataByDay(challengeDay.getDay(), this.mChallengeType.getScaleFactor()));
        } else {
            Toast.makeText(this.mParentActivity, "Please complete previous level first", 0).show();
        }
    }

    public void onChangePlanClick() {
        SharedPrefsService.getInstance().setChallengeType(this.mParentActivity, 0, this.mGender);
        showChooseLevelLayout();
    }

    public void onResetProgressClick() {
        DialogService.getInstance().showRestartProgressConfirmDialog(this.mParentActivity, new SingleButtonCallback() {
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                SharedPrefsService.getInstance().resetChallenge(Challenge30DaysFragment.this.mParentActivity, Challenge30DaysFragment.this.mChallengeType.getType(), Challenge30DaysFragment.this.mGender);
                Challenge30DaysFragment.this.onChangePlanClick();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && requestCode == ConsKeys.REQUEST_LEVEL_PREVIEW && data != null && data.getBooleanExtra(ConsKeys.EXERCISE_DATA_IS_REST_KEY, false) && data.getIntExtra(ConsKeys.EXERCISE_DATA_CHALLENGE_DAY_KEY, 0) == this.challengeCurrentDay + 1) {
            this.challengeCurrentDay++;
            SharedPrefsService.getInstance().setChallengeCurrentDay(this.mParentActivity, this.challengeCurrentDay, this.mChallengeType.getType(), this.mGender);
            this.mChallengeRecyclerAdapter.notifyDataSetChanged();
        }
    }

    private void openReminderFragment() {
        this.viewPager = (ViewPager) this.mParentActivity.findViewById(R.id.pager);
        this.viewPager.postDelayed(new Runnable() {
            public void run() {
                Challenge30DaysFragment.this.viewPager.setCurrentItem(Challenge30DaysFragment.this.viewPager.getCurrentItem() + 1, true);
            }
        }, 100);
    }

    public void beginChallenge() {
        startWorkout(this.mData.getDataByDay(SharedPrefsService.getInstance().getChallengeCurrentDay(this.mParentActivity, this.mChallengeType.getType(), this.mGender) + 1, this.mChallengeType.getScaleFactor()));
    }

    private void startWorkout(LevelData levelData) {
        Intent intent = new Intent(this.mParentActivity, LevelPreviewActivity.class);
        intent.putExtra(ConsKeys.EXERCISE_DATA_KEY, levelData);
        intent.putExtra(ConsKeys.EXERCISE_DATA_TYPE_KEY, ExerciseType.CHALLENGE.getType());
        intent.putExtra(ConsKeys.EXERCISE_CHALLENGE_DAY_TYPE_KEY, this.mChallengeType.getType());
        startActivityForResult(intent, ConsKeys.REQUEST_LEVEL_PREVIEW);
    }

    public void removeAdClicked(int position) {
        this.mParentActivity.onPurchaseClick();
    }

    public void whySeeAd() {
    }

    public void updateRemoveAdsUI() {
        if (this.mChallengeRecyclerAdapter != null) {
            this.mChallengeRecyclerAdapter.removeAds();
        }
    }

    public void setWaitScreen(boolean set) {
    }

    public void iabSetupFailed() {
    }

    public void rateClicked(int position) {
        if (this.mChallengeRecyclerAdapter != null) {
            this.mChallengeRecyclerAdapter.removeAt(identifyRemovableCard(4));
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cLFirstLayout:
                updateChallengePlan(ChallengeDaysType.BEGINNER);
                return;
            case R.id.cLSecondLayout:
                updateChallengePlan(ChallengeDaysType.MEDIUM);
                return;
            case R.id.cLThirdLayout:
                updateChallengePlan(ChallengeDaysType.PROFI);
                return;
            case R.id.chooseLevel:
                updateChallengePlan(ChallengeDaysType.CHALLENGE);
                return;
            default:
                return;
        }
    }

    private void updateChallengePlan(ChallengeDaysType type) {
        switch (type) {
            case CHALLENGE:
                if (SharedPrefsService.getInstance().getGender(this.mParentActivity) == 1) {
                    RateHelper.openPlayStore(getContext(), ConsKeys.RECOMMENDED_APP_PUSH_UPS, getString(R.string.get_stronger));
                    return;
                } else {
                    RateHelper.openPlayStore(getContext(), ConsKeys.RECOMMENDED_LOSE_WEIGHT, getString(R.string.get_stronger));
                    return;
                }
            default:
                SharedPrefsService.getInstance().setChallengeType(getContext(), type.getType(), this.mGender);
                this.mChallengeType = type;
                showChallengeLayout();
                return;
        }
    }
}
