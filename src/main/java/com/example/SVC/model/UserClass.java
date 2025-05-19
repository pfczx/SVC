package com.example.SVC.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Table(name="users")
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserClass {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String login;

    private String password;

    @ToString.Exclude
    @OneToMany
    private List<Document> documents;




}
