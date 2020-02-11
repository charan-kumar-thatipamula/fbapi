package com.charan.fbgroup.repository;

import com.charan.fbgroup.conllection.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends MongoRepository<Post, Integer> {
    List<Post> findByIdForComment(String idForComment);

    List<Post> findByFbOriginalId(String fbOriginalId);

    List<Post> findByCommentList(List<String> commentList);

    List<Post> findByCommentsFetchedFalse();

    List<Post> findByCommentsFetchedTrue();

    List<Post> findBySavedInFileDeleteMeFalse();
}
