package com.charan.fbgroup.service;

import com.charan.fbgroup.conllection.Post;
import com.charan.fbgroup.repository.PostRepository;
import com.charan.fbgroup.response.FeedMessage;
import com.charan.fbgroup.response.PageFeedDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
                System.out.println("post already present");
                continue;
            }
            Post post = new Post();
            post.setPostMessage(feedMessage.getMessage());
            post.setFbOriginalId(feedMessage.getId());
            post.setIdForComment(commentService.getPostIdForComment(feedMessage));
            post.setCommentsFetched(false);
            postRepository.save(post);
        }
    }

    public void upsertCommentsForPostsUsingOnlyCommentId(String idForComment, List<String> comments) {
        List<Post> posts = postRepository.findByIdForComment(idForComment);
        updatePosts(posts, comments);
    }

    public void upsertCommentsForPostsUsingPostId(String commentId, List<String> comments) {
        List<Post> posts = postRepository.findByFbOriginalId(commentId);
        updatePosts(posts, comments);
    }

    private void updatePosts(List<Post> posts, List<String> comments) {
        if (posts == null || posts.size() == 0) {
            return;
        }
        Post post = posts.get(0);
        if (comments != null) {
            post.setCommentList(comments);
        }
        post.setCommentsFetched(true);
//        if (post.getCommentList() == null) {
//            post.setCommentList(comments);
//        } else {
//            List<String> originalComments = post.getCommentList();
//            comments = fileService.appendLists(originalComments, comments);
//            post.setCommentList(comments);
//        }
        postRepository.save(post);
    }
    public List<Post> getPostsWithNoCommnets() {
        return postRepository.findByCommentList(null);
    }

    public List<Post> getPostsNoCommentsFetched() {
        return postRepository.findByCommentsFetchedFalse();
    }
}
