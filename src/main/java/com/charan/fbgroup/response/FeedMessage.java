package com.charan.fbgroup.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FeedMessage {
    java.lang.String message;
    java.lang.String updated_time;
    java.lang.String id;
    List<String> comments;

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    public java.lang.String getMessage() {
        return message;
    }

    public void setMessage(java.lang.String message) {
        this.message = message;
    }

    public java.lang.String getUpdated_time() {
        return updated_time;
    }

    public void setUpdated_time(java.lang.String updated_time) {
        this.updated_time = updated_time;
    }

    public java.lang.String getId() {
        return id;
    }

    public void setId(java.lang.String id) {
        this.id = id;
    }
}