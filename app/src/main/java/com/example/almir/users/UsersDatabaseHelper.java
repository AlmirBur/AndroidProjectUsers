package com.example.almir.users;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UsersDatabaseHelper extends SQLiteOpenHelper {
    private static final String strUrl = "https://www.dropbox.com/s/s8g63b149tnbg8x/users.json?dl=1";
    static final String DB_NAME = "users";
    static final String TABLE_NAME = "USERS";
    static final int DB_VERSION = 1;
    static final String KEY_ID = "ID";
    static final String KEY_AGE = "AGE";
    static final String KEY_IS_ACTIVE = "IS_ACTIVE";
    static final String KEY_NAME = "NAME";
    static final String KEY_COMPANY = "COMPANY";
    static final String KEY_EMAIL = "EMAIL";
    static final String KEY_ADDRESS = "ADDRESS";
    static final String KEY_PHONE = "PHONE";
    static final String KEY_ABOUT = "ABOUT";
    static final String KEY_REGISTERED = "REGISTERED";
    static final String KEY_LATITUDE = "LATITUDE";
    static final String KEY_LONGITUDE = "LONGITUDE";
    static final String KEY_EYE_COLOR = "EYE_COLOR";
    static final String KEY_FAVORITE_FRUIT = "FAVORITE_FRUIT";
    static final String KEY_FRIENDS = "FRIENDS";
    static final String[] STANDARD_QUERY = new String[] {
            KEY_ID,
            KEY_AGE,
            KEY_IS_ACTIVE,
            KEY_NAME,
            KEY_COMPANY,
            KEY_EMAIL,
            KEY_ADDRESS,
            KEY_PHONE,
            KEY_ABOUT,
            KEY_REGISTERED,
            KEY_LATITUDE,
            KEY_LONGITUDE,
            KEY_EYE_COLOR,
            KEY_FAVORITE_FRUIT,
            KEY_FRIENDS };

    UsersDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
        "ID INTEGER, " +
        "AGE INTEGER, " +
        "IS_ACTIVE NUMERIC, " +
        "NAME TEXT, " +
        "COMPANY TEXT, " +
        "EMAIL TEXT, " +
        "ADDRESS TEXT, " +
        "PHONE TEXT, " +
        "ABOUT TEXT, " +
        "REGISTERED TEXT, " +
        "LATITUDE REAL, " +
        "LONGITUDE REAL, " +
        "EYE_COLOR TEXT, " +
        "FAVORITE_FRUIT TEXT, " +
        "FRIENDS TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {  }

    void deleteTable(SQLiteDatabase database) {
        database.delete(TABLE_NAME, null, null);
    }
    
    void insertUser(SQLiteDatabase database,
                                   int id,
                                   int age,
                                   boolean isActive,
                                   String name,
                                   String company,
                                   String email,
                                   String address,
                                   String phone,
                                   String about,
                                   String registered,
                                   double latitude,
                                   double longitude,
                                   String eyeColor,
                                   String favoriteFruit,
                                   String friends) {
        ContentValues userValues = new ContentValues();
        userValues.put("ID", id);
        userValues.put("AGE", age);
        userValues.put("IS_ACTIVE", isActive);
        userValues.put("NAME", name);
        userValues.put("COMPANY", company);
        userValues.put("EMAIL", email);
        userValues.put("ADDRESS", address);
        userValues.put("PHONE", phone);
        userValues.put("ABOUT", about);
        userValues.put("REGISTERED", registered);
        userValues.put("LATITUDE", latitude);
        userValues.put("LONGITUDE", longitude);
        userValues.put("EYE_COLOR", eyeColor);
        userValues.put("FAVORITE_FRUIT", favoriteFruit);
        userValues.put("FRIENDS", friends);
        database.insert(TABLE_NAME, null, userValues);
    }

    private String getJsonString() throws Exception {
        StringBuilder sb = new StringBuilder();
        URL url = new URL(strUrl);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }

    private void insertUsers(String strJson) throws Exception {
        JSONArray jsonUsers = new JSONArray(strJson);
        try (SQLiteDatabase database = getWritableDatabase()) {
            deleteTable(database);
            for (int i = 0; i < jsonUsers.length(); i++) {
                JSONObject jsonUser = (JSONObject) jsonUsers.get(i);
                if (jsonUser.getInt("id") < 0) continue;//id должно быть не меньше чем ноль
                insertUser(database,
                        jsonUser.getInt("id"),
                        jsonUser.getInt("age"),
                        jsonUser.getBoolean("isActive"),
                        jsonUser.getString("name"),
                        jsonUser.getString("company"),
                        jsonUser.getString("email"),
                        jsonUser.getString("address"),
                        jsonUser.getString("phone"),
                        jsonUser.getString("about"),
                        convertFormat(jsonUser.getString("registered")),
                        jsonUser.getDouble("latitude"),
                        jsonUser.getDouble("longitude"),
                        jsonUser.getString("eyeColor"),
                        jsonUser.getString("favoriteFruit"),
                        arrayToString(jsonUser.getJSONArray("friends"))); } }
    }

    void updateDatabase() throws Exception {
        insertUsers(getJsonString());
    }

    private String convertFormat(String registered) {
        if (!registered.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2} [-+]?\\d{2}:\\d{2}"))
            throw new IllegalArgumentException("Неправильный формат");
        String[] parts = registered.split("[- T:]");
        return parts[3] + ":" + parts[4] + " " + parts[2] + "." + parts[1] + "." + parts[0].substring(2);
    }

    private String arrayToString(JSONArray jsonFriends) throws JSONException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < jsonFriends.length() - 1; i++) {
            sb.append(((JSONObject) jsonFriends.get(i)).getInt("id"));
            sb.append(",");
        }
        sb.append(((JSONObject) jsonFriends.get(jsonFriends.length() - 1)).getInt("id"));
        return sb.toString();
    }

    private Set<Integer> stringToSetOfInteger(String strFriends) {
        String[] parts = strFriends.split(",");
        Set<Integer> friends = new HashSet<>();
        for (String id : parts) {
            friends.add(Integer.valueOf(id));
        }
        return friends;
    }

    private User getUser(Cursor cursor) {
        User user = new User();
        try {
            user.setId(cursor.getInt(0));
            user.setAge(cursor.getInt(1));
            user.setActive(cursor.getInt(2) == 1);
            user.setName(cursor.getString(3));
            user.setCompany(cursor.getString(4));
            user.setEmail(cursor.getString(5));
            user.setAddress(cursor.getString(6));
            user.setPhone(cursor.getString(7));
            user.setAbout(cursor.getString(8));
            user.setRegistered(cursor.getString(9));
            user.setLatitude(cursor.getDouble(10));
            user.setLongitude(cursor.getDouble(11));
            user.setEyeColor(User.getEyeColor(cursor.getString(12)));
            user.setFavoriteFruit(User.getFruit(cursor.getString(13)));
            user.setFriends(stringToSetOfInteger(cursor.getString(14)));
        } catch (SQLiteException e) {
            return null;
        }
        return user;
    }

    User getUser(int id) {
        User user = null;
        try (Cursor cursor = getReadableDatabase().query(UsersDatabaseHelper.TABLE_NAME,
                UsersDatabaseHelper.STANDARD_QUERY, "ID = ?",
                new String[] {Integer.toString(id)}, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                user = getUser(cursor);
            }
        }
        return user;
    }

    List<User> getUsers() {
        List<User> users = new ArrayList<>();
        try (Cursor cursor = getReadableDatabase().query(UsersDatabaseHelper.TABLE_NAME,
                UsersDatabaseHelper.STANDARD_QUERY,
                null, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    users.add(getUser(cursor));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            return new ArrayList<>();
        }
        return users;
    }

    boolean databaseIsEmpty() {
        try (Cursor cursor = getReadableDatabase().query(UsersDatabaseHelper.TABLE_NAME,
                new String[] {"_ID"},
                null, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) return false;
        }
        return true;
    }
}