package org.studymate.domain.study.entity;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.studymate.domain.user.entity.User;

public interface AttendanceRepository extends JpaRepository<Attendance, Long>{
	public boolean existsByUserAndStudy(User user, Study study);
	public List<Attendance> findByStudyId(String studyId, Sort sort);
}
