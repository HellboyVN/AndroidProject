package hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.base;

import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class BackBaseActivity extends AppCompatActivity {
    public static int REQUEST_LEVEL_PREVIEW = 2;

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
