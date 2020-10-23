package hb.nativedemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
 String Tag = "Hellboy";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyNDk ndk = new MyNDk();
        Log.e(Tag,"msg= "+ndk.getMyString());
        Log.e(Tag,"Sum ="+ ndk.add(10,11));
        Log.e(toString(),"Fibonacy of 10 = "+ ndk.fibonacy(10));
    }
}
