package com.example.foodgrid;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.example.foodgrid.dao.UserDAO;
import com.example.foodgrid.databinding.ActivityHome2Binding;
import com.example.foodgrid.model.UserOrderModel;
import com.example.foodgrid.viewmodel.UserOrderViewModel;
import com.google.android.material.snackbar.Snackbar;

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
    int LAUNCH_USER_ORDER_ACTIVITY = 1;
    private UserOrderViewModel userOrderViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHome2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userOrderViewModel = new UserOrderViewModel(getApplication());
        // initialize database and dao
        this.db = MyDatabase.getDatabase(getApplicationContext());
        this.dao = db.userDao();
        session = new UserSession(getApplicationContext());
        adapter = new UserOrderAdapter(this, userOrders);
        fetchData();
        binding.orderList.setAdapter(adapter);

        binding.orderList.setLayoutManager(new LinearLayoutManager(this));

        binding.addOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserOrder.class);
                startActivityForResult(intent, LAUNCH_USER_ORDER_ACTIVITY);
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
                new AlertDialog.Builder(viewHolder.itemView.getContext())
                        .setTitle("Alert!!")
                        .setMessage("Are you sure you want to delete " + deletedOrder.getFoodItem()).setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        userOrderViewModel.deleteOrderOfUser(userOrders.get(viewHolder.getAdapterPosition()));
                        adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                        fetchData();

                        Snackbar snackbar
                                = Snackbar
                                .make(
                                        binding.orderList,
                                        deletedOrder.getFoodItem() + " is deleted",
                                        Snackbar.LENGTH_LONG);
                        snackbar.show();

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                adapter.notifyItemChanged(viewHolder.getAdapterPosition());

                            }
                        }

                ).create().show();


            }
        }).attachToRecyclerView(binding.orderList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "ITEM ADDED TO ORDER INSIDE ACTIVITYY");

        if (requestCode == LAUNCH_USER_ORDER_ACTIVITY) {
            Log.d(TAG, "ITEM ADDED TO ORDER INSIDE REQUEST CODE");

            if (resultCode == Activity.RESULT_OK) {
                Log.d(TAG, "ITEM ADDED TO ORDER INSIDE REQUEST CODE OK");

                UserOrderModel userOrder = (UserOrderModel) data.getSerializableExtra("USER_ORDER_DATA");
                Log.d(TAG, "ITEM ADDED TO ORDER" + userOrder.getFoodItem());
                userOrderViewModel.insertOrderOfUser(userOrder);
                fetchData();
            }
        }

    }

    private void fetchData() {
//        UserOrderModel userOrderModel = new UserOrderModel("Gulab jamun", "5 Kg", "MAKE IT GOOD", new Date().toString(), "TORONTO");
//        userOrders.add(userOrderModel);
//
//        userOrderModel = new UserOrderModel("Gulab jamun2", "5 Kg", "MAKE IT GOOD", new Date().toString(), "TORONTO");
//        userOrders.add(userOrderModel);
        userOrders.clear();
        if (userOrderViewModel.getAllOrder(session.getUserId()).isEmpty()) {
            binding.emptyTextview.setText("No data found");
        } else {
            binding.emptyTextview.setText("");
            userOrders.addAll(userOrderViewModel.getAllOrder(session.getUserId()));
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //displaying menu on main activity

        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_view_profile: {
                Log.d(TAG, "onOptionsItemSelected: View profile clicked");
                Intent i = new Intent(this, ViewProfileActivity.class);

                this.startActivity(i);
                break;
            }
            case R.id.action_logout: {
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