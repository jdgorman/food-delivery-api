package com.jdgorman.fooddeliveryapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class FoodDeliveryApiApplicationTests {

    @Test
    void applicationContextLoadsSuccessfully() {
        ConfigurableApplicationContext context = SpringApplication.run(FoodDeliveryApiApplication.class);
        assertNotNull(context);
        context.close();
    }

}
