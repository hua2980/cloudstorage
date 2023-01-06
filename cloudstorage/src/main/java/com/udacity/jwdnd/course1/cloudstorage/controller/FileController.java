package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Controller()
public class FileController {
    private final FileService fileService;
    private final UserService userService;


    public FileController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    @PostMapping("/file-upload")
    public String handleFileUpload(@RequestParam("fileUpload")MultipartFile fileUpload, Authentication authentication, Model model) throws IOException {
        // avoid empty
        // get fileName, contentType, file size and userId
        String username = authentication.getName();

        // check if file name existed
        String fileName = fileUpload.getOriginalFilename();
        String uploadError = null;
        Integer userId = userService.getUser(username).getUserId();
        if (fileService.selectByFilenameAndUserId(fileName, userId) != null) {
            uploadError = "The file name already exists.";
        }

        // upload file
        if (uploadError == null) {
            int rowAdded = fileService.uploadFile(fileUpload, username);
            if (rowAdded < 0) uploadError = "There was an error uploading file. Please try again.";
        }

        if (uploadError == null){
            model.addAttribute("success", true);
        } else {
            model.addAttribute("uploadError", uploadError);
        }
        return "result";
    }

    @RequestMapping(value = "/delete-file", method = RequestMethod.GET)
    public String handleDeleteFile(@RequestParam(name = "fileId") String fileId_, Model model){
        Integer fileId = Integer.parseInt(fileId_);
        int success = fileService.deleteFileByFileId(fileId);
        System.out.println(success);
        if (success == 1) {
            model.addAttribute("success", true);
            return "result";
        } else {
            model.addAttribute("deleteError", true);
            return "result";
        }

    }

    @RequestMapping(value = "/view-file", method = RequestMethod.GET)
    public void viewFileByFileId(@RequestParam(name = "fileId") String fileId_, HttpServletResponse resp) throws IOException {
        Integer fileId = Integer.parseInt(fileId_);
        File file = fileService.selectFileByFileId(fileId);
        if (file != null){
            // set response params
            resp.setContentType(file.getContentType());
            resp.setHeader("Content-Deposition", "attachment; fileName=" + file.getFileName());
//            resp.setContentLength(file.getFileSize());
            // output and copy
            try (OutputStream os = resp.getOutputStream()) {
                os.write(file.getFileData());
            }
        }
    }


}
