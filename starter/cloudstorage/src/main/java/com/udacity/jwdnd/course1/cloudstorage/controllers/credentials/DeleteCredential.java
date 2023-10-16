package com.udacity.jwdnd.course1.cloudstorage.controllers.credentials;

import com.udacity.jwdnd.course1.cloudstorage.mappers.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

@Controller
@RequestMapping("/credential/delete")
public class DeleteCredential {


    private final CredentialService credentialService;
    private final UserService userService;

    public DeleteCredential(CredentialMapper credentialMapper, CredentialService credentialService, UserService userService) {
        this.credentialService = credentialService;
        this.userService = userService;
    }

    @GetMapping
    public String deleteCredential(@RequestParam("credid") Integer credentialId) {
        Credential credential = credentialService.getCredentialById(credentialId);

        User currentUser = userService.getUser(
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName());

        if (!Objects.equals(currentUser.getUserId(), credential.getUserid())) {
            return "redirect:/result?success=0";
        }
        try {
            credentialService.deleteCredential(credentialId);
            return "redirect:/result?success=1";
        } catch (Error e) {
            return "redirect:/result?success=0";
        }
    }

}
