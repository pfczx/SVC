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
import org.springframework.data.domain.Sort;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    Optional<Document> findByTitle(String title);
    @Query("SELECT d.title FROM Document d")
    List<String> findAllTitles();

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

    List<Document> findByCreatedBy_Name(String userName, Sort sort);

    List<Document> findByCreatedBy_NameAndTitleContainingIgnoreCase(String userName, String titleFragment, Sort sort);

    @Query("SELECT d.title FROM Document d WHERE d.createdBy.name = :username")
    List<String> findTitlesByUsername(@Param("username") String username);

    @Query("SELECT d FROM Document d WHERE d.title = :title AND d.createdBy.name = :username")
    Optional<Document> findByTitleAndUsername(@Param("title") String title, @Param("username") String username);




}