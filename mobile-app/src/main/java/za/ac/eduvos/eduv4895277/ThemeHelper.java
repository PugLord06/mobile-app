package za.ac.eduvos.eduv4895277;

import android.app.Activity;

public class ThemeHelper {
    public static void apply(Activity activity) {
        if (Preferences.isDarkMode(activity)) {
            activity.setTheme(android.R.style.Theme_Material);
        } else {
            activity.setTheme(android.R.style.Theme_Material_Light);
        }
    }
} 