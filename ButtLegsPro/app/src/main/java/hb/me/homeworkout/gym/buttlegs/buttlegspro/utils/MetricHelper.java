package hb.me.homeworkout.gym.buttlegs.buttlegspro.utils;

import java.text.NumberFormat;

public class MetricHelper {
    private static MetricHelper _instance;
    public final float KG_TO_LB = 2.205f;

    private MetricHelper() {
    }

    public static MetricHelper getInstance() {
        if (_instance == null) {
            _instance = new MetricHelper();
        }
        return _instance;
    }

    public float convertKGtoLB(float value) {
        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        return Float.parseFloat(formatter.format((double) (2.205f * value)).replace(",", "."));
    }

    public float convertLBtoKG(float value) {
        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        return Float.parseFloat(formatter.format((double) (value / 2.205f)).replace(",", "."));
    }
}
