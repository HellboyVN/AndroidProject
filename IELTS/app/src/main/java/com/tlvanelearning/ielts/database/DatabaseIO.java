package com.tlvanelearning.ielts.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.Build.VERSION;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.tlvanelearning.ielts.common.Config;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class DatabaseIO extends SQLiteAssetHelper implements KEYConstants {
    private static final String TAG = DatabaseIO.class.getName();
    private static DatabaseIO databaseIO;
    private final Context context;

    public static DatabaseIO database(Context context) {
        if (databaseIO == null) {
            databaseIO = new DatabaseIO(context);
        }
        return databaseIO;
    }

    public DatabaseIO(Context context) {
        super(context, "app.db", null, 4);
        this.context = context;
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e(TAG, "Updating table from " + oldVersion + " to " + newVersion);
        int i = oldVersion;
//        while (i < newVersion) {
//            try {
//                String migrationName = String.format("upgrade_%d-%d.sql", new Object[]{Integer.valueOf(i), Integer.valueOf(i + 1)});
//                Log.d(TAG, "Looking for migration file: " + migrationName);
//                readAndExecuteSQLScript(db, this.context, migrationName);
//                i++;
//            } catch (Exception exception) {
//                Log.e(TAG, "Exception running upgrade script:", exception);
//                return;
//            }

    }

    private void readAndExecuteSQLScript(SQLiteDatabase db, Context ctx, String fileName) {
        IOException e;
        Throwable th;
        if (TextUtils.isEmpty(fileName)) {
            Log.d(TAG, "SQL script file name is empty");
            return;
        }
        Log.d(TAG, "Script found. Executing...");
        BufferedReader reader = null;
        try {
            BufferedReader reader2 = new BufferedReader(new InputStreamReader(ctx.getAssets().open("databases/" + fileName)));
            try {
                executeSQLScript(db, reader2);
                if (reader2 != null) {
                    try {
                        reader2.close();
                        reader = reader2;
                        return;
                    } catch (IOException e2) {
                        Log.e(TAG, "IOException:", e2);
                        reader = reader2;
                        return;
                    }
                }
            } catch (IOException e3) {
                e = e3;
                reader = reader2;
                try {
                    Log.e(TAG, "IOException:", e);
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e22) {
                            Log.e(TAG, "IOException:", e22);
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e222) {
                            Log.e(TAG, "IOException:", e222);
                        }
                    }
                    try {
                        throw th;
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            } catch (Throwable th3) {
                th = th3;
                reader = reader2;
                if (reader != null) {
                    reader.close();
                }
                try {
                    throw th;
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        } catch (IOException e4) {
            e = e4;
            Log.e(TAG, "IOException:", e);
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private void executeSQLScript(SQLiteDatabase db, BufferedReader reader) throws IOException {
        StringBuilder statement = new StringBuilder();
        while (true) {
            String line = reader.readLine();
            if (line != null) {
                statement.append(line);
                statement.append("\n");
                if (line.endsWith(";")) {
                    db.execSQL(statement.toString());
                    statement = new StringBuilder();
                }
            } else {
                return;
            }
        }
    }

    public ArrayList<JSONObject> getLessons(int position, boolean favorite) {
        ArrayList<JSONObject> jsonObjects = new ArrayList<>();
        try {
            Cursor cursor;
            SQLiteDatabase sqLiteDatabase = getReadableDatabase();
            SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
            String[] SQLSelect;
            JSONObject jsonObject;
            switch (position) {
                case -4:
                    SQLSelect = new String[]{"id", "title", "audio", "favorite"};
                    sqLiteQueryBuilder.setTables(getLessonTable(position));
                    if (favorite) {
                        cursor = sqLiteQueryBuilder.query(sqLiteDatabase, SQLSelect, "favorite='1'", null, null, null, "id", null);
                    } else {
                        cursor = sqLiteQueryBuilder.query(sqLiteDatabase, SQLSelect, null, null, null, null, "id", null);
                    }
                    if (cursor.moveToFirst()) {
                        do {
                            jsonObjects.add(new JSONObject().put("id", cursor.getInt(0)).put("title", cursor.getString(1)).put("audio", cursor.getString(2)).put("favorite", cursor.getString(3)));
                        } while (cursor.moveToNext());
                        break;
                    }
                    break;
                case -3:
                case -2:
                case -1:
                    SQLSelect = new String[]{"id", "title", "audio", "question"};
                    sqLiteQueryBuilder.setTables("lessons");
                    if (favorite) {
                        cursor = sqLiteQueryBuilder.query(sqLiteDatabase, SQLSelect, "favorite = 1", null, null, null, "id", null);
                    } else {
                        cursor = sqLiteQueryBuilder.query(sqLiteDatabase, SQLSelect, "type = " + (0 - position), null, null, null, "id", null);
                    }
                    if (cursor.moveToFirst()) {
                        do {
                            jsonObjects.add(new JSONObject().put("id", cursor.getInt(0)).put("title", cursor.getString(1)).put("audio", Config.URL + cursor.getString(2)).put("question", cursor.getString(3)));
                        } while (cursor.moveToNext());
                        break;
                    }
                    break;
                case 3:
                    SQLSelect = new String[]{"SubLevel"};
                    sqLiteQueryBuilder.setTables(getLessonTable(position));
                    cursor = sqLiteQueryBuilder.query(sqLiteDatabase, SQLSelect, null, null, "SubLevel", null, "SubLevel", null);
                    if (cursor.moveToFirst()) {
                        do {
                            jsonObjects.add(new JSONObject().put("SubLevel", cursor.getInt(0)).put("title", "IELTS - Quiz " + cursor.getInt(0)));
                        } while (cursor.moveToNext());
                        break;
                    }
                    break;
                case 4:
                    SQLSelect = new String[]{"id", "keyword", "type", "definition", "example"};
                    sqLiteQueryBuilder.setTables(getLessonTable(position));
                    cursor = sqLiteQueryBuilder.query(sqLiteDatabase, SQLSelect, null, null, null, null, "id", null);
                    if (cursor.moveToFirst()) {
                        do {
                            jsonObjects.add(new JSONObject().put("id", cursor.getInt(0)).put("title", cursor.getString(1)).put("desc", cursor.getString(2) + "\n Definition: " + cursor.getString(3) + "\n Example: " + cursor.getString(4)));
                        } while (cursor.moveToNext());
                        break;
                    }
                    break;
                case 6:
                    SQLSelect = new String[]{"id", "title", "audio", "favorite"};
                    sqLiteQueryBuilder.setTables(getLessonTable(position));
                    if (favorite) {
                        cursor = sqLiteQueryBuilder.query(sqLiteDatabase, SQLSelect, "favorite='1'", null, null, null, "id", null);
                    } else {
                        cursor = sqLiteQueryBuilder.query(sqLiteDatabase, SQLSelect, null, null, null, null, "id", null);
                    }
                    if (cursor.moveToFirst()) {
                        do {
                            jsonObjects.add(new JSONObject().put("id", cursor.getInt(0)).put("title", cursor.getString(1)).put("audio", cursor.getString(2)).put("favorite", cursor.getString(3)));
                        } while (cursor.moveToNext());
                        break;
                    }
                    break;
                case 9:
                case 11:
                    SQLSelect = new String[]{"id", "title"};
                    sqLiteQueryBuilder.setTables(getLessonTable(position));
                    cursor = sqLiteQueryBuilder.query(sqLiteDatabase, SQLSelect, null, null, null, null, "id", null);
                    if (cursor.moveToFirst()) {
                        do {
                            jsonObjects.add(new JSONObject().put("id", cursor.getInt(0)).put("title", cursor.getString(1)));
                        } while (cursor.moveToNext());
                        break;
                    }
                    break;
                case 13:
                    SQLSelect = new String[]{"id", "title", "desc", "example", "favorite"};
                    sqLiteQueryBuilder.setTables(getLessonTable(position));
                    if (favorite) {
                        cursor = sqLiteQueryBuilder.query(sqLiteDatabase, SQLSelect, "favorite = '1'", null, null, null, "id", null);
                    } else {
                        cursor = sqLiteQueryBuilder.query(sqLiteDatabase, SQLSelect, null, null, null, null, "id", null);
                    }
                    if (cursor.moveToFirst()) {
                        do {
                            jsonObjects.add(new JSONObject().put("id", cursor.getInt(0)).put("title", cursor.getString(1)).put("desc", cursor.getString(2).replace("**newline**", "\n") + "Example: " + cursor.getString(3).replace("**newline**", "\n")).put("favorite", cursor.getString(4)));
                        } while (cursor.moveToNext());
                        break;
                    }
                    break;
                case 14:
                    SQLSelect = new String[]{"id", "title", "desc", "favorite"};
                    sqLiteQueryBuilder.setTables(getLessonTable(position));
                    if (favorite) {
                        cursor = sqLiteQueryBuilder.query(sqLiteDatabase, SQLSelect, "favorite = '1'", null, null, null, "id", null);
                    } else {
                        cursor = sqLiteQueryBuilder.query(sqLiteDatabase, SQLSelect, null, null, null, null, "id", null);
                    }
                    if (cursor.moveToFirst()) {
                        do {
                            jsonObjects.add(new JSONObject().put("id", cursor.getInt(0)).put("title", cursor.getString(1)).put("desc", cursor.getString(2)).put("favorite", cursor.getString(3)));
                        } while (cursor.moveToNext());
                        break;
                    }
                    break;
                case 15:
                    SQLSelect = new String[]{"baseform", "pastsimple", "pastpart", "person3rd", "gerund", "definition"};
                    sqLiteQueryBuilder.setTables(getLessonTable(position));
                    cursor = sqLiteQueryBuilder.query(sqLiteDatabase, SQLSelect, null, null, null, null, null, null);
                    if (cursor.moveToFirst()) {
                        do {
                            jsonObjects.add(new JSONObject().put("title", cursor.getString(0)).put("desc", "Past Simple: " + cursor.getString(1) + "\nPast Participle: " + cursor.getString(2) + "\n3rd Person Singular: " + cursor.getString(3) + "\nPresent Participle/Gerund: " + cursor.getString(4) + "\n\nDefinition: " + cursor.getString(5).replace("<br>", "\n")));
                        } while (cursor.moveToNext());
                        break;
                    }
                    break;
                case 16:
                    SQLSelect = new String[]{"id", "title", "desc", "favorite"};
                    sqLiteQueryBuilder.setTables(getLessonTable(position));
                    if (favorite) {
                        cursor = sqLiteQueryBuilder.query(sqLiteDatabase, SQLSelect, "favorite = '1'", null, null, null, "id", null);
                    } else {
                        cursor = sqLiteQueryBuilder.query(sqLiteDatabase, SQLSelect, null, null, null, null, "id", null);
                    }
                    if (cursor.moveToFirst()) {
                        do {
                            jsonObjects.add(new JSONObject().put("id", cursor.getInt(0)).put("title", cursor.getString(1)).put("desc", cursor.getString(2)).put("favorite", cursor.getString(3)));
                        } while (cursor.moveToNext());
                        break;
                    }
                    break;
                case 17:
                    SQLSelect = new String[]{"title", "definition", "example"};
                    sqLiteQueryBuilder.setTables(getLessonTable(position));
                    cursor = sqLiteQueryBuilder.query(sqLiteDatabase, SQLSelect, null, null, null, null, null, null);
                    if (cursor.moveToFirst()) {
                        do {
                            jsonObjects.add(new JSONObject().put("title", cursor.getString(0)).put("desc", cursor.getString(1) + "\n" + cursor.getString(2)));
                        } while (cursor.moveToNext());
                        break;
                    }
                    break;
                case 18:
                    SQLSelect = new String[]{"title", "name"};
                    sqLiteQueryBuilder.setTables(getLessonTable(position));
                    cursor = sqLiteQueryBuilder.query(sqLiteDatabase, SQLSelect, null, null, "name", null, null, null);
                    if (cursor.moveToFirst()) {
                        do {
                            jsonObjects.add(new JSONObject().put("id", cursor.getInt(0)).put("title", cursor.getString(1)));
                        } while (cursor.moveToNext());
                        break;
                    }
                    break;
                case 19:
                    SQLSelect = new String[]{"id", "title", "definition", "example", "favorite"};
                    sqLiteQueryBuilder.setTables(getLessonTable(position));
                    if (favorite) {
                        cursor = sqLiteQueryBuilder.query(sqLiteDatabase, SQLSelect, "favorite = '1'", null, null, null, "id", null);
                    } else {
                        cursor = sqLiteQueryBuilder.query(sqLiteDatabase, SQLSelect, null, null, null, null, "id", null);
                    }
                    if (cursor.moveToFirst()) {
                        do {
                            jsonObjects.add(new JSONObject().put("id", cursor.getInt(0)).put("title", cursor.getString(1)).put("desc", "Definition: " + cursor.getString(2) + "\nExample: " + cursor.getString(3)).put("favorite", cursor.getString(4)));
                        } while (cursor.moveToNext());
                        break;
                    }
                    break;
                case 20:
                    SQLSelect = new String[]{"id", "title", "definition", "example", "favorite"};
                    sqLiteQueryBuilder.setTables(getLessonTable(position));
                    if (favorite) {
                        cursor = sqLiteQueryBuilder.query(sqLiteDatabase, SQLSelect, "favorite = '1'", null, null, null, "id", null);
                    } else {
                        cursor = sqLiteQueryBuilder.query(sqLiteDatabase, SQLSelect, null, null, null, null, "id", null);
                    }
                    if (cursor.moveToFirst()) {
                        do {
                            jsonObjects.add(new JSONObject().put("id", cursor.getInt(0)).put("title", cursor.getString(1)).put("desc", "Definition: " + cursor.getString(2) + "\nExample: " + cursor.getString(3)).put("favorite", cursor.getString(4)));
                        } while (cursor.moveToNext());
                        break;
                    }
                    break;
                case 21:
                    SQLSelect = new String[]{"id", "title"};
                    sqLiteQueryBuilder.setTables(getLessonTable(position));
                    cursor = sqLiteQueryBuilder.query(sqLiteDatabase, SQLSelect, null, null, null, null, "id", null);
                    if (cursor.moveToFirst()) {
                        do {
                            jsonObjects.add(new JSONObject().put("id", cursor.getInt(0)).put("title", cursor.getString(1)));
                        } while (cursor.moveToNext());
                        break;
                    }
                    break;
                case 22:
                    SQLSelect = new String[]{"id", "name", "content", "favorite"};
                    sqLiteQueryBuilder.setTables(getLessonTable(position));
                    if (favorite) {
                        cursor = sqLiteQueryBuilder.query(sqLiteDatabase, SQLSelect, "favorite = '1'", null, null, null, "name", null);
                    } else {
                        cursor = sqLiteQueryBuilder.query(sqLiteDatabase, SQLSelect, null, null, null, null, "id", null);
                    }
                    if (cursor.moveToFirst()) {
                        do {
                            jsonObject = new JSONObject().put("id", cursor.getInt(0)).put("favorite", cursor.getString(3));
                            if (VERSION.SDK_INT >= 24) {
                                jsonObject.put("title", Html.fromHtml(cursor.getString(1), 63));
                            } else {
                                jsonObject.put("title", Html.fromHtml(cursor.getString(1)));
                            }
                            jsonObjects.add(jsonObject);
                        } while (cursor.moveToNext());
                        break;
                    }
                    break;
                case 23:
                    SQLSelect = new String[]{"id", "word", "favorite"};
                    sqLiteQueryBuilder.setTables(getLessonTable(position));
                    if (favorite) {
                        cursor = sqLiteQueryBuilder.query(sqLiteDatabase, SQLSelect, "favorite = '1'", null, null, null, "id", null);
                    } else {
                        cursor = sqLiteQueryBuilder.query(sqLiteDatabase, SQLSelect, null, null, null, null, "id", null);
                    }
                    if (cursor.moveToFirst()) {
                        do {
                            jsonObjects.add(new JSONObject().put("id", cursor.getInt(0)).put("title", cursor.getString(1)).put("favorite", cursor.getString(2)));
                        } while (cursor.moveToNext());
                        break;
                    }
                    break;
                default:
                    SQLSelect = new String[]{"id", "title", "favorite", "type"};
                    if (position != 3) {
                        SQLSelect = new String[]{"id", "title", "favorite", "type", "desc"};
                    }
                    sqLiteQueryBuilder.setTables("ielts");
                    if (favorite) {
                        cursor = sqLiteQueryBuilder.query(sqLiteDatabase, SQLSelect, "favorite = 1", null, null, null, "id", null);
                    } else {
                        cursor = sqLiteQueryBuilder.query(sqLiteDatabase, SQLSelect, "type = " + (position - 1), null, null, null, "id", null);
                    }
                    if (cursor.moveToFirst()) {
                        do {
                            jsonObject = new JSONObject().put("id", cursor.getInt(0)).put("title", cursor.getString(1)).put("favorite", cursor.getString(2)).put("type", cursor.getString(3));
                            if (position != 3) {
                                jsonObject.put("desc", cursor.getString(4).replace("\\n", " ").replace("\\n\\n", " "));
                            }
                            jsonObjects.add(jsonObject);
                        } while (cursor.moveToNext());
                        break;
                    }
                    break;
            }
            cursor.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return jsonObjects;
    }

    public ArrayList<JSONObject> getItems(int position, int whereID, boolean favortie) {
        ArrayList<JSONObject> jsonObjects = new ArrayList();
        try {
            Cursor cursor;
            SQLiteDatabase sqLiteDatabase = getReadableDatabase();
            SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
            String[] SQLSelect;
            switch (position) {
                case 8:
                case 10:
                    SQLSelect = new String[4];
                    SQLSelect[0] = "id";
                    SQLSelect[1] = position == 8 ? "phrase" : "word";
                    SQLSelect[2] = "audio";
                    SQLSelect[3] = "favorite";
                    sqLiteQueryBuilder.setTables(getItemTable(position));
                    if (favortie) {
                        cursor = sqLiteQueryBuilder.query(sqLiteDatabase, SQLSelect, "catID=99 AND favorite = '1'", null, null, null, "id", null);
                    } else {
                        cursor = sqLiteQueryBuilder.query(sqLiteDatabase, SQLSelect, "catID=99", null, null, null, "id", null);
                    }
                    if (cursor.moveToFirst()) {
                        do {
                            jsonObjects.add(new JSONObject().put("id", cursor.getInt(0)).put("title", cursor.getString(1)).put("audio", decodeBase64(cursor.getString(2))).put("favorite", cursor.getString(3)));
                        } while (cursor.moveToNext());
                        break;
                    }
                    break;
                case 9:
                case 11:
                    SQLSelect = new String[4];
                    SQLSelect[0] = "id";
                    SQLSelect[1] = position == 9 ? "phrase" : "word";
                    SQLSelect[2] = "audio";
                    SQLSelect[3] = "favorite";
                    sqLiteQueryBuilder.setTables(getItemTable(position));
                    if (favortie) {
                        cursor = sqLiteQueryBuilder.query(sqLiteDatabase, SQLSelect, "catID<>99 AND favorite = '1'", null, null, null, "id", null);
                    } else {
                        cursor = sqLiteQueryBuilder.query(sqLiteDatabase, SQLSelect, "catID=" + whereID, null, null, null, "id", null);
                    }
                    if (cursor.moveToFirst()) {
                        do {
                            jsonObjects.add(new JSONObject().put("id", cursor.getInt(0)).put("title", cursor.getString(1)).put("audio", decodeBase64(cursor.getString(2))).put("favorite", cursor.getString(3)));
                        } while (cursor.moveToNext());
                        break;
                    }
                    break;
                default:
                    SQLSelect = new String[]{"id", "explaination", "sentence"};
                    sqLiteQueryBuilder.setTables(getItemTable(position));
                    if (favortie) {
                        cursor = sqLiteQueryBuilder.query(sqLiteDatabase, SQLSelect, "favorite = '1'", null, null, null, "id", null);
                    } else {
                        cursor = sqLiteQueryBuilder.query(sqLiteDatabase, SQLSelect, "TopicId=" + whereID, null, null, null, "id", null);
                    }
                    if (cursor.moveToFirst()) {
                        do {
                            jsonObjects.add(new JSONObject().put("id", cursor.getInt(0)).put("desc", cursor.getString(1)).put("title", cursor.getString(2)));
                        } while (cursor.moveToNext());
                        break;
                    }
                    break;
            }
            cursor.close();
        } catch (Exception e) {
        }
        return jsonObjects;
    }

    public ArrayList<JSONObject> getIdioms(int position, String where, boolean favortie) {
        ArrayList<JSONObject> jsonObjects = new ArrayList();
        try {
            Cursor cursor;
            SQLiteDatabase sqLiteDatabase = getReadableDatabase();
            SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
            String[] SQLSelect = new String[]{"id", "title", "desc", "example", "favorite"};
            sqLiteQueryBuilder.setTables(getItemTable(position));
            if (favortie) {
                cursor = sqLiteQueryBuilder.query(sqLiteDatabase, SQLSelect, "favorite = '1'", null, null, null, "id", null);
            } else {
                cursor = sqLiteQueryBuilder.query(sqLiteDatabase, SQLSelect, "name='" + where + "'", null, null, null, "id", null);
            }
            if (cursor.moveToFirst()) {
                do {
                    jsonObjects.add(new JSONObject().put("id", cursor.getInt(0)).put("title", cursor.getString(1)).put("desc", cursor.getString(2) + "\nExample: " + cursor.getString(3).replace("^^^", " ")).put("favorite", cursor.getString(4)));
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
        }
        return jsonObjects;
    }

    public ArrayList<JSONObject> getQuestions(int slevel) {
        ArrayList<JSONObject> jsonObjects = new ArrayList();
        try {
            Cursor cursor = getReadableDatabase().query("IELTSTest", new String[]{"QNumber", "QContent", "AnswerA", "AnswerB", "AnswerC", "AnswerD", "CorrectAnswer", "Passed"}, "SubLevel=" + slevel, null, null, null, "QNumber", null);
            if (cursor.moveToFirst()) {
                do {
                    JSONObject jsonObject = new JSONObject().put("SubLevel", slevel).put("QNumber", cursor.getInt(0)).put("question", cursor.getString(1)).put("correct", cursor.getString(6)).put("Passed", cursor.getString(7));
                    JSONArray jAnswers = new JSONArray();
                    if (!(TextUtils.isEmpty(cursor.getString(2)) || cursor.getString(2).equals("-"))) {
                        jAnswers.put(cursor.getString(2));
                    }
                    if (!(TextUtils.isEmpty(cursor.getString(3)) || cursor.getString(3).equals("-"))) {
                        jAnswers.put(cursor.getString(3));
                    }
                    if (!(TextUtils.isEmpty(cursor.getString(4)) || cursor.getString(4).equals("-"))) {
                        jAnswers.put(cursor.getString(4));
                    }
                    if (!(TextUtils.isEmpty(cursor.getString(5)) || cursor.getString(5).equals("-"))) {
                        jAnswers.put(cursor.getString(5));
                    }
                    jsonObject.put("answers", jAnswers);
                    jsonObjects.add(jsonObject);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return jsonObjects;
    }

    public String getContentWithId(int _id, int position) {
        try {
            SQLiteDatabase sqLiteDatabase = getReadableDatabase();
            SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
            String[] SQLSelect = new String[1];
            SQLSelect[0] = position != 23 ? "content" : "meaning";
            sqLiteQueryBuilder.setTables(getLessonTable(position));
            Cursor cursor = sqLiteQueryBuilder.query(sqLiteDatabase, SQLSelect, "id = " + _id, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                return cursor.getString(0).replace("\\n", " ").replace("\\n\\n", " ");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public int updateFavorite(int _id, boolean _fav, int position) {
        try {
            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("favorite", !_fav ? "1" : "0");
            return sqLiteDatabase.update(getLessonTable(position), contentValues, "id = " + _id, null);
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    public int updateFavoriteItem(int _id, boolean _fav, int position) {
        try {
            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("favorite", !_fav ? "1" : "0");
            return sqLiteDatabase.update(getItemTable(position), contentValues, "id = " + _id, null);
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    public void updatePassed(int slevel, int qnumber, boolean passed) {
        try {
            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("Passed", passed ? "1" : "0");
            sqLiteDatabase.update("IELTSTest", contentValues, "SubLevel=" + slevel + " AND " + "QNumber" + "=" + qnumber, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String decodeBase64(String base64String) {
        try {
            return new String(Base64.decode(base64String.getBytes("UTF-8"),Base64.DEFAULT), "UTF-8");
        } catch (Exception e) {
            return "";
        }
    }

    public String getLessonTable(int pos) {
        switch (pos) {
            case -4:
                return "Listening";
            case -3:
            case -2:
            case -1:
                return "lessons";
            case 3:
                return "IELTSTest";
            case 4:
                return "IELTSWords";
            case 6:
                return "Listening";
            case 9:
            case 11:
                return "MostCategories";
            case 13:
                return "Idioms";
            case 14:
                return "Topics";
            case 15:
                return "IVerbsFull";
            case 16:
                return "Proverbs";
            case 17:
                return "Slangs";
            case 18:
                return "TopicIdioms";
            case 19:
                return "PhrasalVerb";
            case 20:
                return "Sat";
            case 21:
                return "Tenses";
            case 22:
                return "grammars";
            case 23:
                return "vocabs";
            default:
                return "ielts";
        }
    }

    public String getItemTable(int pos) {
        switch (pos) {
            case 8:
            case 9:
                return "MostPhrases";
            case 10:
            case 11:
                return "MostWords";
            case 18:
                return "TopicIdioms";
            default:
                return "TopicPhrases";
        }
    }
}
