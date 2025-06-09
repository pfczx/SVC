package com.example.SVC.controller;

import com.example.SVC.model.Document;
import com.example.SVC.model.UserClass;
import com.example.SVC.repository.UserRepository;
import com.example.SVC.service.DocumentService;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DocumentService documentService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, DocumentService documentService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.documentService = documentService;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new UserClass());
        return "register";
    }

    @PostMapping("/register")
    public String processRegistration(@ModelAttribute UserClass user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication auth) {
        model.addAttribute("username", auth.getName());
        return "dashboard";
    }

    @GetMapping("/")
    public String home() {
        return "dashboard";
    }

    @GetMapping("/upload")
    public String uploadPage() {
        return "upload";
    }

    @GetMapping("/compare")
    public String comparePage() {
        return "compare";
    }

    @GetMapping("/browse")
    public String browsePage(
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) String filter, // filtr tekstowy
            Model model,
            Principal principal) {

        if (principal != null) {
            String username = principal.getName();
            List<Document> documents = documentService.getDocumentsBy(username, sortBy, direction, filter);
            model.addAttribute("documents", documents);
            model.addAttribute("sortBy", sortBy);
            model.addAttribute("direction", direction);
            model.addAttribute("filter", filter);
        }
        return "browse";
    }




    @GetMapping("/stats")
    public String statsPage(Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            Map<String, Object> stats = documentService.getStatistics(username);
            model.addAttribute("stats", stats);
        }
        return "stats";
    }
}
