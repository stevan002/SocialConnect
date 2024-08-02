package com.example.SocialConnect.repository;

import com.example.SocialConnect.model.Comment;
import com.example.SocialConnect.model.Post;
import com.example.SocialConnect.model.Reaction;
import com.example.SocialConnect.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    Reaction findByPostAndCreatedBy(Post post, User user);
    Reaction findByCommentAndCreatedBy(Comment comment, User user);
}
