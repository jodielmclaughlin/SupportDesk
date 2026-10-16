package io.nology.supportdesk.user;

import io.nology.supportdesk.common.exceptions.NotFoundException;
import io.nology.supportdesk.user.dtos.CreateUserDTO;
import io.nology.supportdesk.user.dtos.UpdateUserDTO;
import io.nology.supportdesk.user.dtos.UserResponseDTO;
import io.nology.supportdesk.user.entity.User;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LogManager.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(){
        List<User> all = this.userService.getAllUsers();
        return ResponseEntity.ok(UserResponseDTO.fromEntity(all));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getById(@PathVariable Long id){
        log.info("Getting employee with ID: {}", id);
        UserResponseDTO foundUser = UserResponseDTO.fromEntity(this.userService.getUserByID(id).orElseThrow(() -> new NotFoundException("User not found with ID: " + id)));
        return ResponseEntity.ok(foundUser);
    }

    @PostMapping()
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody @Valid CreateUserDTO data){
        User created = this.userService.createUser(data);
        log.info("Created user = {}", created.getName());
        return new ResponseEntity<>(UserResponseDTO.fromEntity(created), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @RequestBody @Valid UpdateUserDTO data){
        User updated = this.userService.updateUser(id, data).orElseThrow(() -> new NotFoundException("User not found with ID: " + id));
        return ResponseEntity.ok(UserResponseDTO.fromEntity(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        boolean successfullyDelete = this.userService.deleteUser(id);
        if(!successfullyDelete){
            throw new NotFoundException("User not found with ID: " + id);
        }
        return ResponseEntity.noContent().build();
    }
}
