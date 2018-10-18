package com.charan.fbgroup.service;

import com.charan.fbgroup.conllection.Post;
import com.charan.fbgroup.repository.PostRepository;
import com.charan.fbgroup.response.Comment;
import com.charan.fbgroup.response.FeedMessage;
import com.charan.fbgroup.response.PageFeedDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class PostService {
    @Autowired
    PostRepository postRepository;
    @Autowired
    CommentService commentService;
    @Autowired
    FileService fileService;
    public void savePost(Post post) {
        postRepository.save(post);
    }

    public void addPosts(PageFeedDetails pageFeedDetails) {
        if (pageFeedDetails == null) {
            return;
        }

        List<FeedMessage> feedMessageList = pageFeedDetails.getData();
        if (feedMessageList == null) {
            System.out.println("feedMessage list emtpy in addPosts");
            return;
        }

        for (FeedMessage feedMessage : feedMessageList) {
            List<Post> posts = postRepository.findByFbOriginalId(feedMessage.getId());
            if (posts !=null && posts.size() != 0) {
                continue;
            }
            Post post = new Post();
            post.setPostMessage(feedMessage.getMessage());
            post.setFbOriginalId(feedMessage.getId());
            post.setIdForComment(commentService.getPostIdForComment(feedMessage));
            postRepository.save(post);
        }
    }

    public void upsertComments(String idForComment, List<String> comments) {
        List<Post> posts = postRepository.findByIdForComment(idForComment);
        if (posts == null || posts.size() == 0) {
            return;
        }
        Post post = posts.get(0);
        if (post.getCommentList() == null) {
            post.setCommentList(comments);
        } else {
            List<String> originalComments = post.getCommentList();
            comments = fileService.appendLists(originalComments, comments);
            post.setCommentList(comments);
        }
        postRepository.save(post);
    }

    public List<Post> getPostsWithNoCommnets() {
        return postRepository.findByCommentList(null);
    }
}
