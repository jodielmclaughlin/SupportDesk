package io.nology.supportdesk.user;

import io.nology.supportdesk.common.exceptions.EmailAlreadyExistsException;
import io.nology.supportdesk.user.dtos.CreateUserDTO;
import io.nology.supportdesk.user.dtos.UpdateUserDTO;
import io.nology.supportdesk.user.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    public List<User> getAllUsers(){
        return this.userRepository.findAll();
    }

    public Optional<User> getUserByID(Long id){
        return this.userRepository.findById(id);

    }

    public User createUser(CreateUserDTO data){
        if(userRepository.existsByEmail(data.getEmail())){
            throw new EmailAlreadyExistsException( "User with email " + data.getEmail() + " already exists.");
        }
        User newUser = modelMapper.map(data, User.class);

        newUser.setPasswordHash(passwordEncoder.encode(data.getPasswordHash()));
        return userRepository.save(newUser);
    }

    public Optional<User> updateUser(Long id, UpdateUserDTO dto){
        Optional<User> result = this.userRepository.findById(id);
        if(result.isEmpty()) {
            return result;
        }
        User toUpdate = result.get();
        modelMapper.map(dto, toUpdate);
        this.userRepository.save(toUpdate);
        return Optional.of(toUpdate);
    }

    public boolean deleteUser(Long id){
        Optional<User> result = this.userRepository.findById(id);
        if(result.isEmpty()) {
            return false;
        }
        this.userRepository.delete(result.get());
        return true;
    }
}
