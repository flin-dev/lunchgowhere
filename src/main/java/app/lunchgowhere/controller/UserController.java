package app.lunchgowhere.controller;


import app.lunchgowhere.dto.request.LoginDto;
import app.lunchgowhere.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@Slf4j
public class UserController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Login with username")
    @ApiResponse(responseCode = "200", description = "Login successfully")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @ApiResponse(responseCode = "403", description = "Unauthorized")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    public ResponseEntity<String> login(HttpServletResponse response,@RequestBody LoginDto loginDto) {
        if(loginDto.getUsername() == null) {
            return ResponseEntity.badRequest().body("Bad Request");
        }
        //call service to check if user id created before
        var user = userService.getUserByUsername(loginDto.getUsername());

        if(user == null) {
            //create user
            user = userService.createUser(loginDto.getUsername());
        }

        //set value with one hour expiry for redis key
        var sessionId = redisTemplate.opsForValue().get("USERNAME:"+loginDto.getUsername());

        if (sessionId != null) {
            return ResponseEntity.status(403).body("User already login");
        }

        //create sessionId
        sessionId = java.util.UUID.randomUUID().toString();

        //store sessionId and username in redis
        //username as key, sessionId as value
        var usernameKey = "USERNAME:"+loginDto.getUsername();
        redisTemplate.opsForValue().set(usernameKey, sessionId);
        redisTemplate.expire(usernameKey, 60 * 60, TimeUnit.SECONDS);
        //sessionId as key, username as value
        var userSessionKey = "USERSESSION:"+sessionId;
        redisTemplate.opsForValue().set(userSessionKey,loginDto.getUsername());
        redisTemplate.expire(userSessionKey, 60 * 60, TimeUnit.SECONDS);

        Cookie cookie = new Cookie("sId", sessionId);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60 * 60); // 1 hour

        response.addCookie(cookie);


        log.info("user login :", loginDto.getUsername(), sessionId);
        return ok().body(sessionId);
    }

}
