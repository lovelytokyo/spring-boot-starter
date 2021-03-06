package com.example.api;

import com.example.App;
import com.example.domain.Customer;
import com.example.repository.CustomerRepository;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = App.class)
@WebAppConfiguration
@IntegrationTest({"server.port:0",
        "spring.datasource.url:jdbc:h2:mem:bookmark;DB_CLOSE_ON_EXIT=FALSE"}) // テストの時だけインメモリのデータベースを使用するといった使い方ができる
public class CustomerRestControllerIntegrationTest {
    @Autowired
    CustomerRepository customerRepository; // customerRepositoryを使う
    @Value("${local.server.port}")
    int port;
    String apiEndpoint;
    RestTemplate restTemplate = new TestRestTemplate();
    Customer customer1;
    Customer customer2;

    // 全件取得するAPIの返り値は、org.springframework.data.domain.Page型であるがこのクラスはRestTemplateで取得できない
    // そのため、テスト用にレスポンスのJSONをマッピングさせるJavaクラスを用意する。
    // 後述のテストで使うフィールド「content」「numberOfElements」のみ定義する
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true) // レスポンスのJSONには、存在する他のフィールドを無視する
    static class Page<T> {
        private List<T> content;
        private int numberOfElements;
    }

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
        apiEndpoint = "http://localhost:" + port + "/api/customers";
    }

    // 全件取得APIテスト
    @Test
    public void testGetCustomers() throws Exception {
        ResponseEntity<Page<Customer>> response = restTemplate.exchange(apiEndpoint,
                HttpMethod.GET,
                null /* body,header */,
                new ParameterizedTypeReference<Page<Customer>>() {
                }); // RestTemplateの汎用リクエストメソッでであるexchangeを使ってテスト用のリクエストを送る。返り値の型がジェネリックスの場合、ParameterizedTypeReferenceを使って型を特定する

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getNumberOfElements(), is(2));

        Customer c1 = response.getBody().getContent().get(0);
        assertThat(c1.getId(), is(customer2.getId()));
        assertThat(c1.getFirstName(), is(customer2.getFirstName()));
        assertThat(c1.getLastName(), is(customer2.getLastName()));

        Customer c2 = response.getBody().getContent().get(1);
        assertThat(c2.getId(), is(customer1.getId()));
        assertThat(c2.getFirstName(), is(customer1.getFirstName()));
        assertThat(c2.getLastName(), is(customer1.getLastName()));
    }

    // 新規作成APIテスト
    @Test
    public void testPostCustomers() throws Exception {
        Customer customer3 = new Customer();
        customer3.setFirstName("Nobita");
        customer3.setLastName("Nobi");

        ResponseEntity<Customer> response = restTemplate.exchange(apiEndpoint,
                HttpMethod.POST,
                new HttpEntity<>(customer3) /* リクエストボティを作る */,
                Customer.class);

        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        Customer customer = response.getBody();
        assertThat(customer.getId(), is(notNullValue()));
        assertThat(customer.getFirstName(), is(customer3.getFirstName()));
        assertThat(customer.getLastName(), is(customer3.getLastName()));

        assertThat(
                restTemplate
                        .exchange(
                                apiEndpoint,
                                HttpMethod.GET,
                                null,
                                new ParameterizedTypeReference<Page<Customer>>() {
                                }).getBody().getNumberOfElements(), is(3));
    }

    // １件削除APIテスト
    @Test
    public void testDeleteCustomers() throws Exception {
        ResponseEntity<Void> response = restTemplate.exchange(apiEndpoint
                        + "/{id}" /* パス中のパラメータはプレーすホールだを使って埋められる */,
                HttpMethod.DELETE,
                null /* body,header */,
                Void.class,
                Collections.singletonMap("id", customer1.getId()));

        assertThat(response.getStatusCode(), is(HttpStatus.NO_CONTENT));

        assertThat(
                restTemplate
                        .exchange(
                                apiEndpoint,
                                HttpMethod.GET,
                                null,
                                new ParameterizedTypeReference<Page<Customer>>() {
                                }).getBody().getNumberOfElements(), is(1));
    }
}