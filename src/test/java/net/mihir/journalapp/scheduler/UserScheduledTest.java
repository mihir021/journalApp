package net.mihir.journalapp.scheduler;

import net.mihir.journalapp.Enum.Sentiment;
import net.mihir.journalapp.Scheduler.UserScheduled;
import net.mihir.journalapp.entity.JournalEntry;
import net.mihir.journalapp.entity.User;
import net.mihir.journalapp.repo.UserRepository;
import net.mihir.journalapp.services.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserScheduledTest {

    @InjectMocks
    private UserScheduled userScheduled;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @Test
    void testFetchAndSendGmail() {
        // --- Arrange ---

        // 1. Create Journal Entries
        JournalEntry entry1 = new JournalEntry();
        entry1.setDate(LocalDateTime.now().minus(1, ChronoUnit.DAYS)); // Recent
        entry1.setSentiment(Sentiment.HAPPY);

        JournalEntry entry2 = new JournalEntry();
        entry2.setDate(LocalDateTime.now().minus(2, ChronoUnit.DAYS)); // Recent
        entry2.setSentiment(Sentiment.HAPPY);

        JournalEntry entry3 = new JournalEntry();
        entry3.setDate(LocalDateTime.now().minus(3, ChronoUnit.DAYS)); // Recent
        entry3.setSentiment(Sentiment.SAD);

        JournalEntry entry4 = new JournalEntry();
        entry4.setDate(LocalDateTime.now().minus(10, ChronoUnit.DAYS)); // Too old
        entry4.setSentiment(Sentiment.ANGRY);

        // 2. Create a user and add entries
        User user1 = new User();
        user1.setEmail("happy@example.com");
        user1.setUserName("happyUser");
        user1.setJournalEntriesOfUser(new ArrayList<>(List.of(entry1, entry2, entry3, entry4)));

        // 3. Define mock behavior
        // Return our test user
        when(userRepository.getUserForSA()).thenReturn(List.of(user1));
        // Mock the email service (it does nothing)
        doNothing().when(emailService).sendEmail(anyString(), anyString(), anyString());

        // --- Act ---
        userScheduled.fetchAndSendGmail(); // Manually run the scheduled method

        // --- Assert ---
        // Verify the repository was called
        verify(userRepository, times(1)).getUserForSA();

        // Verify the email service was called *exactly* once, with the correct details.
        // The logic should find that HAPPY (2) is more frequent than SAD (1)
        // and ignore ANGRY (too old).
        verify(emailService, times(1)).sendEmail(
                eq("happy@example.com"),
                eq("Your 7-Day Sentiment Report"),
                contains("HAPPY") // Check that the email body contains the word "HAPPY"
        );
    }

    @Test
    void testFetchAndSendGmail_NoRecentEntries() {
        // --- Arrange ---

        // 1. Create only old entries
        JournalEntry oldEntry = new JournalEntry();
        oldEntry.setDate(LocalDateTime.now().minus(30, ChronoUnit.DAYS)); // Too old
        oldEntry.setSentiment(Sentiment.HAPPY);

        // 2. Create user
        User user1 = new User();
        user1.setEmail("noemail@example.com");
        user1.setJournalEntriesOfUser(new ArrayList<>(List.of(oldEntry)));

        // 3. Define mock behavior
        when(userRepository.getUserForSA()).thenReturn(List.of(user1));

        // --- Act ---
        userScheduled.fetchAndSendGmail();

        // --- Assert ---
        // Verify the email service was *NEVER* called
        verify(emailService, never()).sendEmail(anyString(), anyString(), anyString());
    }
}