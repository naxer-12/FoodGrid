package com.example.foodgrid.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import static androidx.room.ForeignKey.CASCADE;


@Entity(tableName = "UserOrders")
public class UserOrderModel implements Serializable {


    @PrimaryKey(autoGenerate = true)
    private long order_id;
    private String foodItem;
    private String foodQuantity;
    private String additionalInstructions;

    String date;
    String currentLocation;


    @ForeignKey
            (entity = User.class,
                    parentColumns = "userId",
                    childColumns = "userOrderId",
                    onDelete = CASCADE
            )
    private long userOrderId;

    public String getFoodItem() {
        return foodItem;
    }

    public void setFoodItem(String foodItem) {
        this.foodItem = foodItem;
    }

    public String getFoodQuantity() {
        return foodQuantity;
    }

    public void setFoodQuantity(String foodQuantity) {
        this.foodQuantity = foodQuantity;
    }

    public String getAdditionalInstructions() {
        return additionalInstructions;
    }

    public void setAdditionalInstructions(String additionalInstructions) {
        this.additionalInstructions = additionalInstructions;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public UserOrderModel(String foodItem, String foodQuantity, String additionalInstructions, String date, String currentLocation) {
        this.foodItem = foodItem;
        this.foodQuantity = foodQuantity;
        this.additionalInstructions = additionalInstructions;
        this.date = date;
        this.currentLocation = currentLocation;
    }

    public long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(long order_id) {
        this.order_id = order_id;
    }

    public long getUserOrderId() {
        return userOrderId;
    }

    public void setUserOrderId(long userOrderId) {
        this.userOrderId = userOrderId;
    }
}
