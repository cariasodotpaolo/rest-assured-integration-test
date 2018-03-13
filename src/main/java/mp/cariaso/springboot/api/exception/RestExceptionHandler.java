package mp.cariaso.springboot.api.exception;

import java.util.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler( IllegalArgumentException.class )
    protected ResponseEntity<?> handleBadRequest(WebRequest request, Exception exception) {
        String userErrorMessage = "Please check your request details.";

        return handleExceptionInternal(exception, userErrorMessage,
            new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler( value = { UnauthorizedException.class } )
    protected ResponseEntity<?> handleUnauthorized(WebRequest request, Exception exception) {
        String userErrorMessage = "This request is not authorized.";

        return handleExceptionInternal(exception, userErrorMessage,
            new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler( value = { AuthenticationException.class, UsernameNotFoundException.class } )
    protected ResponseEntity<?> handleInvalidLogin(WebRequest request, Exception exception) {

        logger.error("INVALID LOGIN", exception);

        String userErrorMessage = "There is a problem logging in to your account. Please check your username and password.";

        return handleExceptionInternal(exception, userErrorMessage,
            new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler( ForbiddenException.class )
    protected ResponseEntity<?> handleForbidden(WebRequest request, Exception exception) {

        logger.error("FORBIDDEN", exception);

        String userErrorMessage = "You do not have sufficient access privilege.";

        return handleExceptionInternal(exception, userErrorMessage,
            new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler( value = {NotFoundException.class, NoSuchElementException.class} )
    protected ResponseEntity<?> handleNotFound(WebRequest request, Exception exception) {

        logger.error("NOT FOUND", exception);

        String userErrorMessage = "Unable to find the details regarding your request.";

        return handleExceptionInternal(exception, userErrorMessage,
            new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler( RuntimeException.class )
    protected ResponseEntity<?> handleInternalServerError(WebRequest request, Exception exception) {

        logger.error("INTERNAL SERVER ERROR", exception);

        String userErrorMessage = "Unable to process your request at this time.";

        return handleExceptionInternal(exception, userErrorMessage,
            new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

}