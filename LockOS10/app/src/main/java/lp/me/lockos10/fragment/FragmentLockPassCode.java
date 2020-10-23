package lp.me.lockos10.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import lp.me.lockos10.R;

public class FragmentLockPassCode extends Fragment {
    public static FragmentLockPassCode newInstance() {
        return new FragmentLockPassCode();
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lock_passcode, container, false);
    }
}
