package app.lunchgowhere.service;

import app.lunchgowhere.model.User;
import app.lunchgowhere.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User createUser(String username) {
        var user = new User();
        user.setUsername(username);
        return userRepository.save(user);
    }
}
