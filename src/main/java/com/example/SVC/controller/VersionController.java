package com.example.SVC.controller;

import com.example.SVC.model.DocumentVersion;
import com.example.SVC.service.VersionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/versions")
@RequiredArgsConstructor
public class VersionController {

    private final VersionService versionService;

    @GetMapping("/document/{documentId}")
    public ResponseEntity<List<DocumentVersion>> getAllVersionsByDocumentId(@PathVariable Long documentId) {
        List<DocumentVersion> versions = versionService.getAllVersionsByDocumentId(documentId);
        return ResponseEntity.ok(versions);
    }

    @GetMapping("/diff")
    public ResponseEntity<String> getDiffBetweenVersions(
            @RequestParam Long version1Id,
            @RequestParam Long version2Id) {
        String diff = versionService.generateDiff(version1Id, version2Id);
        return ResponseEntity.ok(diff);
    }

    @GetMapping("/{documentId}/{version}/content")
    public ResponseEntity<String> getVersionContent(@PathVariable Long documentId,@PathVariable BigDecimal version) {
        String content = versionService.displayContent(documentId,version);
        return ResponseEntity.ok(content);
    }
    @GetMapping("/currentversion/{documentId}")
    public ResponseEntity<String> getCurrentVersion(@PathVariable Long documentId) {
        String currentVersion = versionService.getCurrentVersion(documentId);
        return ResponseEntity.ok(currentVersion);
    }
    
}