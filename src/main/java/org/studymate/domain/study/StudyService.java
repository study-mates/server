package org.studymate.domain.study;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.studymate.domain.study.entity.Attendance;
import org.studymate.domain.study.entity.AttendanceRepository;
import org.studymate.domain.study.entity.Notice;
import org.studymate.domain.study.entity.NoticeRepository;
import org.studymate.domain.study.entity.Study;
import org.studymate.domain.study.entity.StudyRepository;
import org.studymate.domain.study.request.CreateNoticeRequest;
import org.studymate.domain.study.request.CreateStudyRequest;
import org.studymate.domain.user.entity.UserRepository;
import org.studymate.global.exception.BadRequestException;
import org.studymate.global.exception.ForbiddenException;
import org.studymate.global.exception.NotFoundException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudyService {

	private final StudyRepository studyRepository;
	private final UserRepository userRepository;

	private final AttendanceRepository attendanceRepository;
	private final NoticeRepository noticeRepository;

	// 스터디 목록 확보하는 서비스
	@Transactional
	public List<Attendance> getStudyListByUser(Long userId) {
		var list = attendanceRepository.findByUserId(userId);
		log.debug("study list {}", list);
		return list;
	}

	// 스터디 생성하는 서비스
	@Transactional
	public String addStudy(Long userId, CreateStudyRequest createStudyRequest) {
		var user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("존재하지 않는 유저입니다"));

		var one = Study.builder().user(user).description(createStudyRequest.getDescription())
				.openDate(createStudyRequest.getOpenDate()).build();

		return studyRepository.save(one).getId();
	}

	// 스터디에 참가자 추가하는 서비스
	@Transactional
	public void addAttendanceToStudy(Long userId, String studyId) {
		var user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("존재하지 않는 유저입니다"));
		var study = studyRepository.findById(studyId).orElseThrow(() -> new NotFoundException("존재하지 스터디 모임입니다"));

		if (attendanceRepository.existsByUserAndStudy(user, study)) {
			throw new BadRequestException("이미 참가중인 스터디 모임입니다");
		}

		var owner = study.getUser().getId() == user.getId();
		var attendance = Attendance.builder().study(study).user(user).owner(owner).build();

		attendanceRepository.save(attendance);

	}

	// 특정 스터디의 참가자 확인하는 서비스
	@Transactional
	public List<Attendance> getAttendanceByStudyId(String studyId) {
		log.debug("getAttendanceByStudyId {}", studyId);
		return attendanceRepository.findByStudyId(studyId, Sort.by("owner").descending());
	}

	// 스터디에 공지 추가하는 서비스
	@Transactional
	public void addNoticeToStudy(Long userId, String studyId, CreateNoticeRequest createNoticeDto) {
		var user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("존재하지 않는 유저입니다"));
		var study = studyRepository.findById(studyId).orElseThrow(() -> new NotFoundException("존재하지 스터디 모임입니다"));
		
		if (study.getUser().getId() != user.getId()) {
			throw new ForbiddenException("공지 작성 권한이 없습니다.");
		}

		var notice = Notice.builder().description(createNoticeDto.getDescription()).study(study)
				.tag(createNoticeDto.getTag()).writed(LocalDateTime.now()).build();
		log.debug("notice {}", notice);
		noticeRepository.save(notice);
	}

	// 특정 스터디의 공지 목록 확인 하는 서비스
	@Transactional
	public List<Notice> getNoticeByStudyId(String studyId) {
		var notice = noticeRepository.findByStudyId(studyId, Sort.by("writed").ascending());

		return notice;
	}

	// 특정 스터디에서 참가 취소하는는 서비스
	@Transactional
	public void removeAttendanceFromStudy(Long userId, String studyId) {
		var user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("존재하지 않는 유저입니다"));
		var study = studyRepository.findById(studyId).orElseThrow(() -> new NotFoundException("존재하지 스터디 모임입니다"));
		if (!attendanceRepository.existsByUserAndStudy(user, study)) {
			throw new BadRequestException("참가중인 스터디 모임이 아닙니다.");
		}
		attendanceRepository.deleteByUserAndStudy(user, study);
		return;
	}

}
