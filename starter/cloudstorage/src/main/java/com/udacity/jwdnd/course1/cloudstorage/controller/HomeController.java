package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller()
public class HomeController {
    private final FileService fileService;
    private final NoteService noteService;

    private final CredentialService credentialService;

    public HomeController(FileService fileService, NoteService noteService, CredentialService credentialService) {
        this.fileService = fileService;
        this.noteService = noteService;
        this.credentialService = credentialService;
    }

    @GetMapping("/home-file")
    public String viewHome(Authentication authentication, Model model){
        String username = authentication.getName();
        List<File> files = fileService.selectFilesByUsername(username);
        model.addAttribute("files", files);
        List<Note> notes = noteService.selectByUsername(username);
        model.addAttribute("notes", notes);
        List<Credential> credentials = credentialService.selectByUsername(username);
        model.addAttribute("credentials", credentials);
        Map<Integer, String> credentialMap = credentialService.credentialMap(credentials);
        model.addAttribute("credentialMap", credentialMap);
        return "home";
    }

    @RequestMapping("/logout")
    public String logoutHome(){
        return "login";
    }
}
