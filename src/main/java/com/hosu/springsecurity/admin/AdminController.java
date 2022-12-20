package com.hosu.springsecurity.admin;

import com.hosu.springsecurity.note.Note;
import com.hosu.springsecurity.note.NoteService;
import com.hosu.springsecurity.user.User;
import com.hosu.springsecurity.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final NoteService noteService;
    private final UserRepository userRepository;
    /**
     * 어드민인 경우 노트 조회
     *
     * @return admin/index.html
     */
    @GetMapping
    public String getNoteForAdmin(Authentication authentication, Model model) {
        User user = userRepository.findByUsername(authentication.name());
        List<Note> notes = noteService.findByUser(user);
        model.addAttribute("notes", notes);
        return "admin/index";
    }
}
