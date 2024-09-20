package com.BMS.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @OneToOne(mappedBy = "payment", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Order order;


    @Column(name = "payement_method")
    private String paymentMethod;

}
