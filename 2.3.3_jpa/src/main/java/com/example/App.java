package com.example;

import com.example.domain.Customer;
import com.example.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@ComponentScan
public class App implements CommandLineRunner {
    @Autowired
    CustomerRepository customerRepository;

    @Override
    public void run(String... strings) throws Exception {
        // データ追加
        Customer created = customerRepository.save(new Customer(
                null,
                "Hidetochi",
                "Dekisugi"
        ));
        System.out.println(created + " is created!");

        // データ表示
        customerRepository.findAllOrderByName()
                .forEach(System.out::println);
    }

    public static void main(String args[]) {
        SpringApplication.run(App.class, args);
    }
}