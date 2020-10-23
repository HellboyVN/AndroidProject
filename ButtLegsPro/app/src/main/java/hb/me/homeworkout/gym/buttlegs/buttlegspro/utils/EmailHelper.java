package hb.me.homeworkout.gym.buttlegs.buttlegspro.utils;

import android.content.Context;
import de.cketti.mailto.EmailIntentBuilder;

public class EmailHelper {
    private static EmailHelper INSTANCE = null;
    private final Context context;
    private final String packageName;

    public static void initialize(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new EmailHelper(context);
        }
    }

    public static EmailHelper getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        throw new IllegalStateException("service not initialized");
    }

    private EmailHelper(Context context) {
        this.context = context;
        this.packageName = context.getApplicationInfo().packageName;
    }

    public void sendEmail(Context context, String email, String subject, String body) {
        EmailIntentBuilder.from(context).to(email).subject(subject).body(body).start();
    }
}
