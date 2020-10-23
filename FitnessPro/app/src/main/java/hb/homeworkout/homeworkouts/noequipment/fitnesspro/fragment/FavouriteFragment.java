package hb.homeworkout.homeworkouts.noequipment.fitnesspro.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;

import java.util.ArrayList;
import java.util.List;

import hb.homeworkout.homeworkouts.noequipment.fitnesspro.R;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.activity.WorkoutDetailActivity;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.adapter.FavouriteRecyclerViewAdapter;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.constant.AppConstants;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.database.DatabaseManager;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.listener.OnListFragmentInteractionListener;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.managers.PersistenceManager;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.modal.BaseModel;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.modal.Workout;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.util.SimpleDividerItemDecoration;

public class FavouriteFragment extends BaseFragment implements OnListFragmentInteractionListener {
    private FavouriteRecyclerViewAdapter favouriteRecyclerViewAdapter;
    private ArrayList<Workout> favouriteWorkoutList = new ArrayList();
    private RecyclerView recyclerView;
    private TextView textViewEmpty;
    private LinearLayout adsView;
    private LinearLayout adView;
    private NativeAd nativeAd;
    private LinearLayout  nativeAdContainer;

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
        Context context = view.getContext();
        loadBannerAdvertisement(view);
        this.recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        this.textViewEmpty = (TextView) view.findViewById(R.id.textViewEmpty);
        this.adsView = (LinearLayout) view.findViewById(R.id.adsView);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        this.recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        this.favouriteRecyclerViewAdapter = new FavouriteRecyclerViewAdapter(this, this.favouriteWorkoutList, this);
        this.recyclerView.setAdapter(this.favouriteRecyclerViewAdapter);
        showNativeAd(view);
        return view;
    }
    private void showNativeAd(final View view) {
        if (!PersistenceManager.isAdsFreeVersion()) {
            Log.e("NativeADS", "----HERE----");
            nativeAd = new NativeAd(getActivity(), "333762600459223_333765303792286");
            nativeAd.setAdListener(new AdListener() {

                @Override
                public void onError(Ad ad, AdError error) {

                }

                @Override
                public void onAdLoaded(Ad ad) {
                    Log.e("NativeADSLoaded", "----HERE----");
                    adsView.setVisibility(View.VISIBLE);
                    if (nativeAd != null) {
                        nativeAd.unregisterView();
                    }

                    // Add the Ad view into the ad container.
                    nativeAdContainer = (LinearLayout) view.findViewById(R.id.adsView);
                    LayoutInflater inflater = LayoutInflater.from(getActivity());
                    // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
                    adView = (LinearLayout) inflater.inflate(R.layout.native_ad_layout, nativeAdContainer, false);
                    nativeAdContainer.addView(adView);

                    // Create native UI using the ad metadata.
                    ImageView nativeAdIcon = (ImageView) adView.findViewById(R.id.native_ad_icon);
                    TextView nativeAdTitle = (TextView) adView.findViewById(R.id.native_ad_title);
                    MediaView nativeAdMedia = (MediaView) adView.findViewById(R.id.native_ad_media);
                    TextView nativeAdSocialContext = (TextView) adView.findViewById(R.id.native_ad_social_context);
                    TextView nativeAdBody = (TextView) adView.findViewById(R.id.native_ad_body);
                    Button nativeAdCallToAction = (Button) adView.findViewById(R.id.native_ad_call_to_action);

                    // Set the Text.
                    nativeAdTitle.setText(nativeAd.getAdTitle());
                    nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
                    nativeAdBody.setText(nativeAd.getAdBody());
                    nativeAdCallToAction.setText(nativeAd.getAdCallToAction());

                    // Download and display the ad icon.
                    NativeAd.Image adIcon = nativeAd.getAdIcon();
                    NativeAd.downloadAndDisplayImage(adIcon, nativeAdIcon);

                    // Download and display the cover image.
                    nativeAdMedia.setNativeAd(nativeAd);

                    // Add the AdChoices icon
                    LinearLayout adChoicesContainer = (LinearLayout) view.findViewById(R.id.ad_choices_container);
                    AdChoicesView adChoicesView = new AdChoicesView(getActivity(), nativeAd, true);
                    adChoicesContainer.addView(adChoicesView);

                    // Register the Title and CTA button to listen for clicks.
                    List<View> clickableViews = new ArrayList<>();
                    clickableViews.add(nativeAdTitle);
                    clickableViews.add(nativeAdCallToAction);
                    nativeAd.registerViewForInteraction(nativeAdContainer, clickableViews);
                }

                @Override
                public void onAdClicked(Ad ad) {

                }

                @Override
                public void onLoggingImpression(Ad ad) {

                }
            });

            nativeAd.loadAd(NativeAd.MediaCacheFlag.ALL);
        }
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
