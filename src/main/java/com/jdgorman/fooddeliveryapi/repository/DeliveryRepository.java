package com.jdgorman.fooddeliveryapi.repository;

import com.jdgorman.fooddeliveryapi.entity.Delivery;
import com.jdgorman.fooddeliveryapi.enumerator.CustomerOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    @Query("SELECT d FROM Delivery d " +
            "JOIN FETCH d.customerOrder co " +
            "JOIN FETCH co.customer " +
            "JOIN FETCH co.restaurant " +
            "JOIN FETCH co.deliveryAddress " +
            "JOIN FETCH d.driver " +
            "WHERE d.id = :id")
    Optional<Delivery> findByIdWithAllAssociations(@Param("id") Long id);

    @Query("SELECT d FROM Delivery d " +
            "JOIN FETCH d.customerOrder co " +
            "JOIN FETCH co.customer " +
            "JOIN FETCH co.restaurant " +
            "JOIN FETCH co.deliveryAddress " +
            "JOIN FETCH d.driver " +
            "WHERE co.id = :customerOrderId")
    Optional<Delivery> findByCustomerOrderIdWithAllAssociations(@Param("customerOrderId") Long customerOrderId);

    Optional<Delivery> findByCustomerOrderId(Long customerOrderId);

    List<Delivery> findByDriverId(Long driverId);

    @Query("SELECT d FROM Delivery d " +
            "JOIN FETCH d.customerOrder co " +
            "JOIN FETCH co.customer " +
            "JOIN FETCH co.restaurant " +
            "JOIN FETCH co.deliveryAddress " +
            "JOIN FETCH d.driver " +
            "WHERE d.driver.id = :driverId " +
            "ORDER BY d.createTimestamp DESC")
    List<Delivery> findByDriverIdWithAllAssociationsOrderByCreateTimestampDesc(@Param("driverId") Long driverId);

    List<Delivery> findByDriverIdOrderByCreateTimestampDesc(Long driverId);

    List<Delivery> findByDriverIdAndStatus(Long driverId, CustomerOrderStatus status);

    boolean existsByCustomerOrderId(Long customerOrderId);
}