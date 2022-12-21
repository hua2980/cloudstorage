package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private UserService userService;
    private NoteMapper noteMapper;

    public NoteService(UserService userService, NoteMapper noteMapper) {
        this.userService = userService;
        this.noteMapper = noteMapper;
    }

    public List<Note> selectByUsername(String username){
        User user = userService.getUser(username);
        return noteMapper.selectByUserId(user.getUserId());
    }

    public int addNote(String username, Note note){
        User user = userService.getUser(username);
        note.setUserId(user.getUserId());
        return noteMapper.insertNote(note);
    }

    public int updateNote(Note note){
        return noteMapper.updateNote(note);
    }

    public int deleteByNoteId(Integer noteId){
        return noteMapper.deleteByNoteId(noteId);
    }



}
