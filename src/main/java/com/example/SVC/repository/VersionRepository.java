package com.example.SVC.repository;

import com.example.SVC.model.Document;
import com.example.SVC.model.DocumentVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;


public interface VersionRepository extends JpaRepository<DocumentVersion, Long> {


    Optional<DocumentVersion> findByVersionAndDocument(double version, Document document);
    List<DocumentVersion> findAllByDocumentId(Long documentId);
    Optional<DocumentVersion> findById(Long id);
   

}