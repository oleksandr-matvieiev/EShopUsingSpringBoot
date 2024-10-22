package org.example.sbappwithprofilesandconfigurations.Exception;

public class ProductNotFoundException extends RuntimeException{
    public ProductNotFoundException(String message){
        super(message);
    }
}
