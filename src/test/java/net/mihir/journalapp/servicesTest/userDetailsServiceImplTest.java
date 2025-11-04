package net.mihir.journalapp.servicesTest;

import net.mihir.journalapp.entity.User;
import net.mihir.journalapp.repo.UserRepository;
import net.mihir.journalapp.services.UserDetailsServiceImpl;
import org.bson.assertions.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

import static org.mockito.Mockito.when;

public class userDetailsServiceImplTest {

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void loadUserByUserNameTest() {


        when(userRepository.findUserByUserName(ArgumentMatchers.anyString())).thenReturn(User.builder().userName("ram").password("fguhjdgfyhw").roles(Collections.singletonList("USER")) // Add roles
                .build());
        UserDetails mihir = userDetailsService.loadUserByUsername("mihir");
        Assertions.assertNotNull(mihir);
    }
}
