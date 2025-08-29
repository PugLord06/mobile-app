package za.ac.eduvos.eduv4895277;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "tripbuddy.db";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE memories (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "photo_uri TEXT, " +
                "audio_uri TEXT, " +
                "notes TEXT, " +
                "location TEXT, " +
                "date INTEGER, " +
                "mood TEXT");
        db.execSQL("CREATE TABLE trips (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "destination TEXT, " +
                "notes TEXT, " +
                "activity TEXT, " +
                "custom REAL, " +
                "meals REAL, " +
                "created_at INTEGER");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS memories");
        db.execSQL("DROP TABLE IF EXISTS trips");
        onCreate(db);
    }

    // Memories
    public long insertMemory(MemoryModel m) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("photo_uri", m.photoUri);
        cv.put("audio_uri", m.audioUri);
        cv.put("notes", m.notes);
        cv.put("location", (String) null);
        cv.put("date", m.createdAt);
        cv.put("mood", (String) null);
        return db.insert("memories", null, cv);
    }

    public List<MemoryModel> getAllMemories() {
        List<MemoryModel> out = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT id, photo_uri, audio_uri, notes, date FROM memories ORDER BY id DESC", null);
        try {
            while (c.moveToNext()) {
                MemoryModel m = new MemoryModel();
                m.id = c.getLong(0);
                m.photoUri = c.getString(1);
                m.audioUri = c.getString(2);
                m.notes = c.getString(3);
                m.createdAt = c.getLong(4);
                out.add(m);
            }
        } finally {
            c.close();
        }
        return out;
    }

    public void updateMemoryNotes(long id, String notes) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("notes", notes);
        db.update("memories", cv, "id=?", new String[]{String.valueOf(id)});
    }

    // Trips
    public long insertTrip(TripModel t) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("destination", t.destination);
        cv.put("notes", t.notes);
        String activities = t.activities == null || t.activities.isEmpty() ? null : t.activities.get(0);
        cv.put("activity", activities);
        cv.put("custom", t.customExpense);
        cv.put("meals", t.mealsExpense);
        cv.put("created_at", System.currentTimeMillis());
        return db.insert("trips", null, cv);
    }

    public TripModel getLastTrip() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT destination, notes, activity, custom, meals FROM trips ORDER BY id DESC LIMIT 1", null);
        try {
            if (c.moveToFirst()) {
                TripModel t = new TripModel();
                t.destination = c.getString(0);
                t.notes = c.getString(1);
                String activity = c.getString(2);
                if (activity != null) {
                    t.activities.add(activity);
                }
                t.customExpense = c.getDouble(3);
                t.mealsExpense = c.getDouble(4);
                return t;
            }
        } finally {
            c.close();
        }
        return null;
    }
} 