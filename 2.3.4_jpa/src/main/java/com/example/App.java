package com.example;

import com.example.domain.Customer;
import com.example.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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

        // ページング処理
        // Pageable インターフェイスで取得するページング情報を用意
        // 実装クラスとして、PageRequestクラスがある。コンストラクタの第１引数はページ数で、第２引数は１ページあたりの件数。
        Pageable pageable = new PageRequest (0, 3);
        Page<Customer> page = customerRepository.findAll(pageable); // 指定したページのCustomerデータを取得

        System.out.println("1ページあたりのデータ数=" + page.getSize());
        System.out.println("現在のページ=" + page.getNumber());
        System.out.println("全ページ数=" + page.getTotalPages());
        System.out.println("全データ数=" + page.getTotalElements());
        page.getContent().forEach(System.out::println); // (4)

    }

    public static void main(String args[]) {
        SpringApplication.run(App.class, args);
    }
}