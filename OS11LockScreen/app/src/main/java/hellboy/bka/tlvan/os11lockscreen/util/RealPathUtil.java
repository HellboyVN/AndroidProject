package hellboy.bka.tlvan.os11lockscreen.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore.Images.Media;

public class RealPathUtil {
    @SuppressLint({"NewApi"})
    public static String getRealPathFromURI_API19(Context context, Uri uri) {
        String filePath = "";
        String id = DocumentsContract.getDocumentId(uri).split(":")[1];
        String[] column = new String[]{"_data"};
        Cursor cursor = context.getContentResolver().query(Media.EXTERNAL_CONTENT_URI, column, "_id=?", new String[]{id}, null);
        int columnIndex = cursor.getColumnIndex(column[0]);
        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }

    @SuppressLint({"NewApi"})
    public static String getRealPathFromURI_API11to18(Context context, Uri contentUri) {
        Cursor cursor = new CursorLoader(context, contentUri, new String[]{"_data"}, null, null, null).loadInBackground();
        if (cursor == null) {
            return null;
        }
        int column_index = cursor.getColumnIndexOrThrow("_data");
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public static String getRealPathFromURI_BelowAPI11(Context context, Uri contentUri) {
        Cursor cursor = context.getContentResolver().query(contentUri, new String[]{"_data"}, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow("_data");
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}
