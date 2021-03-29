package com.bitforce.tuteme.repository;

import com.bitforce.tuteme.model.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Blog,Long> {
    Page<Blog> findAllByUserId(Long userId, Pageable pageable);
}
