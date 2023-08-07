package org.studymate.domain.study.entity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyRepository extends JpaRepository<Study, String>{

	
	List<Study> findByUserId(Long userId);
}
