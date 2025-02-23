package com.cgvsu.objwriter;

public class ObjWriterException extends RuntimeException {
    public ObjWriterException(String message) {
        super("Error writing the Model to the OBJ file: " + message);
    }
}
