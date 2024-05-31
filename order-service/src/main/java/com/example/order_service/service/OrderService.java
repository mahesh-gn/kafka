package com.example.order_service.service;

import com.example.order_service.model.Order;
import com.example.order_service.model.OrderEvent;
import com.example.order_service.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private KafkaProducer orderProducer;

    @Autowired
    private OrderRepository orderRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(String id) {
        return orderRepository.findById(id).orElse(null);
    }

    public Order createOrder(Order order) {
        order.setOrderId(order.getOrderId());
        OrderEvent orderEvent=new OrderEvent();
        orderEvent.setStatus("Pending");
        orderEvent.setMessage("Order is pending");
        List<String> email = order.getEmail();
        orderEvent.setEmail(email);
        orderEvent.setOrder(order);
        orderProducer.sendMessage(orderEvent);

        return orderRepository.save(order);
    }

    public Order updateOrder(String id, Order order) {
        if (orderRepository.existsById(id)) {
            order.setOrderId(id);
            return orderRepository.save(order);
        }
        return null;
    }

    public void deleteOrder(String id) {
        orderRepository.deleteById(id);
    }
}