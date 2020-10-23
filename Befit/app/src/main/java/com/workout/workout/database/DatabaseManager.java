package com.workout.workout.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.workout.workout.constant.AppConstants;
import com.workout.workout.managers.CachingManager;
import com.workout.workout.modal.News;
import com.workout.workout.modal.Workout;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import me.leolin.shortcutbadger.ShortcutBadger;

public class DatabaseManager extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "favourite_workout.db";
    private static final int DATABASE_VERSION = 1;
    private static DatabaseManager databaseManager;
    private static AtomicInteger mOpenCounter = new AtomicInteger();
    private SQLiteDatabase db;

    private DatabaseManager() {
        super(null, null, null, 1);
    }

    public static synchronized DatabaseManager getInstance(Context context) {

        synchronized (DatabaseManager.class) {
            if (databaseManager == null) {
                databaseManager = new DatabaseManager(context);
            }
            databaseManager = databaseManager;
        }
        return databaseManager;
    }

    private DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
    }

    public void addFirstFewNotifications() {
        addNotificationNews(String.valueOf(System.currentTimeMillis()), "10 Weight Loss Tips to Make Things Easier", "1. Eat a high-protein breakfast. Eating a high-protein breakfast has been shown to reduce cravings and calorie intake throughout the day. \n\n2. Avoid sugary drinks and fruit juice. These are the most fattening things you can put into your body, and avoiding them can help you lose weight. \n\n3. Drink water a half hour before meals. One study showed that drinking water a half hour before meals increased weight loss by 44% over 3 months. \n\n4. Choose weight loss-friendly foods. Certain foods are very useful for losing fat. \n\n5. Eat soluble fiber. Studies show that soluble fibers may reduce fat, especially in the belly area. Fiber supplements like glucomannan can also help. \n\n6. Drink coffee or tea. If you're a coffee or tea drinker, then drink as much as you want as the caffeine in them can boost your metabolism by 3-11%. \n\n7. Eat mostly whole, unprocessed foods. Base most of your diet on whole foods. They are healthier, more filling and much less likely to cause overeating. \n\n8. Eat your food slowly. Fast eaters gain more weight over time. Eating slowly makes you feel more full and boosts weight-reducing hormones. \n\n9. Use smaller plates. Studies show that people automatically eat less when they use smaller plates. Strange, but it works. \n\n10. Get a good night's sleep, every night. Poor sleep is one of the strongest risk factors for weight gain, so taking care of your sleep is important.", "https://img.grouponcdn.com/iam/393M4LDiHLHHLBjNFCQW/CL-2048x1242.jpg/v1/c700x420.jpg", false, false);
        addNotificationNews(String.valueOf(System.currentTimeMillis()), "Top 10 Foods Highest in Calories", "Healthy high-calorie foods include nuts, seeds, dark chocolate, dried fruit, avocados, whole grains, milk, dairy, eggs, fish, and meats. The amount of calories a person needs depends on their age, gender, activity level, and muscle mass.\n\n1. Fats & Oils (Beef Tallow, Lard, Fish Oil, Vegetable Oil).\n100 grams= 902 calories \n\n2. Nuts & Seeds (Macadamia Nuts).\n100 grams= 718 calories \n\n3. Nut & Seed Butters (Peanut Butter).\n100 grams= 590 calories \n\n4. Chocolate (Dark 70-85% Cacao).\n100 grams= 598 calories \n\n5. Dried Fruit & Fruit Juices (Prunes).\n100 grams= 339 calories  \n\n 6. Avocados.\n100 grams= 160 calories  \n\n7. Whole Grains (Wholewheat Pasta, Cooked).\n100 grams= 124 calories  \n\n8. Milk, Dairy & Eggs (Goat's Cheese, Hard).\n100 grams= 452 calories  \n\n9. Oily Fish (Mackerel, Cooked).\n100 grams= 262 calories  \n\n10. Meat (Beef Brisket, Cooked).\n100 grams= 358 calories", "https://www.healthaliciousness.com/articles/images/intro/high-calorie-foods.jpg", false, false);
        addNotificationNews(String.valueOf(System.currentTimeMillis()), "8 Health Benefits of Foods High in Protein", "1. Boost Muscle Mass\n\n2. Help Manage Your Weight by Filling You Up\n\n3. Stabilize Blood Sugar Levels\n\n4. Improve Your Mood\n\n5. Promote Healthy Brain Function and Learning\n\n6. Help Maintain Strong Bones\n\n7. Protect Heart Health \n\n8. Slow Aging and Promote Longevity\n\nCheckout PROTEIN CALCULATOR in the Utilities to find out your protein requirements.", "http://www.nutrientsreview.com/wp-content/uploads/2015/07/High-protein-foods.jpg", false, false);
        addNotificationNews(String.valueOf(System.currentTimeMillis()), "Why your BMI is so Important", "BMI, or body mass index, is one of the most reasonable indicators of body fat, for both adults and children. Read on to find out how you can easily calculate your BMI!\n\n BMI is easy to calculate, non-invasive, simple, and inexpensive. \n\nIt is essentially a simple mathematical formula that is used to measure the 'fatness', based on your height and weight. \n\nIt is one of the most common screening tools to indicate obesity, simply because it shows the weight-height relationship and not just the body weight alone. \n\nBut, it is also important to note that your BMI does not take into account your age, sex, ethnicity or muscle mass.\n\n\nGO TO UTILITIES TO CHECK YOUR BMI", "https://i.ndtvimg.com/i/2017-08/weight-loss-body-fat_696x400_41503998974.jpg", false, false);
    }

    private void createNewsNotificationTable() {
        try {
            this.db = openDatabase();
            String CREATE_NOTIFICATION_NEWS_TABLE = "CREATE TABLE IF NOT EXISTS " + AppConstants.TABLE_NAME_NOTIFICATION_NEWS + " " + AppConstants.OPEN_BRACKET + AppConstants.QUOTES + AppConstants.NOTIFICATION_DATE + AppConstants.QUOTES + " " + AppConstants.INTEGER + " " + AppConstants.PRIMARY_KEY + AppConstants.COMMA + " " + AppConstants.QUOTES + "title" + AppConstants.QUOTES + " " + AppConstants.TEXT + " " + AppConstants.COMMA + " " + AppConstants.QUOTES + AppConstants.NOTIFICATION_BODY + AppConstants.QUOTES + " " + AppConstants.TEXT + " " + AppConstants.COMMA + " " + AppConstants.QUOTES + AppConstants.NOTIFICATION_IMAGE + AppConstants.QUOTES + " " + AppConstants.TEXT + " " + AppConstants.COMMA + " " + AppConstants.QUOTES + AppConstants.NOTIFICATION_READ + AppConstants.QUOTES + " " + AppConstants.TEXT + " " + AppConstants.COMMA + " " + AppConstants.QUOTES + AppConstants.NOTIFICATION_SEEN + AppConstants.QUOTES + " " + AppConstants.TEXT + " " + AppConstants.CLOSE_BRACKET;
            Log.e(">>>", "onCreate " + CREATE_NOTIFICATION_NEWS_TABLE);
            this.db.execSQL(CREATE_NOTIFICATION_NEWS_TABLE);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
    }

    private void createNotificationSourceTable() {
        try {
            this.db = openDatabase();
            String CREATE_NOTIFICATION_SOURCE_TABLE = "CREATE TABLE IF NOT EXISTS " + AppConstants.TABLE_NAME_NOTIFICATION_SOURCE + " " + AppConstants.OPEN_BRACKET + AppConstants.QUOTES + AppConstants.NOTIFICATION_DATE + AppConstants.QUOTES + " " + AppConstants.INTEGER + " " + AppConstants.PRIMARY_KEY + AppConstants.COMMA + " " + AppConstants.QUOTES + "source" + AppConstants.QUOTES + " " + AppConstants.TEXT + " " + AppConstants.COMMA + " " + AppConstants.QUOTES + AppConstants.NOTIFICATION_EXTRA + AppConstants.QUOTES + " " + AppConstants.TEXT + " " + AppConstants.CLOSE_BRACKET;
            Log.e(">>>", "onCreate " + CREATE_NOTIFICATION_SOURCE_TABLE);
            this.db.execSQL(CREATE_NOTIFICATION_SOURCE_TABLE);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
    }

    public boolean addNotificationNews(String date, String title, String body, String image, boolean read, boolean seen) {
        try {
            this.db = openDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(AppConstants.NOTIFICATION_DATE, date);
            contentValues.put("title", title);
            contentValues.put(AppConstants.NOTIFICATION_BODY, body);
            contentValues.put(AppConstants.NOTIFICATION_IMAGE, image);
            contentValues.put(AppConstants.NOTIFICATION_READ, Boolean.valueOf(read));
            contentValues.put(AppConstants.NOTIFICATION_SEEN, Boolean.valueOf(seen));
            this.db.insert(AppConstants.TABLE_NAME_NOTIFICATION_NEWS, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
        return true;
    }

    public boolean addSourceToNotification(String date, String source, String extra) {
        try {
            this.db = openDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(AppConstants.NOTIFICATION_DATE, date);
            contentValues.put("source", source);
            contentValues.put(AppConstants.NOTIFICATION_EXTRA, extra);
            this.db.insert(AppConstants.TABLE_NAME_NOTIFICATION_SOURCE, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
        return true;
    }

    public String getNotificationSource(String date) {
        Cursor cursor = null;
        String source = "";
        try {
            String countQuery = "SELECT source FROM " + AppConstants.TABLE_NAME_NOTIFICATION_SOURCE + " WHERE date=" + date;
            this.db = openDatabase();
            cursor = this.db.rawQuery(countQuery, null);
            if (cursor == null || !cursor.moveToFirst()) {
                String str = "";
                closeCursor(cursor);
                closeDatabase();
                return str;
            }
            source = cursor.getString(0);
            return source;
        } catch (SQLiteException e) {
            e.printStackTrace();
            return source;
        } finally {
            closeCursor(cursor);
            closeDatabase();
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.ArrayList<com.workout.workout.modal.News> getNotificationNewsList() {

        ArrayList<News> arrayList = new ArrayList<>();
        StringBuilder data = new StringBuilder();
        data.append("SELECT * FROM MyNotifications");
        this.db = openDatabase();
        if(!this.db.isOpen()){
            this.db = openDatabase();
        }
        Cursor c = db.rawQuery(data.toString(),null);

        if(c.moveToFirst()){
            do
            {
                News news = new News();
                news.setDate(c.getString(0));
                news.setTitle(c.getString(1));
                news.setBody(c.getString(2));
                news.setImage(c.getString(3));
                news.read(c.getString(4).equalsIgnoreCase("1"));
                news.seen(c.getString(5).equalsIgnoreCase("1"));
                arrayList.add(news);

            }while (c.moveToNext());

        }


        closeDatabase();
        closeCursor(c);
        java.util.Collections.reverse(arrayList);
        return arrayList;
    }

    public int newsTableSize() {
        Cursor cursor = null;
        int size = 0;
        try {
            this.db = openDatabase();
            cursor = this.db.rawQuery("SELECT * FROM " + AppConstants.TABLE_NAME_NOTIFICATION_NEWS, null);
            size = cursor.getCount();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
            closeCursor(cursor);
        }
        return size;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int numberOfUnreadReadNews() {
        int arrayList = 0;
        this.db = openDatabase();
        StringBuilder data = new StringBuilder();
        data.append("SELECT * FROM "+com.workout.workout.constant.AppConstants.TABLE_NAME_NOTIFICATION_NEWS);
        Cursor c = db.rawQuery(data.toString(),null);
        if(c.moveToFirst()){
            do
            {
                if(c.getString(4).equalsIgnoreCase("0")){
                    arrayList++;
                }
            }while (c.moveToNext());

        }
        closeCursor(c);
        closeDatabase();
        return arrayList;
    }

    public void changeNewsReadStatus(String date) {
        String query = "UPDATE " + AppConstants.TABLE_NAME_NOTIFICATION_NEWS + " SET " + AppConstants.NOTIFICATION_READ + " = '" + "1" + "' WHERE " + AppConstants.NOTIFICATION_DATE + AppConstants.EQUALS + date;
        try {
            this.db = openDatabase();
            this.db.execSQL(query);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
            Context context = CachingManager.getAppContext();
            ShortcutBadger.applyCount(context, getInstance(context).numberOfUnreadReadNews());
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    private void createFavouriteWorkoutTable() {
        try {
            this.db = openDatabase();
            String CREATE_FAVOURITE_TABLE = "CREATE TABLE IF NOT EXISTS " + AppConstants.TABLE_NAME_FAVOURITE_WORKOUT + " " + AppConstants.OPEN_BRACKET + AppConstants.QUOTES + AppConstants.COLUMN_WORKOUT_ID + AppConstants.QUOTES + " " + AppConstants.INTEGER + " " + AppConstants.PRIMARY_KEY + AppConstants.COMMA + " " + AppConstants.QUOTES + AppConstants.COLUMN_BODY_PART_NAME + AppConstants.QUOTES + " " + AppConstants.TEXT + " " + AppConstants.COMMA + " " + AppConstants.QUOTES + AppConstants.COLUMN_WORKOUT_NAME + AppConstants.QUOTES + " " + AppConstants.TEXT + " " + AppConstants.COMMA + " " + AppConstants.QUOTES + AppConstants.COLUMN_WORKOUT_DESCRIPTION + AppConstants.QUOTES + " " + AppConstants.TEXT + " " + AppConstants.COMMA + " " + AppConstants.QUOTES + AppConstants.COLUMN_WORKOUT_IMAGE_URL + AppConstants.QUOTES + " " + AppConstants.TEXT + " " + AppConstants.COMMA + " " + AppConstants.QUOTES + AppConstants.COLUMN_WORKOUT_IMAGE_NAME + AppConstants.QUOTES + " " + AppConstants.TEXT + " " + AppConstants.COMMA + " " + AppConstants.QUOTES + AppConstants.COLUMN_WORKOUT_VIDEO_NAME + AppConstants.QUOTES + " " + AppConstants.TEXT + " " + AppConstants.COMMA + " " + AppConstants.QUOTES + AppConstants.COLUMN_WORKOUT_VIDEO_URL + AppConstants.QUOTES + " " + AppConstants.TEXT + " " + AppConstants.CLOSE_BRACKET;
            Log.e(">>>", "onCreate" + CREATE_FAVOURITE_TABLE);
            this.db.execSQL(CREATE_FAVOURITE_TABLE);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
    }

    public boolean addWorkout(Workout workout) {
        try {
            this.db = openDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(AppConstants.COLUMN_WORKOUT_ID, workout.getWorkout_id());
            contentValues.put(AppConstants.COLUMN_BODY_PART_NAME, workout.getBody_part_name());
            contentValues.put(AppConstants.COLUMN_WORKOUT_NAME, workout.getWorkout_name());
            contentValues.put(AppConstants.COLUMN_WORKOUT_DESCRIPTION, workout.getWorkout_description());
            contentValues.put(AppConstants.COLUMN_WORKOUT_IMAGE_URL, workout.getWorkout_image_url());
            contentValues.put(AppConstants.COLUMN_WORKOUT_IMAGE_NAME, workout.getWorkout_image_name());
            contentValues.put(AppConstants.COLUMN_WORKOUT_VIDEO_NAME, workout.getWorkout_video_name());
            contentValues.put(AppConstants.COLUMN_WORKOUT_VIDEO_URL, workout.getWorkout_video_url());
            this.db.insert(AppConstants.TABLE_NAME_FAVOURITE_WORKOUT, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
        return true;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.ArrayList<com.workout.workout.modal.Workout> getFavouriteWorkoutList() {

        ArrayList<Workout> arrayList = new ArrayList<>();
        StringBuilder data = new StringBuilder();
        data.append("SELECT * FROM FAVOURITE_WORKOUT");
        this.db = openDatabase();
        Cursor c = db.rawQuery(data.toString(),null);
        if(c.moveToFirst()){
            do
            {
                Workout workout = new Workout();
                workout.setWorkout_id(c.getString(0));
                workout.setWorkout_name(c.getString(2));
                workout.setBody_part_name(c.getString(1));
                workout.setWorkout_description(c.getString(3));
                workout.setWorkout_image_url(c.getString(4));
                workout.setWorkout_image_name(c.getString(5));
                workout.setWorkout_video_name(c.getString(6));
                workout.setWorkout_video_url(c.getString(7));
                arrayList.add(workout);
            }while (c.moveToNext());

        }
        closeCursor(c);
        closeDatabase();

        return arrayList;
    }

    public void removeWorkoutFromFavourite(String workoutId) {
        try {
            this.db = openDatabase();
            this.db.execSQL("delete from " + AppConstants.TABLE_NAME_FAVOURITE_WORKOUT + " where " + AppConstants.COLUMN_WORKOUT_ID + AppConstants.EQUALS + workoutId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabase();
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isFavouriteWorkout(String r8) {
        boolean arrayList = false;
        StringBuilder data = new StringBuilder();
        data.append("SELECT * FROM FAVOURITE_WORKOUT ");
        this.db = openDatabase();
        Cursor c = db.rawQuery(data.toString(),null);
        if(c.moveToFirst()){
            do
            {
                if(c.getInt(0) != java.lang.Integer.parseInt(r8)){
                    arrayList = false;
                }
                else arrayList = true;

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
            }
        }
    }

    public void createNotificationsTableIfNotExists() {
        createNewsNotificationTable();
        createNotificationSourceTable();
        createFavouriteWorkoutTable();
    }

    public synchronized SQLiteDatabase openDatabase() {
        if (mOpenCounter.incrementAndGet() == 1) {
            this.db = databaseManager.getWritableDatabase();
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
