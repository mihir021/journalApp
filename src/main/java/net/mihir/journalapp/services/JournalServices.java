package net.mihir.journalapp.services;

import net.mihir.journalapp.entity.JournalEntry;
import net.mihir.journalapp.entity.User;
import net.mihir.journalapp.repo.JournalRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class JournalServices {

    @Autowired
    private JournalRepository journalRepository;
    @Autowired
    private UserServices userServices;

    @Transactional
    public void jEntry(JournalEntry entry, String userName) {
        User user = userServices.findUserByName(userName);
        JournalEntry save = journalRepository.save(entry);
        user.getJournalEntriesOfUser().add(save);
        userServices.saveUser(user);
    }

    public void jEntry(JournalEntry entry) {
        // This is for updating an existing entry
        journalRepository.save(entry);
    }

    public List<JournalEntry> getAllEntry(){
        return journalRepository.findAll();
    }

    public Optional<JournalEntry> findByJournalId(ObjectId id){
        return journalRepository.findById(id);
    }

    @Transactional
    public boolean deleteJournalEntry(ObjectId id, String userName){
        boolean b = false;
        User user = userServices.findUserByName(userName);
        b = user.getJournalEntriesOfUser().removeIf(x -> x.getId().equals(id));
        if(b){
            userServices.saveUser(user);
            journalRepository.deleteById(id);
        }
        return b;
    }
}