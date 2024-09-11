package com.blueOcean.humanResourceSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
public class HumanResourceSystemApplication {

    public static void main(String[] args) {

//        Dotenv dotenv = Dotenv.load();
//
//		ConfigurableApplicationContext context=
SpringApplication.run(HumanResourceSystemApplication.class, args);
//		 Accessing the bean to print database configuration

//		MyComponent myComponent = context.getBean(MyComponent.class);
//		myComponent.printDatabaseConfig();
//        MyComponent myComponent= new MyComponent();
//        myComponent.printDatabaseConfig();
    }
}
