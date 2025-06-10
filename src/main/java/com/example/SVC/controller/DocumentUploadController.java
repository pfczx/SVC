package com.example.SVC.controller;

import com.example.SVC.security.UserDetailsImpl;
import com.example.SVC.service.DocumentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
public class DocumentUploadController {

    private final DocumentService documentService;

    @Transactional
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {


        Long userId = userDetails.getUser().getId();

        try {
            documentService.handleFileUpload(file, title, userId);
            return "redirect:/dashboard";
        } catch (Exception e) {
            return "redirect:/upload?error=" + e.getMessage();
        }
    }
}
