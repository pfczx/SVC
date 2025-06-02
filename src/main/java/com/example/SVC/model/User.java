package com.example.SVC.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Table(name="users")
@ToString

@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Getter
    private String name;

    @Setter
    @Getter
    @Column(unique = true, nullable = false)
    private String login;

    @Setter
    @Getter
    @Column(nullable = false)
    private String password;

    @ToString.Exclude
    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL)
    private List<Document> documents;



}