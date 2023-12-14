package app.lunchgowhere.controller;

import app.lunchgowhere.dto.LocationSubmission;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class RoomController {

    @Autowired
    private SimpMessagingTemplate template;

        @MessageMapping("/room/test/submit")
        public LocationSubmission sendMessage(@DestinationVariable String roomId, @Payload LocationSubmission submission) {
            log.info(String.valueOf(submission));
            template.send("/room/test", submission);
            return submission;
        }
}
