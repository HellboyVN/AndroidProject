package com.aerotools.photo.sketch.maker.editor;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import java.io.File;
import java.io.IOException;

public final class SavingHelper {
    public static String fileName = ".temp";
    public static String picName = new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath())).append("/Sketch").toString();

    public static void tempFileCreator(Context context) {
        if (isCardMounted()) {
            File file = new File(new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath())).append("/").append(context.getString(R.string.app_name)).append("/").append(fileName).toString());
            if (file.exists()) {
                File[] listFiles = file.listFiles();
                if (listFiles != null) {
                    for (File delete : listFiles) {
                        delete.delete();
                    }
                }
            }
        }
    }

    public static boolean isCardMounted() {
        if (Environment.getExternalStorageState().equals("mounted")) {
            return true;
        }
        return false;
    }

    public static File getFileName(String str, String str2) throws IOException {
        IOException iOException2;
        if (!isCardMounted()) {
            return null;
        }
        File file = new File(str);
        if (file.exists() || file.mkdirs()) {
            file = new File(str, str2);
            if (file.exists()) {
                file.delete();
            }
            try {
                file.createNewFile();
                if (file.isFile() && file.canWrite()) {
                    return file;
                }
                IOException iOException = new IOException(String.valueOf(4353));
                iOException.initCause(new Throwable(file.getAbsolutePath()));
                throw iOException;
            } catch (IOException e) {
                iOException2 = new IOException(String.valueOf(4354));
                iOException2.initCause(new Throwable(str));
                try {
                    throw iOException2;
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        iOException2 = new IOException(String.valueOf(4354));
        iOException2.initCause(new Throwable(str));
        try {
            throw iOException2;
        } catch (IOException e2) {
            e2.printStackTrace();
            return file;
        }
    }

    public static Bitmap getBitmapScale(Context context, String str, int i) throws Throwable {
        System.currentTimeMillis();
        try {
            new File(new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath())).append("/").append(context.getString(R.string.app_name)).append("/").append(fileName).append("/").append(str).toString()).exists();
        } catch (Exception e) {
            Throwable excep2222 = null;
            excep2222.printStackTrace();
        } catch (Throwable th3) {
            Throwable th = th3;
        }
        return null;
    }
}
