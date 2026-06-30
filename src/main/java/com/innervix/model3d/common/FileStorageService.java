package com.innervix.model3d.common;

import com.innervix.model3d.config.AppProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Set;

@Service
public class FileStorageService {

    public static final String PUBLIC_UPLOAD_PATH = "/upload-files";
    public static final String LEGACY_PUBLIC_UPLOAD_PATH = "/uploads";

    private static final Logger log = LoggerFactory.getLogger(FileStorageService.class);

    private static final Set<String> MODEL_FORMATS = Set.of("glb", "gltf", "obj", "fbx", "stl", "ply", "usdz");
    private static final Set<String> ANIMATION_FORMATS = Set.of("fbx", "zip");

    private final Path uploadRoot;

    public FileStorageService(AppProperties properties) {
        this.uploadRoot = Path.of(properties.getUploadDir()).toAbsolutePath().normalize();
        try {
            Files.createDirectories(uploadRoot);
            log.info("Control3D upload root ready: uploadRoot={}, publicPaths=[{}, {}]",
                    uploadRoot, PUBLIC_UPLOAD_PATH, LEGACY_PUBLIC_UPLOAD_PATH);
        } catch (Exception ex) {
            log.error("Control3D upload root could not be created: uploadRoot={}", uploadRoot, ex);
            throw new IllegalStateException("Could not initialize upload directory: " + uploadRoot, ex);
        }
    }

    public Path getUploadRoot() {
        return uploadRoot;
    }

    public StoredFile storeModel(String id, MultipartFile file) {
        return store("models", id, file, MODEL_FORMATS);
    }

    public StoredFile storeModel(String category, String id, MultipartFile file) {
        String safeCategory = sanitizeSegment(category == null || category.isBlank() ? "other" : category);
        return store(Path.of("models", safeCategory), id, file, MODEL_FORMATS, PUBLIC_UPLOAD_PATH + "/models/" + safeCategory);
    }

    public StoredFile storeAnimation(String id, MultipartFile file) {
        return store("animations", id, file, ANIMATION_FORMATS);
    }

    private StoredFile store(String group, String id, MultipartFile file, Set<String> allowedFormats) {
        return store(Path.of(group), id, file, allowedFormats, PUBLIC_UPLOAD_PATH + "/" + group);
    }

    private StoredFile store(Path groupPath, String id, MultipartFile file, Set<String> allowedFormats, String publicBaseUrl) {
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename() == null ? "source" : file.getOriginalFilename());
        String format = extension(originalFilename);
        if (!allowedFormats.contains(format)) {
            throw new IllegalArgumentException("Unsupported file format: " + format);
        }

        try {
            Path directory = uploadRoot.resolve(groupPath).resolve(id).normalize();
            Files.createDirectories(directory);
            Path target = directory.resolve("source." + format).normalize();
            file.transferTo(target);
            String publicUrl = publicBaseUrl + "/" + id + "/source." + format;
            log.info("Stored uploaded file: id={}, originalFilename={}, format={}, size={}, target={}, publicUrl={}",
                    id, originalFilename, format, file.getSize(), target, publicUrl);
            return new StoredFile(originalFilename, format, publicUrl, file.getSize());
        } catch (Exception ex) {
            log.error("Could not store uploaded file: id={}, groupPath={}, originalFilename={}, uploadRoot={}",
                    id, groupPath, originalFilename, uploadRoot, ex);
            throw new IllegalStateException("Could not store uploaded file", ex);
        }
    }

    private String sanitizeSegment(String segment) {
        String clean = segment.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9_-]", "-");
        return clean.isBlank() ? "other" : clean;
    }

    private String extension(String filename) {
        int dot = filename.lastIndexOf('.');
        if (dot < 0 || dot == filename.length() - 1) {
            return "";
        }
        return filename.substring(dot + 1).toLowerCase(Locale.ROOT);
    }

    public record StoredFile(String originalFilename, String format, String fileUrl, long size) {
    }
}
