package app.lunchgowhere.service.RoomService;

import app.lunchgowhere.dto.request.LocationSubmissionDto;
import app.lunchgowhere.dto.request.RoomDto;
import app.lunchgowhere.model.LocationSubmission;
import app.lunchgowhere.model.Room;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface RoomService {

    Boolean verifyLocation(LocationSubmissionDto message);

    Page<List<Room>> getRooms(int pageNum, int pageSize);

    Room getRoom(Long roomId);

    Room createRoom(RoomDto room);

    Boolean closeRoom(String roomId, String Sender);

    Optional<LocationSubmission> closeAndPickLocation(String roomId, String name);
}
