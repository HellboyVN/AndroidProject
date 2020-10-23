package hb.me.homeworkout.gym.buttlegs.buttlegspro.home.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.google.android.gms.ads.NativeExpressAdView;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.R;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.ads.listener.IAdCardClicked;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.MainActivity;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.adapters.ChallengeRecyclerAdapter;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.lisener.IBeginChallenge;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.lisener.IDaySelect;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.models.ChallengeDay;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.models.ChallengeItem;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.type.ChallengeDaysType;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.type.ExerciseType;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.listeners.EventCenter;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.listeners.EventsListener;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.listeners.IRateClicked;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.models.LevelData;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.models.MData;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.preview.LevelPreviewActivity;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.reminder.ReminderActivity;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.ConsKeys;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.DialogService;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.LevelUtils;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.RateHelper;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.RecyclerItemClickListener;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.RecyclerItemClickListener.OnItemClickListener;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.SharedPrefsService;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.TypeFaceService;
import java.util.ArrayList;
import java.util.List;

public class Challenge30DaysFragment extends Fragment implements OnClickListener, IDaySelect, IBeginChallenge, IAdCardClicked, EventsListener, IRateClicked {
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
    @BindView(R.id.clOr)
    AppCompatTextView clOr;
    @BindView(R.id.clTitle)
    AppCompatTextView clTitle;
    @BindView(R.id.clWeightLose)
    LinearLayout clWeightLose;
    @BindView(R.id.clWeightLoseTitle)
    AppCompatTextView clWeightLoseTitle;
    private ChallengeRecyclerAdapter mChallengeRecyclerAdapter;
    private ChallengeDaysType mChallengeType = ChallengeDaysType.NONE;
    private MData mData;
    private List<ChallengeItem> mListData;
    private MainActivity mParentActivity;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

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
        this.clWeightLoseTitle.setTypeface(TypeFaceService.getInstance().getRobotoRegular(this.mParentActivity));
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.cLFirstLayout.setOnClickListener(this);
        this.cLSecondLayout.setOnClickListener(this);
        this.cLThirdLayout.setOnClickListener(this);
        this.clWeightLose.setOnClickListener(this);
        if (RateHelper.appInstalledOrNot(getContext(), ConsKeys.RECOMMENDED_LOSE_WEIGHT)) {
            this.clWeightLose.setVisibility(8);
            this.clOr.setVisibility(8);
        }
        this.mChallengeType = ChallengeDaysType.getByType(SharedPrefsService.getInstance().getChallengeType(getContext()));
        if (this.mChallengeType == ChallengeDaysType.NONE) {
            showChooseLevelLayout();
        } else {
            showChallengeLayout();
        }
    }

    private void showChooseLevelLayout() {
        this.mRecyclerView.setVisibility(8);
        this.cLLayout.setVisibility(0);
    }

    private void showChallengeLayout() {
        if (this.mData == null) {
            this.mData = LevelUtils.getLevelsData(this.mParentActivity, 22);
            this.mData.setDataType(null);
        }
        this.challengeCurrentDay = SharedPrefsService.getInstance().getChallengeCurrentDay(this.mParentActivity, this.mChallengeType.getType());
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
        this.mListData.add(new ChallengeItem());
        if (SharedPrefsService.getInstance().getLastTab(this.mParentActivity) == 22) {
            if (!SharedPrefsService.getInstance().isRecommendCardShowed(this.mParentActivity)) {
                ChallengeItem reminderCardItem = new ChallengeItem();
                reminderCardItem.setViewType(7);
                this.mListData.add(0, reminderCardItem);
            }
            if (!(SharedPrefsService.getInstance().getRatedStatus(this.mParentActivity) || SharedPrefsService.getInstance().getFirstTime(this.mParentActivity))) {
                ChallengeItem rateCardItem = new ChallengeItem();
                rateCardItem.setViewType(2);
                this.mListData.add(0, rateCardItem);
            }
        }
        ChallengeItem titleItemCard = new ChallengeItem();
        titleItemCard.setViewType(10);
        this.mListData.add(0, titleItemCard);
//        if (!this.mParentActivity.isRemoveAdsPurchased()) {
//            NativeExpressAdView adView = new NativeExpressAdView(this.mParentActivity);
//            ChallengeItem adCard = new ChallengeItem();
//            adCard.setViewType(4);
//            adCard.setAdView(adView);
//            this.mListData.add(0, adCard);
////            this.mParentActivity.setUpAndLoadNativeExpressAds(0, this.mRecyclerView, this.mListData);
//        }
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this.mParentActivity));
        this.mChallengeRecyclerAdapter = new ChallengeRecyclerAdapter(this.mParentActivity, this.mListData, this, this, this, this);
        this.mRecyclerView.setAdapter(this.mChallengeRecyclerAdapter);
        this.mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this.mParentActivity, this.mRecyclerView, new OnItemClickListener() {
            public void onItemClick(View view, int position) {
                if (position >= 0 && position <= Challenge30DaysFragment.this.mListData.size() - 1) {
                    ChallengeItem item = (ChallengeItem) Challenge30DaysFragment.this.mListData.get(position);
                    switch (item.getViewType()) {
                        case 2:
                        case 4:
                        case 10:
                            return;
                        case 7:
                            Challenge30DaysFragment.this.startActivity(new Intent(Challenge30DaysFragment.this.mParentActivity, ReminderActivity.class));
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
            SharedPrefsService.getInstance().setChallengeCurrentDay(this.mParentActivity, this.challengeCurrentDay, this.mChallengeType.getType());
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
        SharedPrefsService.getInstance().setChallengeType(this.mParentActivity, 0);
        showChooseLevelLayout();
    }

    public void onResetProgressClick() {
        DialogService.getInstance().showRestartProgressConfirmDialog(this.mParentActivity, new SingleButtonCallback() {
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                SharedPrefsService.getInstance().resetChallenge(Challenge30DaysFragment.this.mParentActivity, Challenge30DaysFragment.this.mChallengeType.getType());
                Challenge30DaysFragment.this.onChangePlanClick();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && requestCode == ConsKeys.REQUEST_LEVEL_PREVIEW && data.getBooleanExtra(ConsKeys.EXERCISE_DATA_IS_REST_KEY, false) && data.getIntExtra(ConsKeys.EXERCISE_DATA_CHALLENGE_DAY_KEY, 0) == this.challengeCurrentDay + 1) {
            this.challengeCurrentDay++;
            SharedPrefsService.getInstance().setChallengeCurrentDay(this.mParentActivity, this.challengeCurrentDay, this.mChallengeType.getType());
            this.mChallengeRecyclerAdapter.notifyDataSetChanged();
        }
    }

    public void beginChallenge() {
        startWorkout(this.mData.getDataByDay(SharedPrefsService.getInstance().getChallengeCurrentDay(this.mParentActivity, this.mChallengeType.getType()) + 1, this.mChallengeType.getScaleFactor()));
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

    public void updateRemoveAdsUI() {
        if (this.mChallengeRecyclerAdapter != null) {
            this.mChallengeRecyclerAdapter.removeAds();
        }
    }

    public void setWaitScreen(boolean set) {
    }

    public void iabSetupFailed() {
    }

    public void notifyAdapterUpdate() {
    }

    public void rateClicked(int position) {
        if (this.mChallengeRecyclerAdapter != null) {
            this.mChallengeRecyclerAdapter.removeAt(identifyRemovableCard(2));
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
            case R.id.clWeightLose:
                updateChallengePlan(ChallengeDaysType.CHALLENGE);
                return;
            default:
                return;
        }
    }

    private void updateChallengePlan(ChallengeDaysType type) {
        switch (type) {
            case CHALLENGE:
                RateHelper.openPlayStore(getContext(), ConsKeys.RECOMMENDED_LOSE_WEIGHT, getString(R.string.get_stronger));
                return;
            default:
                SharedPrefsService.getInstance().setChallengeType(getContext(), type.getType());
                this.mChallengeType = type;
                showChallengeLayout();
                return;
        }
    }
}
