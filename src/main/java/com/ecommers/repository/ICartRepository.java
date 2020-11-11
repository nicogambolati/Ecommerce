package com.ecommers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommers.models.Cart;

@Repository
public interface ICartRepository extends JpaRepository<Cart, Long> {

}
