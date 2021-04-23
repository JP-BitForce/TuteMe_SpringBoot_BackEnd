package com.bitforce.tuteme.repository;

import com.bitforce.tuteme.model.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    Page<Question> findAllByAnsweredEquals(Boolean answered, Pageable pageable);

    Page<Question> findByOrderByVotesDesc(Pageable pageable);
}
