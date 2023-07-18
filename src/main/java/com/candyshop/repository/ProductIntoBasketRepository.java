package com.candyshop.repository;

import com.candyshop.entity.ProductIntoBasket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductIntoBasketRepository extends JpaRepository<ProductIntoBasket, Long> {

    void deleteProductIntoBasketById(Long productId);

    Optional<ProductIntoBasket> findProductIntoBasketByName(String name);

    @Modifying
    @Query(value = """ 
            DELETE
            FROM products_into_basket
            WHERE product_id IN(SELECT product_id
                                FROM basket_product
                                WHERE basket_id = :basketId)
            """,
            nativeQuery = true)
    void deleteAllProductFromBasket(@Param("basketId") Long basketId);

}
