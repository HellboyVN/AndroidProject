package hb.homeworkout.homeworkouts.noequipment.fitnesspro.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import hb.homeworkout.homeworkouts.noequipment.fitnesspro.constant.AppConstants;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.managers.CachingManager;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.modal.News;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.modal.Workout;
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
        addNotificationNews(String.valueOf(System.currentTimeMillis()), "10 Weight Loss Tips ", "1. Drink more water. Because water is involved in many metabolic processes in your body, being dehydrated has the potential to slow your metabolism down, which can hamper weight loss. \n\n2. Be prepared. When eating out at restaurants, it’s a good idea to have a look at menus before to ensure you make healthy choices. \n\n3.Make green tea your tipple. The bitter-tasting refreshment has been of growing interest for dieters because of its ability to stimulate fat oxidation. \n\n4. Spice up your mealtimes. Making your meals flavoursome and enjoyable is an important part of maintaining weight loss. \n\n5. Savour. Every. Mouthful. Focus your mind on your food and enjoy every fork. This is the art of mindful eating. \n\n6. Exercise more. Adding more activity to your daily routine – walking to work or using the stairs – is a sure fire way to aid weight loss. \n\n7. Use smaller plates and bowls. Trading in your huge dinner plate for a slightly smaller one is a very simple but effective weight loss tip. \n\n8. Keep a food diary. \n\n9. Watch your intake of refined carbohydrates. \n\n10.  Sleep more.", "https://i.imgur.com/cHFj1Zh.jpg", false, false);
        addNotificationNews(String.valueOf(System.currentTimeMillis()), "8 Foods High in Protein", "1. Grass-Fed Beef: 3 ounces: 22 grams\n\n2. Organic Chicken: 3 ounces: 21 grams\n\n3. Bone Broth: 1 serving (¼ cup): 20 grams\n\n4. Lentils: 1 cup: 18 grams\n\n5. Wild-Caught Salmon (and other wild fish): 3 ounces: 17 grams\n\n6. Black Beans (and other beans): 1 cup: 15 grams\n\n7. Natto: 1/2 cup: 15 grams \n\n8. Eggs: 1 large free-range egg: 7 grams.", "http://www.nutrientsreview.com/wp-content/uploads/2015/07/High-protein-foods.jpg", false, false);
        addNotificationNews(String.valueOf(System.currentTimeMillis()), "Top 10 Foods Highest in Calories", "Healthy high-calorie foods include nuts, seeds, dark chocolate, dried fruit, avocados, whole grains, milk, dairy, eggs, fish, and meats. The amount of calories a person needs depends on their age, gender, activity level, and muscle mass.\n\n1. Fats & Oils (Beef Tallow, Lard, Fish Oil, Vegetable Oil).\n100 grams= 902 calories \n\n2. Nuts & Seeds (Macadamia Nuts).\n100 grams= 718 calories \n\n3. Nut & Seed Butters (Peanut Butter).\n100 grams= 590 calories \n\n4. Chocolate (Dark 70-85% Cacao).\n100 grams= 598 calories \n\n5. Dried Fruit & Fruit Juices (Prunes).\n100 grams= 339 calories  \n\n 6. Avocados.\n100 grams= 160 calories  \n\n7. Whole Grains (Wholewheat Pasta, Cooked).\n100 grams= 124 calories  \n\n8. Milk, Dairy & Eggs (Goat's Cheese, Hard).\n100 grams= 452 calories  \n\n9. Oily Fish (Mackerel, Cooked).\n100 grams= 262 calories  \n\n10. Meat (Beef Brisket, Cooked).\n100 grams= 358 calories", "http://reachingutopia.com/wp-content/uploads/2013/08/fast-food.jpg", false, false);
        addNotificationNews(String.valueOf(System.currentTimeMillis()), "Why your BMI is so Important", "<p>BMI = (Weight in Pounds x 703) / (Height in inches x Height in inches).</p>\n" +
                "<p>So, now that you know your BMI, is it worth knowing? What are you going to do with it?</p>\n" +
                "<h3>What your BMI means</h3>\n" +
                "<p>To understand what your BMI means, it’s useful to take a step back and understand what it’s measuring and why it’s measured.</p>\n" +
                "<p>BMI is a calculation of your size that takes into account your height and weight. A number of years ago, I remember using charts that asked you to find your height along the left side and then slide your finger to the right to see your “ideal weight” from choices listed under small, medium, or large “frame” sizes.</p>\n" +
                "<p>These charts came from “actuarial” statistics, calculations that life insurance companies use to determine your likelihood of reaching an advanced age based on data from thousands of people. These charts were cumbersome to use, and it was never clear how one was to decide a person&#8217;s &#8220;frame size.&#8221;</p>\n" +
                "<p>BMI does something similar — it expresses the relationship between your height and weight as a single number that is not dependent on “frame size.” Although the origin of the BMI is over 200 years old, it is fairly new as a measure of health.</p>\n" +
                "<h3>What’s a normal BMI?</h3>\n" +
                "<p>A normal BMI is between18.5 and 25; a person with a BMI between 25 and 30 is considered overweight; and a person with a BMI over 30 is considered obese. A person is considered underweight if the BMI is less than 18.5.</p>\n" +
                "<p>As with most measures of health, BMI is not a perfect test. For example, results can be thrown off by pregnancy or high muscle mass, and it may not be a good measure of health for children or the elderly.</p>\n" +
                "<h3>So then, why does BMI matter?</h3>\n" +
                "<p>In general, the higher your BMI, the higher the risk of developing a range of conditions linked with excess weight, including:</p>\n" +
                "<ul>\n" +
                "<li>diabetes</li>\n" +
                "<li>arthritis</li>\n" +
                "<li>liver disease</li>\n" +
                "<li>several types of cancer (such as those of the breast, colon, and prostate)</li>\n" +
                "<li>high blood pressure (hypertension)</li>\n" +
                "<li>high cholesterol</li>\n" +
                "<li>sleep apnea.</li>\n" +
                "</ul>", "https://img.thuocbietduoc.com.vn/images/bmi-nguoi-gay.jpg", false, false);
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
    public ArrayList<hb.homeworkout.homeworkouts.noequipment.fitnesspro.modal.News> getNotificationNewsList() {

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
        data.append("SELECT * FROM "+hb.homeworkout.homeworkouts.noequipment.fitnesspro.constant.AppConstants.TABLE_NAME_NOTIFICATION_NEWS);
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
    public ArrayList<hb.homeworkout.homeworkouts.noequipment.fitnesspro.modal.Workout> getFavouriteWorkoutList() {

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
                if(c.getInt(0) != Integer.parseInt(r8)){
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
