package com.BMS.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;


import java.util.ArrayList;
import java.util.List;

@Entity
@RequiredArgsConstructor
@Data
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String country;

    private String city;

    private String street;

    @Column(name = "building_name")
    private String building;

    @ManyToMany(mappedBy = "addresses")
    private List<User> users = new ArrayList<>();
}
