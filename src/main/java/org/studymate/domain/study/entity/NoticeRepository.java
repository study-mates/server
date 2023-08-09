package org.studymate.domain.study.entity;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

	List<Notice> findByStudyId(String studyId, Sort sort);

}
