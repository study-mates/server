
package org.studymate.domain.user;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.studymate.domain.auth.dto.KakaoUser;
import org.studymate.domain.auth.request.SignUpRequest;
import org.studymate.domain.user.entity.User;
import org.studymate.domain.user.entity.UserRepository;
import org.studymate.global.exception.BadRequestException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	@Transactional
	public User getOneByUserId(Long userId) {
		return userRepository.findById(userId) //
				.orElseThrow(() -> new BadRequestException("존재하지 않는 유저 아이디입니다."));
	}

	@Transactional
	public User addManagedOne(SignUpRequest req) {
		User user = User.builder() //
				.username(req.getUsername()) //
				.profileImage(req.getProfileImage()) //
				.alarm(req.isAlarm()) //
				.provider("MANAGED") //
				.build();

		return userRepository.save(user);
	}

	@Transactional
	public User addKakaoAuthedOne(KakaoUser user) {
		Optional<User> found = userRepository.findById(user.getId());
		if (found.isPresent()) {
			User existed = found.get();
			existed.setAccessToken(user.getAccessToken());
			existed.setProfileImage(user.getProfileImage());
			return existed;
		}
		
		User one = User.builder().accessToken(user.getAccessToken()).id(user.getId()).username(user.getUsername())
				.profileImage(user.getProfileImage()).provider("KAKAO").build();
		User created = userRepository.save(one);
		return created;

	}
}
