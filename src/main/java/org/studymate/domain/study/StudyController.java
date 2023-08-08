package org.studymate.domain.study;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.studymate.domain.study.entity.Study;
import org.studymate.domain.study.request.CreateStudyRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/study")
@RequiredArgsConstructor
@Slf4j
public class StudyController {

	private final StudyService studyService;
	
	@GetMapping 
	public ResponseEntity<?> handleStudyList(@RequestAttribute Long userId){
		log.debug("userId {} " , userId);
		List<Study> list = studyService.getSpecificUsersStudy(userId);
		
		return new ResponseEntity<>(Map.of("items", list), HttpStatus.OK);
	}
	
	@PostMapping 
	public ResponseEntity<?> handleCreateStudy(@RequestAttribute Long userId, @RequestBody CreateStudyRequest createStudyRequest){
		log.debug("userId {} {} " , userId, createStudyRequest);
		String studyId = studyService.addNewStudyToSpecificUser(userId, createStudyRequest);
		
		return new ResponseEntity<>(Map.of("createdId" , studyId), HttpStatus.CREATED);
	}
}
