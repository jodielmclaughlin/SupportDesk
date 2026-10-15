package io.nology.supportdesk.user;

import io.nology.supportdesk.user.dtos.CreateUserDTO;
import io.nology.supportdesk.user.dtos.UserResponseDTO;
import io.nology.supportdesk.user.entity.User;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LogManager.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping()
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody @Valid CreateUserDTO data){
        User created = this.userService.createUser(data);
        System.out.println("Created user = " + created.getName());
        return new ResponseEntity<>(UserResponseDTO.fromEntity(created), HttpStatus.CREATED);
    }
}
