package com.example.foodgrid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.foodgrid.adapter.UserOrderAdapter;
import com.example.foodgrid.databinding.ActivityHome2Binding;
import com.example.foodgrid.model.UserOrderModel;

import java.util.ArrayList;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {
    MyDatabase db = null;
    UserDAO dao = null;
    UserSession session;
    private final String TAG = "ABC";
    ActivityHome2Binding binding;
    UserOrderAdapter adapter;
    ArrayList<UserOrderModel> userOrders = new ArrayList<UserOrderModel>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHome2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // initialize database and dao
        this.db = MyDatabase.getDatabase(getApplicationContext());
        this.dao = db.userDao();

        addDummyData();
        adapter = new UserOrderAdapter(this, userOrders);

        binding.orderList.setAdapter(adapter);

        binding.orderList.setLayoutManager(new LinearLayoutManager(this));

        binding.addOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserOrder.class);
                startActivity(intent);
            }
        });

    }

    private void addDummyData() {
        UserOrderModel userOrderModel = new UserOrderModel("Gulab jamun", "5 Kg", "MAKE IT GOOD", new Date().toString(), "TORONTO");
        userOrders.add(userOrderModel);

        userOrderModel = new UserOrderModel("Gulab jamun2", "5 Kg", "MAKE IT GOOD", new Date().toString(), "TORONTO");
        userOrders.add(userOrderModel);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //displaying menu on main activity

        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_view_profile:{
                Log.d(TAG, "onOptionsItemSelected: View profile clicked");
                Intent i = new Intent(this, ViewProfileActivity.class);

                this.startActivity(i);
                break;
            }
            case R.id.action_logout:{
                Log.d(TAG, "onOptionsItemSelected: Logout clicked");
                session.logout();
//                Intent i = new Intent(this, MainActivity.class);
//                this.startActivity(i);
                finish();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}