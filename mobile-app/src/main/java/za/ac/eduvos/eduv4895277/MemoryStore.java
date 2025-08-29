package za.ac.eduvos.eduv4895277;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MemoryStore {
    private static final String PREFS = "eduvos_memories";
    private static final String KEY_LIST = "memories";

    public static List<MemoryModel> getAll(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String json = sp.getString(KEY_LIST, "[]");
        List<MemoryModel> list = new ArrayList<>();
        try {
            JSONArray arr = new JSONArray(json);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                MemoryModel m = new MemoryModel();
                m.photoUri = o.optString("photoUri", null);
                m.audioUri = o.optString("audioUri", null);
                m.notes = o.optString("notes", null);
                m.createdAt = o.optLong("createdAt", 0);
                if (m.photoUri != null) list.add(m);
            }
        } catch (JSONException ignored) {}
        return list;
    }

    public static void add(Context context, MemoryModel memory) {
        List<MemoryModel> list = getAll(context);
        list.add(memory);
        saveAll(context, list);
    }

    private static void saveAll(Context context, List<MemoryModel> list) {
        JSONArray arr = new JSONArray();
        try {
            for (MemoryModel m : list) {
                JSONObject o = new JSONObject();
                o.put("photoUri", m.photoUri);
                o.put("audioUri", m.audioUri);
                o.put("notes", m.notes);
                o.put("createdAt", m.createdAt);
                arr.put(o);
            }
        } catch (JSONException ignored) {}
        SharedPreferences sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        sp.edit().putString(KEY_LIST, arr.toString()).apply();
    }
} 