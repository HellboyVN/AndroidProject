package com.photo.sketch.gallery;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import com.aerotools.photo.sketch.maker.editor.R;
import java.io.File;

public class MediaChooserConstants {
    public static final int BUCKET_SELECT_IMAGE_CODE = 1000;
    public static final int BUCKET_SELECT_VIDEO_CODE = 2000;
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    public static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    public static int MAX_MEDIA_LIMIT = 1;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static int SELECTED_IMAGE_SIZE_IN_MB = 20;
    public static int SELECTED_MEDIA_COUNT = 0;
    public static int SELECTED_VIDEO_SIZE_IN_MB = 20;
    public static String folderName = "learnNcode";
    public static boolean showCameraVideo = true;
    public static boolean showImage = true;
    public static boolean showVideo = true;

    public static long ChekcMediaFileSize(File mediaFile, boolean isVideo) {
        int requireSize;
        long fileSizeInMB = (mediaFile.length() / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID;
        if (isVideo) {
            requireSize = SELECTED_VIDEO_SIZE_IN_MB;
        } else {
            requireSize = SELECTED_IMAGE_SIZE_IN_MB;
        }
        if (fileSizeInMB <= ((long) requireSize)) {
            return 0;
        }
        return fileSizeInMB;
    }

    public static Builder getDialog(Context context) {
        Builder dialog = new Builder(context);
        dialog.setCancelable(false);
        dialog.setTitle("Please Wait");
        dialog.setView(((LayoutInflater) context.getSystemService("layout_inflater")).inflate(R.layout.view_loading_media_chooser, null));
        return dialog;
    }
}
