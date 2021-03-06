package com.example.repository;

import com.example.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional // (1) Transactionアノテーションがクラス・レベルに付いたクラスをDIコンテナから取得すると各メソッドが他のクラスから呼ばれた場合に自動的にDBトランザクション制御が行われる
public class CustomerRepository {
    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    private static final RowMapper<Customer> customerRowMapper = (rs, i) -> {
        Integer id = rs.getInt("id");
        String firstName = rs.getString("first_name");
        String lastName = rs.getString("last_name");
        return new Customer(id, firstName, lastName);
    };

    public List<Customer> findAll() {
        // NamedParameterJdbcTemplateのqueryメソッドを用いてSQL実行結果をJavaオブジェクトのリストとして取得
        List<Customer> customers = jdbcTemplate.query(
          "SELECT id, first_name, last_name FROM customers ORDER BY id"
                , customerRowMapper
        );
        return customers;
    }

    public Customer findOne(Integer id) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
        return jdbcTemplate.queryForObject(
          "SELECT id, first_name, last_name FROM customers WHERE id=:id"
                , param
                , customerRowMapper
        );
    }

    public Customer save(Customer customer) {
        // 更新用のSqlParameterSourceを作る
        SqlParameterSource param = new BeanPropertySqlParameterSource(customer);

        // BeanPropertySqlParameterSourceを使うことでJAVAオブジェクトのフィールド名と値をマッピングしたSqlParameterSourceが自動的に作成できる
        if (customer.getId() == null) {
            jdbcTemplate.update(
                    "INSERT INTO customers(first_name, last_name) values(:firstName, :lastName)"
                    , param
            );
        } else {
            jdbcTemplate.update(
                    "UPDATE customers SET first_name = :firstName, last_name = :lastName WHERE id = :id"
                    , param
            );
        }
        return customer;
    }

    public void delete(Integer id) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
        // (5)
        jdbcTemplate.update(
                "DELETE FROM customers WHERE id=:id"
                , param
        );
    }

}