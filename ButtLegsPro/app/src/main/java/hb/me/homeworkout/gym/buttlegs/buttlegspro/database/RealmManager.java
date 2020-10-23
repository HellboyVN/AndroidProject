package hb.me.homeworkout.gym.buttlegs.buttlegspro.database;

import android.content.Context;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.database.modelweight.User;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.database.modelweight.UserWeight;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.ConsKeys;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.MetricHelper;
import io.realm.Realm;
import io.realm.Realm.Transaction;
import io.realm.RealmConfiguration.Builder;
import io.realm.RealmResults;
import io.realm.Sort;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RealmManager {
    private static RealmManager _instance;
    private final long DAY_TO_MILLIS = 86400000;

    public interface IUserReady {
        void onUserReady(User user);
    }

    private RealmManager() {
    }

    public static RealmManager getInstance() {
        if (_instance == null) {
            _instance = new RealmManager();
        }
        return _instance;
    }

    public void init(Context context) {
        Realm.setDefaultConfiguration(new Builder().name(ConsKeys.REALM_NAME).deleteRealmIfMigrationNeeded().schemaVersion(0).build());
    }

    public void initUser(final IUserReady iUserReady) {
        Realm realm = Realm.getDefaultInstance();
        User user = (User) realm.where(User.class).findFirst();
        if (user == null) {
            final User finalUser = new User();
            realm.executeTransaction(new Transaction() {
                public void execute(Realm realm) {
                    User userRealm = (User) realm.createObject(User.class);
                    userRealm.setId(finalUser.getId());
                    userRealm.setWeightUnit(finalUser.getWeightUnit());
                    userRealm.setHeight(finalUser.getHeight());
                    userRealm.setGender(finalUser.getGender());
                    iUserReady.onUserReady(finalUser);
                }
            });
            return;
        }
        iUserReady.onUserReady(user);
    }

    public User getUser() {
        return (User) Realm.getDefaultInstance().where(User.class).findFirst();
    }

    public UserWeight getLastWeightRecord() {
        RealmResults<UserWeight> results = Realm.getDefaultInstance().where(UserWeight.class).findAllSorted("time", Sort.DESCENDING);
        if (results == null || results.size() == 0) {
            return null;
        }
        return (UserWeight) results.get(0);
    }

    public UserWeight getWeightForTime(long time) {
        Realm realm = Realm.getDefaultInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.set(10, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        return (UserWeight) realm.where(UserWeight.class).equalTo("time", Long.valueOf(calendar.getTimeInMillis())).findFirst();
    }

    public User changeUserWeightUnit(int which) {
        Realm realm = Realm.getDefaultInstance();
        User user = (User) realm.where(User.class).findFirst();
        realm.beginTransaction();
        if (user != null) {
            user.setWeightUnit(which);
        }
        realm.commitTransaction();
        return user;
    }

    public void addWeightRecord(float weight, long time) {
        final float finalWeight;
        Realm realm = Realm.getDefaultInstance();
        final User user = getInstance().getUser();
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.set(10, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        UserWeight userWeight = getWeightForTime(calendar.getTimeInMillis());
        if (user.getWeightUnit() == 1) {
            finalWeight = MetricHelper.getInstance().convertLBtoKG(weight);
        } else {
            finalWeight = weight;
        }
        if (userWeight == null) {
            realm.executeTransaction(new Transaction() {
                public void execute(Realm realm) {
                    UserWeight userWeight = (UserWeight) realm.createObject(UserWeight.class);
                    userWeight.setId(RealmManager.this.getNextWeightPK());
                    userWeight.setUserId(user.getId());
                    userWeight.setTime(calendar.getTimeInMillis());
                    userWeight.setWeight(finalWeight);
                }
            });
            return;
        }
        realm.beginTransaction();
        userWeight.setWeight(finalWeight);
        realm.commitTransaction();
    }

    public List<UserWeight> getWeightRecords(User user) {
        List<UserWeight> list = new ArrayList();
        list.addAll(Realm.getDefaultInstance().where(UserWeight.class).equalTo("user_id", Long.valueOf(user.getId())).findAll());
        return list;
    }

    public long getNextWeightPK() {
        try {
            return (long) (Realm.getDefaultInstance().where(UserWeight.class).max("id").intValue() + 1);
        } catch (ArrayIndexOutOfBoundsException e) {
            return 0;
        }
    }
}
