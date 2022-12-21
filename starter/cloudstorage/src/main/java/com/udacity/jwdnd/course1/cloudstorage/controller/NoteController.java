package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class NoteController {
    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }


    @PostMapping("/note-submit")
    public String HandleAddNote(@ModelAttribute("note") Note note, Authentication authentication, Model model){
        String addError = null;
        // upload file
        if (addError == null) {
            int rowUpdated = -1;
            if (note.getNoteId() != null) {
                rowUpdated = noteService.updateNote(note);
            } else {
                rowUpdated = noteService.addNote(authentication.getName(), note);
            }
            if (rowUpdated < 0) addError = "There was an error editing note. Please try again.";
        }
        if (addError == null){
            model.addAttribute("success", true);
        } else {
            model.addAttribute("addError", addError);
        }
        return "result";
    }

    @RequestMapping(value = "/delete-note", method = RequestMethod.GET)
    public String handleDeleteNote(@RequestParam(name = "noteId") String noteId_, Model model){
        Integer noteId = Integer.parseInt(noteId_);
        int success = noteService.deleteByNoteId(noteId);
        System.out.println(success);
        if (success == 1) {
            model.addAttribute("success", true);
        } else {
            model.addAttribute("deleteError", true);
        }
        return "result";
    }


}
