package app.lunchgowhere.service.RoomService.impl;

import app.lunchgowhere.dto.LocationSubmission;
import app.lunchgowhere.service.RoomService.RoomService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

@Service
public class RoomServiceImpl implements RoomService {

    private RedisTemplate<String, String> redisTemplate;

    @Override
    public Boolean verifyLocation(LocationSubmission message) {
        var exist = redisTemplate.opsForSet().isMember("user", message.getSender());
        if (!exist) return false;

        if (message.getReason().isEmpty() || message.getStoreName().isEmpty()) return false;

        return true;
    }


}
