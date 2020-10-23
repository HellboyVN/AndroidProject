package hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.exersices;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics.Param;

import butterknife.BindView;
import butterknife.ButterKnife;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.R;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.exersices.ExerciseActivity.MoveReason;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.listeners.IProgressWheel;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.listeners.IRestCountDown;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.models.Exercise;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.utils.AnimationService;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.utils.GlideHelper;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.utils.ResourceService;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.utils.SharedPrefsService;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.utils.SoundHelper;

public class FragmentExercise extends Fragment implements IRestCountDown, IProgressWheel, IExerciseNavigation {
    private static final String TAG = "CountdownTimer";
    @BindView(R.id.exerciseView)
    public ExerciseView exerciseView;
    private boolean isFirstExec = false;
    @BindView(R.id.addTime)
    FloatingActionButton mAddTime;
    @BindView(R.id.dummyView)
    View mDummyView;
    @BindView( R.id.exerciseImage)
    AppCompatImageView mExImage;
    @BindView( R.id.ll_go_next)
    LinearLayout pauseFabNext;
    @BindView( R.id.ll_go_prev)
    LinearLayout pauseFabPrevious;
    @BindView( R.id.pauseFabPlay)
    ProgressBar mProggessBar;
    private Exercise mExercise;
    boolean mIsFragmentVisible = false;
    boolean mIsLast = false;
    private int mLevel;
    private int mPage;
    private ExerciseActivity mParentActivity;
    @BindView(R.id.restCountDown)
    ExerciseRestView mRestCountDown;
    private ExerciseState mState = ExerciseState.START;
    NonSwipeableViewPager viewPager;

    public static FragmentExercise newInstance(int page, boolean isLast, Exercise exersice, int level) {
        Bundle args = new Bundle();
        args.putInt("page", page);
        args.putInt(Param.LEVEL, level);
        args.putSerializable("exerciseData", exersice);
        if (isLast) {
            args.putBoolean("isLast", true);
        }
        FragmentExercise fragment = new FragmentExercise();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int i;
        boolean z = true;
        View view = inflater.inflate(R.layout.fragment_exersice, container, false);
        ButterKnife.bind((Object) this, view);
        this.viewPager = ExerciseActivity.mExercisePager;
        this.mExercise = (Exercise) getArguments().get("exerciseData");
        this.mLevel = getArguments().getInt(Param.LEVEL);
        this.mPage = getArguments().getInt("page");
        this.mIsLast = getArguments().containsKey("isLast");
        int resourceId = ResourceService.getInstance().getdrawableResourceId(this.mExercise.getImgKey(), this.mParentActivity);
        if (resourceId != 0) {
            GlideHelper.loadResource(getActivity(), this.mExImage, resourceId);
        }
        pauseFabNext.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                onNavigation(MoveReason.MANUAL_RIGHT);
            }
        });
        pauseFabPrevious.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                onNavigation(MoveReason.MANUAL_LEFT);
            }
        });
        this.mRestCountDown.init(this, this.mAddTime, this.mParentActivity.getMoveReason() != MoveReason.NATURAL_RIGHT);
        this.exerciseView.setTitle(ResourceService.getInstance().getString(this.mExercise.getNameKey(), this.mParentActivity));
        switch (SharedPrefsService.getInstance().getGender(getContext())) {
            case 1:
                this.exerciseView.setBarColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDarkMale));
                this.exerciseView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDarkMale));
                break;
            case 2:
                this.exerciseView.setBarColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDarkFemale));
                this.exerciseView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDarkFemale));
                break;
        }
        this.exerciseView.setListener(this);
        this.exerciseView.setNavigationListener(this);
        this.exerciseView.setTime(this.mExercise.getDuration().intValue());
        this.exerciseView.setRepeatCount(0);
        this.exerciseView.setToInitialState();
//        animateProgress(mProggessBar);
        View view2 = this.mDummyView;
        if (getPage() != 0) {
            i = 8;
        } else {
            i = 0;
        }
        view2.setVisibility(i);

        ExerciseView exerciseView = this.exerciseView;
        if (getPage() != 0) {
            z = false;
        }
        exerciseView.setFirstTime(z);
        this.mDummyView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                boolean z = true;
                if (FragmentExercise.this.getPage() == 0 && !FragmentExercise.this.isFirstExec) {
                    boolean z2;
                    FragmentExercise.this.mState = ExerciseState.REST;
                    FragmentExercise.this.exerciseView.setVisibility(4);
                    FragmentExercise.this.mRestCountDown.setVisibility(0);
                    pauseFabPrevious.setVisibility(4);
                    pauseFabNext.setVisibility(4);
                    mProggessBar.setVisibility(4);
                    ExerciseRestView exerciseRestView = FragmentExercise.this.mRestCountDown;
                    if (FragmentExercise.this.getPage() == 0) {
                        z2 = true;
                    } else {
                        z2 = false;
                    }
                    if (FragmentExercise.this.mParentActivity.getMoveReason() == MoveReason.NATURAL_RIGHT) {
                        z = false;
                    }
                    exerciseRestView.startRest(z2, z);
                    FragmentExercise.this.mDummyView.setVisibility(8);
                    FragmentExercise.this.exerciseView.setFirstTime(false);
                }
            }
        });
        this.mExImage.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (FragmentExercise.this.mState == ExerciseState.PLAY) {
                    FragmentExercise.this.exerciseView.trigger();
                }
            }
        });
        return view;
    }


    private void showWheel() {
        AnimationService.getInstance().goToCenter(this.mParentActivity, this.exerciseView, new AnimatorListenerAdapter() {
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                FragmentExercise.this.exerciseView.setVisibility(0);
                pauseFabPrevious.setVisibility(0);
                pauseFabNext.setVisibility(0);
                mProggessBar.setVisibility(0);
            }

            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (FragmentExercise.this.exerciseView != null) {
                    FragmentExercise.this.exerciseView.trigger();
                }
            }
        });
        AnimationService.getInstance().goToLeft(this.mParentActivity, this.mRestCountDown, new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (FragmentExercise.this.mRestCountDown != null) {
                    FragmentExercise.this.mRestCountDown.setVisibility(4);
                    FragmentExercise.this.mRestCountDown.setText(FragmentExercise.this.getString(R.string.rest));
                }
            }
        });
    }

    private void showRest() {
        AnimationService.getInstance().goToCenter(this.mParentActivity, this.mRestCountDown, new AnimatorListenerAdapter() {
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                FragmentExercise.this.mRestCountDown.setVisibility(0);
            }

            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
        AnimationService.getInstance().goToLeft(this.mParentActivity, this.exerciseView, new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                FragmentExercise.this.exerciseView.setVisibility(4);
                pauseFabPrevious.setVisibility(4);
                pauseFabNext.setVisibility(4);
                mProggessBar.setVisibility(4);
            }
        });
    }

    public void onTimerPlay() {
        this.mState = ExerciseState.PLAY;
        this.exerciseView.animateProgress(mProggessBar);
        this.exerciseView.updateTimer();
    }

    private void cancelTimers() {
        if (this.exerciseView != null) {
            this.exerciseView.cancelTimer();
        }
        if (this.mRestCountDown != null) {
            this.mRestCountDown.cancelTimer();
        }
    }

    public void onTimerPause() {
        int currPage = this.mParentActivity.getViewPager().getCurrentItem();
        cancelTimers();
        Log.e("hellboy","Pause1");
        if (SharedPrefsService.getInstance().getSoundStatus(getContext())) {
            SoundHelper.getInstance().playSound("pause");
        }
        Toast.makeText(getActivity(),"PAUSE",Toast.LENGTH_SHORT).show();
        //DialogService.getInstance().showPauseDialog(this.mParentActivity, this, this.mParentActivity.getiRestartLevelListener(), this, currPage == 0);
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
            showRest();
            this.mRestCountDown.setText(getString(R.string.rest));
        }
    }

    public void onTimerRepeat(int currentRepeat) {
    }

    public void onPlayState() {
        if (this.mState == ExerciseState.PAUSE) {
            this.exerciseView.playTimer();
        } else if (this.mState == ExerciseState.REST) {
            this.mRestCountDown.updateRestTimer();
        }
    }

    public void onPauseState() {
        if (this.mState == ExerciseState.PLAY) {
            this.exerciseView.pauseTimer();
        } else if (this.mState == ExerciseState.REST) {
            Log.e("hellboy","Pause2");
           // DialogService.getInstance().showPauseDialog(this.mParentActivity, this, this.mParentActivity.getiRestartLevelListener(), this, this.mParentActivity.getViewPager().getCurrentItem() == 0);
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
            this.exerciseView.setVisibility(4);
            pauseFabPrevious.setVisibility(4);
            pauseFabNext.setVisibility(4);
            mProggessBar.setVisibility(4);
            this.mRestCountDown.setVisibility(0);
            ExerciseRestView exerciseRestView = this.mRestCountDown;
            if (getPage() == 0) {
                z2 = true;
            } else {
                z2 = false;
            }
            if (this.mParentActivity.getMoveReason() == MoveReason.NATURAL_RIGHT) {
                z = false;
            }
            exerciseRestView.startRest(z2, z);
        }
    }

    public void restartFragment() {
        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
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
