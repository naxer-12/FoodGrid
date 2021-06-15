package com.example.foodgrid.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.example.foodgrid.MyDatabase;
import com.example.foodgrid.UserSession;
import com.example.foodgrid.dao.UserDAO;
import com.example.foodgrid.model.OrdersOfUser;
import com.example.foodgrid.model.UserOrderModel;

import java.util.Arrays;
import java.util.List;

public class OrderRepository {
    private static final String TAG = "ORDER REPOSITORY";

    private UserDAO userDAO;

    public UserSession userSession;

    public OrderRepository(Application application) {
        MyDatabase database = MyDatabase.getDatabase(application.getApplicationContext());
        userSession = new UserSession(application.getApplicationContext());
        this.userDAO = database.userDao();
    }

    public void insert(UserOrderModel userOrderModel) {
        long userOrderId = userSession.getUserId();
        Log.d(TAG, "user ORDER ID" + userOrderId);

//            for (UserOrderModel userOrderModel : userOrderModels) {
        userOrderModel.setUserOrderId(userOrderId);
//            }

        userDAO.upsert(userOrderModel);
//        new insertAsync(userDAO, userSession).execute(userOrderModel);
    }

    public void delete(UserOrderModel userOrderModel) {
        long userOrderId = userSession.getUserId();
        Log.d(TAG, "user ORDER ID" + userOrderId);
        userDAO.deleteOrder(userOrderId, userOrderModel.getOrder_id());

    }

    public List<UserOrderModel> getOrders(long userId) {
        return userDAO.getAllOrders(userId);
    }

//    private static class insertAsync extends AsyncTask<UserOrderModel, Void, Void> {
//        private UserDAO userDAOAsync;
//        private UserSession userSessionAsync;
//
//        insertAsync(UserDAO userDAO, UserSession userSession) {
//            this.userDAOAsync = userDAO;
//            this.userSessionAsync = userSession;
//        }
//
//
//        @Override
//        protected Void doInBackground(UserOrderModel... userOrderModels) {
//            long userOrderId = userSessionAsync.getUserId();
//            Log.d(TAG, "user ORDER ID" + userOrderId);
//
////            for (UserOrderModel userOrderModel : userOrderModels) {
//            userOrderModels[0].setUserOrderId(userOrderId);
////            }
//
//            userDAOAsync.insertOrder(userOrderModels[0]);
//            return null;
//
//        }
//    }
}
