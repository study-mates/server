package org.studymate.domain.invite;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.studymate.domain.invite.request.HandleInviteRequest;
import org.studymate.domain.study.StudyService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/invite")
@RequiredArgsConstructor
@CrossOrigin("*")
@Slf4j
public class InviteController {
	private final StudyService studyService;

	// 초대 코드 처라하기 
	@PostMapping
	public ResponseEntity<?> handleInviteCode(@RequestAttribute Long userId, @RequestBody HandleInviteRequest req) {
		log.debug("req {}",req);
		var entity = studyService.attendToStudyByInviteCode(userId, req.getCode());

		return new ResponseEntity<>(Map.of("status", "Ok", "studyId", entity.getId()), HttpStatus.OK);
	}

}
