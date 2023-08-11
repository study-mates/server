package org.studymate.domain.study;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.studymate.domain.invite.entity.Invite;
import org.studymate.domain.invite.entity.InviteRepository;
import org.studymate.domain.study.entity.Attendance;
import org.studymate.domain.study.entity.AttendanceRepository;
import org.studymate.domain.study.entity.Image;
import org.studymate.domain.study.entity.ImageRepository;
import org.studymate.domain.study.entity.Notice;
import org.studymate.domain.study.entity.NoticeRepository;
import org.studymate.domain.study.entity.Study;
import org.studymate.domain.study.entity.StudyRepository;
import org.studymate.domain.study.entity.Trace;
import org.studymate.domain.study.entity.TraceRepository;
import org.studymate.domain.study.request.AddTraceRequest;
import org.studymate.domain.study.request.CreateNoticeRequest;
import org.studymate.domain.study.request.CreateStudyRequest;
import org.studymate.domain.study.request.ModifyStudyReqesut;
import org.studymate.domain.user.entity.UserRepository;
import org.studymate.global.constant.Messages;
import org.studymate.global.exception.BadRequestException;
import org.studymate.global.exception.ForbiddenException;
import org.studymate.global.exception.NotFoundException;
import org.studymate.global.util.FileHandler;

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

	private final TraceRepository traceRepository;
	private final ImageRepository imageRepository;

	private final InviteRepository inviteRepository;
	
	private final FileHandler fileHandler;


	
	
	
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
		var user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(Messages.NOT_FOUND_USER));
		var openDate = createStudyRequest.getOpenDate() == null ? LocalDate.now() : createStudyRequest.getOpenDate();

		var one = Study.builder().user(user).description(createStudyRequest.getDescription())
				.openDate(openDate == null ? LocalDate.now() : openDate).build();

		return studyRepository.save(one).getId();
	}

	// 특정 스터디의 기본 정보 제공하는 서비스
	public Study getInfoAboutStudy(String studyId) {
		return studyRepository.findById(studyId).orElseThrow(() -> new NotFoundException(Messages.NOT_FOUND_STUDY));
	}

	// 스터디 수정하는 서비스 (설명)
	@Transactional
	public String updateStudyDescription(Long userId, String studyId, ModifyStudyReqesut modifyStudyRequest) {
		var user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(Messages.NOT_FOUND_USER));
		var study = studyRepository.findById(studyId)
				.orElseThrow(() -> new NotFoundException(Messages.NOT_FOUND_STUDY));
		if (!study.getUser().getId().equals(user.getId()))
			throw new ForbiddenException(Messages.PERMISSION_DENIED);

		study.setDescription(modifyStudyRequest.getDescription());

		return studyRepository.save(study).getId();
	}

	// 스터디 종료하는 서비스
	@Transactional
	public String terminateStudy(Long userId, String studyId) {
		var user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(Messages.NOT_FOUND_USER));
		var study = studyRepository.findById(studyId)
				.orElseThrow(() -> new NotFoundException(Messages.NOT_FOUND_STUDY));

		if (!study.getUser().getId().equals(user.getId()))
			throw new ForbiddenException(Messages.PERMISSION_DENIED);
		if (study.getClass() != null) {
			throw new BadRequestException(Messages.ALREADY_CLOSED);
		}

		study.setCloseDate(LocalDate.now());
		return studyRepository.save(study).getId();
	}

	// 스터디에 참가자 추가하는 서비스
	@Transactional
	public void addAttendanceToStudy(Long userId, String studyId) {
		var user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(Messages.NOT_FOUND_USER));
		var study = studyRepository.findById(studyId)
				.orElseThrow(() -> new NotFoundException(Messages.NOT_FOUND_STUDY));

		if (attendanceRepository.existsByUserAndStudy(user, study)) {
			throw new BadRequestException(Messages.ALREADY_ATTENDANCE);
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
		var user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(Messages.NOT_FOUND_USER));
		var study = studyRepository.findById(studyId)
				.orElseThrow(() -> new NotFoundException(Messages.NOT_FOUND_STUDY));

		if (study.getUser().getId() != user.getId()) {
			throw new ForbiddenException(Messages.PERMISSION_DENIED);
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

	// 특정 스터디에서 참가 취소하는 서비스
	@Transactional
	public void removeAttendanceFromStudy(Long userId, String studyId) {
		var user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(Messages.NOT_FOUND_USER));
		var study = studyRepository.findById(studyId)
				.orElseThrow(() -> new NotFoundException(Messages.NOT_FOUND_STUDY));
		if (!attendanceRepository.existsByUserAndStudy(user, study)) {
			throw new BadRequestException(Messages.NOT_ATTENDANCE);
		}
		if (study.getUser().getId().equals(userId)) {
			throw new ForbiddenException(Messages.NOT_CONDITION);
		}
		attendanceRepository.deleteByUserAndStudy(user, study);
		return;
	}

	// 특정 스터디에 인증글이 달린 날짜 확인하는 서비스
	@Transactional
	public List<LocalDate> getTraceDayInStudy(String studyId) {
		var r = traceRepository.findByStudyId(studyId, Sort.by("created").ascending());
		// log.debug("{}", r);
		return r.stream().map(t -> t.getCreated()).distinct().toList();
	}

	// 특정 스터디의 특정일의 인증글 가져오기 서비스
	@Transactional
	public List<Trace> getTraceByCreated(String studyId, LocalDate date, Integer p) {
		if (date == null)
			date = LocalDate.now();
		int page = p == null ? 0 : p - 1;
		Pageable pageable = PageRequest.of(page, 10, Sort.by("id").ascending());
		return traceRepository.findByStudyIdAndCreated(studyId, date, pageable);
	}

	// 특정 스터디에 인증글 등록하기 서비스
	@Transactional
	public Trace addTraceToStudy(Long userId, String studyId, AddTraceRequest req) {
		var user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(Messages.NOT_FOUND_USER));
		var study = studyRepository.findById(studyId)
				.orElseThrow(() -> new NotFoundException(Messages.NOT_FOUND_STUDY));
		if (!attendanceRepository.existsByUserAndStudy(user, study)) {
			throw new BadRequestException(Messages.NOT_ATTENDANCE);
		}
		var trace = Trace.builder()//
				.writer(user)//
				.study(study) //
				.title(req.getTitle()) //
				.description(req.getDescription()) //
				.created(LocalDate.now()) //
				.build();
		var savedTrace = traceRepository.save(trace);
		if (req.getImages() == null || req.getImages().isEmpty()) {
			return savedTrace;
		}

		String path = "/static/" + studyId + "/" + LocalDate.now().toString().replace("-", "");
		req.getImages().forEach(t -> {
			String filename = fileHandler.save(t, path);
			var image = Image.builder().trace(savedTrace).url(path + "/" + filename).build();
			imageRepository.save(image);
		});
		return savedTrace;
	}
	
	// 특정 인증글 상세 가져 오기
	@Transactional
	public Trace getTraceById(Long traceId) {
		return traceRepository.findById(traceId).orElseThrow(() -> new NotFoundException(Messages.NOT_FOUND_TRACE));
	}
	
	
	// 특정 스터디의 초대 코드 생성하기
	public Invite createInviteCode(String studyId) {
		var study = studyRepository.findById(studyId).orElseThrow(() -> new NotFoundException(Messages.NOT_FOUND_STUDY));
		if(study.getCloseDate() != null) {
			throw new BadRequestException(Messages.ALREADY_CLOSED);
		}
		
		if(inviteRepository.findByStudyId(studyId).isPresent()) {
			return inviteRepository.findByStudyId(studyId).get();
		}
		
		var originalCode = UUID.randomUUID().toString().substring(0, 6);
		var code =Base64.getEncoder().encodeToString(originalCode.getBytes());
		var invite = Invite.builder().code(code).study(study).expired(LocalDateTime.now().plusDays(3)).build();
		
		return inviteRepository.save(invite);
	}
	
	// 특정 초대 코드로 스터디 참가 시키기
	public Study attendToStudyByInviteCode(Long userId, String inviteCode) {
		log.debug("invite code {}", inviteCode);
		var savedEntity = inviteRepository.findByCode(inviteCode).orElseThrow(() -> new NotFoundException(Messages.NOT_FOUND_CODE));
		var now = LocalDateTime.now();
		if(now.isAfter(savedEntity.getExpired())) {
			throw new BadRequestException(Messages.EXPIRED_CDDE);
		}
		if(savedEntity.getStudy().getCloseDate() != null) {
			throw new BadRequestException(Messages.ALREADY_CLOSED);
		}
		
		var user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(Messages.NOT_FOUND_USER));
		

		if (attendanceRepository.existsByUserAndStudy(user, savedEntity.getStudy())) {
			throw new BadRequestException(Messages.ALREADY_ATTENDANCE);
		}

		var attendance = Attendance.builder().study(savedEntity.getStudy()).user(user).owner(false).build();

		attendanceRepository.save(attendance);
		return savedEntity.getStudy();
	}
	
}
