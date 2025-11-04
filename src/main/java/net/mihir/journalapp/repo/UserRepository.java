package net.mihir.journalapp.repo;

import net.mihir.journalapp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {
    User findUserByUserName(String userName);
    void deleteByUserName(String userName);

    List<User> getUserForSA();
}