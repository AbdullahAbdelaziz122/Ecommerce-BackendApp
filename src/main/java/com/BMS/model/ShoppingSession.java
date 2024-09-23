package com.BMS.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Use IDENTITY for better compatibility
    private Long id;

    @Column(unique = true, nullable = false) // Ensure sessionId is unique and not null
    private String sessionId;

    @OneToOne(mappedBy = "shoppingSession", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Cart cart;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false) // Ensure lastActivity is not null
    private Date lastActivity;
}


