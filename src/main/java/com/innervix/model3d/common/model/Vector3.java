package com.innervix.model3d.common.model;

import jakarta.validation.constraints.NotNull;

public record Vector3(@NotNull Double x, @NotNull Double y, @NotNull Double z) {
    public static Vector3 zero() {
        return new Vector3(0.0, 0.0, 0.0);
    }

    public static Vector3 one() {
        return new Vector3(1.0, 1.0, 1.0);
    }
}
