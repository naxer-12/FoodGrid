package com.example.foodgrid.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

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
    public void updatePasswordById(int userId, String updatedPassword);

    // update userStatus
    @Query("UPDATE users SET userStatus = :updatedUserStatus WHERE userId = :userId")
    public void updateUserStatus(int userId, Boolean updatedUserStatus);


    // update profile
    @Query("UPDATE users SET name = :updatedName, email = :updatedEmail, phoneNumber = :updatedPhoneNumber, address = :updatedAddress WHERE userId = :userId")
    public void updateProfile(int userId, String updatedName, String updatedEmail, String updatedPhoneNumber, String updatedAddress);

    // delete profile
    @Query("DELETE FROM users WHERE userId = :userId")
    public void deleteProfile(int userId);

    // get user by email
     @Query("SELECT * FROM users WHERE email = :email")
    public User getUser(String email);


    @Insert
    void insertOrder(UserOrderModel userOrder);


}
