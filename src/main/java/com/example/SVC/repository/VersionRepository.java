package com.example.SVC.repository;

import com.example.SVC.model.Document;
import com.example.SVC.model.DocumentVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.List;


public interface VersionRepository extends JpaRepository<DocumentVersion, Long> {


    Optional<DocumentVersion> findByVersionAndDocument(BigDecimal version, Document document);
    List<DocumentVersion> findAllByDocumentId(Long documentId);
    Optional<DocumentVersion> findById(Long id);
    @Query("SELECT MAX(d.version) FROM DocumentVersion d WHERE d.document.id = :documentId")
    BigDecimal findNewestVersion(@Param("documentId") Long documentId);
   

}