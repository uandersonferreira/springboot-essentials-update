package br.com.uanderson.springboot2essentials.handler;

import br.com.uanderson.springboot2essentials.exeption.BadRequestException;
import br.com.uanderson.springboot2essentials.exeption.BadRequestExceptionDetails;
import br.com.uanderson.springboot2essentials.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
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

}//clas
