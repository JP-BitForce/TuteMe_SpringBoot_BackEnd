package com.bitforce.tuteme.repository;

import com.bitforce.tuteme.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findAllByTitleIn(List<String> title);
    List<Tag> findAllByTitleIsContaining(String title);
    List<Tag> findByOrderByTitleAsc();
}
