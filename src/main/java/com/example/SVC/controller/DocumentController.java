package com.example.SVC.controller;

import com.example.SVC.service.DocumentService;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(
        @RequestParam("file") MultipartFile file,
        @RequestParam("title") String title,
        @RequestParam("userId") Long userId) {

        try {
            documentService.handleFileUpload(file, title, userId);
            return ResponseEntity.ok("File uploaded successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(999).body("Something went wrong "+e.getMessage());
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable("id") Long documentId) {
        try {
            byte[] content = documentService.downoladDocument(documentId);
            String title = documentService.getDocumentTitle(documentId);
        
            String fileName = title + ".txt";

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(content);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
}

    @PostMapping("/{id}/rollback")
    public ResponseEntity<String> rollbackDocument(
            @PathVariable("id") Long documentId,
            @RequestParam("version") double versionNumber) {

        try {
            documentService.rollbackDocument(documentId, versionNumber);
            return ResponseEntity.ok("Rollback successful.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(999).body("something went wrong "+e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDocument(@PathVariable("id") Long documentId) {
        try {
            documentService.deleteDocument(documentId);
            return ResponseEntity.ok("Document deleted.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(999).body("something went wrong  "+e.getMessage());
        }
    }
}