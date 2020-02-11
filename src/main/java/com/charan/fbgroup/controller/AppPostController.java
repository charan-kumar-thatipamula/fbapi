package com.charan.fbgroup.controller;

import com.charan.fbgroup.conllection.Post;
import com.charan.fbgroup.repository.PostRepository;
import com.charan.fbgroup.service.FileService;
import com.charan.fbgroup.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class AppPostController {
    @Autowired
    FileService fileService;
    @Autowired
    PostRepository postRepository;

    @GetMapping("/savepoststofile")
    public String extractposts() {
        try {
//            List<Post> posts1 = postRepository.findByFbOriginalId("2014881298790727_2593026327642885");
//            List<Post> posts = postRepository.findByFbOriginalId("2014881298790727_2593187927626725");
//            posts.addAll(posts1);
            List<Post> posts = postRepository.findAll();
//            fileService.createExcelFromPosts(posts, "posts_comments.xlsx");
            fileService.createTxtFromPosts(posts, "posts_comments.txt");
        } catch (IOException e) {
            e.printStackTrace();
            return "failed: " + e.getLocalizedMessage();
        }
        return null;
    }
}
