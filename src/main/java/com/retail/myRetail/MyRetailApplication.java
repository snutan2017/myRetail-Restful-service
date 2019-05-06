package com.retail.myRetail;

import com.retail.myRetail.repository.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * Class for entry point of app
 */

@SpringBootApplication
public class MyRetailApplication {

    @Autowired
    private PriceRepository priceRepository;

    public static void main(String[] args) {
        SpringApplication.run(MyRetailApplication.class, args);
    }

}
