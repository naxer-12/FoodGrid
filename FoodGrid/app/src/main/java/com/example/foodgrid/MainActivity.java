package com.example.foodgrid;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodgrid.dao.UserDAO;
import com.example.foodgrid.databinding.ActivityMainBinding;
import com.example.foodgrid.model.User;

public class MainActivity extends AppCompatActivity {

    SharedPreferences shared;
    SharedPreferences.Editor editor;
    UserSession session;


    ActivityMainBinding binding;
    private final String TAG = "Maitri";
    MyDatabase db = null;
    UserDAO dao = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        shared = getApplicationContext().getSharedPreferences("User", 0);
        editor = shared.edit();

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // initialize database and dao
        this.db = MyDatabase.getDatabase(getApplicationContext());
        this.dao = db.userDao();

        shared = getApplicationContext().getSharedPreferences("User", 0);


        session = new UserSession(getApplicationContext());

        session.checkLoginStatus();
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

    public void loginUser(View view) {
        String email = this.binding.editEmail.getText().toString();
        String password = this.binding.editPassword.getText().toString();

        if (email.trim().isEmpty()) {
            this.binding.editEmail.setError("Please enter your email address");
        }

        if (password.trim().isEmpty()) {
            this.binding.editPassword.setError("Please enter your password");
        }

        User user = this.dao.getUser(email);

        if (user != null) {

            Log.d(TAG, "loginUser: User exists");
            if (user.getPassword().equals(password)) {
                session.setUserId(user.getUserId());

                Log.d(TAG, "loginUser: Login successful");
                Toast.makeText(this, "User logged in successfully", Toast.LENGTH_LONG).show();
                Boolean rememberMe = this.binding.checkRememberMe.isChecked();

                if (user.getUserStatus()) {
                    session.userLoginSession(email, password, rememberMe);

                    this.binding.editEmail.setText("");
                    this.binding.editPassword.setText("");


                    Intent i = new Intent(this, HomeActivity.class);
                    this.startActivity(i);
                    this.finish();

                } else {
                            AlertDialog.Builder restoreProfileDialog = new AlertDialog.Builder(this);
                            restoreProfileDialog.setMessage("We already have your account want to restore it?");
                            restoreProfileDialog.setTitle("Restore Account");
                            restoreProfileDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.d(TAG, "onClick: restore account");
                                    Intent i = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                                    startActivity(i);
                                }
                            });
                            restoreProfileDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.d(TAG, "onClick: don't restore account");

                                    // delete account from database
                                    Log.d(TAG, "onClick: " + user.getUserId());
                                    dao.deleteProfile(user.getUserId());

                                    Toast.makeText(getApplicationContext(), "Profile deleted successfully", Toast.LENGTH_LONG).show();

                                    //create new account
                                    Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                                    startActivity(i);

                                }
                            });
                    AlertDialog alertProfileDialog = restoreProfileDialog.create();
                    alertProfileDialog.show();
                }

            } else {
                Log.d(TAG, "loginUser: Login failed");
                Toast.makeText(this, "Wrong password", Toast.LENGTH_LONG).show();
            }
        } else {
            Log.e(TAG, "loginUser: No such user exists");
            Toast.makeText(this, "No such user exists", Toast.LENGTH_LONG).show();
        }
    }


}