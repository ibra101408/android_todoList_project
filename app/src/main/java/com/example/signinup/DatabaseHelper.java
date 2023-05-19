package com.example.signinup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String signUpDB = "signup.db";
    public static final String usersTable = "users";
    public static final String tasksTable = "todos";

    public static final String task_title = "title";
    public static final String task_description = "description";

    public DatabaseHelper(@Nullable Context context){
        super(context, "signUpDB", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase MyDatabase) {
        String createTable = "CREATE TABLE " + usersTable + "(user_id INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT NOT NULL, password TEXT NOT NULL)";
        MyDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDatabase, int i, int i1) {
        MyDatabase.execSQL("DROP TABLE IF EXISTS " + usersTable);
        onCreate(MyDatabase);
    }

    public void onCreateTask(SQLiteDatabase MyDatabase) {
        String createTable = "CREATE TABLE IF NOT EXISTS " + tasksTable +
                "(task_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT NOT NULL, " +
                "description TEXT NOT NULL, " +
                "user_id INTEGER, FOREIGN KEY (user_id) REFERENCES usersTable(user_id)" +
                ")";
        MyDatabase.execSQL(createTable);
    }
    public void onUpgradeTask(SQLiteDatabase MyDatabase, int i, int i1) {
        MyDatabase.execSQL("DROP TABLE IF EXISTS " + tasksTable);
        onCreateTask(MyDatabase);
    }

    public Boolean insertTask(String task_title, String task_description, Integer user_id){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("title", task_title);
        contentValues.put("description", task_description);
        contentValues.put("user_id", AuthHelper.getLoggedInUserId()); // add the user_id column
/*
        int user_id = AuthHelper.getLoggedInUserId();
        contentValues.put("user_id", user_id);*/
        long result = MyDatabase.insert(tasksTable, null, contentValues);

        return result != -1;
    }

    /*public long insertTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(task_title, task.getTitle());
        values.put(task_description, task.getDescription());
        // Add more columns as necessary

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(taskTable, null, values);
        db.close();
        return newRowId;
    }*/

    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Boolean insertData(String email, String password){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("password", hashPassword(password));
        //long result = MyDatabase.insert("allusers", null, contentValues);
        long result = MyDatabase.insert(usersTable, null, contentValues);

        return result != -1;
    }

    public Boolean checkEmail(String email){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("SELECT * FROM users WHERE email = ?", new String[]{email});

        if (cursor.getCount() > 0){
            return true;
        }else {
            return false;
        }
    }
    public Boolean checkEmailPassword(String email, String password) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("SELECT * FROM users WHERE email = ? AND password = ?", new String[]{email, password});

        if (cursor.getCount() > 0){
            return true;
        }else {
            return false;
        }
    }
}
