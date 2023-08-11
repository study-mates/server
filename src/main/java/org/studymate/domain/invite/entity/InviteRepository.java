package org.studymate.domain.invite.entity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;



public interface InviteRepository extends JpaRepository<Invite, Long>{
	public Optional<Invite> findByCode(String code);
	public Optional<Invite> findByStudyId(String studyId);
}
