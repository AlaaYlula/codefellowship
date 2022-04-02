package com.example.CodeFellowship.Model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String body;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime postDate;

    @ManyToOne
    AppUser appUser;

    public Post() {
    }

    public Post(String text, LocalDateTime postDate) {
        this.body = text;
        this.postDate = postDate;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return body;
    }

    public void setText(String text) {
        this.body = text;
    }

    public LocalDateTime getPostDate() {
        return postDate;
    }

    public void setPostDate(LocalDateTime postDate) {
        this.postDate = postDate;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }
}
