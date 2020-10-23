package hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.home.tabs.tabtwo.othercards;

import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.R;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.ads.listener.IAdCardClicked;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.home.tabs.tabtwo.ExTimeRA;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.home.tabs.tabtwo.chart.cards.MoreTabAbstractCard;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.home.tabs.tabtwo.model.TabTwoData;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.models.Exercise;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.utils.LevelUtils;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.utils.TypeFaceService;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.ArrayList;
import java.util.List;

public class ExerciseTimeCard extends MoreTabAbstractCard {
    @BindView(R.id.card)
    CardView card;
    private Context context;
    @BindView(R.id.exTimeRecView)
    RecyclerView exTimeRecView;
    private boolean isPurchased;
    @BindView(R.id.lock)
    TextView lock;
    @BindView(R.id.lock_layout)
    LinearLayout lockLayout;
    private IAdCardClicked mAdListener;
    Typeface rLight;
    Typeface rMedium;
    Typeface rRegular;

    public ExerciseTimeCard(Context context, ViewGroup parent) {
        this(context, LayoutInflater.from(context).inflate(R.layout.card_exercise_time, parent, false));
        this.context = context;
    }

    public ExerciseTimeCard(Context context, View view) {
        super(view, context);
        ButterKnife.bind((Object) this, view);
        initFonts();
    }

    public void setAdClickListener(IAdCardClicked iAdCardClicked) {
        this.mAdListener = iAdCardClicked;
    }

    public void bind(Object data) {
        this.isPurchased = ((TabTwoData) data).isPurchased();
        List<Exercise> exerciseList = LevelUtils.getSavedExerciseData(this.context);
        List<Exercise> availableList = new ArrayList();
        this.lockLayout.setVisibility(this.isPurchased ? 8 : 0);
        if (this.isPurchased) {
            availableList.addAll(exerciseList);
        } else if (exerciseList.size() > 0) {
            availableList.add(exerciseList.get(0));
        }
        setupAdapter(availableList);
        this.lock.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (ExerciseTimeCard.this.mAdListener != null) {
                    ExerciseTimeCard.this.mAdListener.removeAdClicked(0);
                }
            }
        });
    }

    private void initFonts() {
        this.rLight = TypeFaceService.getInstance().getRobotoLight(this.context);
        this.rRegular = TypeFaceService.getInstance().getRobotoRegular(this.context);
        this.rMedium = TypeFaceService.getInstance().getRobotoMedium(this.context);
        setFonts();
    }

    private void setupAdapter(List<Exercise> exerciseList) {
        this.exTimeRecView.setLayoutManager(new GridLayoutManager(this.context,2));
        this.exTimeRecView.setHasFixedSize(false);
        this.exTimeRecView.clearFocus();
        this.exTimeRecView.setItemAnimator(new DefaultItemAnimator());
        this.exTimeRecView.setAdapter(new ExTimeRA(this.context, exerciseList));
    }

    private void setFonts() {
        this.lock.setTypeface(this.rRegular);
    }
}
