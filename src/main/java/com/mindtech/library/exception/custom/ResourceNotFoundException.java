package com.mindtech.library.exception.custom;

import org.jetbrains.annotations.NotNull;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(@NotNull final String message) {
        super(message);
    }
}
