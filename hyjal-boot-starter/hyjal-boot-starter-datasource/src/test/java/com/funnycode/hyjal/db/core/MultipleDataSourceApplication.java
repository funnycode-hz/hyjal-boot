package com.funnycode.hyjal.db.core;

import com.funnycode.hyjal.db.annotation.EnableHyjalDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author tc
 * @date 2019-03-24
 */
@EnableHyjalDataSource
@SpringBootApplication(scanBasePackages = "com.funnycode.hyjal.db")
public class MultipleDataSourceApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(MultipleDataSourceApplication.class, args);
    }

}
