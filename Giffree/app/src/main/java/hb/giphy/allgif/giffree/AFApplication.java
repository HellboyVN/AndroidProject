package hb.giphy.allgif.giffree;

import android.app.Application;
import android.support.multidex.MultiDex;

import com.facebook.FacebookSdk;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class AFApplication extends Application {

    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        FacebookSdk.sdkInitialize(this);
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);
    }
}
