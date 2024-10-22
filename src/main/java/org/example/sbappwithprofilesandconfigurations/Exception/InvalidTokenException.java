package org.example.sbappwithprofilesandconfigurations.Exception;

public class InvalidTokenException extends RuntimeException{
    public InvalidTokenException(String message){
        super(message);
    }
}
