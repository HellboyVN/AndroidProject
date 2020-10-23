package com.tlvanelearning.ielts;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.tlvanelearning.ielts.adapter.ItemAdapter;
import com.tlvanelearning.ielts.database.DatabaseIO;
import com.tlvanelearning.ielts.database.KEYConstants;
import com.tlvanelearning.ielts.parent.BaseActivity;
import com.tlvanelearning.ielts.util.NetworkUtil;
import com.tlvanelearning.ielts.util.UIUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemActivity extends BaseActivity implements KEYConstants {
    private String TAG = "IAP";
    @BindView(R.id.adsView)
    LinearLayout adsView;
    private boolean favorite;
    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;
    private ItemAdapter itemAdapter;
    private JSONObject jsonObject;
    @BindView(R.id.list)
    ListView mListView;
    private MediaPlayer mediaPlayer;
    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            try {
                switch (tableIndex) {
                    case 14:
                    case 18:
                        return;
                    default:
                        try {
                            if (NetworkUtil.isOnline(context)) {
                                new PlayerAsyncTask(ItemActivity.this,position).execute();
                                return;
                            }
                            NetworkUtil.toastMessage(context);
                            return;
                        } catch (Exception e) {
                            return;
                        }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };
    private class PlayerAsyncTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog;
        private int pos = 0;
        public PlayerAsyncTask(Context activity,int t) {
            dialog = new ProgressDialog(activity);
            PlayerAsyncTask.this.pos = t;
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading");
            dialog.show();
        }

        protected Void doInBackground(Void... args) {
            try {
                JSONObject jsonObject = itemAdapter.getItem(pos);
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(jsonObject.getString("audio"));
                mediaPlayer.setAudioStreamType(3);
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Void result) {
            // do UI work here
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }
    private OnQueryTextListener onQueryTextListener = new OnQueryTextListener() {
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        public boolean onQueryTextChange(String newText) {
            itemAdapter.filter(newText);
            return false;
        }
    };
    @BindView(R.id.empty)
    ProgressBar progressBar;
    private int tableIndex = 1;

    private class GetDataAsyncTask extends AsyncTask<Context, Void, ArrayList<JSONObject>> {
        private GetDataAsyncTask() {
        }

        protected ArrayList<JSONObject> doInBackground(Context... contexts) {
            try {
                switch (tableIndex) {
                    case 9:
                    case 11:
                    case 14:
                        return DatabaseIO.database(context).getItems(tableIndex, jsonObject.getInt("id"), favorite);
                    case 18:
                        return DatabaseIO.database(context).getIdioms(tableIndex, jsonObject.getString("title"), favorite);
                    default:
                        return DatabaseIO.database(context).getItems(tableIndex, 999, favorite);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }

        protected void onPreExecute() {
            super.onPreExecute();
            floatingActionButton.setVisibility(View.GONE);
            facebookBanner(context, adsView);
        }

        protected void onPostExecute(ArrayList<JSONObject> jsonObjects) {
            super.onPostExecute(jsonObjects);
            if (jsonObjects != null) {
                try {
                    itemAdapter.clear();
                    itemAdapter.filterList.clear();
                    Iterator it = jsonObjects.iterator();
                    while (it.hasNext()) {
                        JSONObject jsonObject = (JSONObject) it.next();
                        itemAdapter.add(jsonObject);
                        itemAdapter.filterList.add(jsonObject);
                    }
                    itemAdapter.setTableIndex(tableIndex);
                    itemAdapter.notifyDataSetChanged();
                    if (tableIndex != 14) {
                        floatingActionButton.setVisibility(View.VISIBLE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_more_item);
        ButterKnife.bind((Activity) this);
        ListView observableListView = this.mListView;
        ItemAdapter itemAdapter = new ItemAdapter(this, R.layout.fragment_main_row, new ArrayList());
        this.itemAdapter = itemAdapter;
        observableListView.setAdapter(itemAdapter);
        this.mListView.setOnItemClickListener(this.onItemClickListener);
//        this.mListView.setScrollViewCallbacks(this);
        this.mListView.setEmptyView(this.progressBar);
        this.floatingActionButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                boolean z;
                ItemActivity itemActivity = ItemActivity.this;
                if (favorite) {
                    z = false;
                } else {
                    z = true;
                }
                itemActivity.favorite = z;
                UIUtil.changeImageSource(floatingActionButton, !favorite ? R.drawable.ic_fab_favorite : R.drawable.ic_fab_list);
                new GetDataAsyncTask().execute(new Context[]{context});
            }
        });
    }

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        try {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                this.tableIndex = extras.getInt("TABLE_INDEX");
                if (!(this.tableIndex == 8 || this.tableIndex == 10)) {
                    this.jsonObject = new JSONObject(extras.getString("EXTRAS"));
                    setTitle(this.jsonObject.getString("title"));
                }
                new GetDataAsyncTask().execute(new Context[0]);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.menu_search));
        if (searchView != null) {
            searchView.setOnQueryTextListener(this.onQueryTextListener);
        }
        restoreActionBar();
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return super.onOptionsItemSelected(item);
        }
        finish();
        return true;
    }

    public void finish() {
        setResult(0, getIntent());
        super.finish();
    }
}
