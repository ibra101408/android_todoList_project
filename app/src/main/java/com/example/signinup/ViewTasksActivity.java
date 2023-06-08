package com.example.signinup;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ViewTasksActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<String> tasksList = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tasks);

        // Initialize the ListView and adapter
        listView = findViewById(R.id.taskListView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tasksList);
        listView.setAdapter(adapter);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        databaseHelper.onCreateTask(db);
        db.close();

        // Fetch the data from the database
        db = databaseHelper.getReadableDatabase();

        Integer userId = AuthHelper.getLoggedInUserId();
        String[] args = {String.valueOf(userId)};
        Cursor cursor = db.rawQuery("SELECT * FROM  todos  WHERE user_id = ?", args);

        // Print the column names
        String[] columnNames = cursor.getColumnNames();
        for (String columnName : columnNames) {
            Toast.makeText(ViewTasksActivity.this, "" + columnName, Toast.LENGTH_SHORT).show();

        }

// Clear the list to show only the column names
        tasksList.clear();

        // Loop through the cursor and add the data to the ArrayList
        while (cursor.moveToNext()) {
            String task_id = cursor.getString(cursor.getColumnIndex("task_id"));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String description = cursor.getString(cursor.getColumnIndex("description"));
            String user_id = cursor.getString(cursor.getColumnIndex("user_id"));
            tasksList.add(task_id + " - " + title + " - " + description + " - "+ user_id);
        }

        // Close the cursor and database connection
        cursor.close();
        db.close();

        // Notify the adapter that the data has changed
        adapter.notifyDataSetChanged();
    }
}