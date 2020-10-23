package hb.giphy.allgif.giffree.presenter;

import android.content.Context;

import com.koushikdutta.ion.Ion;

import hb.giphy.allgif.giffree.util.PrefManager;


/**
 * Created by nxtruong on 7/12/2017.
 */

public class BasePresenter {
    protected PrefManager mPrefManager;
    protected Context mContext;


    public BasePresenter(){

    }
    public BasePresenter(Context context) {
        mPrefManager = new PrefManager(context);
        this.mContext = context;
    }


    public PrefManager getPrefManager() {
        return mPrefManager;
    }

    public void onDestroy() {
        Ion.getDefault(mContext).cancelAll(mContext);
    }
}
