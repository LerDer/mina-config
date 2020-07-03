package com.lww.mina;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author lww
 * @date 2020-07-03 01:21
 */
@SpringBootApplication
@EnableTransactionManagement
public class MinaConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(MinaConfigApplication.class);
    }

}
