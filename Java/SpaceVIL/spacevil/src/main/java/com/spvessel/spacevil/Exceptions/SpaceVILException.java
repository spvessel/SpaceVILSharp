package com.spvessel.spacevil.Exceptions;

public class SpaceVILException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    // static final long serialVersionUID = 0;
    public SpaceVILException() { }

    public SpaceVILException(String message) {
        super(message);
    }
}