package MyTest;

import net.mihir.journalapp.DTO.UserDTO;
import net.mihir.journalapp.entity.User;
import net.mihir.journalapp.repo.UserRepository;
import net.mihir.journalapp.services.UserServices;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServicesTest {

    @InjectMocks
    private UserServices userServices;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserDTO userDTO;
    private User user;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO();
        userDTO.setUserName("testUser");
        userDTO.setPassword("testPass");
        userDTO.setEmail("test@example.com");
        userDTO.setSentimentAnalysis(true);

        user = new User();
        user.setId(new ObjectId());
        user.setUserName("testUser");
        user.setPassword("hashedPassword");
        user.setRoles(List.of("USER"));
    }

    @Test
    void testSaveNewUser() {
        when(passwordEncoder.encode("testPass")).thenReturn("hashedPass123");
        when(userRepository.save(any(User.class))).thenReturn(user);

        userServices.saveNewUser(userDTO);

        verify(passwordEncoder, times(1)).encode("testPass");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testSaveAdmin() {
        when(passwordEncoder.encode("testPass")).thenReturn("hashedAdminPass123");
        when(userRepository.save(any(User.class))).thenReturn(user);

        userServices.saveAdmin(userDTO);

        verify(passwordEncoder, times(1)).encode("testPass");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testGetAllEntry() {
        when(userRepository.findAll()).thenReturn(List.of(user, new User()));

        List<User> users = userServices.getAllEntry();

        assertNotNull(users);
        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testFindByUserId() {
        ObjectId id = new ObjectId();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        Optional<User> foundUser = userServices.findByUserId(id);

        assertTrue(foundUser.isPresent());
        assertEquals("testUser", foundUser.get().getUserName());
        verify(userRepository, times(1)).findById(id);
    }

    @Test
    void testDeleteUserEntry() {
        String userName = "testUser";
        doNothing().when(userRepository).deleteByUserName(userName);

        userServices.deleteUserEntry(userName);

        verify(userRepository, times(1)).deleteByUserName(userName);
    }

    @Test
    void testFindUserByName() {
        String userName = "testUser";
        when(userRepository.findUserByUserName(userName)).thenReturn(user);

        User foundUser = userServices.findUserByName(userName);

        assertNotNull(foundUser);
        assertEquals(userName, foundUser.getUserName());
        verify(userRepository, times(1)).findUserByUserName(userName);
    }

    @Test
    void testSaveUser() {
        when(userRepository.save(user)).thenReturn(user);

        userServices.saveUser(user);

        verify(userRepository, times(1)).save(user);
    }
}