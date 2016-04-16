package com.dev.montherland.customers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.dev.montherland.model.Database;

import java.util.ArrayList;
import java.util.List;

public class dbhelp {

    static final String DATABASE_NAME = "Motherland";
    static final int DATABASE_VERSION = 1;
    static final String DATABASE_TABLE = "user_master";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_PROFILE = "profile_id";
    private static final String KEY_ROLE_ID = "role_id";
    private static final String KEY_CUSTOMER_ID = "customer_id";

    private final Context ourctx;
    private SQLiteDatabase ourdb;
    private DatabaseHelper2 ourhelper;


    public dbhelp(Context ctx) {
        ourctx = ctx;
    }

    public dbhelp open() throws SQLException {
        ourhelper = new DatabaseHelper2(ourctx);
        ourdb = ourhelper.getWritableDatabase();
        return this;
    }


    public void close() {
        ourhelper.close();
    }

    public void logoout() {
        ourdb.delete(DATABASE_TABLE, null, null);
    }

    public void updateeuser(String id, String name, String phone) {
        ContentValues cv4 = new ContentValues();
        cv4.put(KEY_NAME, name);
        cv4.put(KEY_PHONE, phone);
        ourdb.update(DATABASE_TABLE, cv4, KEY_ID + "=" + id, null);
    }

    public void passwordupdate(String id, String newpassword) {
        ContentValues cv5 = new ContentValues();
        cv5.put(KEY_PASSWORD, newpassword);
        ourdb.update(DATABASE_TABLE, cv5, KEY_ID + "=" + id, null);
    }

    public void createuser(String id, String name, String email, String password, String phone, String customer_id, String profile_id, String role_id) {

        ourdb.delete(DATABASE_TABLE, null, null);
        ContentValues cv = new ContentValues();
        cv.put(KEY_ID, id);
        cv.put(KEY_NAME, name);
        cv.put(KEY_EMAIL, email);
        cv.put(KEY_PASSWORD, password);
        cv.put(KEY_PHONE, phone);
        cv.put(KEY_PROFILE, profile_id);

        if (role_id.equals("null") || role_id.equals("")) {
            cv.put(KEY_ROLE_ID, "");
        } else {
            cv.put(KEY_ROLE_ID, role_id);
        }
        if (customer_id.equals("") || customer_id.equals("")) {
            cv.put(KEY_CUSTOMER_ID,"");
        } else {
            cv.put(KEY_CUSTOMER_ID,customer_id);
        }
        ourdb.insert(DATABASE_TABLE, null, cv);

    }


    public void updatepassword(String id, String password) {
        ContentValues cv4 = new ContentValues();
        cv4.put(KEY_PASSWORD, password);
        ourdb.update(DATABASE_TABLE, cv4, KEY_ID + "=" + id, null);

    }

    public static class DatabaseHelper2 extends SQLiteOpenHelper {

        public DatabaseHelper2(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + DATABASE_TABLE + " (" +
                    KEY_ID + " INTEGER NOT NULL, " +
                    KEY_NAME + " TEXT NOT NULL, " +
                    KEY_EMAIL + " TEXT NOT NULL, " +
                    KEY_PASSWORD + " TEXT NOT NULL, " +
                    KEY_PHONE + " TEXT NOT NULL, " +
                    KEY_PROFILE + " TEXT NOT NULL, " +
                    KEY_CUSTOMER_ID + " TEXT NOT NULL, " +
                    KEY_ROLE_ID + " TEXT NOT NULL);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }

        public List<Database> getdatabase() {

            try {
                List<Database> databases = new ArrayList<>();
                SQLiteDatabase db = this.getReadableDatabase();
                String query = "Select * from " + DATABASE_TABLE;
                //String query = "Select * from user_master";
                Cursor c = db.rawQuery(query, null);

                if (c != null) {
                    c.moveToFirst();
                    Database td = new Database();
                    td.setId(c.getString(c.getColumnIndex(KEY_ID)));
                    Log.d("id", c.getString(c.getColumnIndex(KEY_ID)));
                    td.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                    td.setEmail(c.getString(c.getColumnIndex(KEY_EMAIL)));
                    td.setPhone(c.getString(c.getColumnIndex(KEY_PHONE)));
                    td.setPassword(c.getString(c.getColumnIndex(KEY_PASSWORD)));
                    td.setProfile_id(c.getString(c.getColumnIndex(KEY_PROFILE)));
                    td.setCustomer_id(c.getString(c.getColumnIndex(KEY_CUSTOMER_ID)));
                    td.setRole_id(c.getString(c.getColumnIndex(KEY_ROLE_ID)));
                    databases.add(td);
                    c.close();
                }
                return databases;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

    }

}
