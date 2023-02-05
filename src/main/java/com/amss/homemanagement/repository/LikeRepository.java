package com.amss.homemanagement.repository;

import com.amss.homemanagement.model.Comment;
import com.amss.homemanagement.model.Like;
import com.amss.homemanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LikeRepository extends JpaRepository<Like, UUID> {
    List<Like> findLikesByCommentId(UUID commentId);
    Optional<Like> findLikeByCommentAndUser(Comment comment, User user);
}
