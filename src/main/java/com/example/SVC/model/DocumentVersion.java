package com.example.SVC.model;

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

    @OneToOne
    @JoinColumn(name="document",nullable = false)
    private Document document;

    @Lob
    @Column(nullable = false)
    private String content;



    
}
