package com.tlvanelearning.ielts;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.tlvanelearning.ielts.adapter.QuizAdapter;
import com.tlvanelearning.ielts.common.AnswerItem;
import com.tlvanelearning.ielts.database.DatabaseIO;
import com.tlvanelearning.ielts.parent.BaseActivity;
import com.tlvanelearning.ielts.util.UIUtil;
import com.tlvanelearning.ielts.util.UIUtil.UIUtilCallback;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONObject;

public class QuizActivity extends BaseActivity {
    @BindView(R.id.adsView)
    LinearLayout adsView;
    private AlertDialog alertDialog;
    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;
    private HashMap<String, List<AnswerItem>> listDataChild;
    private List<String> listDataHeader;
    @BindView(R.id.list)
    ExpandableListView listView;
    private QuizAdapter listViewAdapter;
    private JSONObject mJsonObject;
    @BindView(R.id.empty)
    ProgressBar progressBar;

    private class GetDataAsyncTask extends AsyncTask<String, Void, ArrayList<JSONObject>> {
        private GetDataAsyncTask() {
        }

        protected ArrayList<JSONObject> doInBackground(String... strings) {
            try {
                ArrayList<JSONObject> questions = DatabaseIO.database(context).getQuestions(mJsonObject.getInt("SubLevel"));
                Iterator it = questions.iterator();
                while (it.hasNext()) {
                    JSONObject jsonObject = (JSONObject) it.next();
                    listDataHeader.add(jsonObject.getString("question"));
                    JSONArray anwserArray = jsonObject.getJSONArray("answers");
                    List<AnswerItem> answerItems = new ArrayList();
                    for (int i = 0; i < anwserArray.length(); i++) {
                        answerItems.add(new AnswerItem(jsonObject.getInt("QNumber"), anwserArray.getString(i), jsonObject.getString("correct")));
                    }
                    listDataChild.put(jsonObject.getString("question"), answerItems);
                }
                return questions;
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }

        protected void onPreExecute() {
            super.onPreExecute();
            floatingActionButton.setVisibility(View.GONE);
            admobBanner(context, adsView);
        }

        protected void onPostExecute(ArrayList<JSONObject> jsonObjects) {
            super.onPostExecute(jsonObjects);
            if (jsonObjects != null && jsonObjects.size() != 0) {
                listViewAdapter.notifyDataSetChanged();
                floatingActionButton.setVisibility(View.VISIBLE);
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_test);
        this.listDataHeader = new ArrayList();
        this.listDataChild = new HashMap();
        ButterKnife.bind((Activity) this);
        this.floatingActionButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                doneClick();
            }
        });
    }

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        try {
            ExpandableListView expandableListView = this.listView;
            QuizAdapter quizAdapter = new QuizAdapter(this, this.listDataHeader, this.listDataChild, true);
            this.listViewAdapter = quizAdapter;
            expandableListView.setAdapter(quizAdapter);
            this.listView.setEmptyView(this.progressBar);
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                this.mJsonObject = new JSONObject(extras.getString("EXTRAS"));
                setTitle(this.mJsonObject.getString("title"));
                new GetDataAsyncTask().execute(new String[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doneClick() {
        try {
            if (this.alertDialog == null || !this.alertDialog.isShowing()) {
                int rightCount = 0;
                for (String key : this.listDataHeader) {
                    for (AnswerItem answerItem : (List<AnswerItem>) this.listDataChild.get(key)) {
                        if (answerItem.isChecked()) {
                            if (answerItem.compareValue(true)) {
                                rightCount++;
                                try {
                                    DatabaseIO.database(this.context).updatePassed(this.mJsonObject.getInt("SubLevel"), answerItem.getQnumber(), true);
                                } catch (Exception e) {
                                }
                            } else {
                                try {
                                    DatabaseIO.database(this.context).updatePassed(this.mJsonObject.getInt("SubLevel"), answerItem.getQnumber(), false);
                                } catch (Exception e2) {
                                }
                            }
                        }
                    }
                }
                int totalQuestion = this.listDataHeader.size();
                this.alertDialog = UIUtil.createDialog(this, getString(R.string.dialog_test_title), String.format(Locale.US, getString(R.string.message_format), new Object[]{Integer.valueOf(rightCount), Integer.valueOf(totalQuestion)}), new UIUtilCallback() {
                    public void onCallback(int position) {
                        switch (position) {
                            case -1:
                                try {
                                    for (String key : listDataHeader) {
                                        ListIterator<AnswerItem> answerItems = ((List) listDataChild.get(key)).listIterator();
                                        while (answerItems.hasNext()) {
                                            AnswerItem answerItem = (AnswerItem) answerItems.next();
                                            answerItem.setChecked(false);
                                            answerItems.set(answerItem);
                                        }
                                    }
                                    listViewAdapter.setCompare(false);
                                    listViewAdapter.notifyDataSetChanged();
                                    return;
                                } catch (Exception e) {
                                    return;
                                }
                            default:
                                listViewAdapter.setCompare(true);
                                listViewAdapter.notifyDataSetChanged();
                                return;
                        }
                    }
                });
                this.alertDialog.show();
            }
        } catch (Exception e3) {
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
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
