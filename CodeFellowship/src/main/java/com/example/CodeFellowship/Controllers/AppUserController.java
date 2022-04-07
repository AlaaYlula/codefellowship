package com.example.CodeFellowship.Controllers;

import com.example.CodeFellowship.Model.AppUser;
import com.example.CodeFellowship.Repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
            model.addAttribute("followingList",appUser.getFollowing());


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
        model.addAttribute("followingList",appUser.getFollowing());

        return "profile";
    }
    ///////////////////////////////// Log Out /////////////////////////

////////////////////////////// Task 17 ///////////////////////////////

    /*
    you can select  all user in the application and show  their information
     */
//    @GetMapping("/users")
//    public String GetUser(Model model){
//        model.addAttribute("usersList",appUserRepository.findAll());
//        final String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
//        model.addAttribute("username",currentUser);
//
//        return "info";
//    }
//    @PostMapping("/users")
//    public String GetUserDetails(@RequestParam Integer userId, Model model) {
//        AppUser foundUser = appUserRepository.findById(userId).orElseThrow();
//        model.addAttribute("user1", foundUser);
//        model.addAttribute("usersList",appUserRepository.findAll());
//        final String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
//        model.addAttribute("username",currentUser);
//        return "info";
//    }
    @GetMapping("/users/{id}")
    public String GetUser(@PathVariable int id , Model model){
        model.addAttribute("usersList",appUserRepository.findAll());
        final String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("username",currentUser);
        AppUser foundUser = appUserRepository.findById(id);
        model.addAttribute("user1", foundUser);

        return "info";
    }
    @PostMapping("/users")
    public RedirectView GetUserDetails(@RequestParam Integer userId, Model model) {
        AppUser foundUser = appUserRepository.findById(userId).orElseThrow();
        model.addAttribute("user1", foundUser);
        model.addAttribute("usersList",appUserRepository.findAll());
        final String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("username",currentUser);
        return new RedirectView("/users/"+userId);
    }
    ////////////////////////////// Task 18 //////////////////////////////
    /////////////////////////////////////////// All Users  /////////////////////////////////

    /*
    show the all users in the application and you can follow or unfollow and show the account for them
     */
    // List all the users in the application
    @GetMapping("/users/follow")
    public String GetAllUser(Model model){

        final String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser appUser = appUserRepository.findByUsername(currentUser);
        // Remove the current user From the users List
        List<AppUser> allUser = appUserRepository.findAll();
        allUser.remove(appUser);
        for (int i = 0; i < allUser.size(); i++) {
            if(appUser.getFollowing().contains(allUser.get(i))){
                // to show unfollow button
                allUser.get(i).setFlag("false");
            }else{
                // to show follow button
                allUser.get(i).setFlag("true");
            }
        }
        model.addAttribute("usersList",allUser);
        model.addAttribute("username",currentUser);
        return "users";
    }
    // Follow and show the account for the user from the Application
    @PostMapping("/users/follow")
    public String FollowUserById(@RequestParam int user_id,Model model){
        AppUser userToFollow = appUserRepository.findById(user_id);
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser currentUser = appUserRepository.findByUsername(name);

        AppUser appUser = appUserRepository.findByUsername(name);
        model.addAttribute("postsList",appUser.getPosts());
        model.addAttribute("userInfo",appUser);
        model.addAttribute("followingList",appUser.getFollowing());
        model.addAttribute("username",name);

        // if this is the first time the current user follow this user // and go to the profile page for the user
        if(!currentUser.getFollowing().contains(userToFollow)) {
            currentUser.getFollowing().add(userToFollow);
            appUserRepository.save(currentUser);

            userToFollow.getFollowers().add(currentUser);
            appUserRepository.save(userToFollow);

        }else{ // if already follow him/her // keep in the users html page
            // Remove the current user From the users List
            List<AppUser> allUser = appUserRepository.findAll();
            allUser.remove(appUser);
            model.addAttribute("usersList",allUser);

            for (int i = 0; i < allUser.size(); i++) {
                if(appUser.getFollowing().contains(allUser.get(i))){
                    // to show unfollow button
                    allUser.get(i).setFlag("false");
                }else{
                    // to show follow button
                    allUser.get(i).setFlag("true");
                }
            }
            return "users";
        }
        return "profile";
    }
    // UnFollow the user from the Application
    @PostMapping("/users/unfollow")
    public String UnFollowUserById(@RequestParam int user_id,Model model){
        AppUser userToFollow = appUserRepository.findById(user_id);
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser currentUser = appUserRepository.findByUsername(name);

        AppUser appUser = appUserRepository.findByUsername(name);
        model.addAttribute("postsList",appUser.getPosts());
        model.addAttribute("userInfo",appUser);
        model.addAttribute("followingList",appUser.getFollowing());
        model.addAttribute("username",name);

        // if the current user doesn't follow this user
        if(!currentUser.getFollowing().contains(userToFollow)) {
            List<AppUser> allUser = appUserRepository.findAll();
            allUser.remove(appUser);
            model.addAttribute("usersList",allUser);

            for (int i = 0; i < allUser.size(); i++) {
                if (appUser.getFollowing().contains(allUser.get(i))) {
                    // to show unfollow button
                    allUser.get(i).setFlag("false");
                } else {
                    // to show follow button
                    allUser.get(i).setFlag("true");
                }
            }
            return "users";
        }else{ // if already follow him/her // Unfollow
            currentUser.getFollowing().remove(userToFollow);
            appUserRepository.save(currentUser);

            userToFollow.getFollowers().remove(currentUser);
            appUserRepository.save(userToFollow);
        }
        return "profile";
    }
////////////////////////////////////////////// User Account ////////////////////////////////////

    // To show the user account in the application
    @GetMapping("/users/account")
    public String GetFollowing(Model model){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("username",name);

        return "userAccount";
    }
    @PostMapping("/users/account")
    public String GetUserFollowingAccount(int user_id,Model model){

        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("username",name);

        AppUser currentuser = appUserRepository.findByUsername(name);
        AppUser appUser = appUserRepository.findById(user_id);
        //check if the user follow the logged in account, and you want to show this logged in account
        if(currentuser.equals(appUser)){
            // it must not have follow or unfollow button
            model.addAttribute("flag","Me");
        }else   //Check if the user followed or not

            if(currentuser.getFollowing().contains(appUser)){
            String flag = "false";
            model.addAttribute("flag",flag);
        }else  {
            String flag = "true";
            model.addAttribute("flag",flag);
        }
        model.addAttribute("followingUser",appUser);
        model.addAttribute("following",appUser.getFollowing());
        model.addAttribute("postsList",appUser.getPosts());

        return "userAccount";
    }

}
