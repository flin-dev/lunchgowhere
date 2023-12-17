package app.lunchgowhere.service.RoomService.impl;

import app.lunchgowhere.dto.LocationSubmission;
import app.lunchgowhere.model.Room;
import app.lunchgowhere.repository.RoomRepository;
import app.lunchgowhere.service.RoomService.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private RoomRepository roomRepository;

    @Override
    public Boolean verifyLocation(LocationSubmission message) {
        var exist = redisTemplate.opsForSet().isMember("user", message.getSender());
        if (Boolean.FALSE.equals(exist)) return false;

        if (message.getReason().isEmpty() || message.getStoreName().isEmpty()) return false;

        return true;
    }

    @Override
    public Page<List<Room>> getRooms(int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        return roomRepository.findAll(pageable);
    }

    @Override
    public Room getRoom(Long roomId) {
        return roomRepository.findById(roomId).orElseThrow();
    }

    @Override
    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }


}
