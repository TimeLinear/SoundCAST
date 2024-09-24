package com.kh.soundcast.config;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	
	@Value("${file.upload-dir}")
	private String uploadBaseDir; // SoundCastWorkspace/SoundCAST_resources/
	
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
		Path path = FileSystems.getDefault().getRootDirectories().iterator().next();
		final String osRootPath = path.toString().replace("\\", "");
		
        // "/images/**" URL 패턴을 외부 디렉토리와 매핑
        registry.addResourceHandler("/resource/**")
                .addResourceLocations("file:/" + osRootPath + uploadBaseDir)
                .setCachePeriod(0);  // 캐시 기간을 0으로 설정하여 즉시 반영
    }
}
