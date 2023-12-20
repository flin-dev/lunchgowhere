package app.lunchgowhere.service.RoomService.impl;

import app.lunchgowhere.model.LocationSubmission;
import app.lunchgowhere.dto.request.LocationSubmissionDto;
import app.lunchgowhere.dto.request.RoomDto;
import app.lunchgowhere.model.Room;
import app.lunchgowhere.repository.RoomRepository;
import app.lunchgowhere.service.RoomService.RoomService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private RoomRepository roomRepository;

    @Override
    public Boolean verifyLocation(LocationSubmissionDto message) {
        var exist = redisTemplate.opsForSet().isMember("user", message.getSender());
        if (Boolean.FALSE.equals(exist)) return false;

        return !message.getReason().isEmpty() && !message.getStoreName().isEmpty();
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
    public Room createRoom(RoomDto roomDto) {
        var room = new Room();
        room.setName(roomDto.getName());
        room.setDescription(roomDto.getDescription());

        //convert timestamp string to date
        try {
            var convertedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .parse(roomDto.getTargetTime());
            room.setTargetTime(convertedDate);
        } catch (Exception e) {
            return null;
        }

        return roomRepository.save(room);
    }

    @Override
    public Boolean closeRoom(String roomId, String Sender) {
        var room = roomRepository.findById(Long.parseLong(roomId)).orElseThrow();

        //check if sender the owner of the room
        if (room.getRoomOwner().getUsername().equals(Sender)) {
            room.setIsActive(false);
            roomRepository.save(room);
            return true;
        }

        return false;
    }

    @Override
    public Optional<LocationSubmission> closeAndPickLocation(String roomId, String name) {
        var result = this.closeRoom(roomId, name);

        if (result) {
            var room = roomRepository.findById(Long.parseLong(roomId)).orElseThrow();
            var submissions = room.getLocationSubmissions();
            var random = (int) (Math.random() * submissions.size());
            var pickedLocation = submissions.get(random);
            pickedLocation.setSelected(true);
            roomRepository.save(room);

            return Optional.of(pickedLocation);
        }
        return Optional.empty();
    }
}
