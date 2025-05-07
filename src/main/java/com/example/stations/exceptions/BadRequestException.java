package com.example.stations.exceptions;

import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
    public BadRequestException(FirebaseMessagingException message) {
        super(message);
    }
}
