package com.mindtech.library.exception.custom;

import org.jetbrains.annotations.NotNull;

public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(@NotNull final String message) {
        super(message);
    }
}
