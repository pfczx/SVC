package com.example.SVC.service;

import com.example.SVC.model.DocumentVersion;
import com.example.SVC.repository.VersionRepository;
import com.example.SVC.repository.DocumentRepository;
import com.example.SVC.model.Document;

import com.github.difflib.DiffUtils;
import com.github.difflib.patch.Patch;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.DeltaType;

import java.util.List;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VersionService {
    private final VersionRepository versionRepository;     
    private final DocumentRepository documentRepository;      


    public List<DocumentVersion> getAllVersionsByDocumentId(Long documentId) {
        return versionRepository.findAllByDocumentId(documentId);
    }

    public String generateDiff(Long version1Id, Long version2Id) {
        DocumentVersion version1 = versionRepository.findById(version1Id)
                .orElseThrow(() -> new IllegalArgumentException("First version not found"));
        DocumentVersion version2 = versionRepository.findById(version2Id)
                .orElseThrow(() -> new IllegalArgumentException("Second version not found"));

        String content1 = version1.getContent();
        String content2 = version2.getContent();

        List<String> lines1 = List.of(content1.split("\n"));
        List<String> lines2 = List.of(content2.split("\n"));

        Patch<String> patch = DiffUtils.diff(lines1, lines2);

        StringBuilder diffResult = new StringBuilder();


        for (AbstractDelta<String> delta : patch.getDeltas()) {
            DeltaType type = delta.getType();
            int origPos = delta.getSource().getPosition() + 1;
            int revPos = delta.getTarget().getPosition() + 1;

            List<String> originalLines = delta.getSource().getLines();
            List<String> revisedLines = delta.getTarget().getLines();

            if (type == DeltaType.DELETE) {
                for (int i = 0; i < originalLines.size(); i++) {
                    diffResult.append(String.format("%3d - %s\n", origPos + i, originalLines.get(i)));
                }
            } else if (type == DeltaType.INSERT) {
                for (int i = 0; i < revisedLines.size(); i++) {
                    diffResult.append(String.format("%3d + %s\n", revPos + i, revisedLines.get(i)));
                }
            } else if (type == DeltaType.CHANGE) {
                for (int i = 0; i < originalLines.size(); i++) {
                    diffResult.append(String.format("%3d - %s\n", origPos + i, originalLines.get(i)));
                }
                for (int i = 0; i < revisedLines.size(); i++) {
                    diffResult.append(String.format("%3d + %s\n", revPos + i, revisedLines.get(i)));
                }
            }
        }


        return diffResult.toString();
    }

    public String displayContent(Long documentId,Double versionnum) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new IllegalArgumentException("Document not found"));
        DocumentVersion version = versionRepository.findByVersionAndDocument(versionnum, document)
                .orElseThrow(() -> new IllegalArgumentException("Version not found"));
        return version.getContent();
            
    }
    public String getCurrentVersion(Long documentId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new IllegalArgumentException("Document not found"));
        DocumentVersion currentVersion = document.getCurrentVersion();
        return currentVersion.getContent();
    }

  
}
