package app.lunchgowhere.repository;

import app.lunchgowhere.model.LocationSubmission;
import app.lunchgowhere.model.Room;
import org.springframework.data.repository.CrudRepository;

public interface LocationSubmissionRepository extends CrudRepository<LocationSubmission, Long> {
}
