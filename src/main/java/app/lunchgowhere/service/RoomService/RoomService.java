package app.lunchgowhere.service.RoomService;

import app.lunchgowhere.dto.LocationSubmission;
import app.lunchgowhere.model.Room;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RoomService {

    Boolean verifyLocation(LocationSubmission message);

    Page<List<Room>> getRooms(int pageNum, int pageSize);

    Room getRoom(Long roomId);

    Room createRoom(Room room);
}
