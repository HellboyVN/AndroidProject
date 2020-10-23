package com.tlvanelearning.ielts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.tlvanelearning.ielts.adapter.MainAdapter;
import com.tlvanelearning.ielts.common.Config;
import com.tlvanelearning.ielts.common.EntryItem;
import com.tlvanelearning.ielts.database.DatabaseIO;
import com.tlvanelearning.ielts.database.KEYConstants;
import com.tlvanelearning.ielts.parent.BaseActivity;
import com.tlvanelearning.ielts.player.PlayerService;
import com.tlvanelearning.ielts.util.MUtil;
import com.tlvanelearning.ielts.util.UIUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoreActivity extends BaseActivity implements KEYConstants {
    private static final int DETAIL_REQUEST_CODE = 100;
    private String TAG = "IAP";
    @BindView(R.id.adsView)
    LinearLayout adsView;
    private boolean favorite;
    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;
    @BindView(R.id.list)
    ListView mListView;
    private MainAdapter mainAdapter;
    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            try {
                JSONObject jsonObject;
                Intent detailIntent;
                switch (tableIndex) {
                    case 3:
                        showAdmobInterstitialAd();
                        jsonObject = mainAdapter.getItem(position);
                        Intent testIntent = new Intent(getBaseContext(), QuizActivity.class);
                        testIntent.putExtra("EXTRAS", jsonObject.toString());
                        startActivity(testIntent);
                        return;
                    case 4:
                    case 13:
                    case 15:
                    case 16:
                    case 17:
                    case 19:
                    case 20:
                        return;
                    case 6:
                        PlayerService playerService = PlayerService.getInstance();
                        if (playerService != null) {
                            showAdmobInterstitialAd();
                            detailIntent = new Intent(getBaseContext(), DetailActivity.class);
                            detailIntent.putExtra("EXTRAS_POS", position);
                            detailIntent.putExtra("EXTRAS_TAB", 4);
                            playerService.setPlaylist(mainAdapter.jsonObjects);
                            startActivityForResult(detailIntent, 100);
                            return;
                        }
                        return;
                    case 9:
                    case 11:
                    case 14:
                    case 18:
                        jsonObject = mainAdapter.getItem(position);
                        Intent itemIntent = new Intent(getBaseContext(), ItemActivity.class);
                        itemIntent.putExtra("TABLE_INDEX", tableIndex);
                        itemIntent.putExtra("EXTRAS", jsonObject.toString());
                        startActivity(itemIntent);
                        return;
                    case 23:
                        try {
                            jsonObject = mainAdapter.getItem(position);
                            String meaning = DatabaseIO.database(context).getContentWithId(jsonObject.getInt("id"), tableIndex);
                            if (!TextUtils.isEmpty(meaning)) {
                                MUtil.createDialog(context, jsonObject.getString("title"), meaning).show();
                                return;
                            }
                            return;
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            return;
                        }
                    default:
                        showFacebookInterstitialAd();
                        jsonObject = mainAdapter.getItem(position);
                        detailIntent = new Intent(getBaseContext(), ContentActivity.class);
                        detailIntent.putExtra("TABLE_INDEX", tableIndex);
                        detailIntent.putExtra("EXTRAS", jsonObject.toString());
                        startActivity(detailIntent);
                        return;
                }
            } catch (Exception ex2) {
                ex2.printStackTrace();
            }
        }
    };
    private OnQueryTextListener onQueryTextListener = new OnQueryTextListener() {
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        public boolean onQueryTextChange(String newText) {
            mainAdapter.filter(newText);
            return false;
        }
    };
    @BindView(R.id.openGame)
    ImageView openGame;
    @BindView(R.id.empty)
    ProgressBar progressBar;
    private int tableIndex = 1;

    private class GetDataAsynTask extends AsyncTask<Context, Void, ArrayList<JSONObject>> {
        private GetDataAsynTask() {
        }

        protected ArrayList<JSONObject> doInBackground(Context... contexts) {
            try {
                return DatabaseIO.database(context).getLessons(tableIndex, favorite);
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }

        protected void onPreExecute() {
            super.onPreExecute();
            admobBanner(context, adsView);
            floatingActionButton.setVisibility(View.GONE);
        }

        protected void onPostExecute(ArrayList<JSONObject> jsonObjects) {
            super.onPostExecute(jsonObjects);
            if (jsonObjects != null) {
                try {
                    mainAdapter.clear();
                    mainAdapter.filterList.clear();
                    Iterator it = jsonObjects.iterator();
                    while (it.hasNext()) {
                        JSONObject jsonObject = (JSONObject) it.next();
                        mainAdapter.add(jsonObject);
                        mainAdapter.filterList.add(jsonObject);
                    }
                    mainAdapter.setTableIndex(tableIndex);
                    mainAdapter.notifyDataSetChanged();
                    switch (tableIndex) {
                        case 3:
                        case 4:
                        case 9:
                        case 11:
                        case 15:
                        case 17:
                        case 18:
                        case 21:
                            floatingActionButton.setVisibility(View.GONE);
                            break;
                        default:
                            floatingActionButton.setVisibility(View.VISIBLE);
                            break;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return;
                }
            }
            setTitle(((EntryItem) Config.items.get(tableIndex)).getTitle());
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_more_item);
        ButterKnife.bind((Activity) this);
        ListView observableListView = this.mListView;
        MainAdapter mainAdapter = new MainAdapter(this, R.layout.fragment_main_row, new ArrayList());
        this.mainAdapter = mainAdapter;
        observableListView.setAdapter(mainAdapter);
        this.mListView.setOnItemClickListener(this.onItemClickListener);
//        this.mListView.setScrollViewCallbacks(this);
        this.mListView.setEmptyView(this.progressBar);
        Log.e("facebook","MoreActivity done");
        loadADS(this.context);
        this.floatingActionButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                boolean z;
                MoreActivity moreActivity = MoreActivity.this;
                if (favorite) {
                    z = false;
                } else {
                    z = true;
                }
                moreActivity.favorite = z;
                UIUtil.changeImageSource(floatingActionButton, !favorite ? R.drawable.ic_fab_favorite : R.drawable.ic_fab_list);
                new GetDataAsynTask().execute(new Context[]{context});
            }
        });
    }

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        try {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                this.tableIndex = extras.getInt("TABLE_INDEX");
                new GetDataAsynTask().execute(new Context[0]);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void onResume() {
        super.onResume();
        this.mainAdapter.notifyDataSetChanged();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        restoreActionBar();
        getMenuInflater().inflate(R.menu.main, menu);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.menu_search));
        if (searchView != null) {
            searchView.setOnQueryTextListener(this.onQueryTextListener);
        }
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return super.onOptionsItemSelected(item);
        }
        finish();
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (100 == requestCode && resultCode == -1) {
            try {
                PlayerService playerService = PlayerService.getInstance();
                if (playerService != null) {
                    this.mainAdapter.jsonObjects = playerService.getPlaylist();
                    this.mainAdapter.notifyDataSetChanged();
                }
            } catch (Exception e) {
            }
        }
    }
}
