package net.mihir.journalapp.servicesTest;

import net.mihir.journalapp.Scheduler.UserScheduled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserSceduledTest {

    @Autowired
    UserScheduled userScheduled;

    @Test
    public void fetchAndSendGmailTest(){
        userScheduled.fetchAndSendGmail();
    }
}
