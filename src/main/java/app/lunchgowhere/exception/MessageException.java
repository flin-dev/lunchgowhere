package app.lunchgowhere.exception;

import lombok.Getter;

@Getter
public class MessageException extends Throwable {

    private ErrorCode errorCode;

    // Enum definition inside the exception class
    public enum ErrorCode {
        ROOM_NOT_FOUND,
        ROOM_SUBMISSION_FAILED,
    }
    public MessageException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

}
