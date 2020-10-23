package hb.me.homeworkout.gym.buttlegs.buttlegspro.exersices;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;


import butterknife.BindView;
import butterknife.ButterKnife;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.R;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.exersices.ExerciseActivity.MoveReason;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.exersices.listeners.IProgressWheel;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.listeners.IExerciseNavigation;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.listeners.IRestCountDown;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.models.Exercise;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.AnimationService;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.DialogService;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.GlideHelper;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.ResourceService;

public class ExerciseFragment extends Fragment implements IRestCountDown, IProgressWheel, IExerciseNavigation {
    private static final String TAG = "FRAGMENT_EXERCISE";
    private boolean isFirstExec = false;
    @BindView(R.id.addTime)
    FloatingActionButton mAddTime;
    @BindView(R.id.dummyView)
    View mDummyView;
    @BindView(R.id.exerciseImage)
    ImageView mExImage;
    private Exercise mExercise;
    @BindView(R.id.exerciseView)
    public ExerciseView mExerciseView;
    boolean mIsFragmentVisible = false;
    boolean mIsLast = false;
    private int mPage;
    private ExerciseActivity mParentActivity;
    @BindView(R.id.restCountDown)
    ExerciseRestView mRestCountDown;
    private ExerciseState mState = ExerciseState.START;

    public static ExerciseFragment newInstance(int page, boolean isLast, Exercise exersice, int level) {
        Bundle args = new Bundle();
        args.putInt("page", page);
        args.putInt("level", level);
        args.putSerializable("exerciseData", exersice);
        if (isLast) {
            args.putBoolean("isLast", true);
        }
        ExerciseFragment fragment = new ExerciseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        boolean z = true;
        View view = inflater.inflate(R.layout.fragment_page, container, false);
        ButterKnife.bind((Object) this, view);
        this.mExercise = (Exercise) getArguments().get("exerciseData");
        this.mPage = getArguments().getInt("page");
        this.mIsLast = getArguments().containsKey("isLast");
        this.mExImage.setImageDrawable(null);
        int resourceId = ResourceService.getInstance().getdrawableResourceId(this.mExercise.getImgKey(), this.mParentActivity);
        if (resourceId != 0) {
            GlideHelper.loadResource(getActivity(), this.mExImage, resourceId);
        }
        this.mExImage.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (ExerciseFragment.this.mState == ExerciseState.PLAY) {
                    ExerciseFragment.this.mExerciseView.trigger();
                }
            }
        });
        this.mRestCountDown.init(this, this.mAddTime, this.mParentActivity.getMoveReason() != MoveReason.NATURAL_RIGHT);
        this.mExerciseView.setTitle(ResourceService.getInstance().getString(this.mExercise.getNameKey(), this.mParentActivity));
        this.mExerciseView.setExerciseNavigationListener(this);
        this.mExerciseView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        this.mExerciseView.setListener(this);
        this.mExerciseView.setTime(this.mExercise.getDuration().intValue());
        this.mExerciseView.setRepeatCount(this.mExercise.getRepetition());
        this.mExerciseView.setToInitialState();
        this.mDummyView.setVisibility(getPage() != 0 ? 8 : 0);
        ExerciseView exerciseView = this.mExerciseView;
        if (getPage() != 0) {
            z = false;
        }
        exerciseView.setFirstTime(z);
        this.mDummyView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                boolean z = true;
                if (ExerciseFragment.this.getPage() == 0 && !ExerciseFragment.this.isFirstExec) {
                    boolean z2;
                    ExerciseFragment.this.mState = ExerciseState.REST;
                    ExerciseFragment.this.mExerciseView.setVisibility(4);
                    ExerciseFragment.this.mRestCountDown.setVisibility(0);
                    ExerciseRestView exerciseRestView = ExerciseFragment.this.mRestCountDown;
                    if (ExerciseFragment.this.getPage() == 0) {
                        z2 = true;
                    } else {
                        z2 = false;
                    }
                    if (ExerciseFragment.this.mParentActivity.getMoveReason() == MoveReason.NATURAL_RIGHT) {
                        z = false;
                    }
                    exerciseRestView.startRest(z2, z);
                    ExerciseFragment.this.mDummyView.setVisibility(8);
                    ExerciseFragment.this.mExerciseView.setFirstTime(false);
                }
            }
        });
        return view;
    }

    private void showWheel() {
        AnimationService.getInstance().goToCenter(this.mExerciseView, new AnimatorListenerAdapter() {
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                ExerciseFragment.this.mExerciseView.setVisibility(0);
            }

            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                ExerciseFragment.this.mExerciseView.trigger();
            }
        });
        AnimationService.getInstance().goToLeft(this.mRestCountDown, new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                ExerciseFragment.this.mRestCountDown.setVisibility(4);
                ExerciseFragment.this.mRestCountDown.setText(ExerciseFragment.this.getStringResource(R.string.rest));
            }
        });
    }

    private void showRest() {
        AnimationService.getInstance().goToCenter(this.mRestCountDown, new AnimatorListenerAdapter() {
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                ExerciseFragment.this.mRestCountDown.setVisibility(0);
            }

            public void onAnimationEnd(Animator animation) {
                boolean z;
                boolean z2 = true;
                super.onAnimationEnd(animation);
                ExerciseRestView exerciseRestView = ExerciseFragment.this.mRestCountDown;
                if (ExerciseFragment.this.getPage() != 0 || ExerciseFragment.this.isFirstExec) {
                    z = false;
                } else {
                    z = true;
                }
                if (ExerciseFragment.this.mParentActivity.getMoveReason() == MoveReason.NATURAL_RIGHT) {
                    z2 = false;
                }
                exerciseRestView.startRest(z, z2);
            }
        });
        AnimationService.getInstance().goToLeft(this.mExerciseView, new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                ExerciseFragment.this.mExerciseView.setVisibility(4);
            }
        });
    }

    public void onTimerPlay() {
        this.mState = ExerciseState.PLAY;
        this.mExerciseView.updateTimer();
    }

    private void cancelTimers() {
        try {
            this.mExerciseView.cancelTimer();
            this.mRestCountDown.cancelTimer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onTimerPause() {
        int currPage = this.mParentActivity.getViewPager().getCurrentItem();
        cancelTimers();
        DialogService.getInstance().showPauseDialog(this.mParentActivity, this, this.mParentActivity.getiRestartLevelListener(), this, currPage == 0);
        this.mState = ExerciseState.PAUSE;
    }

    private void goToFinish() {
        if (this.mIsLast && this.mIsFragmentVisible) {
            this.mParentActivity.goToFinish();
        }
    }

    public int getPage() {
        return this.mPage;
    }

    public void onTimerFinish() {
        goToFinish();
        if (getActivity() != null && this.mRestCountDown != null) {
            this.mRestCountDown.setText(getStringResource(R.string.rest));
        }
    }

    public void onTimerRepeat(int currentRepeat) {
        this.mState = ExerciseState.REST;
        if (this.mParentActivity != null) {
            this.mParentActivity.setRepeatition(currentRepeat, this.mExercise.getRepetition());
        }
        showRest();
    }

    public void onPlayState() {
        if (this.mState == ExerciseState.PAUSE) {
            this.mExerciseView.playTimer();
        } else if (this.mState == ExerciseState.REST) {
            this.mRestCountDown.updateRestTimer();
        }
    }

    public void onPauseState() {
        if (this.mState == ExerciseState.PLAY) {
            this.mExerciseView.pauseTimer();
        } else if (this.mState == ExerciseState.REST) {
            DialogService.getInstance().showPauseDialog(this.mParentActivity, this, this.mParentActivity.getiRestartLevelListener(), this, this.mParentActivity.getViewPager().getCurrentItem() == 0);
            this.mRestCountDown.pauseRestTimer();
        }
    }

    public void setMenuVisibility(boolean visible) {
        boolean z = true;
        super.setMenuVisibility(visible);
        this.mIsFragmentVisible = visible;
        if (visible && getPage() != 0) {
            boolean z2;
            this.mState = ExerciseState.REST;
            this.mExerciseView.setVisibility(4);
            this.mRestCountDown.setVisibility(0);
            ExerciseRestView exerciseRestView = this.mRestCountDown;
            if (getPage() != 0 || this.isFirstExec) {
                z2 = false;
            } else {
                z2 = true;
            }
            if (this.mParentActivity.getMoveReason() == MoveReason.NATURAL_RIGHT) {
                z = false;
            }
            exerciseRestView.startRest(z2, z);
        }
    }

    public void restartFragment() {
        getFragmentManager().beginTransaction().detach(this).attach(this).commitAllowingStateLoss();
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.mParentActivity = (ExerciseActivity) context;
        }
    }

    public void onFinish() {
        showWheel();
    }

    public void onDestroy() {
        super.onDestroy();
        cancelTimers();
    }

    private String getStringResource(int res_id) {
        if (isAdded()) {
            return getString(res_id);
        }
        return "";
    }

    public void onNavigation(MoveReason moveReason) {
        cancelTimers();
        if (moveReason == MoveReason.MANUAL_RIGHT && this.mIsLast) {
            goToFinish();
        } else {
            restartFragment();
        }
        this.mParentActivity.onNavigation(moveReason);
    }
}
