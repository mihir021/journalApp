// 1. Renamed package to lowercase
package net.mihir.journalapp.Scheduler;

import net.mihir.journalapp.Enum.Sentiment;
import net.mihir.journalapp.entity.JournalEntry;
import net.mihir.journalapp.entity.User;
// 2. Import the interface, not the implementation
import net.mihir.journalapp.repo.UserRepository;
import net.mihir.journalapp.services.EmailService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.EnumMap; // <-- Import EnumMap
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class UserScheduled {

    private final EmailService emailService;
    private final UserRepository userRepository;

    // 3. Use Constructor Injection (like we did in SpringSecurity)
    public UserScheduled(EmailService emailService, UserRepository userRepository) {
        this.emailService = emailService;
        this.userRepository = userRepository;
    }

    @Scheduled(cron = "0 0 9 * * SUN") // Every Sunday at 9 AM
    public void fetchAndSendGmail() {
        // 4. Use the interface 'userRepository'
        List<User> users = userRepository.getUserForSA();

        for (User user : users) {
            List<JournalEntry> journalEntries = user.getJournalEntriesOfUser();

            // 5. This one stream pipeline replaces all your loops
            Map<Sentiment, Long> sentimentCounts = journalEntries.stream()
                    .filter(entry -> entry.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS)))
                    .map(JournalEntry::getSentiment)
                    .filter(Objects::nonNull) // Safely skip any null sentiments
                    .collect(Collectors.groupingBy(
                            Function.identity(),
                            () -> new EnumMap<>(Sentiment.class), // <-- Solves the EnumMap issue
                            Collectors.counting()
                    ));

            // 6. Find the most frequent sentiment from the new map
            sentimentCounts.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .ifPresent(entry -> emailService.sendEmail(
                            user.getEmail(),
                            "Your 7-Day Sentiment Report",
                            "Your most frequent sentiment in the last 7 days was: " + entry.getKey().toString()
                    ));
        }
    }
}