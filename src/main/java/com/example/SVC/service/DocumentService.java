package com.example.SVC.service;

import com.example.SVC.model.Document;
import com.example.SVC.model.DocumentVersion;
import com.example.SVC.model.UserClass;
import com.example.SVC.repository.DocumentRepository;
import com.example.SVC.repository.UserRepository;
import com.example.SVC.repository.VersionRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final VersionRepository versionRepository;
    private final UserRepository appUserRepository;

    public DocumentService(DocumentRepository documentRepository, VersionRepository versionRepository, UserRepository appUserRepository) {
        this.documentRepository = documentRepository;
        this.versionRepository = versionRepository;
        this.appUserRepository = appUserRepository;
    }

    public void handleFileUpload(MultipartFile file, String title, Long userId) throws IOException {
        if (!file.getOriginalFilename().endsWith(".txt")) {
            throw new IllegalArgumentException("Only .txt files are supported.");
        }

        String content = new String(file.getBytes(), StandardCharsets.UTF_8);
        String fileName = file.getOriginalFilename();

        UserClass appUser = appUserRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Document document = documentRepository.findByTitle(title)
                .orElse(null);

        BigDecimal newVersionNumber;

        if (document == null) {
            document = new Document();
            document.setTitle(title);
            document.setFileName(fileName);
            document.setCreatedBy(appUser);
            document.setVersions(new ArrayList<>());
            newVersionNumber = new BigDecimal(0);
            document.setCreatedAt(LocalDateTime.now());
            documentRepository.save(document);
        } else {
            BigDecimal latestVersion = documentRepository.findNewestVersion(title);
            newVersionNumber = latestVersion.add(new BigDecimal("0.1"));
        }

        DocumentVersion version = new DocumentVersion();
        version.setContent(content);
        version.setVersion(newVersionNumber);
        version.setDocument(document);

        versionRepository.save(version);

        document.setCurrentVersion(version);
        document.getVersions().add(version);

        documentRepository.save(document);
    }


    public void deleteDocument(Long documentId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new IllegalArgumentException("Document not found"));

        versionRepository.deleteAll(document.getVersions());
        documentRepository.delete(document);
    }

    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    @Transactional
    public List<Document> getDocumentsBy(String userName, String sortBy, String direction, String filter, LocalDate createdAfter) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        List<Document> docs = documentRepository.findByCreatedBy_Name(userName, sort);

        return docs.stream()
                .filter(doc -> filter == null || doc.getTitle().toLowerCase().contains(filter.toLowerCase()))
                .filter(doc -> createdAfter == null || doc.getCreatedAt().toLocalDate().isAfter(createdAfter))
                .toList();
    }


    public void rollbackDocument(Long documentId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new IllegalArgumentException("Document not found"));

        DocumentVersion currentVersion = document.getCurrentVersion();
        if (currentVersion == null) {
            throw new IllegalArgumentException("Current version not found");
        }

        BigDecimal currentVersionNumber = currentVersion.getVersion();

        List<DocumentVersion> versions = versionRepository.findByDocument(document);

        DocumentVersion previousVersion = versions.stream()
                .filter(v -> v.getVersion().compareTo(currentVersionNumber) < 0)
                .max(Comparator.comparing(DocumentVersion::getVersion))
                .orElseThrow(() -> new IllegalArgumentException("No previous version available for rollback"));

        DocumentVersion rollbackedVersion = new DocumentVersion();
        rollbackedVersion.setContent(previousVersion.getContent());

        BigDecimal newVersionNumber = versionRepository.findNewestVersion(documentId).add(new BigDecimal("0.1"));
        rollbackedVersion.setVersion(newVersionNumber);
        rollbackedVersion.setDocument(document);

        versionRepository.save(rollbackedVersion);

        document.setCurrentVersion(rollbackedVersion);
        document.getVersions().add(rollbackedVersion);
        documentRepository.save(document);
    }



    public byte[] downloadDocument(Long documentId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new IllegalArgumentException("Document not found"));

        DocumentVersion currentVersion = document.getCurrentVersion();
        String content = currentVersion.getContent();

        return content.getBytes(StandardCharsets.UTF_8);
    }

    public String getDocumentTitle(Long documentId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new IllegalArgumentException("Document not found"));
        return document.getTitle();
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getStatistics(String username) {

        long documentCount = documentRepository.countDocumentsByCreatedBy(username);

        Long totalCharsLong = documentRepository.sumContentLengthsByCreatedBy(username);

        Double averageCharsDouble = documentRepository.avgContentLengthsByCreatedBy(username);
        double averageChars = (averageCharsDouble != null) ? averageCharsDouble : 0.0;

        long versionCount = documentRepository.countVersionsByCreatedBy(username);

        LocalDate oldest = documentRepository.findOldestDate(username);
        LocalDate newest = documentRepository.findNewestDate(username);

        Map<String, Object> stats = new HashMap<>();
        stats.put("documentCount", documentCount);
        stats.put("averageCharacters", String.format("%.2f", averageChars));
        stats.put("versionCount", versionCount);
        stats.put("oldestDate", oldest != null ? oldest.toString() : "N/A");
        stats.put("newestDate", newest != null ? newest.toString() : "N/A");

        return stats;
    }

}