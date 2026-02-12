package com.jdgorman.fooddeliveryapi.repository;

import com.jdgorman.fooddeliveryapi.entity.CustomerOrder;
import com.jdgorman.fooddeliveryapi.enumerator.CustomerOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {

    List<CustomerOrder> findByCustomerId(Long customerId);

    List<CustomerOrder> findByRestaurantId(Long restaurantId);

    List<CustomerOrder> findByCustomerIdOrderByCreateTimestampDesc(Long customerId);

    List<CustomerOrder> findByRestaurantIdOrderByCreateTimestampDesc(Long restaurantId);

    List<CustomerOrder> findByStatus(CustomerOrderStatus status);

    List<CustomerOrder> findByRestaurantIdAndStatus(Long restaurantId, CustomerOrderStatus status);
}