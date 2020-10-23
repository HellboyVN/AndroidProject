package hb.giphy.allgif.giffree.fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.ButtonCallback;
import com.afollestad.materialdialogs.folderselector.FolderChooserDialog.Builder;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.bumptech.glide.Glide;

import hb.giphy.allgif.giffree.R;
import hb.giphy.allgif.giffree.activity.AGActivity;
import hb.giphy.allgif.giffree.util.AppConstant;


public class SettingsFragment extends Fragment {

    public static class MyPreferenceFragment extends PreferenceFragment implements BillingProcessor.IBillingHandler {
        AGActivity father;
        String folder_path;
        BillingProcessor bp;
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getPreferenceManager().setSharedPreferencesName("settings_data");
            addPreferencesFromResource(R.xml.settings_data);
            bp = new BillingProcessor(getActivity(), AppConstant.ggpublishkey, this);
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
            findPreference("remove_ads").setOnPreferenceClickListener(new OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    if (AppConstant.isRemoveAds(getActivity())) {
                        Toast.makeText(getActivity(), "Ads are already removed!", Toast.LENGTH_SHORT).show();
                        return true;
                    }else{
                        bp.purchase(getActivity(), "android.test.purchased");
                        return true;
                    }

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

        @Override
        public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
            Toast.makeText(getActivity(), "All ads removed! Please restart app for taking effect", Toast.LENGTH_LONG).show();
            AppConstant.setAdsFreeVersion(getActivity(),true);
            Log.e("check sharepreferent ",String.valueOf(AppConstant.isRemoveAds(getActivity())));
        }

        @Override
        public void onPurchaseHistoryRestored() {

        }

        @Override
        public void onBillingError(int errorCode, @Nullable Throwable error) {
            Toast.makeText(getActivity(), "Your Purchase has been canceled!", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onBillingInitialized() {

        }
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (!bp.handleActivityResult(requestCode, resultCode, data)) {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
        @Override
        public void onDestroy() {
            if (bp != null) {
                bp.release();
            }
            super.onDestroy();
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
