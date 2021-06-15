package com.example.foodgrid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodgrid.dao.UserDAO;
import com.example.foodgrid.databinding.ActivityRegisterBinding;
import com.example.foodgrid.model.User;

public class RegisterActivity extends AppCompatActivity {


    private final String TAG = "Maitri";
    private ActivityRegisterBinding binding;
    MyDatabase db = null;
    UserDAO dao = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // initialize database and dao
        this.db = MyDatabase.getDatabase(getApplicationContext());
        this.dao = db.userDao();


        binding.tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLoginPage();
            }
        });

        binding.btnRegister.setOnClickListener(this::registerUser);

    }

    public void registerUser(View view) {
        Log.d(TAG, "registerUser: register button clicked");

        // get the values from the field
        String name = this.binding.editName.getText().toString();
        String email = this.binding.editEmail.getText().toString();
        String phoneNumber = this.binding.editPhoneNumber.getText().toString();
        String address = this.binding.editAddress.getText().toString();
        String password = this.binding.editPassword.getText().toString();
        Boolean userStatus = true;
        Boolean hasError = false;

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String phoneNumberPattern = "\\(?(\\d{3})\\)?[ .-]?(\\d{3})[ .-]?(\\d{4})";
        // reference for regex of phone number
        // link:- https://stackoverflow.com/questions/2998139/custom-regex-expression-for-validating-different-possibilities-of-phone-number-e

        // Validate that the user has provided all the necessary information
        if (name.trim().isEmpty()) {
            this.binding.editName.setError("Please enter your name");
            hasError = true;
        }

        if (email.trim().isEmpty()) {
            this.binding.editEmail.setError("Please enter your email");
            hasError = true;
        }

        if (!email.matches(emailPattern)) {
            this.binding.editEmail.setError("Please enter valid email address");
            hasError = true;
        }

        if (phoneNumber.trim().isEmpty()) {
            this.binding.editPhoneNumber.setError("Please enter your phone number");
            hasError = true;
        }

        if (!phoneNumber.matches(phoneNumberPattern)) {
            this.binding.editPhoneNumber.setError("Please enter valid phone number 111-111-1111");
        }

        if (address.trim().isEmpty()) {
            this.binding.editAddress.setError("Please enter your address");
            hasError = true;
        }

        if (password.trim().isEmpty()) {
            this.binding.editPassword.setError("Please enter password");
            hasError = true;
        }

        hasError = false;

        Log.d(TAG, "registerUser: " + hasError);
        if (!hasError) {
            Log.d(TAG, "registerUser: " + hasError);
            // Create a new Employee object using the form data
            User user = new User(name, email, phoneNumber, address, password, userStatus);


            // Use the DAO functions to insert the Employee into the database

            this.dao.addUser(user);
            Toast.makeText(getApplicationContext(), "User registered successfully", Toast.LENGTH_LONG).show();
            Log.d(TAG, "registerUser: User registered successfully");
            // Clear the textboxes and prepare for the next data entry
            this.binding.editName.setText("");
            this.binding.editPhoneNumber.setText("");
            this.binding.editEmail.setText("");
            this.binding.editPassword.setText("");
            this.binding.editAddress.setText("");

            goToLoginPage();
        }
    }

    public void goToLoginPage() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }
}