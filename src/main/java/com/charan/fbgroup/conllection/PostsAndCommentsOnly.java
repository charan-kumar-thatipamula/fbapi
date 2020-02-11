package com.charan.fbgroup.conllection;

import java.util.List;

public class PostsAndCommentsOnly {

    public String post;
    public List<String> comments;

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

//    @Override
//    public String toString() {
//        return String.format("{post:%s, comments:[%s]}", getPost(), String.join(",", getComments()));
//    }


    @Override
    public String toString() {
        return "PostsAndCommentsOnly{" +
                "post='" + post + '\'' +
                ", comments=" + comments +
                '}';
    }
}
