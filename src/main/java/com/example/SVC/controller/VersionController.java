package com.example.SVC.controller;

import com.example.SVC.model.DocumentVersion;
import com.example.SVC.service.VersionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{versionId}/content")
    public ResponseEntity<String> getVersionContent(@PathVariable Long versionId) {
        String content = versionService.displayContent(versionId);
        return ResponseEntity.ok(content);
    }
}