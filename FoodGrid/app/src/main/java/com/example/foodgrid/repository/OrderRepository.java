package com.example.foodgrid.repository;

import android.app.Application;
import android.os.AsyncTask;

import com.example.foodgrid.MyDatabase;
import com.example.foodgrid.dao.UserDAO;
import com.example.foodgrid.model.OrdersOfUser;
import com.example.foodgrid.model.UserOrderModel;

import java.util.Arrays;

public class OrderRepository {

    private UserDAO userDAO;


    public OrderRepository(Application application) {
        MyDatabase database = MyDatabase.getDatabase(application.getApplicationContext());
        this.userDAO = database.userDao();
    }

    public void insert(UserOrderModel userOrderModel) {
        new insertAsync(userDAO).execute(userOrderModel);
    }

    private static class insertAsync extends AsyncTask<UserOrderModel, Void, Void> {
        private UserDAO userDAOAsync;

        insertAsync(UserDAO userDAO) {
            this.userDAOAsync = userDAO;
        }


        @Override
        protected Void doInBackground(UserOrderModel... userOrderModels) {
            long userOrderId = 0;

//            for (UserOrderModel userOrderModel : userOrderModels) {
            userOrderModels[0].setUserOrderId(userOrderId);
//            }

            userDAOAsync.insertOrder(userOrderModels[0]);
            return null;

        }
    }
}
