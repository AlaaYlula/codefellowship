package com.example.CodeFellowship.Controllers;

import com.example.CodeFellowship.Model.AppUser;
import com.example.CodeFellowship.Repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Controller
public class AppUserController {

    private final AppUserRepository appUserRepository;

    public AppUserController(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }
    @Autowired
    PasswordEncoder encoder;

    //////////////////////////////////// Log In ////////////////////////////////
    @GetMapping("/login")
    public String getLoginPage(Model model){
        final String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        if(currentUser.equals("anonymousUser") ){
            String user= "";
            model.addAttribute("username", user);
        }else{
            model.addAttribute("username", currentUser);
        }
        return "login";
    }

    /////////////////////////////////// Sign Up /////////////////////////////////////
    @GetMapping("/signup")
    public String getSignupPage(Model model) {
        final String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        if(currentUser.equals("anonymousUser") ){
            String user= "";
            model.addAttribute("username", user);
        }else{
            model.addAttribute("username", currentUser);
        }
        return "signup";
    }

    @PostMapping("/signup")
    public String signupUser(@RequestParam String username, @RequestParam String password,
                             @RequestParam String firstName , @RequestParam String lastName ,
                             @RequestParam String  dateOfBirth , @RequestParam String bio, Model model
                        , HttpServletRequest request
    ){
        AppUser appuser = new AppUser(username,firstName,lastName,dateOfBirth,bio,encoder.encode(password));
        appUserRepository.save(appuser);
        //Read : https://www.codegrepper.com/code-examples/java/frameworks/spring/spring+security+auto+login+after+register
        try {
            request.login(username, password);
            final String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
            request.setAttribute("username",currentUser);

            AppUser appUser = appUserRepository.findByUsername(currentUser);
            model.addAttribute("postsList",appUser.getPosts());
            model.addAttribute("userInfo",appUser);

        } catch (ServletException e) {
           // LOGGER.error("Error while login ", e);
        }
        return "profile"; // change this from login to profile //
    }
    ////////////////////////////////// Home Page /////////////////////////////////////
    @GetMapping("/")
    public String getHomePage(Model model){
        final String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        if(currentUser.equals("anonymousUser") ){
            String user= "";
            model.addAttribute("username", user);
        }else{
            model.addAttribute("username", currentUser);
        }
        return "home";
    }
    ////////////////////////////////// my profile Page ////////////////////////////////////
    @GetMapping("/myprofile")
    public String getHelloPage(HttpServletRequest request,Model model){
        //Read https://stackoverflow.com/questions/248562/when-using-spring-security-what-is-the-proper-way-to-obtain-current-username-i
        final String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("username",currentUser);
        request.getAttribute("username");
        AppUser appUser = appUserRepository.findByUsername(currentUser);
        model.addAttribute("postsList",appUser.getPosts());
        model.addAttribute("userInfo",appUser);
        return "profile";
    }
    ///////////////////////////////// Log Out /////////////////////////

////////////////////////////// Task 17 ///////////////////////////////

    @GetMapping("/users")
    public String GetUser(Model model){
        model.addAttribute("usersList",appUserRepository.findAll());
        final String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("username",currentUser);

        return "info";
    }
    @PostMapping("/users")
    public String GetAlbumDetails(@RequestParam Integer userId, Model model) {
        AppUser foundUser = appUserRepository.findById(userId).orElseThrow();
        model.addAttribute("user1", foundUser);
        model.addAttribute("usersList",appUserRepository.findAll());
        final String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("username",currentUser);
        return "info";
    }
}
