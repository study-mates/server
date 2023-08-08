
package org.studymate.domain.user;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.studymate.domain.auth.dto.KakaoUser;
import org.studymate.domain.auth.request.CreateTesterRequest;
import org.studymate.domain.user.entity.User;
import org.studymate.domain.user.entity.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	@Transactional
	public User addKakaoAuthedOne(KakaoUser user) {
		Optional<User> found = userRepository.findById(user.getId());
		if (found.isPresent()) {
			user.setNew(false);
			User existed = found.get();
			existed.setAccessToken(user.getAccessToken());
			existed.setProfileImage(user.getProfileImage());
			return existed;
		}

		User one = User.builder().accessToken(user.getAccessToken()).id(user.getId()).username(user.getUsername())
				.profileImage(user.getProfileImage()).provider("KAKAO").build();
		User created = userRepository.save(one);
		user.setNew(true);
		return created;

	}

	public User addTestedOne(CreateTesterRequest createTesterRequest) {
		Long id = (long)(Math.random()*10000000)+1;
		User user = User.builder() //
				.id(id) //
				.username(createTesterRequest.getUsername()) //
				.provider("MANAGED") //
				.build();

		return userRepository.save(user);
	}
}
