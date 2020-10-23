package com.workout.workout.managers;

import android.util.Log;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.workout.workout.modal.User;

public class UserManager {
    private static final String TAG = UserManager.class.getName();

    public static void createNewUserInFirebaseDB(User user) {
        FirebaseDatabase.getInstance().getReference("users").child(user.getUser_uid()).setValue(user);
    }

    public static void syncUserProfile() {
        if (PersistenceManager.getUserUid() != null && !PersistenceManager.getUserUid().isEmpty()) {
            FirebaseDatabase.getInstance().getReference("users/" + PersistenceManager.getUserUid()).addValueEventListener(new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = (User) dataSnapshot.getValue(User.class);
                    if (user != null) {
                        PersistenceManager.setUserData(user);
                    }
                }

                public void onCancelled(DatabaseError error) {
                    Log.w(UserManager.TAG, "Failed to read value.", error.toException());
                }
            });
        }
    }
}
