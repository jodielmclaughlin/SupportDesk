package io.nology.supportdesk.user;

import io.nology.supportdesk.common.exceptions.NotFoundException;
import io.nology.supportdesk.user.dtos.CreateUserDTO;
import io.nology.supportdesk.user.dtos.UserResponseDTO;
import io.nology.supportdesk.user.entity.User;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LogManager.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getById(@PathVariable Long id){
        log.info("Getting employee with ID: " + id);
        UserResponseDTO foundUser = UserResponseDTO.fromEntity(this.userService.getUserByID(id).orElseThrow(() -> new NotFoundException("User not found with ID: " + id)));
        return ResponseEntity.ok(foundUser);
    }

    @PostMapping()
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody @Valid CreateUserDTO data){
        User created = this.userService.createUser(data);
        System.out.println("Created user = " + created.getName());
        return new ResponseEntity<>(UserResponseDTO.fromEntity(created), HttpStatus.CREATED);
    }
}
