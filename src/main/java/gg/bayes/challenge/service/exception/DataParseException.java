package gg.bayes.challenge.service.exception;

import lombok.Data;

@Data
public class DataParseException extends RuntimeException {
    private String message;

    public DataParseException(String message) {
        this.message = message;
    }

}
