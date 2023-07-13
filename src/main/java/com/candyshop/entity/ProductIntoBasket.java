package com.candyshop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(name = "products_into_basket")
public class ProductIntoBasket implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    private String name;

    private BigDecimal price;

    @Column(name = "number_into_basket")
    private int numberIntoBasket;

    @Column(name = "final_price")
    private BigDecimal finalPrice;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductIntoBasket that)) return false;
        return getNumberIntoBasket() == that.getNumberIntoBasket() && Objects.equals(getId(), that.getId())
                && Objects.equals(getName(), that.getName()) && Objects.equals(getPrice(), that.getPrice())
                && Objects.equals(getFinalPrice(), that.getFinalPrice());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getPrice(), getNumberIntoBasket(), getFinalPrice());
    }
}
