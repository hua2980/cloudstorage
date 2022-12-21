package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import org.springframework.boot.Banner;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CredentialController {
    private final CredentialService credentialService;

    public CredentialController(CredentialService credentialService) {
        this.credentialService = credentialService;
    }


    @PostMapping("/edit-credential")
    public String HandleAddCredential(@ModelAttribute("credential") Credential credential, Authentication authentication, Model model){
        String addError = null;
        // upload file
        if (addError == null) {
            int rowUpdated = -1;
            if (credential.getCredentialId() != null) {
                rowUpdated = credentialService.updateCredential(credential);
            } else {
                rowUpdated = credentialService.addCredential(credential, authentication.getName());
            }
            if (rowUpdated < 0) addError = "There was an error editing credential. Please try again.";
        }
        if (addError == null){
            model.addAttribute("success", true);
        } else {
            model.addAttribute("addError", addError);
        }
        return "result";
    }

    @RequestMapping(value = "/delete-credential", method = RequestMethod.GET)
    public String handleDeleteNote(@RequestParam(name = "credentialId") String credentialId_, Model model){
        Integer credentialId = Integer.parseInt(credentialId_);
        int success = credentialService.deleteCredentialById(credentialId);
        System.out.println(success);
        if (success == 1) {
            model.addAttribute("success", true);
        } else {
            model.addAttribute("deleteError", true);
        }
        return "result";
    }
}
