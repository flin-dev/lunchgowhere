package app.lunchgowhere.dto.request;

import lombok.Data;

import java.util.Date;

@Data
public class MessageDto {

        private String message;

        private String sender;

        private String sendTime;
}
