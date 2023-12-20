package app.lunchgowhere.service;


import app.lunchgowhere.model.User;

public interface UserService {
    User getUserByUsername(String username);

    User createUser(String username);
}
