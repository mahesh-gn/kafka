package com.example.email_service.service;

import com.example.email_service.controller.EmailMessageController;
import com.example.email_service.model.EmailRequest;
import com.example.order_service.model.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderConsumer {
    private static final Logger LOGGER= LoggerFactory.getLogger(OrderConsumer.class);

    @Autowired
    private EmailMessageController emailMessageController;

    @KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(OrderEvent event){
        LOGGER.info(String.format("Order Event retrieved in Email service => %s", event.toString()));

        EmailRequest emailRequest=new EmailRequest();
        List<String> emailList = event.getEmail();
        emailRequest.setTo(emailList);
        emailRequest.setSubject("Order Details");
        emailRequest.setBody("Order Name : "+event.getOrder().getName()+"/nQuantity : "+event.getOrder().getQty()
        +"/nPrice : "+event.getOrder().getPrice()+"/nYour order with ID : "+event.getOrder().getOrderId()+" has been placed Successfully..."
        +"/n Thank You For Shopping through Flipkart ");

        System.out.println(emailMessageController.sendEmail(emailRequest));
    }
}