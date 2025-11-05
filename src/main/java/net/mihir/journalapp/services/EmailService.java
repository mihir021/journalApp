package net.mihir.journalapp.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    // 1. Removed @Autowired and made the field final
    private final JavaMailSender javaMailSender;

    // 2. Added a constructor for Spring to inject the bean
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(to);
            msg.setSubject(subject);
            msg.setText(body);

            javaMailSender.send(msg);
        } catch (Exception e) {
            log.error("error in gmail : ", e);
        }
    }

}