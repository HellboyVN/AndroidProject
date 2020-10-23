package hb.me.makemoneyonline.basic;


import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import hb.me.makemoneyonline.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class BasicTipFragment extends Fragment {

    RecyclerView mRecyclerView;
    List<BasicPageItem> listItems;
    BasicTipAdapter basicTipAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_basic_tip, container, false);
        initUI(view);
        loadData();
        return view;
    }

    public void loadData(){
        listItems = new ArrayList<>();
        TypedArray ta = getContext().getResources().obtainTypedArray(R.array.tipTitleValues);
        //InputStream ims = getActivity().getAssets().open("avatar.jpg");
        // load image as Drawable
        //Drawable d = Drawable.createFromStream(ims, null);
        // set image to ImageView
        //mImage.setImageDrawable(d);
        for(int i=0; i<ta.length(); i++){
            BasicPageItem item = new BasicPageItem();
            item.setTitle(ta.getString(i));
            item.setLocalUrl("file:///android_asset/page"+ String.valueOf(i+1)+"/index.html");
            listItems.add(item);
        }
        basicTipAdapter = new BasicTipAdapter(getContext(), listItems);
        mRecyclerView.setAdapter(basicTipAdapter);

    }

    void initUI(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }


}
