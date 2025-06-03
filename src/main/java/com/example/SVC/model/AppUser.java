package com.example.SVC.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Table(name="users")
@ToString
@Setter
@Getter
//@NoArgsConstructor
//@AllArgsConstructor
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @ToString.Exclude
    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL)
    private List<Document> documents;



}