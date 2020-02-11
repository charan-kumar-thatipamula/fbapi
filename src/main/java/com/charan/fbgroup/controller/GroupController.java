package com.charan.fbgroup.controller;

import com.charan.fbgroup.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FeedOperations;
import org.springframework.social.facebook.api.PagedList;
import org.springframework.social.facebook.api.Post;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;

@Controller
@RequestMapping("/")
public class GroupController {

    private Facebook facebook;
    private ConnectionRepository connectionRepository;
    @Autowired
    GroupService groupService;
    public GroupController(Facebook facebook, ConnectionRepository connectionRepository) {
        this.facebook = facebook;
        this.connectionRepository = connectionRepository;
    }

    @GetMapping
    public String helloFacebook(Model model) {
        if (connectionRepository.findPrimaryConnection(Facebook.class) == null) {
            return "redirect:/connect/facebook";
        }

        model.addAttribute("facebookProfile", facebook.userOperations().getUserProfile());
        PagedList<Post> feed = facebook.feedOperations().getFeed();
        model.addAttribute("feed", feed);
        return "hello";
    }

    @GetMapping("feed")
    public void pageFeed() {
//        Collection<Post> posts =this.facebook.feedOperations().getPosts("2014881298790727");
        this.facebook = new FacebookTemplate("307158980065790|e3tPDiOYzpyEcSwFvaLlL3e5ZFY");
        FeedOperations feedOperations = this.facebook.feedOperations();
        Collection<Post> posts = feedOperations.getPosts("2014881298790727");
        System.out.println();
    }

}