package br.com.uanderson.springboot2essentials.handler;

import br.com.uanderson.springboot2essentials.exeption.BadRequestException;
import br.com.uanderson.springboot2essentials.exeption.BadRequestExceptionDetails;
import br.com.uanderson.springboot2essentials.exeption.ExceptionDetails;
import br.com.uanderson.springboot2essentials.exeption.ValidationExceptionDetails;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Log4j2
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
   /*
   handler Global -  Manipulação/Tratamento global

    */

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<BadRequestExceptionDetails> handleBadRequestException(BadRequestException badRequestException){
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
    @SuppressWarnings("java:S1854")
    @Override //Sobrescrevendo um method de tratamento de erro do spring para um personalizado com nossos atributos
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

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

    @Override //Sobrescrevendo um method de tratamento de erro do spring para um personalizado com nossos atributos
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers,
                                                             HttpStatusCode statusCode, WebRequest request) {

        ExceptionDetails exceptionDetails = ExceptionDetails.builder()
                .title(ex.getCause().getMessage())
                .status(statusCode.value())
                .details(ex.getMessage())
                .developerMessage(ex.getClass().getName())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(exceptionDetails, headers, statusCode);

    }//handleExceptionInternal

}//clas
/*

OBS: A fim de manter uma padronização das mensagens de erros do spring,
podemos sobreescrever o comportamento dos method já existente de tratamento
para um, que tenha como retorno o que queremos. Dentre as classes de erros que podemos
sobrescrever temos:

	@ExceptionHandler({
			HttpRequestMethodNotSupportedException.class,
			HttpMediaTypeNotSupportedException.class,
			HttpMediaTypeNotAcceptableException.class,
			MissingPathVariableException.class,
			MissingServletRequestParameterException.class,
			MissingServletRequestPartException.class,
			ServletRequestBindingException.class,
			MethodArgumentNotValidException.class,
			NoHandlerFoundException.class,
			AsyncRequestTimeoutException.class,
			ErrorResponseException.class,
			ConversionNotSupportedException.class,
			TypeMismatchException.class,
			HttpMessageNotReadableException.class,
			HttpMessageNotWritableException.class,
			BindException.class
		})
 */