package com.example.foodgrid.viewholder;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.example.foodgrid.adapter.OnItemClickListener;
import com.example.foodgrid.databinding.RowLayoutBinding;
import com.example.foodgrid.model.UserOrderModel;

public class ViewHolder extends RecyclerView.ViewHolder {
    private final String TAG = "ABC";

    private RowLayoutBinding binding;
//    private OnItemClickListener clickBack;


    public ViewHolder(RowLayoutBinding binding) {
        super(binding.getRoot());
        this.binding = binding;

    }

    // update our view holder to use best practices
    public void bind(Context context, UserOrderModel userOrder, OnItemClickListener listener
    ) {
        // to associate the UI with your data
        this.binding.titleText.setText(userOrder.getFoodItem());
        this.binding.subtitleText.setText(userOrder.getAdditionalInstructions());


        this.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Log.d("ABC", "Clicked on " + position);
                    listener.addItem(position);
                }
            }
        });

    }


}