
package org.studymate.domain.user;

import org.springframework.stereotype.Service;
import org.studymate.domain.auth.dto.SignUpRequest;
import org.studymate.domain.user.entity.User;
import org.studymate.domain.user.entity.UserRepository;
import org.studymate.global.exception.type.BadRequestException;

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
	public User addOne(SignUpRequest req) {
		User user = User.builder() //
				.username(req.getUsername()) //
				.profileImg(req.getProfileImg()) //
				.alarm(req.isAlarm()) //
				.build();

		return userRepository.save(user);
	}
}
