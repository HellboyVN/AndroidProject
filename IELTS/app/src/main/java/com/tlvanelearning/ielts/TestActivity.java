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
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.tlvanelearning.ielts.adapter.QuizAdapter;
import com.tlvanelearning.ielts.common.AnswerItem;
import com.tlvanelearning.ielts.parent.BaseActivity;
import com.tlvanelearning.ielts.util.UIUtil;
import com.tlvanelearning.ielts.util.UIUtil.UIUtilCallback;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestActivity extends BaseActivity {
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
    private int numsQuestions;
    @BindView(R.id.empty)
    ProgressBar progressBar;

    private class GetDataAsyncTask extends AsyncTask<String, Void, ArrayList<JSONObject>> {
        private GetDataAsyncTask() {
        }

        protected ArrayList<JSONObject> doInBackground(String... strings) {
            try {
                Elements questions = Jsoup.parse(strings[0]).select("questions");
                numsQuestions = questions.size();
                Iterator it = questions.iterator();
                while (it.hasNext()) {
                    Element question = (Element) it.next();
                    Element title = question.select("question").first();
                    listDataHeader.add(title.text());
                    List<AnswerItem> answerItems = new ArrayList<>();
                    Iterator it2 = question.select("answer").iterator();
                    while (it2.hasNext()) {
                        Element answer = (Element) it2.next();
                        answerItems.add(new AnswerItem(answer.text(), answer.attr("value")));
                    }
                    listDataChild.put(title.text(), answerItems);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            facebookBanner(context, adsView);
            floatingActionButton.setVisibility(View.GONE);
        }

        protected void onPostExecute(ArrayList<JSONObject> jsonObjects) {
            super.onPostExecute(jsonObjects);
            listViewAdapter.notifyDataSetChanged();
            floatingActionButton.setVisibility(View.VISIBLE);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_test);
        this.listDataHeader = new ArrayList<>();
        this.listDataChild = new HashMap<>();
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
            QuizAdapter quizAdapter = new QuizAdapter(this, this.listDataHeader, this.listDataChild, false);
            this.listViewAdapter = quizAdapter;
            expandableListView.setAdapter(quizAdapter);
            this.listView.setEmptyView(this.progressBar);
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                String questions = extras.getString("question");
                new GetDataAsyncTask().execute(new String[]{questions});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doneClick() {
        try {
            if (this.alertDialog == null || !this.alertDialog.isShowing()) {
                int rightCount = 0;
                for (String key : this.listDataHeader) {
                    for (AnswerItem answerItem : (List<AnswerItem>) this.listDataChild.get(key)) {
                        if (answerItem.isChecked() && answerItem.compareValue(false)) {
                            rightCount++;
                        }
                    }
                }
                this.alertDialog = UIUtil.createDialog(this, getString(R.string.dialog_test_title), String.format(Locale.US, getString(R.string.message_format), new Object[]{Integer.valueOf(rightCount), Integer.valueOf(this.numsQuestions)}), new UIUtilCallback() {
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
        } catch (Exception e) {
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
}
