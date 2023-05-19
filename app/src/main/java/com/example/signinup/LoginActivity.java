package com.example.signinup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.signinup.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseHelper = new DatabaseHelper(this);

        binding.signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.signInEmail.getText().toString().trim();
                String password = binding.signInPassword.getText().toString().trim();

                String hashedPassword = databaseHelper.hashPassword(password);

                if(email.equals("") || password.equals("")) {
                    SignupActivity.showToast(LoginActivity.this,"All fields are required!");
                    return;
                }
               // else {
                 //   Boolean checkCredentials = databaseHelper.checkEmailPassword(email, password);
                    SQLiteDatabase MyDatabase = databaseHelper.getWritableDatabase();

                    Cursor cursor = MyDatabase.rawQuery("SELECT * FROM users WHERE email = ?", new String[]{email});


                    if (cursor.getCount() > 0){
                        cursor.moveToFirst();
                        int userId = cursor.getInt(cursor.getColumnIndex("user_id"));
                        String storedHashedPassword = cursor.getString(cursor.getColumnIndex("password"));
                        boolean checkCredentials = hashedPassword.equals(storedHashedPassword);

                    if (checkCredentials) {
                        AuthHelper.setLoggedInUserId(userId);
                        SignupActivity.showToast(LoginActivity.this,"Login success!");
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    } else {
                        SignupActivity.showToast(LoginActivity.this,"Invalid email or pass!");
                    }
                }else {
                        SignupActivity.showToast(LoginActivity.this,"Invalid email or pass!");
                    }
            }
        });
        binding.signUpRedirectText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }
}