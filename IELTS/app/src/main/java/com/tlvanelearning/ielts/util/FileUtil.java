package com.tlvanelearning.ielts.util;

import android.content.Context;
import java.io.File;

public class FileUtil {
    public static String getFilename(String filePath) {
        return filePath.substring(filePath.lastIndexOf("/") + 1);
    }

    public static File mediaFile(Context context, String url) {
        return new File(context.getExternalFilesDir(null).getPath() + "/" + getFilename(url));
    }

    public static boolean fileExists(Context context, String url) {
        return new File(context.getExternalFilesDir(null).getPath() + "/" + getFilename(url)).exists();
    }

    public static String filePath(Context context, String url) {
        return new File(context.getExternalFilesDir(null).getPath() + "/" + getFilename(url)).getAbsolutePath();
    }
}
