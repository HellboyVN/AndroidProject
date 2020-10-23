package lp.me.lockos10.activity;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import java.util.Calendar;

import lp.me.lockos10.R;
import lp.me.lockos10.util.Blur;
import lp.me.lockos10.util.MyApplication;
import lp.me.lockos10.util.Utils;
import lp.me.lockos10.util.Values;
import lp.me.lockos10.view.TextViewRobotoLight;
import lp.me.lockos10.view.TextViewRobotoThin;

public class LockHasPasscode extends FragmentActivity implements OnClickListener {
    private static final int EXCELLENT_LEVEL = 75;
    private static final int GOOD_LEVEL = 50;
    static int IDLE = 0;
    private static final int MODERATE_LEVEL = 25;
    private static final int WEAK_LEVEL = 0;
    public static boolean booleanisCall = false;
    static CustomPhoneStateListener customPhoneListener;
    static boolean isCallRegister = false;
    public static boolean isfirst = true;
    private static RelativeLayout layout = null;
    private static WindowManager mWindowManager;
    static TelephonyManager telephony;
    private Bitmap blur;
    Broadcast broadcast;
    private BroadcastCall broadcastCall;
    private ImageView btn0;
    private ImageView btn1;
    private ImageView btn2;
    private ImageView btn3;
    private ImageView btn4;
    private ImageView btn5;
    private ImageView btn6;
    private ImageView btn7;
    private ImageView btn8;
    private ImageView btn9;
    private TextViewRobotoLight btnCancel;
    private StringBuilder code;
    private boolean hasPassCode = true;
    private int idBackground;
    private ImageView imgBackground;
    private ImageView imgBackgroundBlur;
    private ImageView imgBattery;
    private ImageView imgCharging;
    private ImageView imgPass;
    private ImageView imgSignal;
    private ImageView imgWifi;
    private String key;
    int lastPage;
    private int mScreenWidth = 0;
    private ViewPager pager;
    PhoneStateListener phoneStateListener = new PhoneStateListener() {
        int MAX_SIGNAL_DBM_VALUE = 31;

        public void onSignalStrengthChanged(int asu) {
            LockHasPasscode.this.setSignalLevel(asu);
            super.onSignalStrengthChanged(asu);
        }
    };
    private View screen;
    private SharedPreferences sharedPreferences;
    private Shimmer shimmer;
    TelephonyManager telephonyManager;
    private TextViewRobotoLight tvBattery;
    private TextViewRobotoLight tvDate;
    private TextViewRobotoThin tvFormat;
    private TextViewRobotoLight tvOperatorName;
    private ShimmerTextView tvShimmer;
    private TextViewRobotoThin tvTime;
    private View vLock;
    private View vPassCode;
    private LayoutParams wmParams;

    class Broadcast extends BroadcastReceiver {
        Broadcast() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("android.intent.action.BATTERY_CHANGED")) {
                int intExtra = intent.getIntExtra("level", 0);
                int plugin = intent.getIntExtra("plugged", 0);
                LockHasPasscode.this.tvBattery.setText(intExtra + "%");
                if (plugin != 0) {
                    LockHasPasscode.this.imgCharging.setVisibility(View.VISIBLE);
                    LockHasPasscode.this.charging(true, intExtra);
                    return;
                }
                LockHasPasscode.this.imgCharging.setVisibility(View.GONE);
                LockHasPasscode.this.charging(false, intExtra);
            } else if (action.equals("android.intent.action.TIME_SET") || action.equals("android.intent.action.TIME_TICK") || action.equals("android.intent.action.TIMEZONE_CHANGED")) {
                LockHasPasscode.this.setTime();
            }
        }
    }

    class BroadcastCall extends BroadcastReceiver {
        BroadcastCall() {
        }

        public void onReceive(Context context, Intent intent) {
        }
    }

    public class CustomPhoneStateListener extends PhoneStateListener {
        private static final String TAG = "CustomPhoneStateListener";

        public void onCallStateChanged(int state, String incomingNumber) {
            if (LockHasPasscode.this.sharedPreferences.getBoolean(Values.ENABLE_PASSCODE, true)) {
                switch (state) {
                    case 0:
                        Log.e("...........PASS:  ", "CALL_STATE_IDLE");
                        LockHasPasscode.IDLE++;
                        if (LockHasPasscode.IDLE <= 1) {
                            LockHasPasscode.IDLE = 1;
                            Log.e("...........PASS:  ", "IDLE <= 1");
                            return;
                        } else if (!LockHasPasscode.isfirst) {
                            LockHasPasscode.isfirst = false;
                            if (!LockHasPasscode.isfirst) {
                                LockHasPasscode.booleanisCall = false;
                                Intent intent1 = new Intent(LockHasPasscode.this, LockHasPasscode.class);
                                intent1.setFlags(268435460);
                                LockHasPasscode.this.startActivity(intent1);
                                Log.e("...........PASS:  ", "startactivitylock");
                                LockHasPasscode.isfirst = true;
                                return;
                            }
                            return;
                        } else {
                            return;
                        }
                    case 1:
                        Log.e("...........PASS:  ", "CALL_STATE_RINGING");
                        LockHasPasscode.this.unlock();
                        LockHasPasscode.booleanisCall = true;
                        LockHasPasscode.isfirst = false;
                        return;
                    case 2:
                        LockHasPasscode.booleanisCall = true;
                        Log.e("...........PASS:  ", "CALL_STATE_OFFHOOK");
                        return;
                    default:
                        return;
                }
            }
        }
    }

    public class MyPagerAdapter extends PagerAdapter {
        Activity context;

        public MyPagerAdapter(Activity context) {
            this.context = context;
        }

        public int getCount() {
            return 2;
        }

        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        public Object instantiateItem(ViewGroup collection, int position) {
            ((ViewPager) collection).addView(LockHasPasscode.this.vLock, 0);
            ((ViewPager) collection).addView(LockHasPasscode.this.vPassCode, 1);
            switch (position) {
                case 0:
                    return LockHasPasscode.this.vPassCode;
                case 1:
                    return LockHasPasscode.this.vLock;
                default:
                    return LockHasPasscode.this.vLock;
            }
        }

        public int getItemPosition(Object obj) {
            if (obj == LockHasPasscode.this.vPassCode) {
                return 0;
            }
            return obj == LockHasPasscode.this.vLock ? 1 : -1;
        }

        public void destroyItem(ViewGroup collection, int position, Object view) {
            ((ViewPager) collection).removeView((View) view);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mWindowManager == null) {
            Log.e("..............", "oncreate return");
            //return;
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        Log.e("..............", "oncreate");
        layout = (RelativeLayout) View.inflate(this, R.layout.activity_lock2, null);
        this.pager = (ViewPager) layout.findViewById(R.id.pager);
        this.pager.setOffscreenPageLimit(2);
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.hasPassCode = this.sharedPreferences.getBoolean(Values.ENABLE_PASSCODE, true);
        this.key = this.sharedPreferences.getString(Values.PASSCODE, "");
        this.idBackground = this.sharedPreferences.getInt(Values.BACKGROUND_RESOURCE_ID, R.drawable.b1);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        this.vPassCode = layoutInflater.inflate(R.layout.fragment_lock_passcode, null);
        this.vLock = layoutInflater.inflate(R.layout.fragment_lock, null);
        bindView();
        this.btnCancel = (TextViewRobotoLight) this.vPassCode.findViewById(R.id.btnCancel);
        this.btnCancel.setOnClickListener(this);
        setTvShimmer();
        this.code = new StringBuilder();
        mWindowAddView();
        this.pager.setAdapter(new MyPagerAdapter(this));
        this.pager.post(new Runnable() {
            public void run() {
                LockHasPasscode.this.pager.setCurrentItem(1, false);
            }
        });
        this.pager.setOnPageChangeListener(new OnPageChangeListener() {
            private boolean checkDirection;
            private boolean scrollStarted;
            public float thresholdOffset;

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                if (position == 1) {
                    LockHasPasscode.this.imgBackground.setVisibility(View.VISIBLE);
                } else {
                    LockHasPasscode.this.imgBackground.setVisibility(View.GONE);
                }
            }

            public void onPageScrollStateChanged(int state) {
            }
        });
        if (!isCallRegister) {
            telephony = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            customPhoneListener = new CustomPhoneStateListener();
            telephony.listen(customPhoneListener, 32);
            Log.e("............", "register");
            IDLE = 0;
            isCallRegister = true;
        }
        setTime();
        this.telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        this.telephonyManager.listen(this.phoneStateListener, 1);
        this.tvOperatorName.setText(this.telephonyManager.getNetworkOperatorName());
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.BATTERY_CHANGED");
        filter.addAction("android.intent.action.TIME_TICK");
        filter.addAction("android.intent.action.TIMEZONE_CHANGED");
        registerReceiver(this.broadcast, filter);
    }

    private void bindView() {
        this.tvShimmer = (ShimmerTextView) this.vLock.findViewById(R.id.shimmer_tv);
        this.tvDate = (TextViewRobotoLight) this.vLock.findViewById(R.id.tvDate);
        this.tvTime = (TextViewRobotoThin) this.vLock.findViewById(R.id.tvTime);
        this.imgPass = (ImageView) this.vPassCode.findViewById(R.id.imgPass);
        this.btn0 = (ImageView) this.vPassCode.findViewById(R.id.btn0);
        this.btn0.setOnClickListener(this);
        this.btn1 = (ImageView) this.vPassCode.findViewById(R.id.btn1);
        this.btn1.setOnClickListener(this);
        this.btn2 = (ImageView) this.vPassCode.findViewById(R.id.btn2);
        this.btn2.setOnClickListener(this);
        this.btn3 = (ImageView) this.vPassCode.findViewById(R.id.btn3);
        this.btn3.setOnClickListener(this);
        this.btn4 = (ImageView) this.vPassCode.findViewById(R.id.btn4);
        this.btn4.setOnClickListener(this);
        this.btn5 = (ImageView) this.vPassCode.findViewById(R.id.btn5);
        this.btn5.setOnClickListener(this);
        this.btn6 = (ImageView) this.vPassCode.findViewById(R.id.btn6);
        this.btn6.setOnClickListener(this);
        this.btn7 = (ImageView) this.vPassCode.findViewById(R.id.btn7);
        this.btn7.setOnClickListener(this);
        this.btn8 = (ImageView) this.vPassCode.findViewById(R.id.btn8);
        this.btn8.setOnClickListener(this);
        this.btn9 = (ImageView) this.vPassCode.findViewById(R.id.btn9);
        this.btn9.setOnClickListener(this);
        this.tvFormat = (TextViewRobotoThin) this.vLock.findViewById(R.id.tvFormat);
        this.imgSignal = (ImageView) layout.findViewById(R.id.imgSignal);
        this.imgWifi = (ImageView) layout.findViewById(R.id.imgWifi);
        this.imgBattery = (ImageView) layout.findViewById(R.id.imgBattery);
        this.imgCharging = (ImageView) layout.findViewById(R.id.imgCharging);
        this.tvOperatorName = (TextViewRobotoLight) layout.findViewById(R.id.tvOperatorName);
        this.tvBattery = (TextViewRobotoLight) layout.findViewById(R.id.tvBattery);
        setStatusBar();
        this.imgBackground = (ImageView) layout.findViewById(R.id.imgBackground);
        this.imgBackgroundBlur = (ImageView) layout.findViewById(R.id.imgBackgroundBlur);
        if (this.sharedPreferences.getBoolean(Values.BACKGROUND_RESOUECE_BOLEAN, true)) {
            if (this.idBackground == 0) {
                this.imgBackground.setImageResource(R.drawable.b2);
            } else {
                this.imgBackground.setImageResource(this.idBackground);
            }
            if (MyApplication.blur == null) {
                this.blur = Blur.fastblur(this, BitmapFactory.decodeResource(getResources(), this.idBackground), 25);
                MyApplication.blur = this.blur;
            }
        } else {
            try {
                Uri imageUri = Uri.parse(this.sharedPreferences.getString(Values.BACKGROUND_URI, ""));
                Bitmap bitmap = ((BitmapDrawable) Drawable.createFromPath(this.sharedPreferences.getString(Values.BACKGROUND_URI, ""))).getBitmap();
                this.imgBackground.setImageBitmap(bitmap);
                if (MyApplication.blur == null) {
                    this.blur = Blur.fastblur(this, bitmap, 25);
                    MyApplication.blur = this.blur;
                }
            } catch (Exception e) {
                return;
            }
        }
        this.imgBackgroundBlur.setImageBitmap(MyApplication.blur);
    }

    public void setStatusBar() {
        this.broadcast = new Broadcast();
        if (Utils.wifiConected(this)) {
            this.imgWifi.setVisibility(View.VISIBLE);
        } else {
            this.imgWifi.setVisibility(View.GONE);
        }
    }

    public void setTime() {
        boolean isTime12 = this.sharedPreferences.getBoolean(Values.TIME_FORMAT, false);
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int date = c.get(Calendar.DATE);
        int dateWeek = c.get(Calendar.DAY_OF_WEEK);
        int month = c.get(Calendar.MONTH) + 1;
        String minuteString = minute + "";
        if (minuteString.length() == 1) {
            minuteString = "0" + minuteString;
        }
        if (isTime12) {
            int hour12 = hour % 12;
            this.tvFormat.setVisibility(View.VISIBLE);
            if (hour > 12) {
                this.tvFormat.setText("PM");
                this.tvTime.setText(hour12 + ":" + minuteString);
            } else {
                this.tvFormat.setText("AM");
                this.tvTime.setText(hour12 + ":" + minuteString);
            }
        } else {
            this.tvFormat.setVisibility(View.GONE);
            this.tvTime.setText(hour + ":" + minuteString);
        }
        this.tvDate.setText(Utils.getDateInWeek(this, dateWeek) + ", " + Utils.getMonthString(this, month) + " " + date);
    }

    protected void onDestroy() {
        Log.e("..............", "onDestroy HasPass");
        super.onDestroy();
        try {
            unregisterReceiver(this.broadcast);
        } catch (Exception e) {
        }
    }

    private void setImageCode() {
        int size = this.code.length();
        Utils.sound(this, R.raw.type);
        switch (size) {
            case 0:
                this.imgPass.setImageResource(R.drawable.pas7);
                return;
            case 1:
                this.imgPass.setImageResource(R.drawable.pas1);
                return;
            case 2:
                this.imgPass.setImageResource(R.drawable.pas2);
                return;
            case 3:
                this.imgPass.setImageResource(R.drawable.pas3);
                return;
            case 4:
                this.imgPass.setImageResource(R.drawable.pas4);
                if (this.code.toString().equals(this.key)) {
                    Log.e("Code",this.code.toString()+ "\n"+this.key.toString());
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 5s = 5000ms
                            unlock();
                        }
                    }, 500);

                    if (telephony != null && customPhoneListener != null) {
                        telephony.listen(customPhoneListener, 0);
                        Log.e("................", "unregister");
                        isCallRegister = false;
                        return;
                    }
                    return;
                }
                this.imgPass.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
                Utils.vibrate(this);
                this.code = new StringBuilder();
                setImageCode();
                return;
//            case 5:
//                this.imgPass.setImageResource(R.drawable.pas5);
//                return;
//            case 6:
//                this.imgPass.setImageResource(R.drawable.pas6);
//                if (this.code.toString().equals(this.key)) {
//                    Log.e("Code",this.code.toString()+ "\n"+this.key.toString());
//                    unlock();
//                    if (telephony != null && customPhoneListener != null) {
//                        telephony.listen(customPhoneListener, 0);
//                        Log.e("................", "unregister");
//                        isCallRegister = false;
//                        return;
//                    }
//                    return;
//                }
//                this.imgPass.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
//                Utils.vibrate(this);
//                this.code = new StringBuilder();
//                setImageCode();
//                return;
            default:
                return;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void unlock() {
        /*
        r4 = this;
        r3 = 0;
        r0 = layout;	 Catch:{ Exception -> 0x0038, all -> 0x003f }
        if (r0 == 0) goto L_0x0013;

    L_0x0005:
        r0 = mWindowManager;	 Catch:{ Exception -> 0x0038, all -> 0x003f }
        r1 = layout;	 Catch:{ Exception -> 0x0038, all -> 0x003f }
        r0.removeView(r1);	 Catch:{ Exception -> 0x0038, all -> 0x003f }
        r0 = "...........PASS: ";
        r1 = "mWindowManager.removeView(layout)";
        android.util.Log.e(r0, r1);	 Catch:{ Exception -> 0x0038, all -> 0x003f }
    L_0x0013:
        r0 = r4.sharedPreferences;	 Catch:{ Exception -> 0x0038, all -> 0x003f }
        r1 = "enable_vibrate";
        r2 = 1;
        r0 = r0.getBoolean(r1, r2);	 Catch:{ Exception -> 0x0038, all -> 0x003f }
        if (r0 == 0) goto L_0x0021;
    L_0x001e:
        lp.me.lockos10.util.Utils.vibrate(r4);	 Catch:{ Exception -> 0x0038, all -> 0x003f }
    L_0x0021:
        r0 = r4.sharedPreferences;	 Catch:{ Exception -> 0x0038, all -> 0x003f }
        r1 = "enable_sound";
        r2 = 1;
        r0 = r0.getBoolean(r1, r2);	 Catch:{ Exception -> 0x0038, all -> 0x003f }
        if (r0 == 0) goto L_0x0032;
    L_0x002c:
        r0 = 2131099649; // 0x7f060001 float:1.7811657E38 double:1.0529031244E-314;
        lp.me.lockos10.util.Utils.sound(r4, r0);	 Catch:{ Exception -> 0x0038, all -> 0x003f }
    L_0x0032:
        mWindowManager = r3;
        r4.finish();
    L_0x0037:
        return;
    L_0x0038:
        r0 = move-exception;
        mWindowManager = r3;
        r4.finish();
        goto L_0x0037;
    L_0x003f:
        r0 = move-exception;
        mWindowManager = r3;
        r4.finish();
        throw r0;
        */
        try {

            int r3 = 0;
            if (layout == null) {
            } else {
                mWindowManager.removeView(layout);
            }

            boolean isViber = this.sharedPreferences.getBoolean("enable_vibrate", true);
            if (isViber == true) {
                Utils.vibrate(this);
            }

            boolean isSound = this.sharedPreferences.getBoolean("enable_sound", true);
            if (isSound == true) {
                Utils.sound(this, R.raw.unlock);

            }

            this.finish();
        } catch (Exception e) {
            e.printStackTrace();
            mWindowManager = null;
            this.finish();

        }
        Log.d("LAMPARD","Unlock screen");
        //throw new UnsupportedOperationException("Method not decompiled: lp.me.lockos10.activity.LockHasPasscode.unlock():void");
    }

    private void setTvShimmer() {
        this.shimmer = new Shimmer();
        this.shimmer.setRepeatCount(Callback.DEFAULT_DRAG_ANIMATION_DURATION).setDuration(2000).setStartDelay(500).setDirection(0).setAnimatorListener(new AnimatorListener() {
            public void onAnimationStart(Animator animation) {
            }

            public void onAnimationEnd(Animator animation) {
            }

            public void onAnimationCancel(Animator animation) {
            }

            public void onAnimationRepeat(Animator animation) {
            }
        });
        this.tvShimmer.setText(this.sharedPreferences.getString(Values.UNLOCK_TEXT, getString(R.string.slidetounlock)));
        this.tvShimmer.setTypeface(Utils.getTypefaceRobotoRegular(this));
        this.shimmer.start(this.tvShimmer);
    }

    private void mWindowAddView() {
        mWindowManager = (WindowManager) getApplicationContext().getSystemService(WINDOW_SERVICE);
        this.wmParams = new LayoutParams();
        this.wmParams.format = -3;
        this.wmParams.type = LayoutParams.TYPE_SYSTEM_ERROR;
        this.wmParams.flags = 1280;
        this.wmParams.width = -1;
        this.wmParams.height = -1;
        layout.setSystemUiVisibility(3846);
        mWindowManager.addView(layout, this.wmParams);
        Log.e(".............", "addView");
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancel:
                if (this.code.length() > 0) {
                    this.code.deleteCharAt(this.code.length() - 1);
                    setImageCode();
                    return;
                }
                this.pager.post(new Runnable() {
                    public void run() {
                        LockHasPasscode.this.pager.setCurrentItem(1);
                    }
                });
                return;
            case R.id.btn1:
                this.code.append(1);
                setImageCode();
                return;
            case R.id.btn2:
                this.code.append(2);
                setImageCode();
                return;
            case R.id.btn3:
                this.code.append(3);
                setImageCode();
                return;
            case R.id.btn4:
                this.code.append(4);
                setImageCode();
                return;
            case R.id.btn5:
                this.code.append(5);
                setImageCode();
                return;
            case R.id.btn6:
                this.code.append(6);
                setImageCode();
                return;
            case R.id.btn7:
                this.code.append(7);
                setImageCode();
                return;
            case R.id.btn8:
                this.code.append(8);
                setImageCode();
                return;
            case R.id.btn9:
                this.code.append(9);
                setImageCode();
                return;
            case R.id.btn0:
                this.code.append(0);
                setImageCode();
                return;
            default:
                return;
        }
    }

    public void charging(boolean charging, int intExtra) {
        if (charging) {
            if (intExtra <= 10) {
                this.imgBattery.setImageResource(R.drawable.battery_10_green);
            } else if (intExtra > 10 && intExtra <= 20) {
                this.imgBattery.setImageResource(R.drawable.battery_20_green);
            } else if (intExtra > 20 && intExtra <= 35) {
                this.imgBattery.setImageResource(R.drawable.battery_35_green);
            } else if (intExtra > 35 && intExtra <= 50) {
                this.imgBattery.setImageResource(R.drawable.battery_50_green);
            } else if (intExtra > 50 && intExtra <= 75) {
                this.imgBattery.setImageResource(R.drawable.battery_75_green);
            } else if (intExtra > 75 && intExtra <= 90) {
                this.imgBattery.setImageResource(R.drawable.battery_90_green);
            } else if (intExtra > 90) {
                this.imgBattery.setImageResource(R.drawable.battery_full_green);
            }
        } else if (intExtra <= 10) {
            this.imgBattery.setImageResource(R.drawable.battery_10);
        } else if (intExtra > 10 && intExtra <= 20) {
            this.imgBattery.setImageResource(R.drawable.battery_20);
        } else if (intExtra > 20 && intExtra <= 35) {
            this.imgBattery.setImageResource(R.drawable.battery_35);
        } else if (intExtra > 35 && intExtra <= 50) {
            this.imgBattery.setImageResource(R.drawable.battery_50);
        } else if (intExtra > 50 && intExtra <= 75) {
            this.imgBattery.setImageResource(R.drawable.battery_75);
        } else if (intExtra > 75 && intExtra <= 90) {
            this.imgBattery.setImageResource(R.drawable.battery_90);
        } else if (intExtra > 90) {
            this.imgBattery.setImageResource(R.drawable.battery_full);
        }
        try {
            mWindowManager.updateViewLayout(layout, this.wmParams);
        } catch (Exception e) {
        }
        layout.postInvalidate();
    }

    protected void onStop() {
        Log.e("..............", "onStop HasPass");
        super.onStop();
    }

    protected void onResume() {
        super.onResume();
    }

    public void setImgSignal(int level) {
        this.imgSignal.setImageResource(new int[]{R.drawable.phone_signal_1, R.drawable.phone_signal_2, R.drawable.phone_signal_3, R.drawable.signal_4, R.drawable.phone_signal_4}[level - 1]);
    }

    private void setSignalLevel(int level) {
        int progress = (int) ((((double) ((float) level)) / 31.0d) * 100.0d);
        if (progress > 75) {
            this.imgSignal.setImageResource(R.drawable.phone_signal_5);
        } else if (progress > 50) {
            this.imgSignal.setImageResource(R.drawable.phone_signal_4);
        } else if (progress > 25) {
            this.imgSignal.setImageResource(R.drawable.phone_signal_3);
        } else if (progress > 0) {
            this.imgSignal.setImageResource(R.drawable.phone_signal_2);
        } else if (progress <= 0) {
            this.imgSignal.setVisibility(View.GONE);
            this.tvOperatorName.setText(getString(R.string.paddy_no_signal));
        }
    }
}
