package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class FileService {
    private UserService userService;
    private FileMapper fileMapper;

    public FileService(UserService userService, FileMapper fileMapper) {
        this.userService = userService;
        this.fileMapper = fileMapper;
    }

    public List<File> selectFilesByUsername(String username){
        User user = userService.getUser(username);
        return fileMapper.selectByUserId(user.getUserId());
    }

    public File selectFileByFileId(Integer fileId){
        return fileMapper.selectByFileId(fileId);
    }

    public File selectByFilenameAndUserId(String fileName, Integer userId){
        return fileMapper.selectByFilenameAndUserId(fileName, userId);
    }

    public int deleteFileByFileId(Integer fileId){
        File file = fileMapper.selectByFileId(fileId);
        // if the file exists
        if (file != null) return fileMapper.deleteFileByFileId(fileId);
        else return Integer.MIN_VALUE;
    }

    public int uploadFile(MultipartFile fileUpload, String username) throws IOException {
        // MultipartFile to inputStream
        InputStream fis = fileUpload.getInputStream();
        // input stream to byte array
        byte[] in2b = input2byte(fis);
        fis.close();

        // get fileName, contentType, file size and userId
        User user = userService.getUser(username);

        // create File object
        File file = new File(fileUpload.getOriginalFilename(),
                fileUpload.getContentType(),
                fileUpload.getSize(),
                user.getUserId(), in2b);

        return fileMapper.insertFile(file);
    }

    private byte[] input2byte(InputStream fis) throws IOException {
        // inputStream to byte array
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        byte[] buff = new byte[32];
        int n = 0;
        n = fis.read(buff);
        while(n != -1) {
            bStream.write(buff);
            n = fis.read(buff);
        }
        byte[] in2b = bStream.toByteArray();

        return in2b;
    }

}
