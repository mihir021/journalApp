package net.mihir.journalapp.repo;

import net.mihir.journalapp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class UserRepositoryImpl {

    @Autowired
    MongoTemplate mongoTemplate;

    // SA --> true na dg=have email in their file
    public List<User> getUserForSA(){

        Query query = new Query();

        // make query to find where gmail is present and have SA = true
        query.addCriteria(Criteria.where("email").regex("^[\\w.+\\-]+@gmail\\.com$", "i"));
        query.addCriteria(Criteria.where("sentimentAnalysis").is(true));

        return mongoTemplate.find(query, User.class);
    }

}
