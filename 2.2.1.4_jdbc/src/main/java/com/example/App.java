package com.example;

import com.example.domain.Customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

@EnableAutoConfiguration
public class App implements CommandLineRunner {
    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate; //DIコンテナに登録されたNamedParameterJdbcTemplateオブジェクト取得

    @Override
    public void run(String... strings) throws Exception {
        String sql = "SELECT id, first_name, last_name FROM customers WHERE id = :id"; // customerテーブルから主キーで情報取得
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", 1); // paramとして取得対象の主キーを設定

        // ここから変更
        Customer result = jdbcTemplate.queryForObject(sql, param,
                (rs, rowNum) -> new Customer(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"))

        ); // RowMapper<Customer>の匿名クラスの代わりに「(引数）→返り値」 形式のラムダを使う

        System.out.println("result = " + result);
    }

    public static void main(String args[]) {
        SpringApplication.run(App.class, args);
    }
}