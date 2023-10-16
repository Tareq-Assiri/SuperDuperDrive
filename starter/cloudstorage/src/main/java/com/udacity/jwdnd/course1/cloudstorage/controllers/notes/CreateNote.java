package com.udacity.jwdnd.course1.cloudstorage.controllers.notes;

import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/note")
public class CreateNote {
    private final NoteService noteService;
    private final UserService userService;

    public CreateNote(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @PostMapping
    public String postNote(Model model, Note note) {
        try {
            if (note.getNoteId() != null) {
                return "forward:/note/update";
            }
            User currentUser = userService.getUser(
                    SecurityContextHolder
                            .getContext()
                            .getAuthentication()
                            .getName());

            noteService.addNote(note, currentUser.getUserId());
            return "redirect:/result?success=1";
        } catch (Error e) {
            return "redirect:/result?success=0";
        }

    }
}
