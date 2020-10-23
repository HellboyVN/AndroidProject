package hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.home.tabs.soon;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;

import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.R;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.home.tabs.BaseFragment;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.utils.TypeFaceService;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SoonFragment extends BaseFragment {
    public static final String ARG_INITIAL_POSITION = "ARG_INITIAL_POSITION";
    private View headerView;
    Activity parentActivity;
    private Typeface rBold;
    private Typeface rLight;
    @BindView(R.id.soonText)
    AppCompatTextView soonText;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_soon, container, false);
        ButterKnife.bind((Object) this, view);
        this.parentActivity = getActivity();
        this.soonText.setTypeface(TypeFaceService.getInstance().getRobotoRegular(getActivity()));
        this.headerView = LayoutInflater.from(getActivity()).inflate(R.layout.padding, null);
        if (this.parentActivity instanceof ObservableScrollViewCallbacks) {
            Bundle args = getArguments();
            if (args != null && args.containsKey("ARG_INITIAL_POSITION")) {
                args.getInt("ARG_INITIAL_POSITION", 0);
            }
        }
        return view;
    }
}
