package com.example.SVC.controller;

import com.example.SVC.model.Document;
import com.example.SVC.model.UserClass;
import com.example.SVC.security.UserDetailsImpl;
import com.example.SVC.service.DocumentService;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable("id") Long documentId) {
        try {
            byte[] content = documentService.downloadDocument(documentId);
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
    public ResponseEntity<String> rollbackDocument(@PathVariable("id") Long documentId) {
        try {
            documentService.rollbackDocument(documentId);
            return ResponseEntity.ok("Rollback successful.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body("something went wrong " + e.getMessage());
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


