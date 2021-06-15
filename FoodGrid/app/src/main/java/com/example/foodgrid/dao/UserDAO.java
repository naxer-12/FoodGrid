package com.example.foodgrid.dao;

import android.util.Log;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.foodgrid.UserOrder;
import com.example.foodgrid.model.User;
import com.example.foodgrid.model.UserOrderModel;

import java.util.List;

@Dao
public interface UserDAO {

    // add
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void addUser(User user);

    // update password
    @Query("UPDATE users SET password = :updatedPassword WHERE userId = :userId")
    public void updatePasswordById(long userId, String updatedPassword);

    // update userStatus
    @Query("UPDATE users SET userStatus = :updatedUserStatus WHERE userId = :userId")
    public void updateUserStatus(long userId, Boolean updatedUserStatus);


    // update profile
    @Query("UPDATE users SET name = :updatedName, email = :updatedEmail, phoneNumber = :updatedPhoneNumber, address = :updatedAddress WHERE userId = :userId")
    public void updateProfile(long userId, String updatedName, String updatedEmail, String updatedPhoneNumber, String updatedAddress);

    // delete profile
    @Query("DELETE FROM users WHERE userId = :userId")
    public void deleteProfile(long userId);

    // get user by email
     @Query("SELECT * FROM users WHERE email = :email")
    public User getUser(String email);


    @Insert(onConflict = OnConflictStrategy.IGNORE
    )
    long insertOrder(UserOrderModel userOrder);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void update(UserOrderModel userOrder);

    @Transaction
    default void upsert(UserOrderModel userOrder) {

        String TAG = "INSERT OPERATION";
        Log.d(TAG, "INSERT ID " + userOrder.getOrder_id());
        List<UserOrderModel> order = getOrderById(userOrder.getOrder_id());
        Log.d(TAG, "INSERT ID " + order.isEmpty());
        if (order.isEmpty()) {
            insertOrder(userOrder);
        } else {
            update(userOrder);
        }

    }

    @Query("DELETE FROM UserOrders WHERE userOrderId =:userId AND order_id=:orderID")
    void deleteOrder(long userId, long orderID);

    @Query("SELECT * FROM UserOrders WHERE userOrderId =:userId ORDER BY date DESC")
    List<UserOrderModel> getAllOrders(long userId);


    @Query("SELECT * from UserOrders WHERE order_id= :id")
    List<UserOrderModel> getOrderById(long id);

}
