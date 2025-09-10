package za.ac.eduvos.eduv4895277;

import android.content.Context;
import android.content.SharedPreferences;

public class TripStore {
    private static final String PREFS = "eduvos_trips";
    private static final String KEY_TRIP_COUNT = "trip_count";

    public static int getTripCount(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return sp.getInt(KEY_TRIP_COUNT, 0);
    }

    public static void addTrip(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        int count = sp.getInt(KEY_TRIP_COUNT, 0);
        sp.edit().putInt(KEY_TRIP_COUNT, count + 1).apply();
    }
} 