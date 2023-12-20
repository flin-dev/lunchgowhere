package app.lunchgowhere.service;

import app.lunchgowhere.model.LocationSubmission;
import app.lunchgowhere.dto.request.LocationSubmissionDto;
import app.lunchgowhere.dto.request.RoomDto;
import app.lunchgowhere.model.Room;
import app.lunchgowhere.repository.LocationSubmissionRepository;
import app.lunchgowhere.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private LocationSubmissionRepository locationSubmissionRepository;

    @Override
    public Boolean verifyLocation(LocationSubmissionDto message) {
        var exist = redisTemplate.opsForSet().isMember("user", message.getSender());
        if (Boolean.FALSE.equals(exist)) return false;

        return !message.getReason().isEmpty() && !message.getName().isEmpty();
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
    public Room createRoom(RoomDto roomDto, String roomOwnerUsername) {
        var room = new Room();
        room.setName(roomDto.getName());
        room.setDescription(roomDto.getDescription());

        var user = userService.getUserByUsername(roomOwnerUsername);
        room.setRoomOwner(user);

        //convert timestamp string to date
        try {
            LocalDateTime localDateTime = LocalDateTime.parse(roomDto.getTargetTime());
            Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
            Date date = Date.from(instant);
            room.setTargetTime(date);
            room.setIsActive(!date.before(new Date()));
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

    @Override
    public LocationSubmission createLocationSubmission(LocationSubmissionDto locationSubmissionDto, String username) {

        var room = roomRepository.findById(locationSubmissionDto.getRoomId()).orElseThrow();
        var user = userService.getUserByUsername(username);

        var locationSubmission = new LocationSubmission();
        locationSubmission.setReason(locationSubmissionDto.getReason());
        locationSubmission.setDescription(locationSubmissionDto.getDescription());
        locationSubmission.setName(locationSubmissionDto.getName());
        locationSubmission.setSummiter(user);
        locationSubmission.setRoom(room);
        locationSubmission.setSelected(false);

        try {
            locationSubmission = locationSubmissionRepository.save(locationSubmission);
        } catch (DataIntegrityViolationException e) {
            //catch ERROR: duplicate key value violates unique constraint
            throw new RuntimeException("One user can only submit one location");
        }

        return locationSubmission;
    }

    @Override
    public List<LocationSubmission> getLocationSubmissions(Long roomId) {
        return locationSubmissionRepository.findByRoomId(roomId);
    }
}
