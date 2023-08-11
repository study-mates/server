package org.studymate.domain.study;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.studymate.domain.study.dto.SimpleAttendance;
import org.studymate.domain.study.dto.SimpleNotice;
import org.studymate.domain.study.dto.SimpleStudy;
import org.studymate.domain.study.dto.SimpleTrace;
import org.studymate.domain.study.request.AddTraceRequest;
import org.studymate.domain.study.request.CreateNoticeRequest;
import org.studymate.domain.study.request.CreateStudyRequest;
import org.studymate.domain.study.request.ModifyStudyReqesut;
import org.studymate.domain.study.response.AttendanceListResponse;
import org.studymate.domain.study.response.NoticeListResponse;
import org.studymate.domain.study.response.StudyInfoResponse;
import org.studymate.domain.study.response.StudyListResposne;
import org.studymate.domain.study.response.TraceListResponse;
import org.studymate.domain.user.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/study")
@RequiredArgsConstructor
@CrossOrigin("*")
@Slf4j
public class StudyController {

	private final StudyService studyService;
	private final UserService userService;

	@Value("${deploy.servername}")
	private String deployServername;

	// 스터디 목록 확인
	@GetMapping
	public ResponseEntity<?> handleStudyList(@RequestAttribute Long userId) {
		log.debug("userId {} ", userId);
		var list = studyService.getStudyListByUser(userId);

		var studyList = list.stream().map(t -> new SimpleStudy(t.getStudy())).toList();
		studyList.stream().forEach(t -> t.setRole(userId.equals(t.getStudyLeadUserId()) ? "master" : "guest"));

		var response = StudyListResposne.builder() //
				.status("Ok") //
				.userId(userId) //
				.study(studyList)//
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
		return new ResponseEntity<>(Map.of("status", "Created", "createdId", studyId), HttpStatus.CREATED);
	}

	// 특정 스터디의 종합 정보 확인
	@GetMapping("/{studyId}")
	public ResponseEntity<?> handleInformationOfStudy(@RequestAttribute Long userId, @PathVariable String studyId) {

		userService.updateLastAccessStudy(userId, studyId);

		var study = studyService.getInfoAboutStudy(studyId);
		var notice = studyService.getNoticeByStudyId(studyId);
		var attendance = studyService.getAttendanceByStudyId(studyId);
		var trace = studyService.getTraceByCreated(studyId, null, null).stream().map(SimpleTrace::new).toList();
		trace.forEach(t -> {
			t.setDescription(null);
			t.setImages(null);
			t.setMainImage(deployServername + t.getMainImage());
		});

		var existTrace = studyService.getTraceDayInStudy(studyId);
		var other = studyService.getStudyListByUser(userId).stream().map(t -> new SimpleStudy(t.getStudy()))
				.filter(t -> t.getEnabled()).toList();
		other.forEach(t -> {
			t.setOpenDate(null);
			t.setStudyLeadUserId(null);
			t.setRole(null);
			t.setEnabled(null);
		});
		log.debug("existTrace {} ", existTrace);
		var response = StudyInfoResponse.builder() //
				.status("Ok") //
				.study(new SimpleStudy(study)) //
				.today(LocalDate.now()) //
				.notice(notice.stream().map(SimpleNotice::new).toList()) //
				.elapsed(ChronoUnit.DAYS.between(study.getOpenDate(), LocalDate.now()) + 1) //
				.attendanceCount((long) attendance.size()) //
				.todaysTrace(trace) //
				.studyList(other) //
				.traceDate(existTrace) //
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// 특정 스터디 종료
	@DeleteMapping("/{studyId}")
	public ResponseEntity<?> handleCreateStudy(@RequestAttribute Long userId, @PathVariable String studyId) {
		var result = studyService.terminateStudy(userId, studyId);
		userService.updateLastAccessStudy(userId, null);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	// 특정 스터디 이름 변경
	@PatchMapping("/{studyId}")
	public ResponseEntity<?> handleModifyStudy(@RequestAttribute Long userId, @PathVariable String studyId,
			@RequestBody ModifyStudyReqesut modifyStudyRequest) {
		var result = studyService.updateStudyDescription(userId, studyId, modifyStudyRequest);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	// 특정 스터디의 참가자 확인
	@GetMapping("/{studyId}/attendance")
	public ResponseEntity<?> handleAttendanceOfStudy(@RequestAttribute Long userId, @PathVariable String studyId) {
		var list = studyService.getAttendanceByStudyId(studyId);
//		log.debug("list size {}", list.size());
		var response = AttendanceListResponse.builder()//
				.status("Ok") //
				.studyId(studyId) //
				.attendance(list.stream().map(SimpleAttendance::new).toList()) //
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// 특정 스터디에 참가취소
	@DeleteMapping("/{studyId}/attendance")
	public ResponseEntity<?> handleCancleAttendanceOfStudy(@RequestAttribute Long userId,
			@PathVariable String studyId) {
		studyService.removeAttendanceFromStudy(userId, studyId);
//			log.debug("list size {}", list.size());

		return new ResponseEntity<>(Map.of("status", "No content"), HttpStatus.NO_CONTENT);
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
				.notice(list.stream().map(SimpleNotice::new).toList()) //
				.build();

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// 특정 스터디에 인증글 등록하기
	@PostMapping("/{studyId}/trace")
	public ResponseEntity<?> handleCreateTrace(@RequestAttribute Long userId, @PathVariable String studyId,
			AddTraceRequest req) {
		var entity = studyService.addTraceToStudy(userId, studyId, req);

		return new ResponseEntity<>(Map.of("status", "Created", "traceId", entity.getId()), HttpStatus.CREATED);
	}

	// 특정 스터디의 인증글 목록 가져오기
	@GetMapping("/{studyId}/trace")
	public ResponseEntity<?> handleGetTrace(@PathVariable String studyId, @RequestParam(required = false) Integer page,
			@RequestParam(required = false) LocalDate date) {
		var entitylist = studyService.getTraceByCreated(studyId, date, page);
		var list = entitylist.stream().map(SimpleTrace::new).toList();
		list.forEach(t -> {
			t.setImages(null);
			t.setDescription(null);
			t.setMainImage(deployServername + t.getMainImage());
		});

		var response = TraceListResponse.builder().status("Ok").studyId(studyId)
				.date(date == null ? LocalDate.now() : date).page(page == null ? 1 : page).trace(list).build();
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	// 특정 스터디의 인증글 상세
	@GetMapping("/{studyId}/trace/{traceId}")
	public ResponseEntity<?> handleGetTraceDetail(@PathVariable String studyId, @PathVariable Long traceId) {
//		var list = studyService.getTraceByCreated(studyId, date, page);
		var entity = studyService.getTraceById(traceId);
		var trace = new SimpleTrace(entity);
		trace.setMainImage(deployServername + trace.getMainImage());
		trace.setImages(trace.getImages().stream().map(t -> deployServername + t).toList());

		return new ResponseEntity<>(Map.of("status", "Ok", "trace", trace), HttpStatus.OK);
	}

	// 특정 스터디의 초대 코드 생성하기

	@GetMapping("/{studyId}/invite")
	public ResponseEntity<?> handleCreateInvite(@PathVariable String studyId) {

		var entity = studyService.createInviteCode(studyId);

		return new ResponseEntity<>(Map.of("status", "Ok", "code", entity.getCode()), HttpStatus.OK);
	}


}
