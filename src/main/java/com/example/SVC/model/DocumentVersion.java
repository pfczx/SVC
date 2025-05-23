package com.example.SVC.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.*;

@Entity
@Table(name="versions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DocumentVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private Double version;

    @ManyToOne
    @JoinColumn(name="document")
    @JsonBackReference
    private Document document;

    @Lob
    @Column(nullable = false)
    private String content;



    
}
