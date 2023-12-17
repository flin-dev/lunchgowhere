package app.lunchgowhere.controller;

import app.lunchgowhere.dto.LocationSubmission;
import app.lunchgowhere.service.RoomService.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class RoomController {

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private RoomService roomService;


    //sample for stomp message forwarding
//    @MessageMapping("/hello")
//    @SendTo("/topic/greetings")
//    public greeting(LocationSubmission message) throws Exception {
//        Thread.sleep(1000); // simulated delay
//        return new Greeting("Hello, " + "nice " + "!");
//    }


    @MessageMapping("/room/{roomId}/submitLocation")
    public void submitLocation(@DestinationVariable String roomId, @Payload LocationSubmission message) throws Exception {
        log.info("Received message: {}", message);

        var result = roomService.verifyLocation(message.getEscapedObject());

        if (!result) {
            log.info("Message rejected: {}", message);
            return;
        }

        template.convertAndSend("/room/" + roomId + "/location", message);
    }


}
