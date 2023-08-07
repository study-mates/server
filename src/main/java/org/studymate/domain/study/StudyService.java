package org.studymate.domain.study;

import java.util.List;

import org.springframework.stereotype.Service;
import org.studymate.domain.study.entity.Study;
import org.studymate.domain.study.entity.StudyRepository;
import org.studymate.domain.study.request.CreateStudyRequest;
import org.studymate.domain.user.entity.User;
import org.studymate.domain.user.entity.UserRepository;
import org.studymate.global.exception.BadRequestException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudyService {

	private final StudyRepository studyRepository;
	private final UserRepository userRepository;

	
	public List<Study> getSpecificUsersStudy(Long userId) {
		// TODO Auto-generated method stub

		List<Study> list = studyRepository.findByUserId(userId);
		log.debug("study list {}", list);
		return list;
	}

	@Transactional
	public String addNewStudyToSpecificUser(Long userId, CreateStudyRequest createStudyRequest) {
		User user = userRepository.findById(userId).orElseThrow(() -> new BadRequestException("존재하지 않는 유저입니다"));

		Study one = Study.builder().user(user).description(createStudyRequest.getDescription())
				.targetDate(createStudyRequest.getTargetDate()).build();

		return studyRepository.save(one).getId();
	}

}
