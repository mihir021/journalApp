// 1. FIX: Package name is now all lowercase
package net.mihir.journalapp;

import lombok.extern.slf4j.Slf4j; // <-- 2. FIX: Import the logger
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
@Slf4j // <-- 2. FIX: Add logger annotation
// 3. FIX: Class name is now PascalCase
public class JournalApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(JournalApplication.class, args);

        // 4. FIX: Use logger instead of System.out
        String[] activeProfiles = run.getEnvironment().getActiveProfiles();
        if (activeProfiles.length > 0) {
            log.info("Application started with active profile: {}", activeProfiles[0]);
        } else {
            log.info("Application started with no active profile.");
        }
    }

    @Bean
    public PlatformTransactionManager transactionManager(MongoDatabaseFactory dbname){
        return new MongoTransactionManager(dbname);
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}