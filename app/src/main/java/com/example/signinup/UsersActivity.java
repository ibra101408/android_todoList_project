package com.example.signinup;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class UsersActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<String> usersList = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        // Initialize the ListView and adapter
        listView = findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, usersList);
        listView.setAdapter(adapter);

        // Fetch the data from the database
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users", null);

        // Loop through the cursor and add the data to the ArrayList
        while (cursor.moveToNext()) {
            String user_id = cursor.getString(cursor.getColumnIndex("user_id"));
            String email = cursor.getString(cursor.getColumnIndex("email"));
            String password = cursor.getString(cursor.getColumnIndex("password"));
            usersList.add(user_id + " - " + email + " - " + password);
        }

        // Close the cursor and database connection
        cursor.close();
        db.close();

        // Notify the adapter that the data has changed
        adapter.notifyDataSetChanged();
    }
}
