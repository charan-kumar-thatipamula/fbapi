package com.charan.fbgroup.service;

import com.charan.fbgroup.api.RequestRelated;
import com.charan.fbgroup.api.RestApiManager;
import com.charan.fbgroup.response.Comment;
import com.charan.fbgroup.response.CommentResponse;
import com.charan.fbgroup.response.FeedMessage;
import com.charan.fbgroup.response.PageFeedDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class CommentService {

    @Value("${fb.app.accesstoken}")
    java.lang.String accessToken;
    @Value("${fb.graph.url}")
    java.lang.String graphUrl;
    @Value("${fb.post.commentsendpoint}")
    java.lang.String commentsEndPoint;
    @Autowired
    RestApiManager restApiManager;
    @Autowired
    RequestRelated requestRelated;
    @Autowired
    FileService fileService;
    @Autowired
    PostService postService;

    public void getCommentsForPosts(PageFeedDetails pageFeedDetails) throws Exception {
        List<FeedMessage> feedMessages = pageFeedDetails.getData();
        for (FeedMessage feedMessage : feedMessages) {
            java.lang.String postIdForString = getPostIdForComment(feedMessage);
            CommentResponse commentResponse = getCommentsForPost(postIdForString);
            feedMessage.setId(null);
            fileService.writeToFile(commentResponse, "comments_" + postIdForString + "_" + new Date().getTime() + ".txt");
            List<Comment> comments = commentResponse.getData();
            updateFeedMessageWithComments(feedMessage, comments);
        }
    }

    public CommentResponse getCommentsForPost(String postIdForString) throws Exception {
        java.lang.String query = requestRelated.getQueryForComments(accessToken);
        HttpHeaders httpHeaders = null;
        CommentResponse commentResponse = restApiManager.get(graphUrl, "/" + postIdForString + commentsEndPoint, query, httpHeaders, CommentResponse.class);
        return commentResponse;
    }

    public CommentResponse getCommentsForPost(String postIdForString, String accessToken) throws Exception {
        java.lang.String query = requestRelated.getQueryForComments(accessToken);
        HttpHeaders httpHeaders = null;
        CommentResponse commentResponse = restApiManager.get(graphUrl, "/" + postIdForString + commentsEndPoint, query, httpHeaders, CommentResponse.class);
        return commentResponse;
    }

    private void updateFeedMessageWithComments(FeedMessage feedMessage, List<Comment> comments) {
        if (feedMessage.getComments() == null) {
            feedMessage.setComments(new LinkedList<>());
        }
        for (Comment comment : comments) {
            feedMessage.getComments().add(comment.getMessage());
        }
    }
    public java.lang.String getPostIdForComment(FeedMessage feedMessage) {
        java.lang.String postId = feedMessage.getId();
        if (postId != null) {
            postId = postId.substring(postId.indexOf("_") + 1, postId.length());
        }
        return postId;
    }

    public void processCommentsFromFolder(String folderPath) throws IOException {
        File[] files = fileService.getFilesFromFolder(folderPath);
        for (File file : files) {
            String fName = file.getName();
            if (fName.indexOf("comments") != 0 || file.isDirectory()) {
                continue;
            }
            String idForComment = getIdFromFileName(fName);
            CommentResponse commentResponse = new ObjectMapper().readValue(file, CommentResponse.class);
            processForCommentsInsertionInDB(idForComment, commentResponse);
        }
    }

    public void processForCommentsInsertionInDB(String idForComment, CommentResponse commentResponse) {
        List<String> comments = new LinkedList<>();
        for (Comment comment : commentResponse.getData()) {
            comments.add(comment.getMessage());
        }
        postService.upsertCommentsForPostsUsingOnlyCommentId(idForComment, comments);

    }

    public void addCommentsUsingPostId(String postId, CommentResponse commentResponse) {
        List<String> comments = new LinkedList<>();
        for (Comment comment : commentResponse.getData()) {
            comments.add(comment.getMessage());
        }
        postService.upsertCommentsForPostsUsingPostId(postId, comments);

    }

    private String getIdFromFileName(String fName) {
        return fName.substring(fName.indexOf("_") + 1, fName.lastIndexOf("_"));
    }
}
