package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {
    @Select("SELECT * FROM NOTES WHERE userid = #{userId}")
    List<Note> selectByUserId(Integer userId);

    @Update("UPDATE NOTES SET notetitle = #{noteTitle}, " +
            "notedescription = #{noteDescription} " +
            "WHERE noteid = #{noteId}")
    int updateNote(Note note);


    @Insert("INSERT INTO NOTES(notetitle, notedescription, userid) VALUES(" +
            "#{noteTitle}," +
            "#{noteDescription}," +
            "#{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    int insertNote(Note note);

    @Delete("DELETE FROM NOTES WHERE noteid = #{noteId}")
    int deleteByNoteId(Integer noteId);
}
