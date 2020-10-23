package hb.me.homeworkout.gym.buttlegs.buttlegspro.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class BackBaseActivity extends AppCompatActivity {
    public static int REQUEST_LEVEL_TRAIN = 1;
    public static int REQUEST_LEVEL_TREAD = 2;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void onBackPressed() {
        finish();
    }
}
