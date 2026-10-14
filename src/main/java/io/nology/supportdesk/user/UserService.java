package io.nology.supportdesk.user;

import io.nology.supportdesk.user.entity.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getUserByID(Long id){
        return this.userRepository.findById(id);

    }
}
