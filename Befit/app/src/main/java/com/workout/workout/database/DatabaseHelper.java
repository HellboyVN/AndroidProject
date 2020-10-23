package com.workout.workout.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.workout.workout.constant.AppConstants;
import com.workout.workout.managers.PersistenceManager;
import com.workout.workout.modal.BodyPart;
import com.workout.workout.modal.Plan;
import com.workout.workout.modal.PlanCategory;
import com.workout.workout.modal.Training;
import com.workout.workout.modal.Workout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String COLUMN_BODY_PART_IMAGE_NAME = "body_part_image_name";
    private static final String COLUMN_BODY_PART_IMAGE_URL = "body_part_image_url";
    private static final String COLUMN_BODY_PART_NAME = "body_part_name";
    private static final String COLUMN_DAY = "day";
    public static final String COLUMN_PLAN_CATEGORY_ID = "plan_category_id";
    public static final String COLUMN_PLAN_DESCRIPTION = "plan_description";
    private static final String COLUMN_PLAN_ID = "plan_id";
    public static final String COLUMN_PLAN_IMAGE_NAME = "plan_image_name";
    public static final String COLUMN_PLAN_IMAGE_URL = "plan_image_url";
    public static final String COLUMN_PLAN_LOCK = "plan_lock";
    public static final String COLUMN_PLAN_NAME = "plan_name";
    public static final String COLUMN_PLAN_PRICE = "plan_price";
    public static final String COLUMN_PLAN_TYPE = "plan_type";
    private static final String COLUMN_SETS_AND_REPS = "sets_and_reps";
    private static final String COLUMN_WORKOUT_DESCRIPTION = "workout_description";
    private static final String COLUMN_WORKOUT_ID = "workout_id";
    private static final String COLUMN_WORKOUT_IMAGE_NAME = "workout_image_name";
    private static final String COLUMN_WORKOUT_IMAGE_URL = "workout_image_url";
    private static final String COLUMN_WORKOUT_NAME = "workout_name";
    private static final String COLUMN_WORKOUT_VIDEO_NAME = "workout_video_name";
    private static final String COLUMN_WORKOUT_VIDEO_URL = "workout_video_url";
    private static String DB_NAME = "WorkoutDB.db";
    private static String DB_PATH = "/data/data/com.workout.workout/databases/";
    private static DatabaseHelper databaseHelper = null;
    private static AtomicInteger mOpenCounter = new AtomicInteger();
    private final Context context;
    private SQLiteDatabase db = null;
    private SQLiteDatabase sqLiteDatabase;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
    }

    public void createSystemDataBaseFromDBFile() throws IOException {
        if (checkDataBase() && PersistenceManager.getDatabaseFileVersion().intValue() == 1) {
            Log.e("DatabaseHelper", "Database file already exists no need to create again");
            return;
        }
        Log.e("DatabaseHelper", "Database file not exists creating new DB");
        getReadableDatabase();
        try {
            copyDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        PersistenceManager.setDatabaseFileVersion(1);
    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH + DB_NAME;
            File file = new File(myPath);
            if (!file.exists() || file.isDirectory()) {
                Log.e("---EVANS---","No database");
                checkDB = null;
                if (checkDB != null) {
                    checkDB.close();
                }
                if (checkDB == null) {
                    return true;
                }
                return false;
            }
            checkDB = SQLiteDatabase.openDatabase(myPath, null, 1);
            if (checkDB != null) {
                checkDB.close();
            }
            if (checkDB == null) {
                return false;
            }
            return true;
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        return true;
    }

    private void copyDataBase() throws IOException {
        InputStream myInput = this.context.getAssets().open(DB_NAME);
        OutputStream myOutput = new FileOutputStream(DB_PATH + DB_NAME);
        byte[] buffer = new byte[1024];
        while (true) {
            int length = myInput.read(buffer);
            if (length > 0) {
                myOutput.write(buffer, 0, length);
            } else {
                myOutput.flush();
                myOutput.close();
                myInput.close();
                return;
            }
        }
    }

    public void openDataBase() throws SQLException {
        try {
            this.sqLiteDatabase = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, 1);
            Log.e("---EVANS----", "database open");
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

    public synchronized void close() {
        if (this.sqLiteDatabase != null) {
            this.sqLiteDatabase.close();
        }
        super.close();
    }

    public void onCreate(SQLiteDatabase db) {
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        //DatabaseHelper databaseHelper = null;
        synchronized (DatabaseHelper.class) {
            if (databaseHelper == null) {
                databaseHelper = new DatabaseHelper(context);
            }
        //    databaseHelper = databaseHelper;
        }
        return databaseHelper;
    }


    public java.util.ArrayList<com.workout.workout.modal.Workout> getWorkoutListForBodyPart(String r8) {
        ArrayList<Workout> arrayList = new ArrayList<>();
        StringBuilder data = new StringBuilder();
        data.append("SELECT * FROM Workout  WHERE  body_part_name = '"+r8+"'");
        this.db = openDatabase();
        Cursor c = db.rawQuery(data.toString(),null);
        if(c.moveToFirst()){
            do
            {
                Workout workout = new Workout();
                workout.setWorkout_id(c.getString(0));
                workout.setWorkout_name(c.getString(1));
                workout.setBody_part_name(c.getString(2));
                workout.setWorkout_description(c.getString(3));
                workout.setWorkout_image_url(c.getString(5));
                workout.setWorkout_image_name(c.getString(6));
                workout.setWorkout_video_name(c.getString(7));
                workout.setWorkout_video_url(c.getString(8));
                arrayList.add(workout);
            }while (c.moveToNext());

        }
        closeCursor(c);
        closeDatabase();

        return arrayList;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.ArrayList<com.workout.workout.modal.Training> getBodyPartList() {

        ArrayList<Training> arrayList = new ArrayList<>();
        StringBuilder data = new StringBuilder();
        data.append("SELECT * FROM Bodyparts");
        this.db = openDatabase();
        Cursor c = db.rawQuery(data.toString(),null);
        if(c.moveToFirst()){
            do
            {
                Training training = new Training();
                training.setPart_name(c.getString(0));
                training.setPart_image_name(c.getString(1));
                training.setImage_url(c.getString(2));
                arrayList.add(training);
            }while (c.moveToNext());

        }
        closeCursor(c);
        closeDatabase();

        return arrayList;


    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setBodyPartList(List<com.workout.workout.modal.Training> r9) {
        /*
        r8 = this;
        r1 = r8.getBodyPartList();
        r4 = r8.openDatabase();	 Catch:{ SQLException -> 0x00ca }
        r8.db = r4;	 Catch:{ SQLException -> 0x00ca }
        if (r1 == 0) goto L_0x006f;
    L_0x000c:
        r4 = r1.size();	 Catch:{ SQLException -> 0x00ca }
        r5 = r9.size();	 Catch:{ SQLException -> 0x00ca }
        if (r4 != r5) goto L_0x006f;
    L_0x0016:
        r3 = 0;
    L_0x0017:
        r4 = r9.size();	 Catch:{ SQLException -> 0x00ca }
        if (r3 >= r4) goto L_0x00c6;
    L_0x001d:
        r0 = new android.content.ContentValues;	 Catch:{ SQLException -> 0x00ca }
        r0.<init>();	 Catch:{ SQLException -> 0x00ca }
        r5 = "body_part_name";
        r4 = r9.get(r3);	 Catch:{ SQLException -> 0x00ca }
        r4 = (com.workout.workout.modal.Training) r4;	 Catch:{ SQLException -> 0x00ca }
        r4 = r4.getPart_name();	 Catch:{ SQLException -> 0x00ca }
        r0.put(r5, r4);	 Catch:{ SQLException -> 0x00ca }
        r5 = "body_part_image_name";
        r4 = r9.get(r3);	 Catch:{ SQLException -> 0x00ca }
        r4 = (com.workout.workout.modal.Training) r4;	 Catch:{ SQLException -> 0x00ca }
        r4 = r4.getPart_image_name();	 Catch:{ SQLException -> 0x00ca }
        r0.put(r5, r4);	 Catch:{ SQLException -> 0x00ca }
        r5 = "body_part_image_url";
        r4 = r9.get(r3);	 Catch:{ SQLException -> 0x00ca }
        r4 = (com.workout.workout.modal.Training) r4;	 Catch:{ SQLException -> 0x00ca }
        r4 = r4.getImage_url();	 Catch:{ SQLException -> 0x00ca }
        r0.put(r5, r4);	 Catch:{ SQLException -> 0x00ca }
        r4 = r8.db;	 Catch:{ SQLException -> 0x00ca }
        r5 = "Bodyparts";
        r6 = new java.lang.StringBuilder;	 Catch:{ SQLException -> 0x00ca }
        r6.<init>();	 Catch:{ SQLException -> 0x00ca }
        r7 = "rowid=";
        r6 = r6.append(r7);	 Catch:{ SQLException -> 0x00ca }
        r7 = r3 + 1;
        r6 = r6.append(r7);	 Catch:{ SQLException -> 0x00ca }
        r6 = r6.toString();	 Catch:{ SQLException -> 0x00ca }
        r7 = 0;
        r4.update(r5, r0, r6, r7);	 Catch:{ SQLException -> 0x00ca }
        r3 = r3 + 1;
        goto L_0x0017;
    L_0x006f:
        if (r1 == 0) goto L_0x00c6;
    L_0x0071:
        r4 = r1.size();	 Catch:{ SQLException -> 0x00ca }
        r5 = r9.size();	 Catch:{ SQLException -> 0x00ca }
        if (r4 == r5) goto L_0x00c6;
    L_0x007b:
        r4 = r8.db;	 Catch:{ SQLException -> 0x00ca }
        r5 = "delete from Bodyparts";
        r4.execSQL(r5);	 Catch:{ SQLException -> 0x00ca }
        r3 = 0;
    L_0x0083:
        r4 = r9.size();	 Catch:{ SQLException -> 0x00ca }
        if (r3 >= r4) goto L_0x00c6;
    L_0x0089:
        r0 = new android.content.ContentValues;	 Catch:{ SQLException -> 0x00ca }
        r0.<init>();	 Catch:{ SQLException -> 0x00ca }
        r5 = "body_part_name";
        r4 = r9.get(r3);	 Catch:{ SQLException -> 0x00ca }
        r4 = (com.workout.workout.modal.Training) r4;	 Catch:{ SQLException -> 0x00ca }
        r4 = r4.getPart_name();	 Catch:{ SQLException -> 0x00ca }
        r0.put(r5, r4);	 Catch:{ SQLException -> 0x00ca }
        r5 = "body_part_image_name";
        r4 = r9.get(r3);	 Catch:{ SQLException -> 0x00ca }
        r4 = (com.workout.workout.modal.Training) r4;	 Catch:{ SQLException -> 0x00ca }
        r4 = r4.getPart_image_name();	 Catch:{ SQLException -> 0x00ca }
        r0.put(r5, r4);	 Catch:{ SQLException -> 0x00ca }
        r5 = "body_part_image_url";
        r4 = r9.get(r3);	 Catch:{ SQLException -> 0x00ca }
        r4 = (com.workout.workout.modal.Training) r4;	 Catch:{ SQLException -> 0x00ca }
        r4 = r4.getImage_url();	 Catch:{ SQLException -> 0x00ca }
        r0.put(r5, r4);	 Catch:{ SQLException -> 0x00ca }
        r4 = r8.db;	 Catch:{ SQLException -> 0x00ca }
        r5 = "Bodyparts";
        r6 = 0;
        r4.insert(r5, r6, r0);	 Catch:{ SQLException -> 0x00ca }
        r3 = r3 + 1;
        goto L_0x0083;
    L_0x00c6:
        r8.closeDatabase();
    L_0x00c9:
        return;
    L_0x00ca:
        r2 = move-exception;
        r2.printStackTrace();	 Catch:{ all -> 0x00d2 }
        r8.closeDatabase();
        goto L_0x00c9;
    L_0x00d2:
        r4 = move-exception;
        r8.closeDatabase();
        throw r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.workout.workout.database.DatabaseHelper.setBodyPartList(java.util.List):void");
    }

    public void setWorkoutListForBodyPart(List<BodyPart> bodyPartList) {
        try {
            this.db = openDatabase();
            ContentValues contentValues = new ContentValues();
            removeAllWorkouts(this.db);
            for (int i = 0; i < bodyPartList.size(); i++) {
                updateWorkoutForBodyPart(((BodyPart) bodyPartList.get(i)).getWorkoutList(), contentValues, this.db);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
    }

    private void updateWorkoutForBodyPart(List<Workout> workoutList, ContentValues contentValues, SQLiteDatabase db) {
        for (int i = 0; i < workoutList.size(); i++) {
            String workoutId = ((Workout) workoutList.get(i)).getWorkout_id();
            contentValues.put(AppConstants.COLUMN_WORKOUT_ID, ((Workout) workoutList.get(i)).getWorkout_id());
            contentValues.put(AppConstants.COLUMN_WORKOUT_NAME, ((Workout) workoutList.get(i)).getWorkout_name());
            contentValues.put(AppConstants.COLUMN_BODY_PART_NAME, ((Workout) workoutList.get(i)).getBody_part_name());
            contentValues.put(AppConstants.COLUMN_WORKOUT_DESCRIPTION, ((Workout) workoutList.get(i)).getWorkout_description());
            contentValues.put(AppConstants.COLUMN_WORKOUT_IMAGE_URL, ((Workout) workoutList.get(i)).getWorkout_image_url());
            contentValues.put(AppConstants.COLUMN_WORKOUT_IMAGE_NAME, ((Workout) workoutList.get(i)).getWorkout_image_name());
            contentValues.put(AppConstants.COLUMN_WORKOUT_VIDEO_NAME, ((Workout) workoutList.get(i)).getWorkout_video_name());
            contentValues.put(AppConstants.COLUMN_WORKOUT_VIDEO_URL, ((Workout) workoutList.get(i)).getWorkout_video_url());
            db.replace(AppConstants.TABLE_NAME_WORKOUT, null, contentValues);
        }
    }

    private void removeAllWorkouts(SQLiteDatabase db) {
        try {
            db.execSQL("delete from Workout");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void removeAllWorkoutPlans() {
        try {
            this.db = openDatabase();
            this.db.execSQL("delete from workout_plan");
            this.db.execSQL("delete from plan_detail");
            this.db.execSQL("delete from plan_category");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.ArrayList<com.workout.workout.modal.Plan> getWorkoutListForPlanDay(com.workout.workout.modal.Plan r8) {
        ArrayList<Plan> arrayList = new ArrayList<>();
        StringBuilder data = new StringBuilder();
        data.append("SELECT * FROM workout_plan INNER JOIN Workout ON workout_plan.workout_id = Workout.workout_id WHERE workout_plan.plan_id='");
        data.append(r8.getPlan_id());
        data.append("' AND workout_plan.day='");
        data.append(r8.getDay());
        data.append("'");
        this.db = openDatabase();
        Cursor c = db.rawQuery(data.toString(),null);
        if(c.moveToFirst()){
            do
            {
                Plan plan = new Plan();
                plan.setPlan_id(c.getString(c.getColumnIndex("plan_id")));
                plan.setDay(c.getString(c.getColumnIndex("day")));
                plan.setBody_part_name(c.getString(c.getColumnIndex("body_part_name")));
                plan.setWorkout_id(c.getString(c.getColumnIndex("workout_id")));
                plan.setWorkout_name(c.getString(c.getColumnIndex("workout_name")));
                plan.setSets_and_reps(c.getString(c.getColumnIndex("sets_and_reps")));
                plan.setWorkout_image_name(c.getString(c.getColumnIndex("workout_image_name")));
                plan.setWorkout_image_name(c.getString(c.getColumnIndex("workout_image_name")));
                plan.setWorkout_image_url(c.getString(c.getColumnIndex("workout_image_url")));
                arrayList.add(plan);
            }while (c.moveToNext());

        }
        closeCursor(c);
        closeDatabase();

        return arrayList;

    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.ArrayList<com.workout.workout.modal.Plan> getWorkoutListForMyPlanDay(com.workout.workout.modal.Plan r8) {

        ArrayList<Plan> arrayList = new ArrayList<>();
        StringBuilder data = new StringBuilder();
        data.append("SELECT * FROM MyWorkoutPlans WHERE plan_id=");
        data.append(r8.getPlan_id());
        data.append(" AND day='");
        data.append(r8.getDay());
        data.append("'");
        this.db = openDatabase();
        Cursor c = db.rawQuery(data.toString(),null);
        if(c.moveToFirst()){
            do
            {
                Plan plan = new Plan();
                plan.setPlan_id(c.getString(0));
                plan.setPlan_name(c.getString(1));
                plan.setPlan_type(c.getString(2));
                plan.setPlan_description(c.getString(3));
                plan.setDay(c.getString(4));
                plan.setBody_part_name(c.getString(5));
                plan.setBody_part_image_name(c.getString(6));
                plan.setBody_part_image_url(c.getString(7));
                plan.setWorkout_id(c.getString(8));
                plan.setWorkout_name(c.getString(9));
                plan.setSets_and_reps(c.getString(10));
                plan.setWorkout_image_name(c.getString(11));
                plan.setWorkout_image_url(c.getString(12));
                arrayList.add(plan);
            }while (c.moveToNext());

        }
        closeCursor(c);
        closeDatabase();

        return arrayList;

    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.workout.workout.modal.Workout getWorkout(String r7) {
        Workout workout = null;
        StringBuilder data = new StringBuilder();
        data.append("SELECT * FROM Workout WHERE workout_id=");
        data.append(r7);
        this.db = openDatabase();
        Cursor c = db.rawQuery(data.toString(),null);
        if(c.moveToFirst()){
            do
            {
                workout = new Workout();
                workout.setWorkout_id(c.getString(0));
                workout.setWorkout_name(c.getString(1));
                workout.setBody_part_name(c.getString(2));
                workout.setWorkout_description(c.getString(3));
                workout.setWorkout_image_url(c.getString(5));
                workout.setWorkout_image_name(c.getString(6));
                workout.setWorkout_video_name(c.getString(7));
                workout.setWorkout_video_url(c.getString(8));

            }while (c.moveToNext());

        }
        closeCursor(c);
        closeDatabase();

        return workout;

    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.ArrayList<com.workout.workout.modal.Plan> getMyPlansWorkoutList() {

        ArrayList<Plan> arrayList = new ArrayList<>();
        StringBuilder data = new StringBuilder();
        data.append("SELECT DISTINCT plan_id, plan_name,plan_type, plan_description FROM MyWorkoutPlans WHERE plan_type='mine'");
        this.db = openDatabase();
        Cursor c = db.rawQuery(data.toString(),null);
        if(c.moveToFirst()){
            do
            {
                Plan plan = new Plan();
                plan.setPlan_id(c.getString(0));
                plan.setPlan_name(c.getString(1));
                plan.setPlan_type(c.getString(2));
                plan.setPlan_description(c.getString(3));

                arrayList.add(plan);
            }while (c.moveToNext());

        }
        closeCursor(c);
        closeDatabase();

        return arrayList;

    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public String getExercisesCount(com.workout.workout.modal.Plan r7) {
        String plan = null;
        StringBuilder data = new StringBuilder();
        data.append("SELECT COUNT(day) FROM workout_plan where day = '");
        data.append(r7.getDay());
        data.append("' and plan_id='");
        data.append(r7.getPlan_id());
        data.append("'");
        this.db = openDatabase();
        Cursor c = db.rawQuery(data.toString(),null);
        if(c.moveToFirst()){
            do
            {
                plan = c.getString(0);
            }while (c.moveToNext());

        }
        closeCursor(c);
        closeDatabase();

        return plan;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public String getMyPlanExercisesCount(com.workout.workout.modal.Plan r7) {
        String plan = null;
        StringBuilder data = new StringBuilder();
        data.append("SELECT COUNT(day) FROM MyWorkoutPlans where day = '");
        data.append(r7.getDay());
        data.append("' and plan_id='");
        data.append(r7.getPlan_id());
        data.append("'");
        this.db = openDatabase();
        Cursor c = db.rawQuery(data.toString(),null);
        if(c.moveToFirst()){
            do
            {
                plan = c.getString(0);
            }while (c.moveToNext());

        }
        closeCursor(c);
        closeDatabase();

        return plan;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.ArrayList<com.workout.workout.modal.Workout> getAllWorkoutList() {
        ArrayList<Workout> arrayList = new ArrayList<>();
        StringBuilder data = new StringBuilder();
        data.append("SELECT * FROM Workout");
        this.db = openDatabase();
        Cursor c = db.rawQuery(data.toString(),null);
        if(c.moveToFirst()){
            do
            {
                Workout workout = new Workout();
                workout.setWorkout_id(c.getString(0));
                workout.setWorkout_name(c.getString(1));
                workout.setBody_part_name(c.getString(2));
                workout.setWorkout_description(c.getString(3));
                workout.setWorkout_image_url(c.getString(5));
                workout.setWorkout_image_name(c.getString(6));
                workout.setWorkout_video_name(c.getString(7));
                workout.setWorkout_video_url(c.getString(8));
                arrayList.add(workout);
            }while (c.moveToNext());

        }
        closeCursor(c);
        closeDatabase();

        return arrayList;

    }

    public boolean createNewPlan(Plan plan) {
        long rowId = 0;
        try {
            this.db = openDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_PLAN_ID, plan.getPlan_id());
            values.put("plan_name", plan.getPlan_name());
            values.put(COLUMN_PLAN_TYPE, plan.getPlan_type());
            values.put(COLUMN_PLAN_DESCRIPTION, plan.getPlan_description());
            rowId = this.db.insert(AppConstants.TABLE_NAME_MY_WORKOUT_PLANS, null, values);
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
        if (rowId == -1) {
            return false;
        }
        return true;
    }

    public void deletePlan(Plan plan) {
        try {
            this.db = openDatabase();
            String[] whereArgs = new String[]{String.valueOf(plan.getPlan_id())};
            this.db.delete(AppConstants.TABLE_NAME_MY_WORKOUT_PLANS, "plan_id=?", whereArgs);
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
    }

    public boolean addWorkoutToPlan(Workout workout, Plan plan) {
        long rowId = 0;
        try {
            this.db = openDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_PLAN_ID, plan.getPlan_id());
            values.put("plan_name", plan.getPlan_name());
            values.put(COLUMN_PLAN_TYPE, plan.getPlan_type());
            values.put(COLUMN_PLAN_DESCRIPTION, plan.getPlan_description());
            values.put(COLUMN_DAY, plan.getDay());
            values.put(COLUMN_SETS_AND_REPS, plan.getSets_and_reps());
            values.put(COLUMN_WORKOUT_ID, workout.getWorkout_id());
            values.put(COLUMN_WORKOUT_NAME, workout.getWorkout_name());
            values.put(COLUMN_BODY_PART_NAME, workout.getBody_part_name());
            values.put(COLUMN_WORKOUT_IMAGE_NAME, workout.getWorkout_image_name());
            values.put(COLUMN_WORKOUT_IMAGE_URL, workout.getWorkout_image_url());
            values.put(COLUMN_BODY_PART_IMAGE_NAME, workout.getBody_part_image_name());
            values.put(COLUMN_BODY_PART_IMAGE_URL, workout.getBody_part_image_url());
            String table = AppConstants.TABLE_NAME_MY_WORKOUT_PLANS;
            String whereClause = "plan_id=?";
            //new String = String.valueOf(plan.getPlan_id());
            rowId = this.db.insert(table, null, values);
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
        if (rowId == -1) {
            return false;
        }
        return true;
    }

    public void deleteWorkoutFromPlan(Plan plan, Workout workout) {
        try {
            this.db = openDatabase();
            String[] whereArgs = new String[]{String.valueOf(plan.getPlan_id()), String.valueOf(workout.getWorkout_id())};
            this.db.delete(AppConstants.TABLE_NAME_MY_WORKOUT_PLANS, "plan_id=? and workout_id=?", whereArgs);
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isWorkoutAlreadyAddedForDay(com.workout.workout.modal.Workout r7, com.workout.workout.modal.Plan r8) {

        boolean arrayList = false;
        StringBuilder data = new StringBuilder();
        data.append("SELECT * FROM MyWorkoutPlans WHERE plan_id=");
        data.append(r8.getPlan_id());
        data.append(" and plan_name='");
        data.append(r8.getPlan_name());
        data.append("' and plan_type='");
        data.append(r8.getPlan_type());
        data.append("' and plan_description='");
        data.append(r8.getPlan_description());
        data.append("' and ");
        data.append("day='");
        data.append(r8.getDay());
        data.append("' and workout_id='");
        data.append(r7.getWorkout_id());
        data.append("'");
        this.db = openDatabase();
        Cursor c = db.rawQuery(data.toString(),null);
        if(c.moveToFirst()){
            do
            {
               arrayList = true;

            }while (c.moveToNext());

        }
        closeCursor(c);
        closeDatabase();

        return arrayList;
    }

    public String getAllDataAndGenerateJSON() throws JSONException, FileNotFoundException {
        String plan_id = COLUMN_PLAN_ID;
        String plan_name = "plan_name";
        String plan_type = COLUMN_PLAN_TYPE;
        String plan_description = COLUMN_PLAN_DESCRIPTION;
        String day = COLUMN_DAY;
        String body_part_name = COLUMN_BODY_PART_NAME;
        String body_part_image_name = COLUMN_BODY_PART_IMAGE_NAME;
        String body_part_image_url = COLUMN_BODY_PART_IMAGE_URL;
        String workout_id = COLUMN_WORKOUT_ID;
        String workout_name = COLUMN_WORKOUT_NAME;
        String sets_and_reps = COLUMN_SETS_AND_REPS;
        String workout_image_name = COLUMN_WORKOUT_IMAGE_NAME;
        String workout_image_url = COLUMN_WORKOUT_IMAGE_URL;
        String query = "select " + plan_id + "," + plan_name + "," + plan_type + "," + plan_description + "," + day + "," + body_part_name + "," + body_part_image_name + "," + body_part_image_url + "," + workout_id + "," + workout_name + "," + sets_and_reps + "," + workout_image_name + "," + workout_image_url + " from WorkoutPlans";
        this.db = openDatabase();
        Cursor c = this.db.rawQuery(query, null);
        c.moveToFirst();
        JSONObject root = new JSONObject();
        JSONArray workoutPlans = new JSONArray();
        PrintStream printStream = new PrintStream(new FileOutputStream(new File(Environment.getExternalStorageDirectory() + "/WorkoutPlans.txt"), true));
        int i = 0;
        while (!c.isAfterLast()) {
            JSONObject contact = new JSONObject();
            try {
                contact.put(COLUMN_PLAN_ID, c.getString(c.getColumnIndex(plan_id)));
                contact.put("plan_name", c.getString(c.getColumnIndex(plan_name)));
                contact.put(COLUMN_PLAN_TYPE, c.getString(c.getColumnIndex(plan_type)));
                contact.put(COLUMN_PLAN_DESCRIPTION, c.getString(c.getColumnIndex(plan_description)));
                contact.put(COLUMN_DAY, c.getString(c.getColumnIndex(day)));
                contact.put(COLUMN_BODY_PART_NAME, c.getString(c.getColumnIndex(body_part_name)));
                contact.put(COLUMN_BODY_PART_IMAGE_NAME, c.getString(c.getColumnIndex(body_part_image_name)));
                contact.put(COLUMN_BODY_PART_IMAGE_URL, c.getString(c.getColumnIndex(body_part_image_url)));
                contact.put(COLUMN_WORKOUT_ID, c.getString(c.getColumnIndex(workout_id)));
                contact.put(COLUMN_WORKOUT_NAME, c.getString(c.getColumnIndex(workout_name)));
                contact.put(COLUMN_SETS_AND_REPS, c.getString(c.getColumnIndex(sets_and_reps)));
                contact.put(COLUMN_WORKOUT_IMAGE_NAME, c.getString(c.getColumnIndex(workout_image_name)));
                contact.put(COLUMN_WORKOUT_IMAGE_URL, c.getString(c.getColumnIndex(workout_image_url)));
                c.moveToNext();
                workoutPlans.put(i, contact);
                i++;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        root.put("WorkoutPlans", workoutPlans);
        printStream.append(root.toString());
        return root.toString();
    }

    public void setWorkoutPlanList(List<PlanCategory> workoutPlanList) {
        removeAllWorkoutPlans();
        for (int i = 0; i < workoutPlanList.size(); i++) {
            PlanCategory planCategory = (PlanCategory) workoutPlanList.get(i);
            addPlanCategory(planCategory);
            List<Plan> plansList = planCategory.getPlansList();
            for (int j = 0; j < plansList.size(); j++) {
                Plan plans = (Plan) plansList.get(j);
                addPlanDetail(planCategory, plans);
                List<Plan> workout_list = plans.getPlanList();
                for (int k = 0; k < workout_list.size(); k++) {
                    addWorkoutPlan(planCategory, plans, (Plan) workout_list.get(k));
                }
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean addPlanCategory(com.workout.workout.modal.PlanCategory r9) {
        boolean arrayList = false;

        this.db = openDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("plan_category_id",r9.getPlan_category_id());
        contentValues.put("plan_category_name",r9.getPlan_category_name());
        long check = this.db.replace("plan_category",null,contentValues);
        if (check != -1) arrayList = true;
        closeDatabase();
        return arrayList;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean addPlanDetail(com.workout.workout.modal.PlanCategory r9, com.workout.workout.modal.Plan r10) {
        boolean arrayList = false;

        this.db = openDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("plan_id",r10.getPlan_id());
        contentValues.put("plan_name",r10.getPlan_name());
        contentValues.put("plan_description",r10.getPlan_description());
        contentValues.put("plan_category_id",r9.getPlan_category_id());
        contentValues.put("plan_type",r10.getPlan_type());
        contentValues.put("plan_price",r10.getPlan_price());
        contentValues.put("plan_lock",java.lang.Boolean.valueOf(r10.isLocked()));
        contentValues.put("plan_image_url",r10.getPlan_image_url());
        contentValues.put("plan_image_name",r10.getPlan_image_name());
        long check = this.db.replace("plan_detail",null,contentValues);
        if (check != -1) arrayList = true;
        closeDatabase();
        return arrayList;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean addWorkoutPlan(com.workout.workout.modal.PlanCategory r9, com.workout.workout.modal.Plan r10, com.workout.workout.modal.Plan r11) {
        boolean arrayList = false;

        this.db = openDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("workout_id",r11.getWorkout_id());
        contentValues.put("workout_name",r11.getWorkout_name());
        contentValues.put("sets_and_reps",r11.getSets_and_reps());
        contentValues.put("day",r11.getDay());
        contentValues.put("plan_id",r10.getPlan_id());
        contentValues.put("plan_category_id",r9.getPlan_category_id());
        long check = this.db.replace("workout_plan",null,contentValues);
        if (check != -1) arrayList = true;
        closeDatabase();
        return arrayList;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.ArrayList<com.workout.workout.modal.PlanCategory> getPlanCategoryList() {

        ArrayList<PlanCategory> arrayList = new ArrayList<>();
        StringBuilder data = new StringBuilder();
        data.append("SELECT * from plan_category");
        this.db = openDatabase();
        Cursor c = db.rawQuery(data.toString(),null);
        if(c.moveToFirst()){
            do
            {
                PlanCategory planCategory = new PlanCategory();
                planCategory.setPlan_category_id(c.getString(0));
                planCategory.setPlan_category_name(c.getString(1));
                arrayList.add(planCategory);
            }while (c.moveToNext());

        }
        closeCursor(c);
        closeDatabase();

        return arrayList;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.ArrayList<com.workout.workout.modal.Plan> getPlansList(String r11) {

        ArrayList<Plan> arrayList = new ArrayList<>();
        StringBuilder data = new StringBuilder();
        data.append("SELECT * from plan_detail where plan_category_id = '");
        data.append(r11);
        data.append("'");
        this.db = openDatabase();
        Cursor c = db.rawQuery(data.toString(),null);
        if(c.moveToFirst()){
            do
            {
                Plan plan = new Plan();
                plan.setPlan_id(c.getString(0));
                plan.setPlan_name(c.getString(1));
                plan.setPlan_description(c.getString(2));
                plan.setPlan_type(c.getString(4));
                plan.setPlan_price(Long.valueOf(c.getLong(5)));
                plan.setLocked(true);
                plan.setPlan_image_name(c.getString(7));
                plan.setPlan_image_url(c.getString(8));
                arrayList.add(plan);
            }while (c.moveToNext());

        }
        closeCursor(c);
        closeDatabase();

        return arrayList;
    }

    public static void closeCursor(Cursor cursor) {
        if (cursor != null) {
            try {
                if (!cursor.isClosed()) {
                    cursor.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void beginTransaction(SQLiteDatabase sqliteDatabase) {
        if (sqliteDatabase != null) {
            try {
                sqliteDatabase.beginTransactionNonExclusive();
            } catch (Exception e) {
            }
        }
    }

    public static void setTransactionSuccessful(SQLiteDatabase sqliteDatabase) {
        if (sqliteDatabase != null) {
            try {
                sqliteDatabase.setTransactionSuccessful();
            } catch (Exception e) {
            }
        }
    }

    public static void endTransaction(SQLiteDatabase sqliteDatabase) {
        if (sqliteDatabase != null) {
            try {
                sqliteDatabase.endTransaction();
            } catch (Exception e) {
            }
        }
    }

    public synchronized SQLiteDatabase openDatabase() {
        if (mOpenCounter.incrementAndGet() == 1) {
            this.db = databaseHelper.getWritableDatabase();
            this.db.enableWriteAheadLogging();
        }
        return this.db;
    }

    public synchronized void closeDatabase() {
        if (mOpenCounter.decrementAndGet() == 0 && this.db != null) {
            this.db.close();
        }
    }
}
