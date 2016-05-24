package com.example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * Unit test for simple App.
 */
@RunWith(SpringJUnit4ClassRunner.class) // JUnitテスト内でSpring機能を使えるように
@SpringApplicationConfiguration(classes = App.class) // テスト用のApplicationContextを作る
@WebAppConfiguration // WEBアプリのテストである。SpringApplicationConfigurationと組み合わせることで「組み込みサーバ」を立ち上げることができる
@IntegrationTest("server.port:0") // 結合テスト機能を有効にする。value属性でテスト時に使うプロパティを上書きできる。ポート０指定で空いているポートを使用できる
public class AppTest {
    @Value("${local.server.port}") // ポート番号をインジェクションする
    int port;
    RestTemplate restTemplate = new TestRestTemplate(); // HTTPクライアントを用意する

    @Test
    public void testHome() {
        // RestTemplateのgetForEntityでHTTｐのGETに相当するリクエストを行う
        // 第２引数にレスポンスボティーを、第３引数にシリアライズする型を指定する
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port, String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK)); // ResponseEntityにHTTPレスポンスのステータスコードやヘッダー、ボディーが格納されている
        assertThat(response.getBody(), is("Hello world!"));
    }

}
