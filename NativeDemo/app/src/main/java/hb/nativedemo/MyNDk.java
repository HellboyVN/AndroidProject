package hb.nativedemo;

/**
 * Created by tlvan on 1/31/2018.
 */

public class MyNDk {
    static {
        System.loadLibrary("Hellboy");
    }
    public native String getMyString();
    public native int fibonacy(int a);
    public native int add(int a, int b);
}
