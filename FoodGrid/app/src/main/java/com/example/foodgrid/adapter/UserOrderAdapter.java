package com.example.foodgrid.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.foodgrid.UserOrder;
import com.example.foodgrid.databinding.RowLayoutBinding;

import com.example.foodgrid.model.UserOrderModel;
import com.example.foodgrid.viewholder.ViewHolder;

import java.io.Serializable;
import java.util.ArrayList;

public class UserOrderAdapter extends RecyclerView.Adapter<ViewHolder> {
    private final String TAG = "ABC";

    private ArrayList<UserOrderModel> userOrders;
    private Context context;
    private OnItemClickListener clickBack;


    public UserOrderAdapter(Context context, ArrayList<UserOrderModel> userOrdersConst) {
        this.userOrders = userOrdersConst;
        this.context = context;

        clickBack = new OnItemClickListener() {


            @Override
            public void addItem(int position) {
                Log.d(TAG, "CURRENT POSITION" + position);


                UserOrderModel userOrder = userOrders.get(position);

                Log.d(TAG, "CURRENT POSITION USER" + userOrder.getFoodItem());
                Intent intent = new Intent(context, UserOrder.class);
                intent.putExtra("USER_ORDER", (Serializable) userOrder);
                context.startActivity(intent);


            }
        };

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item

        RowLayoutBinding binding = RowLayoutBinding.inflate(LayoutInflater.from(this.context), viewGroup, false);
        return new ViewHolder(binding);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        UserOrderModel userOrderModel = this.userOrders.get(position);
        viewHolder.bind(this.context, userOrderModel, clickBack);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.userOrders.size();
    }


}