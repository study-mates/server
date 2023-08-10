package org.studymate.domain.study.entity;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TraceRepository extends JpaRepository<Trace, Long>{
	public List<Trace> findByStudyIdAndCreated(String studyId, LocalDate date , Pageable pageable );
	public List<Trace> findByStudyId(String studyId, Sort sort);
}
