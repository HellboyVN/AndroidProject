package lp.me.allgifs.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.folderselector.FolderChooserDialog;
import com.afollestad.materialdialogs.folderselector.FolderChooserDialog.FolderCallback;

import java.io.File;

import lp.me.allgifs.R;

public class AboutFragment extends Fragment implements FolderCallback {
    String[][] data;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.about_fragment, container, false);
//        ImageButton email = (ImageButton) root.findViewById(R.id.about_email);
//        ImageButton instagram = (ImageButton) root.findViewById(R.id.about_instagram);
//        ImageButton translate = (ImageButton) root.findViewById(R.id.about_translate_btn);
//        ImageButton rate = (ImageButton) root.findViewById(R.id.about_rate);
//        root.findViewById(R.id.about_facebook).setOnClickListener(new OnClickListener() {
//            public void onClick(View view) {
//                try {
//                     startActivity(new Intent("android.intent.action.VIEW", Uri.parse("fb://facewebmodal/f?href=https://www.facebook.com/al.hadidi.apps")));
//                } catch (Exception e) {
//                     startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://www.facebook.com/al.hadidi.apps")));
//                }
//            }
//        });
//        instagram.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                Intent likeIng = new Intent("android.intent.action.VIEW", Uri.parse("http://instagram.com/_u/al.hadidi.apps"));
//                likeIng.setPackage("com.instagram.android");
//                try {
//                     startActivity(likeIng);
//                } catch (ActivityNotFoundException e) {
//                     startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://instagram.com/al.hadidi.apps")));
//                }
//            }
//        });
//        email.setOnClickListener(new OnClickListener() {
//            public void onClick(View view) {
//                try {
//                    Intent intent = new Intent("android.intent.action.SENDTO", Uri.fromParts("mailto", "alhadidi.apps@gmail.com", null));
//                    intent.putExtra("android.intent.extra.SUBJECT",  getResources().getString(R.string.about_app_name));
//                     startActivity(intent);
//                } catch (Exception e) {
//                    Toast.makeText( getActivity(), "No Email client found.\n Send from web to: alhadidi.apps@gmail.com", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//        translate.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                 startActivity(new Intent("android.intent.action.VIEW", Uri.parse( getString(R.string.translate_link))));
//            }
//        });
//        rate.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                try {
//                     startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=lp.me.allgifs")));
//                } catch (Exception e) {
//                     startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=lp.me.allgifs")));
//                }
//            }
//        });
        return root;
    }

    public void onFolderSelection(@NonNull FolderChooserDialog dialog, @NonNull File folder) {
    }

    @Override
    public void onFolderChooserDismissed(@NonNull FolderChooserDialog dialog) {

    }
}
