package com.candyshop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(name = "products_order")
public class ProductOrder implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "number_into_basket")
    private int numberIntoBasket;

    @Column(name = "final_price")
    private BigDecimal finalPrice;

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductOrder that)) {
            return false;
        }
        return getNumberIntoBasket() == that.getNumberIntoBasket()
                && Objects.equals(getId(), that.getId())
                && Objects.equals(getName(), that.getName())
                && Objects.equals(getPrice(), that.getPrice())
                && Objects.equals(getFinalPrice(), that.getFinalPrice());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getId(),
                getName(),
                getPrice(),
                getNumberIntoBasket(),
                getFinalPrice());
    }
}
