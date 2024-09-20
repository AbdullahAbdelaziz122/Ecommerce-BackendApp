//package com.BMS.model;
//
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.RequiredArgsConstructor;
//import lombok.Setter;
//
//import java.util.HashSet;
//import java.util.Set;
// // - todo: For now this is not needed for this application but it will be added in a future update .
//@Entity
//@Table(name = "role")
//@RequiredArgsConstructor
//@Getter
//@Setter
//public class Role {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "role_id")
//    private Long Id;
//
//    @Column(name = "role_name")
//    private String name;
//
//    @ManyToMany(mappedBy = "roles")
//    private Set<User> users = new HashSet<>();
//
//}
