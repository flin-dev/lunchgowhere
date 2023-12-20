package app.lunchgowhere.repository;

import app.lunchgowhere.model.Room;
import app.lunchgowhere.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);
}
