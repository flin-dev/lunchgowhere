package app.lunchgowhere.controller;

import app.lunchgowhere.dto.request.LocationSubmissionDto;
import app.lunchgowhere.dto.request.MessageDto;
import app.lunchgowhere.dto.request.RoomDto;
import app.lunchgowhere.exception.MessageException;
import app.lunchgowhere.exception.RoomException;
import app.lunchgowhere.model.Room;
import app.lunchgowhere.service.RoomService.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@Slf4j
public class RoomController {

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private RoomService roomService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    //sample for stomp message forwarding
//    @MessageMapping("/hello")
//    @SendTo("/topic/greetings")
//    public greeting(LocationSubmission message) throws Exception {
//        Thread.sleep(1000); // simulated delay
//        return new Greeting("Hello, " + "nice " + "!");
//    }x`

    @Operation(summary = "Get Rooms", description = "Get Rooms listing with pagination")
    @ApiResponse(responseCode = "200", description = "Get Rooms successfully",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Room.class))})
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    @GetMapping("/rooms")
        public ResponseEntity<Page<List<Room>>> getRooms(@RequestParam(defaultValue = "0") @Min(0) int pageNum,
                                                     @RequestParam(defaultValue = "10") @Min(1) int pageSize) {
        //get room with pageable that get pageNum and pagesize from request
        var rooms = roomService.getRooms(pageNum, pageSize);
        return ResponseEntity.ok(rooms);
    }

    @Operation(summary = "Get Room", description = "Get Room by roomId")
    @ApiResponse(responseCode = "200", description = "Get Room successfully",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Room.class))})
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    @GetMapping("/room/{roomId}")
    public ResponseEntity<Room> getRoom(@PathVariable @Min(0) Long roomId) {
        var room = roomService.getRoom(roomId);
        return ResponseEntity.ok(room);
    }


    @Operation(summary = "Create Room", description = "Create Room")
    @ApiResponse(responseCode = "200", description = "Create Room successfully",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Room.class))})
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    public ResponseEntity<Room> createRoom(@RequestParam RoomDto roomDto) {
        var savedRoom = roomService.createRoom(roomDto);
        return ResponseEntity.ok(savedRoom);
    }


//-------------------------web socket related implementation-------------------------
    @MessageMapping("/room/{roomId}/submission")
    public void submitLocation(@DestinationVariable String roomId, @Payload LocationSubmissionDto message) throws Exception {
        log.info("Received message: {}", message);

        var result = roomService.verifyLocation(message.getEscapedObject());

        if (!result) {
            log.info("Message rejected: {}", message);
            return;
        }

        template.convertAndSend("/room/" + roomId + "/location", message);
    }

    @MessageMapping("/room/{roomId}/close")
    public void submitLocation(@DestinationVariable String roomId, Principal principal) throws Exception {
        log.info("Room closed: room id {}", roomId);

        var selectedLocation = roomService.closeAndPickLocation(roomId, principal.getName());


        //throw room exception if room permission denied
        if (selectedLocation.isEmpty()) {
            throw new MessageException("Room permission denied");
        }

        //TODO send close message to all users in the room
        //define the endpoint latter
//        template.convertAndSend("/room/" + roomId + "/submission/close", selectedLocation.get());
    }


    //forward normal chat message to all users in the room
    @MessageMapping("/room/{roomId}/chat")
    public void chat(@DestinationVariable String roomId, @Payload MessageDto message) throws Exception {
        log.info("Received message: {}", message);
        template.convertAndSend("/app/room/" + roomId + "/chat", message);
        template.convertAndSend("/room/" + roomId + "/chat", message);
    }

    //capture subscribe event of /room/{roomId}/chat and set userId to redis ROOMID:ONLINE:USER capture online users
            @SubscribeMapping("/room/{roomId}/chat")
    public void subscribe(@DestinationVariable String roomId, Principal principal) throws Exception {
        //check if room key exist in redis
        var exist = redisTemplate.opsForValue().get("ROOM:" + roomId);
        //add user to room's online user list
        redisTemplate.opsForSet().add("ROOM:" + roomId + ":ONLINE", principal.getName());
        //add roomId to user's joined room list
        redisTemplate.opsForSet().add("USER:" + principal.getName() + ":ROOMS", roomId);
        log.info("New user {} joined room {}", principal.getName(), roomId);
    }

    @MessageExceptionHandler(RoomException.class)
    @SendToUser("/room/error")
    public String handleRoomException(Throwable exception) {
        return "Server Error";
    }

    //handle room's message related exception and send message to user topic /room/error to notify user
    @MessageExceptionHandler(MessageException.class)
    @SendToUser("/room/error")
    public String handleException(Throwable exception) {
        return "Server Error";
    }

}
