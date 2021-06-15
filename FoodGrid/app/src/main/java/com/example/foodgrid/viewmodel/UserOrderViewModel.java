package com.example.foodgrid.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.example.foodgrid.model.UserOrderModel;
import com.example.foodgrid.repository.OrderRepository;

import java.util.List;

public class UserOrderViewModel extends AndroidViewModel {


    private OrderRepository orderRepository;

    public UserOrderViewModel(Application application) {
        super(application);
        orderRepository = new OrderRepository(application);

    }

    public void insertOrderOfUser(UserOrderModel userOrder) {

        this.orderRepository.insert(userOrder);
    }
    public void deleteOrderOfUser(UserOrderModel userOrder) {

        this.orderRepository.delete(userOrder);
    }
    public List<UserOrderModel> getAllOrder(long userId) {
        return this.orderRepository.getOrders(userId);
    }
}
