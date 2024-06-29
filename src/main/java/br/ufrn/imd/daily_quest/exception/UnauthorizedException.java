package br.ufrn.imd.daily_quest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends Exception {

    public UnauthorizedException(String message) {
        super(message);
    }
}
