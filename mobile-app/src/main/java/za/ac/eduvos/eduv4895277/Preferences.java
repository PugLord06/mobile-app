package za.ac.eduvos.eduv4895277;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
    private static final String PREFS = "eduvos_prefs";
    private static final String KEY_NAME = "user_name";
    private static final String KEY_LOGGED_IN = "logged_in";
    private static final String KEY_DARK = "pref_dark";
    private static final String KEY_MUSIC = "pref_music";
    private static final String KEY_LANG = "pref_lang";

    public static boolean isRegistered(Context context) {
        return getUserName(context) != null && getUserName(context).trim().length() > 0;
    }

    public static void setUserName(Context context, String name) {
        SharedPreferences sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        sp.edit().putString(KEY_NAME, name == null ? "" : name.trim()).apply();
    }

    public static String getUserName(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String value = sp.getString(KEY_NAME, null);
        if (value == null || value.trim().length() == 0) return null;
        return value;
    }

    public static void setLoggedIn(Context context, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        sp.edit().putBoolean(KEY_LOGGED_IN, value).apply();
    }

    public static boolean isLoggedIn(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return sp.getBoolean(KEY_LOGGED_IN, false);
    }

    public static void setDarkMode(Context context, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        sp.edit().putBoolean(KEY_DARK, value).apply();
    }

    public static boolean isDarkMode(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return sp.getBoolean(KEY_DARK, false);
    }

    public static void setMusicEnabled(Context context, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        sp.edit().putBoolean(KEY_MUSIC, value).apply();
    }

    public static boolean isMusicEnabled(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return sp.getBoolean(KEY_MUSIC, true);
    }

    public static void setLanguage(Context context, String lang) {
        SharedPreferences sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        sp.edit().putString(KEY_LANG, lang).apply();
    }

    public static String getLanguage(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return sp.getString(KEY_LANG, "en");
    }
} 