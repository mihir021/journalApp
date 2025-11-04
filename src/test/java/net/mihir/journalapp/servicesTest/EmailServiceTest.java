package net.mihir.journalapp.servicesTest;

import net.mihir.journalapp.services.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailServiceTest {

    @Autowired
    EmailService emailService;

    @Test
    void sendEmailTest(){
        emailService.sendEmail(
                "rathodmihir1113@gmail.com",
                "java mail ",
                "hi mail send completed ");
    }
}
