package com.charan.fbgroup.conllection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection="posts_new")
public class Post {

    @Id
    private String id;

    private String postMessage;

    private String fbOriginalId;

    private String idForComment;

    private List<String> commentList;

    private boolean commentsFetched;

    private boolean savedInFileDeleteMe;

    public String getFbOriginalId() {
        return fbOriginalId;
    }

    public void setFbOriginalId(String fbOriginalId) {
        this.fbOriginalId = fbOriginalId;
    }

    public String getIdForComment() {
        return idForComment;
    }

    public void setIdForComment(String idForComment) {
        this.idForComment = idForComment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostMessage() {
        return postMessage;
    }

    public void setPostMessage(String postMessage) {
        this.postMessage = postMessage;
    }

    public List<String> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<String> commentList) {
        this.commentList = commentList;
    }

    public boolean areCommentsFetched() {
        return commentsFetched;
    }

    public void setCommentsFetched(boolean commentsFetched) {
        this.commentsFetched = commentsFetched;
    }

    public boolean isSavedInFileDeleteMe() {
        return savedInFileDeleteMe;
    }

    public void setSavedInFileDeleteMe(boolean savedInFileDeleteMe) {
        this.savedInFileDeleteMe = savedInFileDeleteMe;
    }
}
