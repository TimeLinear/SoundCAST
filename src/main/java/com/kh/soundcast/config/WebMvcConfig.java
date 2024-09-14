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
		
        registry.addResourceHandler("/resource/**")
                .addResourceLocations("file:/" + osRootPath + uploadBaseDir)
                .setCachePeriod(0);
    }
}
