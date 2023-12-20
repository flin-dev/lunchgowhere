package app.lunchgowhere.exception;

import lombok.Getter;

@Getter
public class MessageException extends RoomException {

    public MessageException(String message) {
          super(message);
    }


}
