package com.candyshop.repository;

import com.candyshop.entity.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductOrderRepository
        extends JpaRepository<ProductOrder, Long> {
}
