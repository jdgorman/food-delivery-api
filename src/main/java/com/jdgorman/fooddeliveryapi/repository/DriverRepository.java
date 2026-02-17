package com.jdgorman.fooddeliveryapi.repository;

import com.jdgorman.fooddeliveryapi.entity.Driver;
import com.jdgorman.fooddeliveryapi.enumerator.DriverStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    boolean existsByEmail(String email);

    Optional<Driver> findByEmail(String email);

    List<Driver> findByStatus(DriverStatus status);

    @Query("SELECT COUNT(d) FROM Delivery d WHERE d.driver.id = :driverId")
    long countTotalDeliveriesByDriverId(@Param("driverId") Long driverId);

    @Query("SELECT COUNT(d) FROM Delivery d WHERE d.driver.id = :driverId " +
            "AND d.status NOT IN ('DELIVERED', 'CANCELLED')")
    long countActiveDeliveriesByDriverId(@Param("driverId") Long driverId);
}