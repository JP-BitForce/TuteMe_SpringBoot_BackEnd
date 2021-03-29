package com.bitforce.tuteme.repository;

import com.bitforce.tuteme.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Long> {
}
