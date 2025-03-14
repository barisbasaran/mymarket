package com.mymarket.product;

import com.mymarket.productimage.ProductImageEntity;
import com.mymarket.productcategory.ProductCategoryEntity;
import com.mymarket.store.StoreEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_category_id")
    private ProductCategoryEntity productCategory;

    private boolean active;

    private String name;

    private String description;

    @OneToMany(mappedBy = "product",
        fetch = FetchType.EAGER,
        cascade = {CascadeType.ALL},
        orphanRemoval = true
    )
    private List<ProductImageEntity> images;

    @Column(columnDefinition = "Decimal(10,2)")
    private BigDecimal priceAmount;

    private Currency priceCurrency;

    private String specs;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private StoreEntity store;
}
