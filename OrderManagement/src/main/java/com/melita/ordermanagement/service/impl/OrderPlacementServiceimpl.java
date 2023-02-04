package com.melita.ordermanagement.service.impl;

import com.melita.ordermanagement.model.dto.OrderDto;
import com.melita.ordermanagement.model.entity.Product;
import com.melita.ordermanagement.repository.ProductRepository;
import com.melita.ordermanagement.service.OrderPlacementService;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 * @author sorokus.dev@gmail.com
 */

@Service
public class OrderPlacementServiceimpl implements OrderPlacementService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${amqp.exchange.name}")
    private String exchangeName;

    @Value("${amqp.routing.key}")
    private String routingKey;

    @Override
    public List<Product> getAvailableProductsWithPackages() {
        return productRepository.findAllByOrderByIdAsc();
    }

    @Override
    public void placeOrder(OrderDto orderData) {
        rabbitTemplate.convertAndSend(exchangeName, routingKey, orderData);
    }
}