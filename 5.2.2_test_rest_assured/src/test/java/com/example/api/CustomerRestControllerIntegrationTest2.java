package com.example.api;

import com.example.App;
import com.example.domain.Customer;
import com.example.repository.CustomerRepository;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Arrays;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = App.class)
@WebAppConfiguration
@IntegrationTest({"server.port:0",
        "spring.datasource.url:jdbc:h2:mem:bookmark;DB_CLOSE_ON_EXIT=FALSE"}) // テストの時だけインメモリのデータベースを使用するといった使い方ができる
public class CustomerRestControllerIntegrationTest2 {
    @Autowired
    CustomerRepository customerRepository; // customerRepositoryを使う
    @Value("${local.server.port}")
    int port;
    Customer customer1;
    Customer customer2;

    // テストの初期化。すべてのデータを削除した後にテスト用のデータを投入する
    @Before
    public void setUp() {
        customerRepository.deleteAll();
        customer1 = new Customer();
        customer1.setFirstName("Taro");
        customer1.setLastName("Yamada");
        customer2 = new Customer();
        customer2.setFirstName("Ichiro");
        customer2.setLastName("Suzuki");

        customerRepository.save(Arrays.asList(customer1, customer2));
        RestAssured.port = port;
    }

    // 全件取得APIテスト
    @Test
    public void testGetCustomers() throws Exception {
        when().get("/api/customers")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("numberOfElements", is(2)) // responseのjsonフィールドの値を確認
                .body("content[0].id", is(customer2.getId()))
                .body("content[0].firstName", is(customer2.getFirstName()))
                .body("content[0].lastName", is(customer2.getLastName()))
                .body("content[1].id", is(customer2.getId()))
                .body("content[1].firstName", is(customer2.getFirstName()))
                .body("content[1].lastName", is(customer2.getLastName()));
    }

    // 新規作成APIテスト
    @Test
    public void testPostCustomers() throws Exception {
        Customer customer3 = new Customer();
        customer3.setFirstName("Nobita");
        customer3.setLastName("Nobi");

        given().body(customer3) // リクエストボティ設定
                .contentType(ContentType.JSON)
                .and()
                .when().post("api/customers")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", is(notNullValue()))
                .body("firstName", is(customer3.getFirstName()))
                .body("lastName", is(customer3.getLastName()));

        when().get("/api/customers")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("numberOfElements", is(3));
    }

    // １件削除APIテスト
    @Test
    public void testDeleteCustomers() throws Exception {
        when().delete("/api/customers/{id}", customer1.getId())
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        when().get("api/customers")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("numberOfElements", is(1));
    }
}