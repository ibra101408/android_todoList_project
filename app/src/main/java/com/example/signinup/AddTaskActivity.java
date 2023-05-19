package com.example.signinup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.signinup.databinding.ActivityAddTaskBinding;

public class AddTaskActivity extends AppCompatActivity {
    private ActivityAddTaskBinding binding;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(this);

        binding.saveTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = binding.taskTitle.getText().toString().trim();
                String description = binding.taskDescription.getText().toString().trim();
                //String user_id = AuthHelper.getLoggedInUserId();
                Integer user_id = AuthHelper.getLoggedInUserId();
               // int userId = Integer.parseInt(user_id);
                if (!title.isEmpty() && !description.isEmpty()) {

                    dbHelper.insertTask(title, description, user_id);
                   /* Task task = new Task(title, description);Task task = new Task(0, title, description);
                    String result = dbHelper.insertTask(title.getTitle(), description);
*/
                    Toast.makeText(AddTaskActivity.this, "Task saved successfully" + user_id, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddTaskActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }
}
