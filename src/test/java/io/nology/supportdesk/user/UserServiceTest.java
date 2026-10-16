package io.nology.supportdesk.user;

import io.nology.supportdesk.common.exceptions.EmailAlreadyExistsException;
import io.nology.supportdesk.user.dtos.CreateUserDTO;
import io.nology.supportdesk.user.dtos.UpdateUserDTO;
import io.nology.supportdesk.user.entity.User;
import io.nology.supportdesk.user.entity.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private ModelMapper modelMapper;


    @Spy
    @InjectMocks
    private UserService userService;

    @Test
    public void getUserById_callsFindById(){
        this.userService.getUserByID(1L);
        verify(this.userRepository).findById(1L);
    }

    @Test
    public void getUserById_callsRepositoryWithRequestedId() {
        Long userId = 42L;

        userService.getUserByID(userId);

        verify(userRepository).findById(userId);
    }

    @Test
    public void getUserById_whenUserExists_returnsUser(){
        User fakeUser = new User();
        fakeUser.setId(1L);
        fakeUser.setName("Fake User");
        when(this.userRepository.findById(1L)).thenReturn(Optional.of(fakeUser));
        Optional<User> result = userService.getUserByID(1L);
        assertTrue(result.isPresent());
        assertEquals(fakeUser.getName(), result.get().getName());
    }

    @Test
    public void getUserById_whenUserDoesntExist_returnsEmptyOptional(){
        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        Optional<User> result = userService.getUserByID(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    public void createUser_callsRepositorySave() {
        CreateUserDTO dto = new CreateUserDTO();
        User user = new User();
        when(this.modelMapper.map(dto, User.class)).thenReturn(user);
        user.setName("Fake User");

        userService.createUser(dto);

        verify(userRepository).save(user);
    }

    @Test
    public void createUser_savesAndReturnsNewUser(){
        CreateUserDTO dto = new CreateUserDTO();
        User user = new User();
        when(this.modelMapper.map(dto, User.class)).thenReturn(user);
        user.setName("FakeUser");
        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setName("FakeUser");

        when(userRepository.save(user)).thenReturn(savedUser);

        User result = userService.createUser(dto);

        assertEquals(savedUser, result);
    }

    @Test
    public void createUser_whenEmailAlreadyExists_throwsException() {
        CreateUserDTO dto = new CreateUserDTO();
        dto.setName("Fake User");
        dto.setEmail("fake@example.com");

        when(userRepository.existsByEmail("fake@example.com"))
                .thenReturn(true);

        assertThrows(
                EmailAlreadyExistsException.class,
                () -> userService.createUser(dto)
        );

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void createUser_whenEmailDoesNotExist_savesUser() {
        CreateUserDTO dto = new CreateUserDTO();
        dto.setEmail("new@example.com");
        User user = new User();
        when(this.modelMapper.map(dto, User.class)).thenReturn(user);


        when(userRepository.existsByEmail("new@example.com"))
                .thenReturn(false);

        userService.createUser(dto);

        verify(userRepository).save(user);
    }
    @Test
    void createUser_encodesPasswordBeforeSaving() {
        CreateUserDTO dto = new CreateUserDTO();
        dto.setEmail("fake@example.com");
        dto.setPasswordHash("plainPassword123");
        User user = new User();
        when(this.modelMapper.map(dto, User.class)).thenReturn(user);

        when(userRepository.existsByEmail("fake@example.com"))
                .thenReturn(false);
        when(passwordEncoder.encode("plainPassword123"))
                .thenReturn("encodedPassword123");
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.createUser(dto);

        assertEquals("encodedPassword123", result.getPasswordHash());
        verify(passwordEncoder).encode("plainPassword123");
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(userCaptor.capture());

        assertEquals("encodedPassword123", userCaptor.getValue().getPasswordHash());
    }


    @Test
    public void updateUser_whenUserExists_updatesPermittedFields(){
        UpdateUserDTO dto = new UpdateUserDTO();
        User fakeUser = new User();
        when(this.userRepository.findById(1L)).thenReturn(Optional.of(fakeUser));
        Optional<User> result = this.userService.updateUser(1L, dto);
        verify(this.modelMapper).map(dto, fakeUser);
        verify(this.userRepository).save(fakeUser);
        assertEquals(fakeUser, result.get());
    }

    @Test
    public void updateUser_whenUserDoesntExist_returnsEmptyOptionalDoNotSave(){
        when(this.userRepository.findById(1L)).thenReturn(Optional.empty());
        Optional<User> result = this.userService.updateUser(1L, new UpdateUserDTO());
        assertTrue(result.isEmpty());
        verifyNoInteractions(this.modelMapper);
        verify(this.userRepository, never()).save(any(User.class));
    }

    @Test
    public void updateUser_changesRoleButPreservesPasswordHash() {
        User existingUser = new User();
        existingUser.setName("Fake User");
        existingUser.setPasswordHash("existingEncodedHash");
        existingUser.setRole(UserRole.REQUESTER);

        UpdateUserDTO dto = new UpdateUserDTO();
        dto.setName("Updated User");
        dto.setRole(UserRole.AGENT);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        ModelMapper realMapper = new ModelMapper();

        UserService service = new UserService(
                userRepository,
                passwordEncoder,
                realMapper
        );

        Optional<User> result = service.updateUser(1L, dto);
        assertTrue(result.isPresent());
        assertEquals("Updated User", result.get().getName());
        assertEquals(UserRole.AGENT, result.get().getRole());
        assertEquals("existingEncodedHash", result.get().getPasswordHash());

        verify(userRepository).save(existingUser);

    }

    @Test
    public void deleteUser_whenUserExists_returnsTrueAndCallsDelete(){
        User fakeUser = new User();
        when(this.userRepository.findById(1L)).thenReturn(Optional.of(fakeUser));
        boolean result = this.userService.deleteUser(1L);
        assertTrue(result);
        verify(this.userRepository).delete(fakeUser);
    }

    @Test
    public void deleteUser_userDoesNotExist_returnsFalseAndDoesNotCallDelete(){
        when(this.userRepository.findById(1L)).thenReturn(Optional.empty());
        boolean result = this.userService.deleteUser(1L);
        assertFalse(result);
        verify(this.userRepository, never()).delete(any(User.class));
    }

}

