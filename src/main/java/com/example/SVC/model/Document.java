package com.example.SVC.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Table(name="documents")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String fileName;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "createdby",nullable = false)
    private UserClass createdBy;

    @OneToOne
    @JoinColumn(name="currentversion",nullable = false)
    private DocumentVersion currentVersion;

    @ToString.Exclude
    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL)
    private List<DocumentVersion> versions;


}
