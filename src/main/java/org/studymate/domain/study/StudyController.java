package org.studymate.domain.study;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.studymate.domain.study.request.CreateNoticeRequest;
import org.studymate.domain.study.request.CreateStudyRequest;
import org.studymate.domain.study.response.AttendanceListResponse;
import org.studymate.domain.study.response.NoticeListResponse;
import org.studymate.domain.study.response.StudyListResposne;
import org.studymate.domain.user.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/study")
@RequiredArgsConstructor
@Slf4j
public class StudyController {

	private final StudyService studyService;
	private final UserService userService;

	// 스터디 목록 확인
	@GetMapping
	public ResponseEntity<?> handleStudyList(@RequestAttribute Long userId) {
		log.debug("userId {} ", userId);
		var list = studyService.getStudyListByUser(userId);

		var response = StudyListResposne.builder() //
				.status("Ok") //
				.userId(userId) //
				.study(list.stream().map(t -> new StudyListResposne.SimpleStudy(t, userId)).toList()) //
				.build();

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// 스터디 생성
	@PostMapping
	public ResponseEntity<?> handleCreateStudy(@RequestAttribute Long userId,
			@RequestBody CreateStudyRequest createStudyRequest) {
		log.debug("userId {} {} ", userId, createStudyRequest);
		var studyId = studyService.addStudy(userId, createStudyRequest);
		studyService.addAttendanceToStudy(userId, studyId);
		userService.updateLastAccessStudy(userId, studyId);
		return new ResponseEntity<>(Map.of("createdId", studyId), HttpStatus.CREATED);
	}

	// 특정 스터디의 종합 정보 확인
	@GetMapping("/{studyId}")
	public ResponseEntity<?> handleInformationOfStudy(@RequestAttribute Long userId, @PathVariable String studyId) {
		var list = studyService.getAttendanceByStudyId(studyId);
		log.debug("list size {}", list.size());
		return new ResponseEntity<>(Map.of("status", "Ok", "attendance", list), HttpStatus.OK);
	}

	// 특정 스터디에 참여
	@PostMapping("/{studyId}/attendance")
	public ResponseEntity<?> handleAttendToStudy(@RequestAttribute Long userId, @PathVariable String studyId) {
		studyService.addAttendanceToStudy(userId, studyId);

		return new ResponseEntity<>(Map.of("status", "Created"), HttpStatus.CREATED);
	}

	// 특정 스터디의 참가자 확인
	@GetMapping("/{studyId}/attendance")
	public ResponseEntity<?> handleAttendanceOfStudy(@RequestAttribute Long userId, @PathVariable String studyId) {
		var list = studyService.getAttendanceByStudyId(studyId);
//		log.debug("list size {}", list.size());
		var response = AttendanceListResponse.builder()//
				.status("Ok") //
				.studyId(studyId) //
				.attendance(list.stream().map(AttendanceListResponse.SimpleAttendance::new).toList()) //
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// 특정 스터디에 참가취소
	@DeleteMapping("/{studyId}/attendance")
	public ResponseEntity<?> handleCancleAttendanceOfStudy(@RequestAttribute Long userId, @PathVariable String studyId) {
		studyService.removeAttendanceFromStudy(userId, studyId);
//			log.debug("list size {}", list.size());
		var response = AttendanceListResponse.builder()//
				.status("Ok") //
				.studyId(studyId) //
				.attendance(list.stream().map(AttendanceListResponse.SimpleAttendance::new).toList()) //
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// 특정 스터디에 공지 등록
	@PostMapping("/{studyId}/notice")
	public ResponseEntity<?> handleNoticeToStudy(@RequestAttribute Long userId, @PathVariable String studyId,
			@RequestBody CreateNoticeRequest noticeRequest) {
		studyService.addNoticeToStudy(userId, studyId, noticeRequest);

		return new ResponseEntity<>(Map.of("status", "Created"), HttpStatus.CREATED);
	}

	// 특정 스터디의 공지 확인
	@GetMapping("/{studyId}/notice")
	public ResponseEntity<?> handleNoticeOfStudy(@PathVariable String studyId) {
		var list = studyService.getNoticeByStudyId(studyId);
//		log.debug("list size {}", list.size());
		var response = NoticeListResponse.builder()//
				.status("Ok")//
				.studyId(studyId)//
				.notice(list.stream().map(NoticeListResponse.SimpleNotice::new).toList()) //
				.build();

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
