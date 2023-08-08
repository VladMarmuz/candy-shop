package com.candyshop.entity;

import com.candyshop.entity.enums.Balance;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "products")
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private Balance balance;

    @Column(name = "image")
    private String image;

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product product)) {
            return false;
        }
        return Objects.equals(getId(), product.getId())
                && Objects.equals(getName(), product.getName())
                && Objects.equals(getDescription(), product.getDescription())
                && Objects.equals(getPrice(), product.getPrice())
                && getBalance() == product.getBalance();
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getId(), getName(), getDescription(), getPrice(), getBalance());
    }

}
