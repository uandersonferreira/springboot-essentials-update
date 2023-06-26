package br.com.uanderson.springboot2essentials.handler;

import br.com.uanderson.springboot2essentials.exeption.BadRequestException;
import br.com.uanderson.springboot2essentials.exeption.BadRequestExceptionDetails;
import br.com.uanderson.springboot2essentials.exeption.ValidationExceptionDetails;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Log4j2
public class RestExceptionHandler {
   /*
   handler Global -  Manipulação/Tratamento global

    */

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<BadRequestExceptionDetails> handlerBadRequestException(BadRequestException badRequestException){
        return new ResponseEntity<>(
          BadRequestExceptionDetails.builder()
                  .title("Bad Request Exception, Check the Documentation")
                  .status(HttpStatus.BAD_REQUEST.value())
                  .details(badRequestException.getMessage())
                  .developerMessage(badRequestException.getClass().getName())
                  .timestamp(LocalDateTime.now())
                  .build(), HttpStatus.BAD_REQUEST
        );

        /*
            @ControllerAdvice: é usada para definir um componente global que lida com exceções
            em vários controladores do Spring. Ela oferece a possibilidade de tratar exceções de
            forma centralizada, personalizar respostas de erro e compartilhar atributos em modelos.
            Isso ajuda a melhorar a consistência e a modularidade do tratamento de exceções em toda
            a aplicação. DE certa, forma pode ser visto como uma barreira que intercepta todas as exceções.


            O @ExceptionHandler(exceptionPersonalizada.class) é uma anotação usada para lidar com as exceções
            específicas e o envio das respostas personalizadas ao cliente.
                 */

    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @SuppressWarnings("java:S1854")
    public ResponseEntity<ValidationExceptionDetails> handlerBadRequestException(
            MethodArgumentNotValidException exception){
        log.info("Fields: {}", exception.getBindingResult().getFieldError().getField());//pegar um campo com erro

        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();

        String fields = fieldErrors.stream().map(FieldError::getField).collect(Collectors.joining(", "));
        String fieldsMessage = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));

        return new ResponseEntity<>(
          ValidationExceptionDetails.builder()
                  .title("Bad Request Exception, Invalid Fields")
                  .status(HttpStatus.BAD_REQUEST.value())
                  .details("Check the field(s) error")
                  .developerMessage(exception.getClass().getName())
                  .timestamp(LocalDateTime.now())
                  .fields(fields)
                  .fieldsMessage(fieldsMessage)
                  .build(), HttpStatus.BAD_REQUEST
        );

    }

}//clas
