package org.studymate.domain.user.entity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long>{

	//
	Optional<User> findByUsername(String username);
}