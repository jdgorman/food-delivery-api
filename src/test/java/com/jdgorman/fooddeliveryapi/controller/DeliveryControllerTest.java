package com.jdgorman.fooddeliveryapi.controller;

import com.jdgorman.fooddeliveryapi.dto.DeliveryRequest;
import com.jdgorman.fooddeliveryapi.dto.DeliveryResponse;
import com.jdgorman.fooddeliveryapi.service.DeliveryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class DeliveryControllerTest {

    @Mock
    private DeliveryService deliveryService;

    @InjectMocks
    private DeliveryController deliveryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createDeliverySuccessfully() {
        DeliveryRequest request = DeliveryRequest.builder()
                .orderId(1L)
                .driverId(1L)
                .build();

        DeliveryResponse response = DeliveryResponse.builder()
                .id(1L)
                .orderId(1L)
                .driverId(1L)
                .build();

        when(deliveryService.createDelivery(request)).thenReturn(response);

        ResponseEntity<DeliveryResponse> result = deliveryController.createDelivery(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void getDeliveryByIdSuccessfully() {
        DeliveryResponse response = DeliveryResponse.builder()
                .id(1L)
                .orderId(1L)
                .driverId(1L)
                .build();

        when(deliveryService.getDeliveryById(1L)).thenReturn(response);

        ResponseEntity<DeliveryResponse> result = deliveryController.getDeliveryById(1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void getDeliveryByOrderIdSuccessfully() {
        DeliveryResponse response = DeliveryResponse.builder()
                .id(1L)
                .orderId(1L)
                .driverId(1L)
                .build();

        when(deliveryService.getDeliveryByOrderId(1L)).thenReturn(response);

        ResponseEntity<DeliveryResponse> result = deliveryController.getDeliveryByOrderId(1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void getDeliveriesByDriverSuccessfully() {
        DeliveryResponse response = DeliveryResponse.builder()
                .id(1L)
                .orderId(1L)
                .driverId(1L)
                .build();

        when(deliveryService.getDeliveriesByDriver(1L)).thenReturn(Collections.singletonList(response));

        ResponseEntity<List<DeliveryResponse>> result = deliveryController.getDeliveriesByDriver(1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());
        assertEquals(response, result.getBody().get(0));
    }

    @Test
    void markAsPickedUpSuccessfully() {
        DeliveryResponse response = DeliveryResponse.builder()
                .id(1L)
                .orderId(1L)
                .driverId(1L)
                .build();

        when(deliveryService.markAsPickedUp(1L)).thenReturn(response);

        ResponseEntity<DeliveryResponse> result = deliveryController.markAsPickedUp(1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void markAsDeliveredSuccessfully() {
        DeliveryResponse response = DeliveryResponse.builder()
                .id(1L)
                .orderId(1L)
                .driverId(1L)
                .build();

        when(deliveryService.markAsDelivered(1L)).thenReturn(response);

        ResponseEntity<DeliveryResponse> result = deliveryController.markAsDelivered(1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }
}
