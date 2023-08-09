package org.studymate.global;

import java.time.LocalDate;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class HealthCheckController {

	@GetMapping("/")
	public String handleHealthCheck() {
		log.debug("LocalDate.now {} ", LocalDate.now());
		return "health check success";
	}
}
