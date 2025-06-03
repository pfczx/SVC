package com.example.SVC.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;


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
    private AppUser createdBy;

    @OneToOne
    @JoinColumn(name="currentversion")
    private DocumentVersion currentVersion;

    @ToString.Exclude
    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<DocumentVersion> versions;


}
