package com.tlvanelearning.ielts.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.tlvanelearning.ielts.DetailActivity;
import com.tlvanelearning.ielts.ItemActivity;
import com.tlvanelearning.ielts.MoreActivity;
import com.tlvanelearning.ielts.R;
import com.tlvanelearning.ielts.TabActivity.FragmentCallback;
import com.tlvanelearning.ielts.adapter.MainAdapter;
import com.tlvanelearning.ielts.adapter.MenuAdapter;
import com.tlvanelearning.ielts.common.Config;
import com.tlvanelearning.ielts.database.DatabaseIO;
import com.tlvanelearning.ielts.player.PlayerService;
import com.tlvanelearning.ielts.util.StoreUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TabFragment extends Fragment implements ObservableScrollViewCallbacks {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static FragmentCallback fragmentCallback;
    @BindView(R.id.empty)
    ProgressBar emptyView;
    @BindView(R.id.list)
    ListView mListView;
    private MainAdapter mainAdapter;
    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            try {
                int tabIndex = TabFragment.this.getArguments().getInt("section_number");
                Intent detailIntent;
                switch (tabIndex) {
                    case 4:
                        switch (position) {
                            case 8:
                            case 10:
                                detailIntent = new Intent(TabFragment.this.getContext(), ItemActivity.class);
                                detailIntent.putExtra("TABLE_INDEX", position);
                                TabFragment.this.startActivity(detailIntent);
                                return;
                            case 25:
                                StoreUtil.rateApp(TabFragment.this.getContext());
                                return;
                            case 26:
                                StoreUtil.moreApp(TabFragment.this.getContext());
                                return;
                            case 27:
                                StoreUtil.sendEmail(TabFragment.this.getContext());
                                return;
                            default:
                                try {
                                    detailIntent = new Intent(TabFragment.this.getContext(), MoreActivity.class);
                                    detailIntent.putExtra("TABLE_INDEX", position);
                                    TabFragment.this.startActivity(detailIntent);
                                    return;
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    return;
                                }
                        }
                    default:
                        PlayerService playerService = PlayerService.getInstance();
                        if (playerService != null) {
                            if (TabFragment.fragmentCallback != null) {
                                TabFragment.fragmentCallback.callback();
                            }

                            detailIntent = new Intent(TabFragment.this.getContext(), DetailActivity.class);
                            detailIntent.putExtra("EXTRAS_POS", position);
                            detailIntent.putExtra("EXTRAS_TAB", tabIndex);
                            playerService.setPlaylist(TabFragment.this.mainAdapter.jsonObjects);
                            TabFragment.this.startActivity(detailIntent);
                            return;
                        }
                        return;
                }
            } catch (Exception ex2) {
                ex2.printStackTrace();
            }
        }
    };

    private class GetDataAsynTask extends AsyncTask<Context, Void, ArrayList<JSONObject>> {
        private GetDataAsynTask() {
        }

        protected ArrayList<JSONObject> doInBackground(Context... contexts) {
            ArrayList<JSONObject> arrayList = null;
            try {
                int value = TabFragment.this.getArguments().getInt("section_number");
                switch (value) {
                    case 4:
                        break;
                    default:
                        arrayList = DatabaseIO.database(TabFragment.this.getContext()).getLessons(0 - value, false);
                        break;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return arrayList;
        }

        protected void onPostExecute(ArrayList<JSONObject> jsonObjects) {
            super.onPostExecute(jsonObjects);
            if (jsonObjects != null) {
                try {
                    TabFragment.this.mainAdapter.clear();
                    Iterator it = jsonObjects.iterator();
                    while (it.hasNext()) {
                        TabFragment.this.mainAdapter.add((JSONObject) it.next());
                    }
                    TabFragment.this.mainAdapter.notifyDataSetChanged();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public static TabFragment newInstance(int sectionNumber, FragmentCallback fragmentCallback) {
        TabFragment fragment = new TabFragment();
        Bundle args = new Bundle();
        args.putInt("section_number", sectionNumber);
        fragment.setArguments(args);
        fragmentCallback = fragmentCallback;
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind((Object) this, rootView);
        this.mListView.setOnItemClickListener(this.
                onItemClickListener);
//        this.mListView.setScrollViewCallbacks(this);
        this.mListView.setEmptyView(this.emptyView);
        new GetDataAsynTask().execute(new Context[0]);
        return rootView;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        switch (getArguments().getInt("section_number")) {
            case 4:
                this.mListView.setAdapter(new MenuAdapter(getContext(), Config.items));
                return;
            default:
                ListView observableListView = this.mListView;
                MainAdapter mainAdapter = new MainAdapter(getContext(), R.layout.fragment_main_row, new ArrayList());
                this.mainAdapter = mainAdapter;
                observableListView.setAdapter(mainAdapter);
                return;
        }
    }

    public void onScrollChanged(int i, boolean b, boolean b1) {
    }

    public void onDownMotionEvent() {
    }

    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (scrollState == ScrollState.UP) {
            if (ab.isShowing()) {
                ab.hide();
            }
        } else if (scrollState == ScrollState.DOWN && !ab.isShowing()) {
            ab.show();
        }
    }
}
