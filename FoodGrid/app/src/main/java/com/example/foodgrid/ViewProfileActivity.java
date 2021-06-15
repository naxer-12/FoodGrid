package com.example.foodgrid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodgrid.databinding.ActivityViewProfileBinding;
import com.example.foodgrid.model.User;

public class ViewProfileActivity extends AppCompatActivity {

    private static final String SHARED_PREFERENCE_NAME = "User";
    UserSession session;
    private SharedPreferences shared;

    MyDatabase db = null;
    UserDAO dao = null;
    User user;

    ActivityViewProfileBinding binding;
    private final String TAG = "Maitri";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // initialize database and dao
        this.db = MyDatabase.getDatabase(getApplicationContext());
        this.dao = db.userDao();

        session = new UserSession(getApplicationContext());
        shared = getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);

        String uEmail = shared.getString("Email", "");

        // get user by email
        user = this.dao.getUser(uEmail);

        Log.d(TAG, "onCreate: " + user.getPhoneNumber());

        Log.d(TAG, "onCreate: Email" + uEmail);

        binding.tvName.setText(user.getName());
        binding.tvEmail.setText(user.getEmail());
        binding.tvAddress.setText(user.getAddress());
        binding.tvPhoneNumber.setText(user.getPhoneNumber());
        binding.tvPassword.setText(user.getPassword());

        binding.btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Update profile button clicked");

                Intent i = new Intent(getApplicationContext(), UpdateProfileActivity.class);
                startActivity(i);
            }
        });

        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Cancel button clicked");

                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(i);
            }
        });
    }
}