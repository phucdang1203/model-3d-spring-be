package com.innervix.model3d.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;

import static com.innervix.model3d.common.FileStorageService.PUBLIC_UPLOAD_PATH;

@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class WebMvcConfig implements WebMvcConfigurer {

    private final AppProperties properties;

    public WebMvcConfig(AppProperties properties) {
        this.properties = properties;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String location = Path.of(properties.getUploadDir()).toAbsolutePath().normalize().toUri().toString();
        registry.addResourceHandler(PUBLIC_UPLOAD_PATH + "/**").addResourceLocations(location);
        registry.addResourceHandler("/uploads/**").addResourceLocations(location);
    }
}
