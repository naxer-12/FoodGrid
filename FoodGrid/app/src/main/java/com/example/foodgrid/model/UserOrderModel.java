package com.example.foodgrid.model;

import java.io.Serializable;

public class UserOrderModel implements Serializable {
    private String foodItem;
    private String foodQuantity;
    private String additionalInstructions;

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

    String date;
    String currentLocation;
}
