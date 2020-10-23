package hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.utils;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import java.util.Locale;

public class LocaleHelper {
    public static String DEFAULT_LANG_CODE = "en";
    public static String SELECTED_LANGUAGE = "hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.utils_KEY_SETTING_LANGUAGE_CODE_NEW";

    public static void onCreate(Context context) {
        setLocale(context, getPersistedData(context, DEFAULT_LANG_CODE));
    }

    public static void onCreate(Context context, String defaultLanguage) {
        setLocale(context, getPersistedData(context, defaultLanguage));
    }

    public static String getLanguage(Context context, String defaultLanguage) {
        return getPersistedData(context, defaultLanguage);
    }

    public static String getRequestLanguage(Context context) {
        String locale = getPersistedData(context, Locale.getDefault().getLanguage());
        if (locale.equalsIgnoreCase("hy")) {
            return "am";
        }
        return locale;
    }

    public static void setLocale(Context context, String language) {
        persist(context, language);
        updateResources(context, language);
    }

    private static String getPersistedData(Context context, String defaultLanguage) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(SELECTED_LANGUAGE, defaultLanguage);
    }

    private static void persist(Context context, String language) {
        Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(SELECTED_LANGUAGE, language);
        editor.apply();
    }

    private static void updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }
}
