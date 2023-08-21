package org.example.spring.exception;

public class AccessDenied extends Exception{
    public AccessDenied(String message) {
        super(message);
    }
}
