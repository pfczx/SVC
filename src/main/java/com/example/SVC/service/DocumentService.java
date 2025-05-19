package com.example.SVC.service;

import com.example.SVC.model.Document;
import com.example.SVC.model.DocumentVersion;
import com.example.SVC.model.UserClass;
import com.example.SVC.repository.DocumentRepository;
import com.example.SVC.repository.UserRepository;
import com.example.SVC.repository.VersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;



@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final VersionRepository versionRepository;
    private final UserRepository userRepository;

    public void handleFileUpload(MultipartFile file, String title, Long userId) throws IOException {
        if (!file.getOriginalFilename().endsWith(".txt")) {
            throw new IllegalArgumentException("Only .txt files are supported.");
        }

        String content = new String(file.getBytes(), StandardCharsets.UTF_8);
        String fileName = file.getOriginalFilename();

        UserClass user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Document document = documentRepository.findByTitle(title)
                .orElse(null);

        double newVersionNumber=0.0;

        if (document == null) {
            document = new Document();
            document.setTitle(title);
            document.setFileName(fileName);
            document.setCreatedBy(user);
            document.setVersions(new ArrayList<>());
            documentRepository.save(document);
        } else {
            newVersionNumber = documentRepository.findNewestVersion(title) + 0.1;
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

    public void rollbackDocument(Long documentId, double versionNumber) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new IllegalArgumentException("Document not found"));

        DocumentVersion versionToRollback = versionRepository.findByVersionAndDocument(versionNumber, document)
                .orElseThrow(() -> new IllegalArgumentException("Version not found"));

        document.setCurrentVersion(versionToRollback);
        documentRepository.save(document);

        List<DocumentVersion> versions = document.getVersions();
        for (DocumentVersion version : versions) {
            if (version.getVersion() > versionNumber) {
                versionRepository.delete(version);
            }
        }
        document.setVersions(new ArrayList<>(versions));
        documentRepository.save(document);

        
    }

    public byte[] downoladDocument(Long documentId) {
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


}