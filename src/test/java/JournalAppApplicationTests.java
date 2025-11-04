import net.mihir.journalapp.entity.User;
import net.mihir.journalapp.repo.UserRepository;
import net.mihir.journalapp.servicesTest.UserArgumentProvider;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JournalAppApplicationTests {

    @Autowired
    UserRepository userRepository;

    @ParameterizedTest
    @ValueSource(strings = {"mihir", "chotu"})
    void findByUserName(String name) {
        User user = userRepository.findUserByUserName(name);

        assertNotNull(user, "User not found in database");
        assertNotNull(user.getUserName(), "Username field is null");
    }
    // @Disabled to avoide test
    // argumentSource for custom in puts
    // Before each  Before all
    // After Each After All

    @ParameterizedTest
    @CsvSource({
            "1, 1, 2",
            "2, 10, 12"
    })
    public void test(int a, int b, int expected) {
        assertEquals(expected, a + b);
    }


    @ParameterizedTest
    @ArgumentsSource(UserArgumentProvider.class)
    void testUserCreation(User user) {
        assertNotNull(user.getUserName());
        assertNotNull(user.getPassword());
    }

}
