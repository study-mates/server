package org.studymate.domain.study.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class AddTraceRequest {
	
	private String title;
	private String description;
	
	private List<MultipartFile> images;
	
	
}
