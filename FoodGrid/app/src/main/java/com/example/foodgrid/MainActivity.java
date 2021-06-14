package com.example.foodgrid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodgrid.databinding.ActivityMainBinding;
import com.example.foodgrid.model.User;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private final String TAG = "Maitri";
    MyDatabase db = null;
    UserDAO dao = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // initialize database and dao
        this.db = MyDatabase.getDatabase(getApplicationContext());
        this.dao = db.userDao();

        binding.tvRegisterNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: register clicked");
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
            }
        });

        binding.tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(i);
            }
        });

        binding.btnLogin.setOnClickListener(this::loginUser);
    }

    public void loginUser(View view){
        String email = this.binding.editEmail.getText().toString();
        String password = this.binding.editPassword.getText().toString();

        if(email.trim().isEmpty()){
            this.binding.editEmail.setError("Please enter your email address");
        }

        if(password.trim().isEmpty()){
            this.binding.editPassword.setError("Please enter your password");
        }

        User user = this.dao.getUser(email);

        if(user != null){
            Log.d(TAG, "loginUser: User exists");
            if(user.getPassword().equals(password)){
                Log.d(TAG, "loginUser: Login successful");
            }
            else{
                Log.d(TAG, "loginUser: Login failed");
            }
        }
        else{
            Log.e(TAG, "loginUser: No such user exists");
        }
    }


}