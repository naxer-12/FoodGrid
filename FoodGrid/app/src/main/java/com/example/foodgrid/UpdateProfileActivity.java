package com.example.foodgrid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodgrid.dao.UserDAO;
import com.example.foodgrid.databinding.ActivityUpdateProfileBinding;
import com.example.foodgrid.model.User;

public class UpdateProfileActivity extends AppCompatActivity {

    private static final String SHARED_PREFERENCE_NAME = "User";
    UserSession session;
    private SharedPreferences shared;

    MyDatabase db = null;
    UserDAO dao = null;
    User user;

    ActivityUpdateProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // initialize database and dao
        this.db = MyDatabase.getDatabase(getApplicationContext());
        this.dao = db.userDao();

        session = new UserSession(getApplicationContext());
        shared = getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);

        String uEmail = shared.getString("Email", "");

        // get user by email
        user = this.dao.getUser(uEmail);


        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String phoneNumberPattern = "\\(?(\\d{3})\\)?[ .-]?(\\d{3})[ .-]?(\\d{4})";

        binding.editName.setText(user.getName());
        binding.editAddress.setText(user.getAddress());
        binding.editEmail.setText(user.getEmail());
        binding.editPhoneNumber.setText(user.getPhoneNumber());

        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ViewProfileActivity.class);
                startActivity(i);
            }
        });

        binding.btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updatedName = binding.editName.getText().toString();
                String updatedEmail = binding.editEmail.getText().toString();
                String updatedAddress = binding.editAddress.getText().toString();
                String updatedPhoneNumber = binding.editPhoneNumber.getText().toString();

                Boolean hasError = false;

                if (updatedName.trim().isEmpty()) {
                    binding.editName.setError("Please enter your name");
                    hasError = true;
                } else {
                    binding.editName.setError(null);
                    hasError = false;
                }

                if (updatedEmail.trim().isEmpty()) {
                    binding.editEmail.setError("Please enter your email");
                    hasError = true;
                } else {
                    binding.editEmail.setError(null);
                    hasError = false;
                }

                if (!updatedEmail.matches(emailPattern)) {
                    binding.editEmail.setError("Please enter valid email address");
                    hasError = true;
                } else {
                    binding.editEmail.setError(null);
                    hasError = false;
                }

                if (updatedPhoneNumber.trim().isEmpty()) {
                    binding.editPhoneNumber.setError("Please enter your phone number");
                    hasError = true;
                } else {
                    binding.editPhoneNumber.setError(null);
                    hasError = false;
                }

                if (!updatedPhoneNumber.matches(phoneNumberPattern)) {
                    binding.editPhoneNumber.setError("Please enter valid phone number 111-111-1111");
                } else {
                    binding.editPhoneNumber.setError(null);
                    hasError = false;
                }

                if (updatedAddress.trim().isEmpty()) {
                    binding.editAddress.setError("Please enter your address");
                    hasError = true;
                } else {
                    binding.editAddress.setError(null);
                    hasError = false;
                }


                if (!hasError) {
                    dao.updateProfile(user.getUserId(), updatedName, updatedEmail, updatedPhoneNumber, updatedAddress);

                    Toast.makeText(getApplicationContext(), "User details updated successfully", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getApplicationContext(), ViewProfileActivity.class);
                    startActivity(i);
                }

            }
        });


    }
}