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

    // update profile
    @Query("UPDATE users SET name = :updatedName, email = :updatedEmail, phoneNumber = :updatedPhoneNumber, address = :updatedAddress WHERE userId = :userId")
    public void updateProfile(int userId, String updatedName, String updatedEmail, String updatedPhoneNumber, String updatedAddress);

    // delete profile
    @Query("DELETE FROM users WHERE userId = :userId")
    public void deleteProfile(int userId);

    // get by email
//    @Query("SELECT * FROM users WHERE email = :email ")
//    public User getUser(String email);

    //    @Query("SELECT user.name AS userName, book.name AS bookName " +
//            "FROM user, book " +
//            "WHERE user.id = book.user_id")
//    public LiveData<List<UserBook>> loadUserAndBookNames();
    @Query("SELECT * FROM users WHERE email = :email")
    public User getUser(String email);


    @Insert
    void insertOrder(UserOrderModel userOrder);


}
