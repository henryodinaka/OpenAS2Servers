package connect.as2.as2middleware.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import connect.as2.as2middleware.config.APIException;
import connect.as2.as2middleware.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.naming.AuthenticationException;
import java.util.Objects;

@ControllerAdvice
@RestController
@Slf4j
public class ControllerErrorHandlers extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> noAccessException(Exception e) {
        log.error("Unknown Exception", e);
        return ResponseEntity.badRequest().body(new Response("100", e.getMessage()+"your request",null));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Response> handleMaxUploadSizeExceededExceptionException(MaxUploadSizeExceededException e) {
        log.error("IllegalArgumentException Exception", e);
        return ResponseEntity.badRequest().body(new Response("400", e.getLocalizedMessage().split(";")[0]+" 1MB required",null));
    }

    @ExceptionHandler(APIException.class)
    public ResponseEntity<Response> handleSizeLimitExceededExceptionException(APIException e) {
        log.error("IllegalArgumentException Exception", e);
        return ResponseEntity.badRequest().body(new Response(e.getResponse().getCode(), e.getResponse().getStatus(),null));
    }


}
