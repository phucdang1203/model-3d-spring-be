package com.innervix.model3d.common;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@RestController
public class UploadedFileController {

    private static final Logger log = LoggerFactory.getLogger(UploadedFileController.class);

    private final Path uploadRoot;

    public UploadedFileController(FileStorageService fileStorageService) {
        this.uploadRoot = fileStorageService.getUploadRoot();
    }

    @GetMapping({"/upload-files/**", "/uploads/**"})
    public ResponseEntity<Resource> readUploadedFile(HttpServletRequest request) {
        String requestUri = URLDecoder.decode(request.getRequestURI(), StandardCharsets.UTF_8);
        String prefix = requestUri.startsWith(FileStorageService.PUBLIC_UPLOAD_PATH + "/")
                ? FileStorageService.PUBLIC_UPLOAD_PATH + "/"
                : FileStorageService.LEGACY_PUBLIC_UPLOAD_PATH + "/";
        String relativePath = requestUri.substring(prefix.length());

        Path file = resolveUpload(relativePath)
                .orElseThrow(() -> {
                    log.warn("Uploaded file not found: requestUri={}, relativePath={}, uploadRoot={}",
                            requestUri, relativePath, uploadRoot);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Uploaded file not found");
                });

        MediaType mediaType = mediaTypeFor(file);
        log.debug("Serving uploaded file: requestUri={}, relativePath={}, file={}, mediaType={}",
                requestUri, relativePath, file, mediaType);

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CACHE_CONTROL, "public, max-age=31536000, immutable")
                .body(new FileSystemResource(file));
    }

    private Optional<Path> resolveUpload(String relativePath) {
        List<Path> candidates = new ArrayList<>();
        addCandidate(candidates, relativePath);

        if (relativePath.contains("/original.")) {
            addCandidate(candidates, relativePath.replace("/original.", "/source."));
        }

        addModelCategoryCandidates(candidates, relativePath);

        for (Path candidate : candidates) {
            if (Files.isRegularFile(candidate)) {
                if (!candidate.equals(candidates.get(0))) {
                    log.info("Resolved uploaded file via fallback: requested={}, resolved={}",
                            candidates.get(0), candidate);
                }
                return Optional.of(candidate);
            }
        }

        log.warn("Uploaded file candidates missing: relativePath={}, candidates={}", relativePath, candidates);
        return Optional.empty();
    }

    private void addCandidate(List<Path> candidates, String relativePath) {
        Path candidate = uploadRoot.resolve(relativePath).normalize();
        if (!candidate.startsWith(uploadRoot)) {
            log.warn("Blocked uploaded file path traversal: uploadRoot={}, relativePath={}, candidate={}",
                    uploadRoot, relativePath, candidate);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid upload path");
        }
        candidates.add(candidate);
    }

    private void addModelCategoryCandidates(List<Path> candidates, String relativePath) {
        String[] parts = relativePath.split("/");
        if (parts.length < 3 || !"models".equals(parts[0])) {
            return;
        }

        String modelId = parts[1];
        String requestedFilename = parts[parts.length - 1];
        String fallbackFilename = requestedFilename.replaceFirst("^original\\.", "source.");
        Path modelsRoot = uploadRoot.resolve("models").normalize();
        if (!Files.isDirectory(modelsRoot)) {
            return;
        }

        try (Stream<Path> categoryDirs = Files.list(modelsRoot)) {
            categoryDirs
                    .filter(Files::isDirectory)
                    .map(categoryDir -> categoryDir.resolve(modelId).resolve(fallbackFilename).normalize())
                    .filter(candidate -> candidate.startsWith(uploadRoot))
                    .forEach(candidates::add);
        } catch (Exception ex) {
            log.warn("Could not inspect model category upload folders: modelsRoot={}, modelId={}",
                    modelsRoot, modelId, ex);
        }
    }

    private MediaType mediaTypeFor(Path file) {
        String name = file.getFileName().toString().toLowerCase();
        if (name.endsWith(".glb")) {
            return MediaType.parseMediaType("model/gltf-binary");
        }
        if (name.endsWith(".gltf")) {
            return MediaType.parseMediaType("model/gltf+json");
        }
        if (name.endsWith(".fbx") || name.endsWith(".obj") || name.endsWith(".stl") || name.endsWith(".ply")) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
        try {
            String detected = Files.probeContentType(file);
            if (detected != null && !detected.isBlank()) {
                return MediaType.parseMediaType(detected);
            }
        } catch (Exception ex) {
            log.debug("Could not detect content type for uploaded file: file={}", file, ex);
        }
        return MediaType.APPLICATION_OCTET_STREAM;
    }
}
