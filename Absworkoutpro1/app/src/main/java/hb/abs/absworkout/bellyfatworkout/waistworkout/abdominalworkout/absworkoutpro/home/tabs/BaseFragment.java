package hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.home.tabs;

import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.R;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.utils.TypeFaceService;
import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;

public abstract class BaseFragment extends Fragment {
    public String placeName;
    public String placeType;
    public Typeface rBold;
    public Typeface rMedium;
    public Typeface rRegular;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFonts();
    }

    public void onStart() {
        super.onStart();
    }

    private void initFonts() {
        this.rMedium = TypeFaceService.getInstance().getRobotoMedium(getActivity());
        this.rRegular = TypeFaceService.getInstance().getRobotoRegular(getActivity());
        this.rBold = TypeFaceService.getInstance().getRobotoBold(getActivity());
    }

    protected int getActionBarSize() {
        Activity activity = getActivity();
        if (activity == null) {
            return 0;
        }
        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
        TypedArray a = activity.obtainStyledAttributes(new TypedValue().data, textSizeAttr);
        int actionBarSize = a.getDimensionPixelSize(0, -1);
        a.recycle();
        return actionBarSize;
    }

    protected int getScreenHeight() {
        Activity activity = getActivity();
        if (activity == null) {
            return 0;
        }
        return activity.findViewById(16908290).getHeight();
    }
}
