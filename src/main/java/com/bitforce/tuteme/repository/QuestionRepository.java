package com.bitforce.tuteme.repository;

import com.bitforce.tuteme.model.Question;
import com.bitforce.tuteme.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    Page<Question> findAllByAnsweredEquals(Boolean answered, Pageable pageable);

    Page<Question> findByOrderByVotesDesc(Pageable pageable);

    Page<Question> findAllByTagsIn(List<Tag> tags, Pageable pageable);

    Page<Question> findAllByCreatedAtLessThanEqualAndCreatedAtGreaterThanOrderByCreatedAtDesc(
            LocalDateTime end,
            LocalDateTime start,
            Pageable pageable
    );

    Page<Question> findAllByTitleIsContaining(String title, Pageable pageable);
}
