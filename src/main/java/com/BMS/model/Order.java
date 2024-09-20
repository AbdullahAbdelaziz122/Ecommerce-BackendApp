package com.BMS.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@RequiredArgsConstructor
@Table(name = "`order`")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    private String email;

    @Column(name = "order_date")
    private LocalDate date;

    @Column(name = "order_status")
    private String status;

    @Column(name = "total-amount")
    private double totalAmount;


    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<OrderItem> orderItems = new ArrayList<>();


    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;


}
