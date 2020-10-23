package hellboy.bka.tlvan.os11lockscreen.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hellboy.bka.tlvan.os11lockscreen.R;


public class FragmentLock extends Fragment {
    public static FragmentLock newInstance() {
        return new FragmentLock();
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lock, container, false);
    }
}
