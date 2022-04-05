package com.example.CodeFellowship.Controllers;

import com.example.CodeFellowship.Model.AppUser;
import com.example.CodeFellowship.Model.Post;
import com.example.CodeFellowship.Repository.AppUserRepository;
import com.example.CodeFellowship.Repository.PostRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PostController {
    private final PostRepository postRepository;
    private final AppUserRepository appUserRepository;

    public PostController(PostRepository postRepository, AppUserRepository appUserRepository) {
        this.postRepository = postRepository;
        this.appUserRepository = appUserRepository;
    }
    ///////////////////////////

    @GetMapping("/post")
    public String GetPostPage(Model model){
        final String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("username",currentUser);
        return "post";
    }
    @PostMapping("/post")
    public String AddPost(@RequestParam String body, Model model){
        final String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser appUser = appUserRepository.findByUsername(currentUser);
        LocalDateTime date = LocalDateTime.now();
        Post post = new Post(body,date);
        post.setPostDate(date);
        List<Post> userPosts = appUser.getPosts();
        userPosts.add(post);
        post.setAppUser(appUser);
        appUser.setPosts(userPosts);
        postRepository.save(post);
        model.addAttribute("postsList",appUser.getPosts());
        model.addAttribute("username",currentUser);
        model.addAttribute("userInfo",appUser);

        AppUser currentusernew = appUserRepository.findByUsername(currentUser);
        model.addAttribute("followingList",currentusernew.getFollowing());

        return "profile";
    }
//////////////////////////////////// show All the Posts for All following User //////////////////////////
    @GetMapping("/feed")
    public String GetAllFollowingPosts(Model model){
        final String name = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser currentUser = appUserRepository.findByUsername(name);

        List<AppUser> allUsersFollowing = currentUser.getFollowing();
        List<Post> allUsersFollowingPosts = new ArrayList<>();
        for (AppUser UserFollow : allUsersFollowing) {
            List<Post> userPosts = UserFollow.getPosts();
            allUsersFollowingPosts.addAll(userPosts);
        }

        model.addAttribute("postsList", allUsersFollowingPosts);
        model.addAttribute("username",name);

        return "feed";
    }
}
