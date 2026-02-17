package com.jdgorman.fooddeliveryapi.repository;

import com.jdgorman.fooddeliveryapi.entity.Delivery;
import com.jdgorman.fooddeliveryapi.enumerator.CustomerOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    Optional<Delivery> findByCustomerOrderId(Long customerOrderId);

    List<Delivery> findByDriverId(Long driverId);

    List<Delivery> findByDriverIdOrderByCreateTimestampDesc(Long driverId);

    List<Delivery> findByDriverIdAndStatus(Long driverId, CustomerOrderStatus status);

    boolean existsByCustomerOrderId(Long customerOrderId);
}