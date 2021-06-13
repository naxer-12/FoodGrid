package com.example.foodgrid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.foodgrid.adapter.UserOrderAdapter;
import com.example.foodgrid.databinding.ActivityHome2Binding;
import com.example.foodgrid.model.UserOrderModel;

import java.util.ArrayList;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {

    ActivityHome2Binding binding;
    UserOrderAdapter adapter;
    ArrayList<UserOrderModel> userOrders = new ArrayList<UserOrderModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHome2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
}