package lp.me.allgifs;

import android.app.Application;
import android.content.Intent;

import com.facebook.FacebookSdk;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class AFApplication extends Application {

    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(this);
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);
    }
}
