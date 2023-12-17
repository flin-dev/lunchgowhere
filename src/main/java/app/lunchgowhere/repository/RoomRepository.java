package app.lunchgowhere.repository;

import app.lunchgowhere.model.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RoomRepository extends CrudRepository<Room, Long> {

   //define a pagable roomList with find all method
    Page<List<Room>> findAll(Pageable pageable);
}
