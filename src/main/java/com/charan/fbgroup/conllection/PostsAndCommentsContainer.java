package com.charan.fbgroup.conllection;

import java.util.List;

public class PostsAndCommentsContainer {
    public List<PostsAndCommentsOnly> allPosts;

    public List<PostsAndCommentsOnly> getAllPosts() {
        return allPosts;
    }

    public void setAllPosts(List<PostsAndCommentsOnly> allPosts) {
        this.allPosts = allPosts;
    }
}
