package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {
    @Select("SELECT * FROM FILES WHERE userid = #{userId}")
    List<File> selectByUserId(Integer userId);

    @Select("SELECT * FROM FILES WHERE fileId = #{fileId}")
    File selectByFileId(Integer fileId);

    @Select("SELECT * FROM FILES WHERE filename = #{fileName} AND userid = #{userId}")
    File selectByFilenameAndUserId(String fileName, Integer userId);

    @Delete("DELETE FROM FILES WHERE fileId = #{fileId}")
    int deleteFileByFileId(Integer fileId);

    @Insert("INSERT INTO FILES(filename, contentType, fileSize, userId, fileData) values(" +
            "#{fileName}," +
            "#{contentType}," +
            "#{fileSize}," +
            "#{userId}," +
            "#{fileData}" +
            ")")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insertFile(File file);
}
