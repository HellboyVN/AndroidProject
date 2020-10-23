package com.workout.workout.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.workout.workout.R;
import com.workout.workout.activity.WorkoutDetailActivity;
import com.workout.workout.adapter.FavouriteRecyclerViewAdapter;
import com.workout.workout.constant.AppConstants;
import com.workout.workout.database.DatabaseManager;
import com.workout.workout.listener.OnListFragmentInteractionListener;
import com.workout.workout.modal.BaseModel;
import com.workout.workout.modal.Workout;
import com.workout.workout.util.SimpleDividerItemDecoration;
import java.util.ArrayList;

public class FavouriteFragment extends BaseFragment implements OnListFragmentInteractionListener {
    private FavouriteRecyclerViewAdapter favouriteRecyclerViewAdapter;
    private ArrayList<Workout> favouriteWorkoutList = new ArrayList();
    private RecyclerView recyclerView;
    private TextView textViewEmpty;

    class FavouriteWorkoutListAsyncTask extends AsyncTask<Void, Void, Void> {
        FavouriteWorkoutListAsyncTask() {
        }

        protected Void doInBackground(Void... voids) {
            FavouriteFragment.this.favouriteWorkoutList = DatabaseManager.getInstance(FavouriteFragment.this.getActivity()).getFavouriteWorkoutList();
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            FavouriteFragment.this.favouriteRecyclerViewAdapter = new FavouriteRecyclerViewAdapter(FavouriteFragment.this, FavouriteFragment.this.favouriteWorkoutList, FavouriteFragment.this);
            FavouriteFragment.this.recyclerView.setAdapter(FavouriteFragment.this.favouriteRecyclerViewAdapter);
            if (FavouriteFragment.this.favouriteWorkoutList == null || FavouriteFragment.this.favouriteWorkoutList.size() <= 0) {
                FavouriteFragment.this.textViewEmpty.setVisibility(View.VISIBLE);
            } else {
                FavouriteFragment.this.textViewEmpty.setVisibility(View.GONE);
            }
        }
    }

    public static FavouriteFragment newInstance() {
        FavouriteFragment fragment = new FavouriteFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() == null) {
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
        if (FirebaseRemoteConfig.getInstance().getBoolean("favourite_banner_ads_enable")) {
            loadBannerAdvertisement(view, AppConstants.ADMOB_FAVOURITE_BANNER_AD_ID);
        }
        Context context = view.getContext();
        this.recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        this.textViewEmpty = (TextView) view.findViewById(R.id.textViewEmpty);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        this.recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        this.favouriteRecyclerViewAdapter = new FavouriteRecyclerViewAdapter(this, this.favouriteWorkoutList, this);
        this.recyclerView.setAdapter(this.favouriteRecyclerViewAdapter);
        return view;
    }

    public void onResume() {
        super.onResume();
        new FavouriteWorkoutListAsyncTask().execute(new Void[0]);
    }

    public void showEmptyView() {
        this.textViewEmpty.setVisibility(View.VISIBLE);
    }

    public void hideEmptyView() {
        this.textViewEmpty.setVisibility(View.GONE);
    }

    public void onDetach() {
        super.onDetach();
    }

    public void onListFragmentInteraction(BaseModel model, int position) {
        if (model instanceof Workout) {
            Workout workout = (Workout) model;
            Intent intent = new Intent(getActivity(), WorkoutDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable(AppConstants.WORKOUT_OBJECT, workout);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
