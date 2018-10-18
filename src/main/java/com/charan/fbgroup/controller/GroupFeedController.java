package com.charan.fbgroup.controller;

import com.charan.fbgroup.conllection.Post;
import com.charan.fbgroup.response.Comment;
import com.charan.fbgroup.response.CommentResponse;
import com.charan.fbgroup.response.PageFeedDetails;
import com.charan.fbgroup.service.CommentService;
import com.charan.fbgroup.service.FileService;
import com.charan.fbgroup.service.GroupService;
import com.charan.fbgroup.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class GroupFeedController {
    @Autowired
    GroupService groupService;
    @Autowired
    CommentService commentService;
    @Autowired
    FileService fileService;
    @Autowired
    PostService postService;
    @GetMapping(value="pagefeed/{group_id}")
    public PageFeedDetails pageFeedDetails(@PathVariable(value="group_id") String groupId) {
        return groupService.getFeed(groupId);
    }

    @PostMapping(value = "comments")
    public String getCommentsForPosts(@RequestBody Map<String, String> input) throws Exception {
        String filePath = input.get("postsFilePath");
        PageFeedDetails pageFeedDetails = groupService.getPageFeedDetailsFromFile(filePath);
        commentService.getCommentsForPosts(pageFeedDetails);
        String filePostsComments = "postsWithComments_" + new Date().getTime() + ".txt";
        fileService.writeToFile(pageFeedDetails, filePostsComments);
        return filePostsComments;
    }

    @PostMapping(value="updatedb")
    public void updatePostsInDB(@RequestBody Map<String, String> input) {
        String filePath = input.get("postsFilePath");
        PageFeedDetails pageFeedDetails = groupService.getPageFeedDetailsFromFile(filePath);
        postService.addPosts(pageFeedDetails);
    }

    @PostMapping(value = "updatecomments")
    public void updateComments(@RequestBody Map<String, String> input) throws IOException {
        String folderPath = input.get("commentsFolderPath");
        commentService.processCommentsFromFolder(folderPath);
    }

    @PostMapping(value = "updateCommentsFromFB")
    public void updateCommentsFromFB() throws Exception {
        List<Post> postsWithNoComments = postService.getPostsWithNoCommnets();
        System.out.println("Remaining posts: " + postsWithNoComments.size());
        if (postsWithNoComments == null || postsWithNoComments.size() == 0) {
            return;
        }
        long waitTime = 1200000;
        int i=0;
        for (Post post : postsWithNoComments) {
            i++;
            try {
                String idForComment = post.getIdForComment();
                CommentResponse commentResponse = commentService.getCommentsForPost(idForComment);
                commentService.processForCommentsInsertionInDB(idForComment, commentResponse);
                System.out.println("idForComment: " + idForComment);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Processed posts count: " + i);
                i=0;
                try {
                    System.out.println("Going to sleep");
                    Thread.sleep(waitTime);
                    if (waitTime > 5) {
                        waitTime /= 2;
                    }
                } catch (InterruptedException ie) {
                    System.out.println("Interrupted exception occurred");
                }
            }
        }
    }
}
