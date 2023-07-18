package com.candyshop.repository;

import com.candyshop.entity.Basket;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BasketRepository extends JpaRepository<Basket, Long> {

    Basket findBasketByUserId(Long userId);

    @Query(value = """
            SELECT b
            FROM Basket b
            WHERE b.id IN(SELECT b.id
                          FROM ProductIntoBasket p 
                          WHERE p.id = :productId )           
            """)
    Basket findBasketByProductId(@Param("productId") Long productId);


}
