package app.lunchgowhere.service.RoomService;

import app.lunchgowhere.dto.LocationSubmission;

public interface RoomService {

    Boolean verifyLocation(LocationSubmission message);
}
