package app.lunchgowhere.config;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    @Autowired
    private final RedisTemplate<String, String> redisTemplate;

    @EventListener
    public void handleWebSocketDisconnectSession(SessionDisconnectEvent event) {
        log.info("User {} Disconnected ", Objects.requireNonNull(event.getUser()).getName());


        //clean up user related data from redis when user disconnect
        var rooms = redisTemplate.opsForSet().members("USER:"+ event.getUser().getName()+":ROOMS");
        if(rooms != null) {
            for (String room : rooms) {
                redisTemplate.opsForSet().remove("ROOM:"+room+":ONLINE", event.getUser().getName());
                redisTemplate.opsForSet().remove("USER:"+event.getUser().getName()+":ROOMS", room);
            }
        }
        var sessionId = redisTemplate.opsForValue().get("USERNAME:"+event.getUser().getName());
        //do not remove user session from redis when user disconnect, as user may reconnect
//        redisTemplate.delete("USERNAME:"+event.getUser().getName());
//        redisTemplate.delete("USERSESSION:"+Objects.requireNonNull(sessionId));
//        redisTemplate.delete("USER:"+event.getUser().getName()+":ROOMS");
    }

}
