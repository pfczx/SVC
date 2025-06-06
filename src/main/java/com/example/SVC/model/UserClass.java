package com.example.SVC.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    private String password;

    @ToString.Exclude
    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Document> documents;




}
