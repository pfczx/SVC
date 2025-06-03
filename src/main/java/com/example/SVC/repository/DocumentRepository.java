package com.example.SVC.repository;

import com.example.SVC.model.AppUser;
import com.example.SVC.model.Document;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface DocumentRepository extends JpaRepository<Document, Long> {
    Optional<Document> findByTitle(String title);
    
    @Query("SELECT MAX(dv.version) FROM DocumentVersion dv WHERE dv.document.title = :title")
    BigDecimal findNewestVersion(@Param("title") String title);
    List<Document> findByCreatedBy(AppUser AppUser);
}