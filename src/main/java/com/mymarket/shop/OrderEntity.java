package com.mymarket.shop;

import com.mymarket.membership.member.MemberEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    @OneToOne
    @JoinColumn(name = "shipment_id")
    private ShipmentEntity shipment;

    @OneToMany(mappedBy = "order",
        fetch = FetchType.EAGER,
        cascade = {CascadeType.ALL},
        orphanRemoval = true
    )
    private List<OrderItemEntity> items;

    private LocalDateTime dateCreated;
}
