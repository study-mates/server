package org.studymate.global.util;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.studymate.global.constant.Messages;
import org.studymate.global.exception.BadRequestException;
import org.studymate.global.exception.InternalServerErrorException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class FileHandler {
	@Value("${upload.dir}")
	private String uploadDir;

	public String save(MultipartFile file, String path) {
		if (file == null) {
			return null;
		}
		if (!file.getContentType().toLowerCase().startsWith("image")) {
			throw new BadRequestException(Messages.NOT_SUPPORTED);
		}
		File savePath = new File(uploadDir, path);
		savePath.mkdirs();
		try {
			String filename = System.currentTimeMillis()+ (file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")));
			File dest = new File(savePath, filename);
			if(!dest.getParentFile().exists()) {
				dest.getParentFile().mkdirs();
			}
			file.transferTo(dest);
			Thread.sleep(10);
			String url = (savePath+"\\"+filename).replace("\\", "/");
			System.out.println(filename);
			return filename;
		} catch (Exception e) {
			log.debug("save error {}" ,e.getMessage());
			throw new InternalServerErrorException(Messages.TRANSFER_FAIL);
		}
	}

	public boolean delete(String pathname) {
		File file = new File(pathname);
		if (!file.exists()) {
			return false;
		}
		try {
			file.delete();
			return true;
		} catch (Exception e) {
			log.debug("delete error {}", e.getMessage());
			// throw new InternalServerErrorException(Messages.TRANSFER_FAIL);
			return false;
		}
	}
}
