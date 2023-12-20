package app.lunchgowhere.repository;

import app.lunchgowhere.model.LocationSubmission;
import app.lunchgowhere.model.Room;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LocationSubmissionRepository extends CrudRepository<LocationSubmission, Long> {
    List<LocationSubmission> findByRoomId(Long roomId);
}

