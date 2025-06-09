package com.example.SVC.controller;

import com.example.SVC.model.Document;
import com.example.SVC.model.UserClass;
import com.example.SVC.service.DocumentService;
import jakarta.transaction.Transactional;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class DocumentViewController {

    private final DocumentService documentService;

    public DocumentViewController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping("/files")
    public String getSortedDocuments(@RequestParam(defaultValue = "id") String sortBy,
                                     @RequestParam(defaultValue = "asc") String direction,
                                     Model model,
                                     Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            List<Document> documents = documentService.getDocumentsBy(username, sortBy, direction);
            model.addAttribute("documents", documents);
            model.addAttribute("sortBy", sortBy);
            model.addAttribute("direction", direction);
        }
        return "browse";
    }


}
