package lp.me.lockos10.activity;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import java.util.Calendar;

import lp.me.lockos10.R;
import lp.me.lockos10.util.Utils;
import lp.me.lockos10.util.Values;
import lp.me.lockos10.view.TextViewRobotoLight;
import lp.me.lockos10.view.TextViewRobotoThin;

import static android.view.WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;

public class LockNoPasscode extends FragmentActivity implements OnClickListener {
    public static boolean booleanisCall = false;
    private static boolean isCall = false;
    public static boolean isNotFirst = true;
    public static boolean isfirst = true;
    private static RelativeLayout layout = null;
    private static WindowManager mWindowManager = null;
    Broadcast broadcast;
    CustomPhoneStateListener customPhoneListener;
    private boolean hasPassCode = false;
    int i = 0;
    private ImageView imgBackground;
    private ImageView imgBattery;
    private ImageView imgCharging;
    private ImageView imgSignal;
    private ImageView imgWifi;
    private int mScreenWidth = 0;
    private ViewPager pager;
    PhoneStateListener phoneStateListener = new PhoneStateListener() {
        int MAX_SIGNAL_DBM_VALUE = 31;

        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            if (signalStrength.isGsm()) {
                LockNoPasscode.this.setImgSignal(calculateSignalStrengthInPercent(signalStrength.getGsmSignalStrength()));
            }
        }

        private int calculateSignalStrengthInPercent(int signalStrength) {
            return (int) ((((float) signalStrength) / ((float) this.MAX_SIGNAL_DBM_VALUE)) * 100.0f);
        }
    };
    private View screen;
    private SharedPreferences sharedPreferences;
    private Shimmer shimmer;
    TelephonyManager telephony;
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
                LockNoPasscode.this.tvBattery.setText(intExtra + "%");
                if (plugin != 0) {
                    LockNoPasscode.this.imgCharging.setVisibility(View.VISIBLE);
                    LockNoPasscode.this.charging(true, intExtra);
                    return;
                }
                LockNoPasscode.this.imgCharging.setVisibility(View.GONE);
                LockNoPasscode.this.charging(false, intExtra);
            } else if (action.equals("android.intent.action.TIME_SET") || action.equals("android.intent.action.TIME_TICK") || action.equals("android.intent.action.TIMEZONE_CHANGED")) {
                LockNoPasscode.this.setTime();
            }
        }
    }

    public class BroadcastCall extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            if (LockNoPasscode.isNotFirst && LockNoPasscode.isNotFirst) {
                String phoneNr = intent.getExtras().getString("incoming_number");
                LockNoPasscode.isNotFirst = false;
            }
        }
    }

    public class CustomPhoneStateListener extends PhoneStateListener {
        private static final String TAG = "CustomPhoneStateListener";

        public void onCallStateChanged(int state, String incomingNumber) {
            if (!LockNoPasscode.this.sharedPreferences.getBoolean(Values.ENABLE_PASSCODE, false)) {
                LockNoPasscode lockNoPasscode = LockNoPasscode.this;
                lockNoPasscode.i++;
                if (incomingNumber != null && incomingNumber.length() > 0) {
                    switch (state) {
                        case 0:
                            Log.e("...........NO:  ", "CALL_STATE_IDLE ");
                            if (!LockNoPasscode.isfirst) {
                                LockNoPasscode.isfirst = false;
                                if (!LockNoPasscode.isfirst) {
                                    Log.e("...........NO:  ", "CALL_STATE_IDLE startactivity ");
                                    LockNoPasscode.booleanisCall = false;
                                    Intent intent1 = new Intent(LockNoPasscode.this, LockNoPasscode.class);
                                    intent1.setFlags(268435460);
                                    LockNoPasscode.this.startActivity(intent1);
                                    LockNoPasscode.isfirst = true;
                                }
                                if (LockNoPasscode.this.telephony != null && LockNoPasscode.this.customPhoneListener != null) {
                                    LockNoPasscode.this.telephony.listen(LockNoPasscode.this.customPhoneListener, 0);
                                    return;
                                }
                                return;
                            }
                            return;
                        case 1:
                            Log.e("...........NO:  ", "CALL_STATE_RINGING");
                            LockNoPasscode.booleanisCall = true;
                            LockNoPasscode.this.unlock();
                            LockNoPasscode.isfirst = false;
                            return;
                        case 2:
                            Log.e("...........NO:  ", "CALL_STATE_OFFHOOK");
                            LockNoPasscode.booleanisCall = true;
                            return;
                        default:
                            return;
                    }
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
            ((ViewPager) collection).addView(LockNoPasscode.this.vLock, 0);
            ((ViewPager) collection).addView(LockNoPasscode.this.vPassCode, 1);
            switch (position) {
                case 0:
                    return LockNoPasscode.this.vPassCode;
                case 1:
                    return LockNoPasscode.this.vLock;
                default:
                    return LockNoPasscode.this.vLock;
            }
        }

        public int getItemPosition(Object obj) {
            if (obj == LockNoPasscode.this.vPassCode) {
                return 0;
            }
            return obj == LockNoPasscode.this.vLock ? 1 : -1;
        }

        public void destroyItem(ViewGroup collection, int position, Object view) {
            ((ViewPager) collection).removeView((View) view);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.hasPassCode = this.sharedPreferences.getBoolean(Values.ENABLE_PASSCODE, false);
        super.onCreate(savedInstanceState);
//        if (mWindowManager != null) {
//            Log.e("..............", "oncreate return");
//            return;
//        }

        layout = (RelativeLayout) View.inflate(this, R.layout.activity_lock2, null);
        this.pager = (ViewPager) layout.findViewById(R.id.pager);
        this.pager.setAdapter(new MyPagerAdapter(this));
        this.pager.post(new Runnable() {
            public void run() {
                LockNoPasscode.this.pager.setCurrentItem(1, false);
            }
        });
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        this.vPassCode = layoutInflater.inflate(R.layout.view_null, null);
        this.vLock = layoutInflater.inflate(R.layout.fragment_lock, null);
        bindView();
        setTvShimmer();
        mWindowAddView();
        this.pager.setOnPageChangeListener(new OnPageChangeListener() {
            boolean start = false;

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.e(".............", positionOffset + " " + position);
            }

            public void onPageSelected(int position) {
                Log.e(".............", position + "");
                if (position == 0) {
                    LockNoPasscode.this.unlock();
                    if (LockNoPasscode.this.telephony != null && LockNoPasscode.this.customPhoneListener != null) {
                        LockNoPasscode.this.telephony.listen(LockNoPasscode.this.customPhoneListener, 0);
                    }
                }
            }

            public void onPageScrollStateChanged(int state) {
            }
        });
        this.telephony = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        this.customPhoneListener = new CustomPhoneStateListener();
        this.telephony.listen(this.customPhoneListener, 32);
        setTime();
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
        this.imgSignal = (ImageView) layout.findViewById(R.id.imgSignal);
        this.imgWifi = (ImageView) layout.findViewById(R.id.imgWifi);
        this.imgBattery = (ImageView) layout.findViewById(R.id.imgBattery);
        this.imgCharging = (ImageView) layout.findViewById(R.id.imgCharging);
        this.tvOperatorName = (TextViewRobotoLight) layout.findViewById(R.id.tvOperatorName);
        this.tvBattery = (TextViewRobotoLight) layout.findViewById(R.id.tvBattery);
        setStatusBar();
        this.imgBackground = (ImageView) layout.findViewById(R.id.imgBackground);
        boolean isResource = this.sharedPreferences.getBoolean(Values.BACKGROUND_RESOUECE_BOLEAN, true);
        int idBackground = this.sharedPreferences.getInt(Values.BACKGROUND_RESOURCE_ID, 0);
        if (!isResource) {
            Uri imageUri = Uri.parse(this.sharedPreferences.getString(Values.BACKGROUND_URI, ""));
            this.imgBackground.setImageBitmap(((BitmapDrawable) Drawable.createFromPath(this.sharedPreferences.getString(Values.BACKGROUND_URI, ""))).getBitmap());
        } else if (idBackground == 0) {
            this.imgBackground.setImageResource(R.drawable.ic_wallpaper);
        } else {
            this.imgBackground.setImageResource(idBackground);
        }
        this.tvFormat = (TextViewRobotoThin) this.vLock.findViewById(R.id.tvFormat);
    }

    public void setStatusBar() {
        this.telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        this.telephonyManager.listen(this.phoneStateListener, 1);
        this.tvOperatorName.setText(this.telephonyManager.getNetworkOperatorName());
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
        super.onDestroy();
        try {
            unregisterReceiver(this.broadcast);
        } catch (Exception e) {
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void unlock() {

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
        /*
        r4 = this;
        r3 = 0;
        r0 = layout;	 Catch:{ Exception -> 0x0031, all -> 0x0038 }
        if (r0 == 0) goto L_0x000c;
    L_0x0005:
        r0 = mWindowManager;	 Catch:{ Exception -> 0x0031, all -> 0x0038 }
        r1 = layout;	 Catch:{ Exception -> 0x0031, all -> 0x0038 }
        r0.removeView(r1);	 Catch:{ Exception -> 0x0031, all -> 0x0038 }
    L_0x000c:
        r0 = r4.sharedPreferences;	 Catch:{ Exception -> 0x0031, all -> 0x0038 }
        r1 = "enable_vibrate";
        r2 = 1;
        r0 = r0.getBoolean(r1, r2);	 Catch:{ Exception -> 0x0031, all -> 0x0038 }
        if (r0 == 0) goto L_0x001a;
    L_0x0017:
        lp.me.lockos10.util.Utils.vibrate(r4);	 Catch:{ Exception -> 0x0031, all -> 0x0038 }
    L_0x001a:
        r0 = r4.sharedPreferences;	 Catch:{ Exception -> 0x0031, all -> 0x0038 }
        r1 = "enable_sound";
        r2 = 1;
        r0 = r0.getBoolean(r1, r2);	 Catch:{ Exception -> 0x0031, all -> 0x0038 }
        if (r0 == 0) goto L_0x002b;
    L_0x0025:
        r0 = 2131099649; // 0x7f060001 float:1.7811657E38 double:1.0529031244E-314;
        lp.me.lockos10.util.Utils.sound(r4, r0);	 Catch:{ Exception -> 0x0031, all -> 0x0038 }
    L_0x002b:
        mWindowManager = r3;
        r4.finish();
    L_0x0030:
        return;
    L_0x0031:
        r0 = move-exception;
        mWindowManager = r3;
        r4.finish();
        goto L_0x0030;
    L_0x0038:
        r0 = move-exception;
        mWindowManager = r3;
        r4.finish();
        throw r0;
        */
      //  throw new UnsupportedOperationException("Method not decompiled: lp.me.lockos10.activity.LockNoPasscode.unlock():void");
    }

    private void setTvShimmer() {
        this.shimmer = new Shimmer();
        this.shimmer.setRepeatCount(Callback.DEFAULT_DRAG_ANIMATION_DURATION).setDuration(2000).setStartDelay(1000).setDirection(0).setAnimatorListener(new AnimatorListener() {
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
        this.wmParams.format = 1;
        this.wmParams.type = TYPE_SYSTEM_ERROR;
        this.wmParams.flags = 1280;
        this.wmParams.width = -1;
        this.wmParams.height = -1;
        layout.setSystemUiVisibility(3846);
        mWindowManager.addView(layout, this.wmParams);
    }

    public void onClick(View v) {
        v.getId();
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onStop() {
        super.onStop();
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

    public void setImgSignal(int level) {
        this.imgSignal.setImageResource(new int[]{R.drawable.phone_signal_1, R.drawable.phone_signal_2, R.drawable.phone_signal_3, R.drawable.signal_4, R.drawable.phone_signal_4}[level - 1]);
    }
}
