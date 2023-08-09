
package org.studymate.domain.user;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.studymate.domain.auth.dto.KakaoUser;
import org.studymate.domain.auth.request.CreateTesterRequest;
import org.studymate.domain.study.entity.StudyRepository;
import org.studymate.domain.user.entity.User;
import org.studymate.domain.user.entity.UserRepository;
import org.studymate.global.exception.NotFoundException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final StudyRepository studyRepository;
	
	// 카카오 인증 유저 추가하기 - 이미 기존회원이면 기존정보 업데이트 아니면 추가
	@Transactional
	public boolean addKakaoAuthedOne(KakaoUser user) {
		Optional<User> found = userRepository.findById(user.getId());
		if (found.isPresent()) {
			user.setNew(false);
			User existed = found.get();
			existed.setAccessToken(user.getAccessToken());
			existed.setProfileImage(user.getProfileImage());
			userRepository.save(existed);
			return false;
		}

		User one = User.builder().accessToken(user.getAccessToken()).id(user.getId()).username(user.getUsername())
				.profileImage(user.getProfileImage()).provider("KAKAO").build();
		userRepository.save(one);
//		user.setNew(true);
		return true;

	}

	// 테스트 유저 추가하기
	@Transactional
	public User addTestedOne(CreateTesterRequest createTesterRequest) {
		Long id = (long)(Math.random()*10000000)+1;
		User user = User.builder() //
				.id(id) //
				.username(createTesterRequest.getUsername()) //
				.provider("MANAGED") //
				.build();

		return userRepository.save(user);
	}
	
	@Transactional
	public User getUserById(Long userId) {
		return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("해당 아이디의 유저정보가 존재하지 않습니다"));
	}
	

	// 마지막 접근한 스터디 아이디 변경하기
	@Transactional
	public void updateLastAccessStudy(Long userId, String studyId) {
		var foundUser = userRepository.findById(userId).orElseThrow(() ->  new NotFoundException("해당 유저를 찾을수 없습니다"));
		if(studyId != null) {
			var foundStudy = studyRepository.findById(studyId).orElseThrow(() ->  new NotFoundException("해당 스터디를 찾을수 없습니다")); 
			foundUser.setLastAccessedStudy(foundStudy);
		}else {
			foundUser.setLastAccessedStudy(null);
		}
		
		userRepository.save(foundUser);
	}
	
	
}











