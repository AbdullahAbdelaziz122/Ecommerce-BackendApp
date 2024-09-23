package com.BMS.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.RequiredArgsConstructor;

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

    private String secretId;

    @NotBlank(message = "please provide a name")
    private String name;

    @NotBlank(message = "please provide an email")
    @Email(message = "please provide a valid email address")
    private String email;

    @NotBlank(message = "Please provide an address")
    private String address;

    @Pattern(regexp = "\\d{11}", message = "Phone number must be exactly 11 digits")
    private String phoneNo;

    private Double totalAmount = 0.0;

    private String orderStatus;

    @OneToMany(mappedBy = "order", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<OrderItem> orderItemsList = new ArrayList<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderRequest> orderRequests;
}
