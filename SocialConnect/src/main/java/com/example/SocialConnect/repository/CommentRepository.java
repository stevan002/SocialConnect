package com.example.SocialConnect.repository;

import com.example.SocialConnect.model.Comment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = {"reactions"})
    List<Comment> findAllByPostId(Long postId);
}
