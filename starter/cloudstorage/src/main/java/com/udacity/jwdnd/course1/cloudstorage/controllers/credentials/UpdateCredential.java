package com.udacity.jwdnd.course1.cloudstorage.controllers.credentials;

import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/credential/update")
public class UpdateCredential {
    private final CredentialService credentialService;

    public UpdateCredential(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    @PostMapping
    public String postUpdateCredential(Model model, Credential credential) {
        try {
            credentialService.updateCredential(credential);
            return "redirect:/result?success=1";
        } catch (Error e) {
            return "redirect:/result?success=0";
        }

    }

}
