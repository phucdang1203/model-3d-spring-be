package com.innervix.model3d.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;

import static com.innervix.model3d.common.FileStorageService.PUBLIC_UPLOAD_PATH;

@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class WebMvcConfig implements WebMvcConfigurer {

    private static final Logger log = LoggerFactory.getLogger(WebMvcConfig.class);

    private final AppProperties properties;

    public WebMvcConfig(AppProperties properties) {
        this.properties = properties;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadRoot = Path.of(properties.getUploadDir()).toAbsolutePath().normalize();
        try {
            Files.createDirectories(uploadRoot);
        } catch (Exception ex) {
            log.error("Could not create upload static directory: uploadRoot={}", uploadRoot, ex);
        }
        String location = uploadRoot.toUri().toString();
        registry.addResourceHandler(PUBLIC_UPLOAD_PATH + "/**").addResourceLocations(location);
        registry.addResourceHandler("/uploads/**").addResourceLocations(location);
        log.info("Registered upload static handlers: {} -> {}, /uploads/** -> {}",
                PUBLIC_UPLOAD_PATH + "/**", location, location);
    }
}
