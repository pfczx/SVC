package com.example.SVC.controller;

import com.example.SVC.model.Document;
import com.example.SVC.model.DocumentVersion;
import com.example.SVC.repository.DocumentRepository;
import com.example.SVC.repository.VersionRepository;
import com.example.SVC.service.VersionService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class DocumentDiffController {

    private final DocumentRepository documentRepo;
    private final VersionRepository versionRepo;
    private final VersionService versionService;

    public DocumentDiffController(DocumentRepository documentRepo,
                                  VersionRepository versionRepo,
                                  VersionService versionService) {
        this.documentRepo = documentRepo;
        this.versionRepo = versionRepo;
        this.versionService = versionService;
    }

    @GetMapping("/compare")
    public String showDocumentTitleForm(Model model) {
        model.addAttribute("titles", documentRepo.findAllTitles()); // t
        return "compare";
    }
    @Transactional
    @PostMapping("/compare/select-versions")
    public String selectVersions(@RequestParam String title, Model model) {
        Optional<Document> doc = documentRepo.findByTitle(title);
        if (doc.isEmpty()) {
            model.addAttribute("errorMessage", "Nie znaleziono dokumentu o tytule: " + title);
            return "compare";
        }

        List<DocumentVersion> versions = versionRepo.findByDocument(doc.get());
        model.addAttribute("documentTitle", title);
        model.addAttribute("documentId", doc.get().getId());
        model.addAttribute("versions", versions);
        return "compare";
    }
    @Transactional
    @PostMapping("/compare/diff")
    public String compareVersions(@RequestParam Long version1Id,
                                  @RequestParam Long version2Id,
                                  Model model) {
        Optional<DocumentVersion> v1 = versionRepo.findById(version1Id);
        Optional<DocumentVersion> v2 = versionRepo.findById(version2Id);

        if (v1.isEmpty() || v2.isEmpty()) {
            model.addAttribute("errorMessage", "Nie znaleziono jednej z wersji.");
            return "compare";
        }

        String diffHtml = versionService.generateDiff(version1Id, version2Id);
        model.addAttribute("diffTitle", "Różnice między: " + v1.get().getVersion() + " i " + v2.get().getVersion());
        model.addAttribute("diffContent", diffHtml);

        return "compare";


    }


}