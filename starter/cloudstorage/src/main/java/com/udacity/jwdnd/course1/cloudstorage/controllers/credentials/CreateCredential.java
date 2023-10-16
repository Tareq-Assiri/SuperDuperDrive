package com.udacity.jwdnd.course1.cloudstorage.controllers.credentials;

import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/credential")
public class CreateCredential {
    private final CredentialService credentialService;
    private final UserService userService;

    public CreateCredential(CredentialService credentialService, UserService userService) {
        this.credentialService = credentialService;
        this.userService = userService;
    }

    @PostMapping
    public String postCredential(Model model, Credential credential) {
        if (credential.getCredentialId() != null) {
            return "forward:/credential/update";
        }
        User currentUser = userService.getUser(
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName());
        try {
            credentialService.addCredential(credential, currentUser.getUserId());
            return "redirect:/result?success=1";
        } catch (Error e) {
            return "redirect:/result?success=0";
        }

    }
}
