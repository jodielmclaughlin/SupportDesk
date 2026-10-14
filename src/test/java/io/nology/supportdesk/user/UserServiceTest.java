package io.nology.supportdesk.user;

import io.nology.supportdesk.user.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Spy
    @InjectMocks
    private UserService userService;

    @Test
    public void getUserById_callsFindById(){
        this.userService.getUserByID(1l);
        verify(this.userRepository).findById(1l);
    }

    @Test
    public void getUserById_whenUserExists_returnsUser(){
        User fakeUser = new User();
        fakeUser.setId(1l);
        fakeUser.setName("Fake User");
        when(this.userRepository.findById(1l)).thenReturn(Optional.of(fakeUser));
        Optional<User> result = userService.getUserByID(1l);
        assertTrue(result.isPresent());
        assertEquals(fakeUser.getName(), result.get().getName());
    }

    @Test
    public void getUserById_whenUserDoesntExist_ThrowsNotFoundException(){

    }

    @Test
    public void getUserByEmail_whenUserExists_returnsMatchingUser(){

    }

    @Test
    public void createUser_whenGivenValidDetails_savesAndReturnsNewUser(){

    }

    @Test
    public void createUser_whenEmailAlreadyExists_RejectsDuplicateEmail(){

    }

    @Test
    public void updateUser_whenUserExists_UpdatesPermittedFields(){

    }

    @Test
    public void updateUser_whenUserDoesntExist_ThrowsNotFoundException(){

    }
}

//Test	Expected behaviour
//getUserById — user exists	Returns the requested user
//getUserById — user doesn't exist	Throws your not-found exception
//getUserByEmail — email exists	Returns the matching user
//createUser — valid details	Saves and returns the new user
//createUser — email already exists	Rejects the duplicate email
//updateUser — user exists	Updates the permitted fields
//updateUser — user doesn't exist	Throws your not-found exception
