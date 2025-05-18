package com.example.SVC.repository;

import com.example.SVC.model.DocumentVersion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VersionRepository extends JpaRepository<DocumentVersion, Long> {
}