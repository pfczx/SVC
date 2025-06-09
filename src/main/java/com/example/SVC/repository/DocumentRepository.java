package com.example.SVC.repository;

import com.example.SVC.model.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.example.SVC.model.UserClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface DocumentRepository extends JpaRepository<Document, Long> {
    Optional<Document> findByTitle(String title);


    @Query("SELECT MAX(dv.version) FROM DocumentVersion dv WHERE dv.document.title = :title")
    BigDecimal findNewestVersion(@Param("title") String title);

    @Query("SELECT COUNT(d) FROM Document d WHERE d.createdBy.name = :username")
    long countDocumentsByCreatedBy(@Param("username") String username);

    @Query("SELECT SUM(LENGTH(dv.content)) FROM Document d JOIN d.versions dv WHERE d.createdBy.name = :username")
    Long sumContentLengthsByCreatedBy(@Param("username") String username);

    @Query("SELECT AVG(LENGTH(dv.content)) FROM Document d JOIN d.versions dv WHERE d.createdBy.name = :username")
    Double avgContentLengthsByCreatedBy(@Param("username") String username);

    @Query("SELECT COUNT(dv) FROM Document d JOIN d.versions dv WHERE d.createdBy.name = :username")
    long countVersionsByCreatedBy(@Param("username") String username);

    @Query("SELECT MIN(d.createdAt) FROM Document d WHERE d.createdBy.name = :username")
    LocalDate findOldestDate(String username);

    @Query("SELECT MAX(d.createdAt) FROM Document d WHERE d.createdBy.name= :username")
    LocalDate findNewestDate(String username);

    @Query("SELECT d FROM Document d WHERE d.createdBy.name = :username")
    List<Document> findAllByCreatedByUsername(@Param("username") String username);





}