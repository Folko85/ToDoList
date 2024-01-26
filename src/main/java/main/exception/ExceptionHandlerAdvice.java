package main.exception;

import java.util.Arrays;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;
import main.dto.response.ErrorResponse;

@ControllerAdvice
@Slf4j
class ExceptionHandlerAdvice {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException exc) {
        ErrorResponse badResponse = new ErrorResponse("unauthorized", exc.getMessage());
        log.warn(Arrays.toString(exc.getStackTrace()));
        return new ResponseEntity<>(badResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserExistException.class)
    public ResponseEntity<ErrorResponse> handleRegisterUserExistException(UserExistException exc) {
        ErrorResponse badRequestResponse = new ErrorResponse("invalid_request", exc.getMessage());
        return new ResponseEntity<>(badRequestResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IncorrectNiknameException.class)
    public ResponseEntity<ErrorResponse> handleIncorrectNicknameException(IncorrectNiknameException exc) {
        ErrorResponse badRequestResponse = new ErrorResponse("invalid_request", exc.getMessage());
        return new ResponseEntity<>(badRequestResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException exc) {
        ErrorResponse badRequestResponse = new ErrorResponse("not_found", exc.getMessage());
        return new ResponseEntity<>(badRequestResponse, HttpStatus.NOT_FOUND);
    }

}
