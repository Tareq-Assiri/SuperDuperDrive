package com.udacity.jwdnd.course1.cloudstorage.controllers.files;


import com.udacity.jwdnd.course1.cloudstorage.models.File;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

@Controller
@RequestMapping("/file/delete")
public class DeleteController {
    private final UserService userService;
    private final FileService fileService;

    public DeleteController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    @GetMapping
    public String deleteController(@RequestParam("fileid") int fileId) {
        try {
            File file = fileService.getFileById(fileId);

            User currentUser = userService.getUser(
                    SecurityContextHolder
                            .getContext()
                            .getAuthentication()
                            .getName());

            if (!Objects.equals(currentUser.getUserId(), file.getUserId())) {
                return "redirect:/result?success=0";
            }
            fileService.deleteFile(fileId);


            return "redirect:/result?success=1";
        } catch (Exception e) {
            return "redirect:/result?success=0";
        }

    }
}
