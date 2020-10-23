package hb.homeworkout.homeworkouts.noequipment.fitnesspro.service;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    public void onTokenRefresh() {
        System.out.println("token sush " + FirebaseInstanceId.getInstance().getToken());
    }
}
