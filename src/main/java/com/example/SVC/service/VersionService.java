package com.example.SVC.service;

import com.example.SVC.model.DocumentVersion;
import com.example.SVC.repository.VersionRepository;
import com.example.SVC.repository.DocumentRepository;
import com.example.SVC.model.Document;

import com.github.difflib.DiffUtils;
import com.github.difflib.patch.Patch;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.DeltaType;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.util.HtmlUtils;

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

        List<String> lines1 = List.of(version1.getContent().split("\n"));
        List<String> lines2 = List.of(version2.getContent().split("\n"));

        Patch<String> patch = DiffUtils.diff(lines1, lines2);
        List<AbstractDelta<String>> deltas = patch.getDeltas();

        StringBuilder result = new StringBuilder();
        int oldIndex = 0;
        int newIndex = 0;
        int deltaIndex = 0;

        while (newIndex < lines2.size() || oldIndex < lines1.size()) {
            if (deltaIndex < deltas.size() && oldIndex == deltas.get(deltaIndex).getSource().getPosition()) {
                AbstractDelta<String> delta = deltas.get(deltaIndex);
                List<String> removed = delta.getSource().getLines();
                List<String> added = delta.getTarget().getLines();

                for (String line : removed) {
                    result.append(toHtml(oldIndex + 1, "-", line, "diff-removed"));
                    oldIndex++;
                }

                for (String line : added) {
                    result.append(toHtml(newIndex + 1, "+", line, "diff-added"));
                    newIndex++;
                }

                deltaIndex++;
            } else if (newIndex < lines2.size() && oldIndex < lines1.size()
                    && lines1.get(oldIndex).equals(lines2.get(newIndex))) {
                result.append(toHtml(newIndex + 1, " ", lines2.get(newIndex), "diff-line"));
                oldIndex++;
                newIndex++;
            } else {
                if (oldIndex < lines1.size()) {
                    result.append(toHtml(oldIndex + 1, "-", lines1.get(oldIndex), "diff-removed"));
                    oldIndex++;
                }
                if (newIndex < lines2.size()) {
                    result.append(toHtml(newIndex + 1, "+", lines2.get(newIndex), "diff-added"));
                    newIndex++;
                }
            }
        }

        return result.toString();
    }

    private String toHtml(int lineNumber, String symbol, String line, String cssClass) {
        return String.format(
                "<div class=\"%s\"><code>%4d %s %s</code></div>",
                cssClass,
                lineNumber,
                symbol,
                StringEscapeUtils.escapeHtml4(line)
        );
    }



    public String displayContent(Long documentId, BigDecimal versionnum) {
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