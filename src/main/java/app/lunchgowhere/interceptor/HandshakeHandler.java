package app.lunchgowhere.interceptor;

import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Component
public class HandshakeHandler extends DefaultHandshakeHandler{

    private RedisTemplate<String, String> redisTemplate; // Inject RedisTemplate

    @Autowired
    public HandshakeHandler(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {
        // add your own code to determine the user
        //cast to ServletServerHttpRequest to get cookies, as currently all ws is upgraded from http
        //expecting user to be logged in, and sessionId to be stored in cookie
        ServletServerHttpRequest servletServerRequest = (ServletServerHttpRequest) request;

        // Extract sessionId from the request's cookies
        Optional<String> sessionId = extractSessionId(servletServerRequest);

        System.out.println("sessionId: " + sessionId);

        Principal principal = null;
        if (sessionId.isEmpty()) {
            return principal;
        }

        //      Retrieve user data from Redis using sessionId
        String userData = redisTemplate.opsForValue().get("USERSESSION:" + sessionId.get());

        principal = new Principal() {
            @Override
            public String getName() {
                return userData;
            }
        };

        return principal;
    }

    private Optional<String> extractSessionId(ServletServerHttpRequest request) {
        var cookies = (request.getServletRequest().getCookies());
        return cookies == null ? Optional.empty() : Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("sId"))
                .findFirst()
                .map(Cookie::getValue);
    }
}