package lp.me.allgifs.fragment;

import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.ButtonCallback;
import com.afollestad.materialdialogs.folderselector.FolderChooserDialog.Builder;
import com.bumptech.glide.Glide;

import lp.me.allgifs.R;
import lp.me.allgifs.activity.AGActivity;

public class SettingsFragment extends Fragment {

    public static class MyPreferenceFragment extends PreferenceFragment {
        AGActivity father;
        String folder_path;

        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getPreferenceManager().setSharedPreferencesName("settings_data");
            addPreferencesFromResource(R.xml.settings_data);
            this.folder_path = getActivity().getSharedPreferences("settings_data", 0).getString("folder_chooser", Environment.getExternalStorageDirectory().getPath());
            this.father = (AGActivity) getActivity();
            Preference filePicker = findPreference("folder_chooser");
            filePicker.setSummary(this.folder_path + "/GIFs");
            filePicker.setOnPreferenceClickListener(new OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    if (MyPreferenceFragment.checkAndroidVersion()) {
                        MyPreferenceFragment.this.requestPermission();
                    } else {
                        new Builder(MyPreferenceFragment.this.father).chooseButton(R.string.folder_choose).cancelButton(R.string.folder_cancel).initialPath(MyPreferenceFragment.this.folder_path).show();
                    }
                    return false;
                }
            });
            findPreference("clear_cache").setOnPreferenceClickListener(new OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    new AsyncTask<String, String, String>() {
                        protected String doInBackground(String... strings) {
                            Glide.get(MyPreferenceFragment.this.father).clearDiskCache();
                            return null;
                        }

                        @Override
                        protected void onPostExecute(String s) {
                            super.onPostExecute(s);
                            Toast.makeText(MyPreferenceFragment.this.father, "Clear successfully", Toast.LENGTH_SHORT).show();
                        }
                    }.execute(new String[0]);
                    return false;
                }
            });
        }

        public void FolderChooser() {
            new Builder(this.father).chooseButton(R.string.folder_choose).cancelButton(R.string.folder_cancel).initialPath(this.folder_path).show();
        }

        public static boolean checkAndroidVersion() {
            return VERSION.SDK_INT >= 23;
        }

        @TargetApi(23)
        public void requestPermission() {
            if (getActivity().checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED) {
                FolderChooser();
            } else if (shouldShowRequestPermissionRationale("android.permission.WRITE_EXTERNAL_STORAGE")) {
                new MaterialDialog.Builder(getActivity()).title(R.string.app_name).content(R.string.permission_request_explain2)
                        .positiveText(R.string.ok).positiveColorRes(R.color.primary).contentColor(ViewCompat.MEASURED_STATE_MASK)
                        .callback(new ButtonCallback() {
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        dialog.dismiss();
                        MyPreferenceFragment.this.requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 2016);
                    }
                }).show();
            } else {
                requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 2016);
            }
        }

        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            if (requestCode != 2016) {
                return;
            }
            if (grantResults[0] == 0) {
                Toast.makeText(getActivity(), "Permission is given. Thanks", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), "Permission request denied. Operation fail", Toast.LENGTH_LONG).show();
            }
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.settings, container, false);
        try {
            getActivity().getFragmentManager().beginTransaction().replace(R.id.setting_layout, new MyPreferenceFragment()).commit();
        } catch (Exception e) {
        }
        return root;
    }

}
