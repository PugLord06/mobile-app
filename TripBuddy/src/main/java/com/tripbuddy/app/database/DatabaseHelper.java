package com.tripbuddy.app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tripbuddy.app.models.Activity;
import com.tripbuddy.app.models.Memory;
import com.tripbuddy.app.models.Trip;
import com.tripbuddy.app.models.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    private static final String DATABASE_NAME = "tripbuddy.db";
    private static final int DATABASE_VERSION = 1;

    // Tables
    private static final String TABLE_USERS = "users";
    private static final String TABLE_TRIPS = "trips";
    private static final String TABLE_MEMORIES = "memories";
    private static final String TABLE_ACTIVITIES = "activities";

    // Common columns
    private static final String KEY_ID = "id";

    // User columns
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";

    // Trip columns
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_DESTINATION = "destination";
    private static final String KEY_START_DATE = "start_date";
    private static final String KEY_END_DATE = "end_date";
    private static final String KEY_NOTES = "notes";
    private static final String KEY_TOTAL_COST = "total_cost";
    private static final String KEY_ACTIVITIES_JSON = "activities_json";
    private static final String KEY_CUSTOM_EXPENSES_JSON = "custom_expenses_json";

    // Memory columns
    private static final String KEY_IMAGE_PATH = "image_path";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_DATE = "date";
    private static final String KEY_MOOD = "mood";
    private static final String KEY_MUSIC_PATH = "music_path";

    // Activity columns
    private static final String KEY_NAME = "name";
    private static final String KEY_COST = "cost";
    private static final String KEY_CATEGORY = "category";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create users table
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_USERNAME + " TEXT UNIQUE,"
                + KEY_EMAIL + " TEXT,"
                + KEY_PASSWORD + " TEXT" + ")";
        db.execSQL(CREATE_USERS_TABLE);

        // Create trips table
        String CREATE_TRIPS_TABLE = "CREATE TABLE " + TABLE_TRIPS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_USER_ID + " INTEGER,"
                + KEY_DESTINATION + " TEXT,"
                + KEY_START_DATE + " TEXT,"
                + KEY_END_DATE + " TEXT,"
                + KEY_NOTES + " TEXT,"
                + KEY_TOTAL_COST + " REAL,"
                + KEY_ACTIVITIES_JSON + " TEXT,"
                + KEY_CUSTOM_EXPENSES_JSON + " TEXT,"
                + "FOREIGN KEY(" + KEY_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + KEY_ID + ")" + ")";
        db.execSQL(CREATE_TRIPS_TABLE);

        // Create memories table
        String CREATE_MEMORIES_TABLE = "CREATE TABLE " + TABLE_MEMORIES + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_USER_ID + " INTEGER,"
                + KEY_IMAGE_PATH + " TEXT,"
                + KEY_LOCATION + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_MOOD + " TEXT,"
                + KEY_MUSIC_PATH + " TEXT,"
                + "FOREIGN KEY(" + KEY_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + KEY_ID + ")" + ")";
        db.execSQL(CREATE_MEMORIES_TABLE);

        // Create activities table
        String CREATE_ACTIVITIES_TABLE = "CREATE TABLE " + TABLE_ACTIVITIES + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT,"
                + KEY_COST + " REAL,"
                + KEY_CATEGORY + " TEXT" + ")";
        db.execSQL(CREATE_ACTIVITIES_TABLE);

        // Insert predefined activities
        insertPredefinedActivities(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTIVITIES);
        onCreate(db);
    }

    private void insertPredefinedActivities(SQLiteDatabase db) {
        String[][] activities = {
                {"Sightseeing", "50", "Tourism"},
                {"Hiking", "30", "Adventure"},
                {"Dining", "40", "Food"},
                {"Museum Tour", "20", "Culture"}
        };

        for (String[] activity : activities) {
            ContentValues values = new ContentValues();
            values.put(KEY_NAME, activity[0]);
            values.put(KEY_COST, Double.parseDouble(activity[1]));
            values.put(KEY_CATEGORY, activity[2]);
            db.insert(TABLE_ACTIVITIES, null, values);
        }
    }

    // User operations
    public long addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, user.getUsername());
        values.put(KEY_EMAIL, user.getEmail());
        values.put(KEY_PASSWORD, user.getPassword());
        
        long id = db.insert(TABLE_USERS, null, values);
        db.close();
        return id;
    }

    public User getUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{KEY_ID, KEY_USERNAME, KEY_EMAIL, KEY_PASSWORD},
                KEY_USERNAME + "=? AND " + KEY_PASSWORD + "=?", new String[]{username, password},
                null, null, null, null);

        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            user = new User();
            user.setId(cursor.getInt(0));
            user.setUsername(cursor.getString(1));
            user.setEmail(cursor.getString(2));
            user.setPassword(cursor.getString(3));
            cursor.close();
        }
        db.close();
        return user;
    }

    // Trip operations
    public long addTrip(Trip trip) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, trip.getUserId());
        values.put(KEY_DESTINATION, trip.getDestination());
        values.put(KEY_START_DATE, trip.getStartDate());
        values.put(KEY_END_DATE, trip.getEndDate());
        values.put(KEY_NOTES, trip.getNotes());
        values.put(KEY_TOTAL_COST, trip.getTotalCost());
        
        try {
            // Convert activities to JSON
            JSONArray activitiesArray = new JSONArray();
            for (Activity activity : trip.getActivities()) {
                JSONObject obj = new JSONObject();
                obj.put("name", activity.getName());
                obj.put("cost", activity.getCost());
                obj.put("category", activity.getCategory());
                obj.put("selected", activity.isSelected());
                activitiesArray.put(obj);
            }
            values.put(KEY_ACTIVITIES_JSON, activitiesArray.toString());

            // Convert custom expenses to JSON
            JSONArray expensesArray = new JSONArray();
            for (Trip.CustomExpense expense : trip.getCustomExpenses()) {
                JSONObject obj = new JSONObject();
                obj.put("name", expense.getName());
                obj.put("cost", expense.getCost());
                expensesArray.put(obj);
            }
            values.put(KEY_CUSTOM_EXPENSES_JSON, expensesArray.toString());
        } catch (Exception e) {
            Log.e(TAG, "Error converting to JSON", e);
        }

        long id = db.insert(TABLE_TRIPS, null, values);
        db.close();
        return id;
    }

    public int getTripCount(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_TRIPS + " WHERE " + KEY_USER_ID + "=?",
                new String[]{String.valueOf(userId)});
        
        int count = 0;
        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(0);
            cursor.close();
        }
        db.close();
        return count;
    }

    public List<Trip> getAllTrips(int userId) {
        List<Trip> trips = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TRIPS, null, KEY_USER_ID + "=?",
                new String[]{String.valueOf(userId)}, null, null, KEY_ID + " DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Trip trip = new Trip();
                trip.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                trip.setUserId(cursor.getInt(cursor.getColumnIndex(KEY_USER_ID)));
                trip.setDestination(cursor.getString(cursor.getColumnIndex(KEY_DESTINATION)));
                trip.setStartDate(cursor.getString(cursor.getColumnIndex(KEY_START_DATE)));
                trip.setEndDate(cursor.getString(cursor.getColumnIndex(KEY_END_DATE)));
                trip.setNotes(cursor.getString(cursor.getColumnIndex(KEY_NOTES)));
                trip.setTotalCost(cursor.getDouble(cursor.getColumnIndex(KEY_TOTAL_COST)));
                
                // Parse JSON data
                try {
                    String activitiesJson = cursor.getString(cursor.getColumnIndex(KEY_ACTIVITIES_JSON));
                    if (activitiesJson != null) {
                        JSONArray array = new JSONArray(activitiesJson);
                        List<Activity> activities = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            Activity activity = new Activity();
                            activity.setName(obj.getString("name"));
                            activity.setCost(obj.getDouble("cost"));
                            activity.setCategory(obj.getString("category"));
                            activity.setSelected(obj.getBoolean("selected"));
                            activities.add(activity);
                        }
                        trip.setActivities(activities);
                    }

                    String expensesJson = cursor.getString(cursor.getColumnIndex(KEY_CUSTOM_EXPENSES_JSON));
                    if (expensesJson != null) {
                        JSONArray array = new JSONArray(expensesJson);
                        List<Trip.CustomExpense> expenses = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            Trip.CustomExpense expense = new Trip.CustomExpense(
                                    obj.getString("name"),
                                    obj.getDouble("cost")
                            );
                            expenses.add(expense);
                        }
                        trip.setCustomExpenses(expenses);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error parsing JSON", e);
                }
                
                trips.add(trip);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return trips;
    }

    // Memory operations
    public long addMemory(Memory memory) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, memory.getUserId());
        values.put(KEY_IMAGE_PATH, memory.getImagePath());
        values.put(KEY_LOCATION, memory.getLocation());
        values.put(KEY_DATE, memory.getDate());
        values.put(KEY_MOOD, memory.getMood());
        values.put(KEY_MUSIC_PATH, memory.getMusicPath());
        
        long id = db.insert(TABLE_MEMORIES, null, values);
        db.close();
        return id;
    }

    public List<Memory> getAllMemories(int userId) {
        List<Memory> memories = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MEMORIES, null, KEY_USER_ID + "=?",
                new String[]{String.valueOf(userId)}, null, null, KEY_ID + " DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Memory memory = new Memory();
                memory.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                memory.setUserId(cursor.getInt(cursor.getColumnIndex(KEY_USER_ID)));
                memory.setImagePath(cursor.getString(cursor.getColumnIndex(KEY_IMAGE_PATH)));
                memory.setLocation(cursor.getString(cursor.getColumnIndex(KEY_LOCATION)));
                memory.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
                memory.setMood(cursor.getString(cursor.getColumnIndex(KEY_MOOD)));
                memory.setMusicPath(cursor.getString(cursor.getColumnIndex(KEY_MUSIC_PATH)));
                memories.add(memory);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return memories;
    }

    // Activity operations
    public List<Activity> getAllActivities() {
        List<Activity> activities = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ACTIVITIES, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Activity activity = new Activity();
                activity.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                activity.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
                activity.setCost(cursor.getDouble(cursor.getColumnIndex(KEY_COST)));
                activity.setCategory(cursor.getString(cursor.getColumnIndex(KEY_CATEGORY)));
                activities.add(activity);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return activities;
    }
}