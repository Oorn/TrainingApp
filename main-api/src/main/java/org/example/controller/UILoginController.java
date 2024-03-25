package org.example.controller;

import lombok.Setter;
import org.example.security.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/UI-login")
@CrossOrigin
public class UILoginController {
    @Setter(onMethod_={@Autowired})
    private JWTUtils jwtUtils;

    @GetMapping
    public String landingPage() {
        return "landing";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/content")
    @PostMapping("/content")
    public String getContentPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        String token = jwtUtils.generateToken(user);
        String authority = user.getAuthorities().stream().reduce("",(s,ga)->s+" "+ga.getAuthority(), (s1,s2)->s1+" "+s2);

        model.addAttribute("authority", authority);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("content", token);
        return "content";
    }


}
