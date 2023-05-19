package com.example.signinup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.signinup.databinding.ActivitySignupBinding;

import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    ActivitySignupBinding binding;
    DatabaseHelper databaseHelper;

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
    private boolean isValidEmail(String email) {
        // regular expression to validate email format
        String emailRegex = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
        return pattern.matcher(email).matches();
    }
    private boolean isValidPassword(String password){
        int minLength = 4;
        return password.length() >= minLength;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseHelper = new DatabaseHelper(this);

        binding.signUpBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String email = binding.email.getText().toString().trim();
                String password = binding.password.getText().toString().trim();
                String confirmPassword = binding.confirmPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)){
                    showToast(SignupActivity.this,"All fields are required");
                    return;
                }

                if (!isValidEmail(email)) {
                    showToast(SignupActivity.this,"Invalid email format");
                    return;
                }

                if (!isValidPassword(password)) {
                    showToast(SignupActivity.this,"Invalid password format: min 4 char");
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    showToast(SignupActivity.this,"Passwords do not match");
                    return;
                }

                Boolean doesUserExist = databaseHelper.checkEmail(email);
                if (doesUserExist) {
                    showToast(SignupActivity.this,"User exists");
                    return;
                }

                boolean isInsertSuccessful = databaseHelper.insertData(email, password);
                showToast(SignupActivity.this, isInsertSuccessful ? "Sign up success" : "Sign up failed");
                if (isInsertSuccessful) {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));

                }
            }
        });

        binding.loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
