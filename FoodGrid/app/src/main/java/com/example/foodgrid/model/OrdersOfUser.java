package com.example.foodgrid.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class OrdersOfUser {


    @Embedded
    public User user;
    @Relation(
            parentColumn = "userId",
            entityColumn = "order_id"
    )
    public List<UserOrderModel> orderList;


    public OrdersOfUser(User user, List<UserOrderModel> orderList) {
        this.user = user;
        this.orderList = orderList;
    }
}
