package com.udacity.jwdnd.course1.cloudstorage.controllers.files;

import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.sql.SQLException;

@Controller
@RequestMapping("/file/upload")
public class UploadController {
    private final FileService fileService;
    private final UserService userService;

    public UploadController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    @PostMapping
    public String homePost(@RequestParam("fileUpload") MultipartFile uploadedFile, Model model) throws SQLException, IOException {
        try {
            boolean isUploadSuccessful, isNoteSuccessful, isCredentialSuccessful = false;

            User currentUser = userService.getUser(
                    SecurityContextHolder
                            .getContext()
                            .getAuthentication()
                            .getName());

            fileService.uploadFile(uploadedFile, currentUser.getUserId());
        } catch (FileAlreadyExistsException e) {
            return "redirect:/result?success=0";
        } catch (FileUploadException e) {
            return "redirect:/result?success=0";
        } catch (Error e) {
            return "redirect:/result?success=0";
        }
        return "redirect:/result?success=1";
    }
}
