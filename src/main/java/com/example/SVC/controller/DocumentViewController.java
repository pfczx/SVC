package com.example.SVC.controller;

import com.example.SVC.model.Document;
import com.example.SVC.model.UserClass;
import com.example.SVC.service.DocumentService;
import jakarta.transaction.Transactional;
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
    public String getUsersDocuments(Model model, UserClass user) {
        List<Document> documents = documentService.getDocumentsBy(user.getName());
        model.addAttribute("documents", documents);
        return "browse";
    }
}
