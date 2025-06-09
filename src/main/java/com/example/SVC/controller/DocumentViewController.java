package com.example.SVC.controller;

import com.example.SVC.model.Document;
import com.example.SVC.service.DocumentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DocumentViewController {

    private final DocumentService documentService;

    public DocumentViewController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping("/files")
    public String getAllDocuments(Model model) {
        List<Document> documents = documentService.getAllDocuments();
        model.addAttribute("documents", documents);
        return "browse";
    }
}
