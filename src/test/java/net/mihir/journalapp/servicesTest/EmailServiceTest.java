package net.mihir.journalapp.servicesTest; // 1. Use the correct package

import net.mihir.journalapp.services.EmailService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    // 3. Create a mock of the mail sender
    @Mock
    private JavaMailSender javaMailSender;

    // 4. Inject the mock into our service
    @InjectMocks
    private EmailService emailService;

    @Test
    void sendEmailTest() {
        // --- Arrange ---
        String to = "shinchan021021@gmail.com";
        String subject = "java mail";
        String body = "hi mail send completed";

        // Tell the mock to do nothing when .send() is called
        doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));

        // Create an "Argument Captor" to catch the message
        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        // --- Act ---
        emailService.sendEmail(to, subject, body);

        // --- Assert ---
        // 5. This is our assertion!
        // We verify that .send() was called exactly 1 time
        // and we capture the message that was sent.
        verify(javaMailSender, times(1)).send(messageCaptor.capture());

        // 6. (Optional but good) Assert the message content
        SimpleMailMessage capturedMessage = messageCaptor.getValue();
        Assertions.assertNotNull(capturedMessage.getTo());
        assertEquals(to, capturedMessage.getTo()[0]);
        assertEquals(subject, capturedMessage.getSubject());
        assertEquals(body, capturedMessage.getText());
    }
}