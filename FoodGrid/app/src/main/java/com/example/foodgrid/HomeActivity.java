package com.example.foodgrid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.foodgrid.adapter.UserOrderAdapter;
import com.example.foodgrid.databinding.ActivityHome2Binding;
import com.example.foodgrid.model.UserOrderModel;
import com.google.android.material.snackbar.Snackbar;

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
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {


                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {


                UserOrderModel deletedOrder = userOrders.get(viewHolder.getAdapterPosition());
                userOrders.remove(viewHolder.getAdapterPosition());
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                Snackbar snackbar
                        = Snackbar
                        .make(
                                binding.orderList,
                                deletedOrder.getFoodItem() + " is deleted",
                                Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }).attachToRecyclerView(binding.orderList);
    }

    private void addDummyData() {
        UserOrderModel userOrderModel = new UserOrderModel("Gulab jamun", "5 Kg", "MAKE IT GOOD", new Date().toString(), "TORONTO");
        userOrders.add(userOrderModel);

        userOrderModel = new UserOrderModel("Gulab jamun2", "5 Kg", "MAKE IT GOOD", new Date().toString(), "TORONTO");
        userOrders.add(userOrderModel);
    }
}