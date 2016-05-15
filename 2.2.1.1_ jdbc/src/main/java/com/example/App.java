package com.example;

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
        String sql = "SELECT 1";
        SqlParameterSource param = new MapSqlParameterSource(); // SqlParameterSourceクラスでSQLに踏めこむパラメータを作る
        // 引数１：sql、引数2 : パラメータ、引数3 : 返り値となるオブジェクトのクラス
        // 1件じゃない場合は、IncorrectResultSizeDataAccessExceptionがスローされる
        Integer result = jdbcTemplate.queryForObject(sql, param, Integer.class); // NamedParameterJdbctemplateのqueryForObjectメソッドを使ってクエリの実行結果をオブジェクトに変換して取得

        System.out.println("result = " + result);
    }

    public static void main(String args[]) {
        SpringApplication.run(App.class, args);
    }
}