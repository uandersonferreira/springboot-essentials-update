package br.com.uanderson.springboot2essentials.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException{
    public BadRequestException(String message){
        super(message);
    }
    /*
    Exception (para exceções verificadas) ou RuntimeException (para exceções não verificadas)
     */
}
