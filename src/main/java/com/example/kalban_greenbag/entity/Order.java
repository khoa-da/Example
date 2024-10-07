package com.example.kalban_greenbag.entity;

import com.example.kalban_greenbag.constant.ConstStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "Orders")
public class Order extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID")
    private User userID;

    @Column(name = "OrderDate")
    private Instant orderDate;

    @Column(name = "TotalAmount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Nationalized
    @Lob
    @Column(name = "ShippingAddress")
    private String shippingAddress;

    @Column(name= "Reason")
    private String reason;

    @Column(name = "OrderStatus")
    private String orderStatus;

    @Column(name = "OrderCode", length = 10, unique = true)
    private Long orderCode;

    @OneToMany(mappedBy = "orderID")
    private Set<OrderItem> orderItems = new LinkedHashSet<>();

    @PrePersist
    public void prePersist() {
        this.orderStatus = ConstStatus.OrderStatus.ORDER_STATUS_PENDING;
    }

}