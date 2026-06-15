package com.innervix.model3d.domain.animation.model;

import java.util.List;

public record AnimationUploadRequest(String name, String description, List<String> tags) {
}
