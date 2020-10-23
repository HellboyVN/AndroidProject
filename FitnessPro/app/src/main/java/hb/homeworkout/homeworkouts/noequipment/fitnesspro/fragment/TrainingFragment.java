package hb.homeworkout.homeworkouts.noequipment.fitnesspro.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.support.v7.widget.SearchView.OnSuggestionListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.internal.zzagz;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.analytics.FirebaseAnalytics.Event;

import java.util.ArrayList;
import java.util.List;

import hb.homeworkout.homeworkouts.noequipment.fitnesspro.R;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.activity.NewsListActivity;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.activity.TrainingDetailActivity;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.adapter.SearchWorkoutAdapter;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.adapter.TrainingRecyclerViewAdapter;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.constant.AppConstants;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.database.DatabaseHelper;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.database.DatabaseManager;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.listener.OnListFragmentInteractionListener;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.managers.NativeAdsTaskManager;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.modal.BaseModel;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.modal.Training;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.modal.Workout;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.util.AppUtil;
import me.leolin.shortcutbadger.ShortcutBadger;

public class TrainingFragment extends BaseFragment implements OnListFragmentInteractionListener, OnQueryTextListener, OnSuggestionListener {
    public static final int NATIVE_APP_MAX_WIDTH = 1200;
    private static int availableWidthForNativeAds;
    public static String[] columns = new String[]{"_id", "workout_name", "workout_image_name", "workout_image_url", "body_part_name", "workout_description", "workout_video_name", "workout_video_url", "body_part_image_url"};
    static int mNotifCount = 0;
    static Button notifCount;
    private FirebaseAnalytics mFirebaseAnalytics;
    private Menu mMenu;
    SearchWorkoutAdapter mSearchViewAdapter;
    private Button notificationButton;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    SearchView searchView = null;
    private View toolbar;
    private ArrayList<Training> trainingList = new ArrayList();
    private TrainingRecyclerViewAdapter trainingRecyclerViewAdapter;
    private TextView ui_hot = null;

    static abstract class MyMenuItemStuffListener implements OnClickListener, OnLongClickListener {
        private String hint;
        private View view;

        public abstract void onClick(View view);

        MyMenuItemStuffListener(View view, String hint) {
            this.view = view;
            this.hint = hint;
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        public boolean onLongClick(View v) {
            int[] screenPos = new int[2];
            Rect displayFrame = new Rect();
            this.view.getLocationOnScreen(screenPos);
            this.view.getWindowVisibleDisplayFrame(displayFrame);
            Context context = this.view.getContext();
            int width = this.view.getWidth();
            int height = this.view.getHeight();
            int midy = screenPos[1] + (height / 2);
            int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
            Toast cheatSheet = Toast.makeText(context, this.hint, Toast.LENGTH_SHORT);
            if (midy < displayFrame.height()) {
                cheatSheet.setGravity(53, (screenWidth - screenPos[0]) - (width / 2), height);
            } else {
                cheatSheet.setGravity(81, 0, height);
            }
            cheatSheet.show();
            return true;
        }
    }

    class BodyPartListAsyncTask extends AsyncTask<Void, Void, Void> {
        BodyPartListAsyncTask() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
            TrainingFragment.this.progressBar.setVisibility(View.VISIBLE);
        }

        protected Void doInBackground(Void... voids) {
            TrainingFragment.this.getBodyPartList();
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            TrainingFragment.this.progressBar.setVisibility(View.GONE);
            TrainingFragment.this.trainingRecyclerViewAdapter.setRowItemList(NativeAdsTaskManager.prepareNativeAdsInTrainingRowItemList(TrainingFragment.this.getActivity(), TrainingFragment.this.trainingList));
            TrainingFragment.this.trainingRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

    class LoadWorkoutListAsyncTask extends AsyncTask<Void, Void, Void> {
        List<Workout> filteredWorkoutList;
        MatrixCursor matrixCursor;
        String searchText;

        LoadWorkoutListAsyncTask(String searchText) {
            this.searchText = searchText;
//            try {
//                Answers.getInstance().logSearch(new SearchEvent().putQuery(searchText));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }

        protected Void doInBackground(Void... voids) {
            this.filteredWorkoutList = TrainingFragment.this.filter(DatabaseHelper.getInstance(TrainingFragment.this.getActivity()).getAllWorkoutList(), this.searchText);
            this.matrixCursor = TrainingFragment.this.convertToCursor(this.filteredWorkoutList);
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (TrainingFragment.this.mSearchViewAdapter != null && this.matrixCursor != null) {
                TrainingFragment.this.mSearchViewAdapter.changeCursor(this.matrixCursor);
            }
        }
    }

    public static TrainingFragment newInstance() {
        TrainingFragment fragment = new TrainingFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() == null) {
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_training, container, false);
        //loadBannerAdvertisement(view);
        availableWidthForNativeAds = calculateAvailableWidthForNativeAdView();
//        if (FirebaseRemoteConfig.getInstance().getBoolean("exercise_banner_ads_enable")) {
//            loadBannerAdvertisement(view, AppConstants.ADMOB_BODY_PART_LIST_BANNER_AD_ID);
//        }
        Context context = view.getContext();
        this.progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        this.recyclerView = (RecyclerView) view.findViewById(R.id.list);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        this.trainingRecyclerViewAdapter = new TrainingRecyclerViewAdapter(getActivity(), this, this);
        this.recyclerView.setAdapter(this.trainingRecyclerViewAdapter);
        this.recyclerView.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (TrainingFragment.this.mMenu != null) {
                    if (TrainingFragment.this.getActivity().getCurrentFocus() != null) {
                        ((InputMethodManager) TrainingFragment.this.getActivity().getSystemService("input_method")).hideSoftInputFromWindow(TrainingFragment.this.getActivity().getWindow().getCurrentFocus().getWindowToken(), 0);
                    }
                    try {
                        TrainingFragment.this.searchView.setQuery("", false);
                        TrainingFragment.this.searchView.clearFocus();
                        TrainingFragment.this.searchView.setIconified(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });
        new BodyPartListAsyncTask().execute(new Void[0]);
        setHasOptionsMenu(true);
        this.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        return view;
    }

    public void onListFragmentInteraction(BaseModel model, int position) {
        if (model instanceof Training) {
            Training training = (Training) model;
            Bundle bundle1 = new Bundle();
            bundle1.putString("body_part_name", training.getPart_name());
            this.mFirebaseAnalytics.logEvent(Event.VIEW_ITEM, bundle1);
//            try {
//                Answers.getInstance().logContentView(new ContentViewEvent().putContentName(training.getPart_name()));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            Intent intent = new Intent(getActivity(), TrainingDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable(AppConstants.BODY_PART, training);
            intent.putExtras(bundle);
            getActivity().startActivity(intent);
        }
    }

    private synchronized void getBodyPartList() {
        this.trainingList = DatabaseHelper.getInstance(getActivity()).getBodyPartList();
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.mMenu = menu;
        inflater.inflate(R.menu.training_menu, menu);
        View menu_hotlist = menu.findItem(R.id.Notificationbadge).getActionView();
        this.ui_hot = (TextView) menu_hotlist.findViewById(R.id.hotlist_hot);
        try {
            checkUnreadNotifsAndUpdateCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
        MyMenuItemStuffListener anonymousClass2 = new MyMenuItemStuffListener(menu_hotlist, "Notifications") {
            public void onClick(View v) {
//                try {
//                    Answers.getInstance().logContentView(new ContentViewEvent().putContentName("Notification Bell Icon click"));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                try {
                    TrainingFragment.this.startActivity(new Intent(TrainingFragment.this.getActivity(), NewsListActivity.class));
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        };
        this.searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        if (this.searchView != null) {
            this.searchView.setMaxWidth(Integer.MAX_VALUE);
            this.searchView.setQueryHint("Search Workouts");
            this.searchView.setOnQueryTextListener(this);
            this.searchView.setOnSuggestionListener(this);
            try {
                this.mSearchViewAdapter = new SearchWorkoutAdapter(getActivity(), R.layout.create_plan_workout_list_row, null, columns, null, NotificationManagerCompat.IMPORTANCE_UNSPECIFIED);
                this.searchView.setSuggestionsAdapter(this.mSearchViewAdapter);
            } catch (Exception e2) {
                if (isAdded()) {
                    Toast.makeText(getActivity(), "Error occured, please try again", Toast.LENGTH_SHORT).show();
                }
                e2.printStackTrace();
            }
        }
    }

    private void checkUnreadNotifsAndUpdateCount() {
        try {
            updateHotCount(DatabaseManager.getInstance(getActivity()).numberOfUnreadReadNews());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateHotCount(final int new_hot_number) {
        if (this.ui_hot != null) {
            zzagz.runOnUiThread(new Runnable() {
                public void run() {
                    if (new_hot_number == 0) {
                        TrainingFragment.this.ui_hot.setVisibility(4);
                        return;
                    }
                    TrainingFragment.this.ui_hot.setVisibility(View.VISIBLE);
                    TrainingFragment.this.ui_hot.setText(Integer.toString(new_hot_number));
                }
            });
        }
    }

    public void onResume() {
        checkUnreadNotifsAndUpdateCount();
        ShortcutBadger.applyCount(getActivity(), DatabaseManager.getInstance(getActivity()).numberOfUnreadReadNews());
        super.onResume();
    }

    public boolean onQueryTextSubmit(String s) {
        if (s.length() > 1) {
            new LoadWorkoutListAsyncTask(s).execute(new Void[0]);
        }
        return true;
    }

    public boolean onQueryTextChange(String s) {
        if (s.length() > 1) {
            new LoadWorkoutListAsyncTask(s).execute(new Void[0]);
        }
        return true;
    }

    public boolean onSuggestionSelect(int position) {
        this.searchView.setQuery(((Cursor) this.searchView.getSuggestionsAdapter().getItem(position)).getString(4), false);
        this.searchView.clearFocus();
        return true;
    }

    public boolean onSuggestionClick(int position) {
        this.searchView.setQuery(((Cursor) this.searchView.getSuggestionsAdapter().getItem(position)).getString(4), false);
        this.searchView.clearFocus();
        return true;
    }

    private void loadData(String searchText) {
        this.mSearchViewAdapter.changeCursor(convertToCursor(filter(DatabaseHelper.getInstance(getActivity()).getAllWorkoutList(), searchText)));
    }

    private MatrixCursor convertToCursor(List<Workout> workoutList) {
        MatrixCursor cursor = new MatrixCursor(columns);
        int i = 0;
        for (Workout workout : workoutList) {
            String temp[] = new String[9];
            i++;
            temp[0] = workout.getWorkout_id();
            temp[1] = workout.getWorkout_name();
            temp[2] = workout.getWorkout_image_name();
            temp[3] = workout.getWorkout_image_url();
            temp[4] = workout.getBody_part_name();
            temp[5] = workout.getWorkout_description();
            temp[6] = workout.getWorkout_video_name();
            temp[7] = workout.getWorkout_video_url();
            temp[8] = workout.getBody_part_image_url();
            cursor.addRow(temp);
        }
        return cursor;
    }

    private List<Workout> filter(List<Workout> models, String query) {
        query = query.toLowerCase();
        List<Workout> filteredModelList = new ArrayList();
        for (Workout model : models) {
            if (model.getWorkout_name().toLowerCase().contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
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
