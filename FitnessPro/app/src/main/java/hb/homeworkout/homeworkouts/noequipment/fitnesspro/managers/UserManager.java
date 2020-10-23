package hb.homeworkout.homeworkouts.noequipment.fitnesspro.managers;

import hb.homeworkout.homeworkouts.noequipment.fitnesspro.modal.User;

public class UserManager {
    private static final String TAG = UserManager.class.getName();

    public static void createNewUserInFirebaseDB(User user) {
       // FirebaseDatabase.getInstance().getReference("users").child(user.getUser_uid()).setValue(user);
    }


}
