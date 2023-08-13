/* * Copyright 2023-2024 the original author or authors. * * TBD */

package com.stickyio.customerservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stickyio.customerservice.dao.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer getByEmailId(String emailId);

}
