package com.example.repository;

import com.example.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.annotation.PostConstruct;
import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Integer>{
}