package net.mihir.journalapp.Controller; // Sonar will ask to rename to 'controller'

import io.swagger.v3.oas.annotations.tags.Tag;
import net.mihir.journalapp.DTO.JournalEntryDto; // <-- IMPORT THE NEW DTO
import net.mihir.journalapp.entity.JournalEntry;
import net.mihir.journalapp.entity.User;
import net.mihir.journalapp.services.JournalServices;
import net.mihir.journalapp.services.UserServices;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("journal")
@Tag(name = "Journal APIs")
public class JournalEntryController {

    @Autowired
    JournalServices journalServices;
    @Autowired
    UserServices userServices;

    @GetMapping
    // FIX 1 (Wildcard): Changed from ResponseEntity<?>
    public ResponseEntity<List<JournalEntry>> getAllJournalEntryOfUser(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userServices.findUserByName(userName);
        List<JournalEntry> journalEntriesOfUser = user.getJournalEntriesOfUser();

        if(journalEntriesOfUser != null && !journalEntriesOfUser.isEmpty()){
            return new ResponseEntity<>(journalEntriesOfUser, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    // FIX 2 (Security): Changed from JournalEntry to JournalEntryDto
    public ResponseEntity<JournalEntry> addEntry(@RequestBody JournalEntryDto entryDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        try{
            // Map from DTO to Entity
            JournalEntry newEntry = new JournalEntry();
            newEntry.setTitle(entryDto.getTitle());
            newEntry.setContent(entryDto.getContent());
            newEntry.setDate(LocalDateTime.now());  // Set date securely on the server

            journalServices.jEntry(newEntry, userName);
            return new ResponseEntity<>(newEntry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("id/{myID}")
    public ResponseEntity<JournalEntry> getInfoFromID(@PathVariable String myID){
        // ... (your existing logic is fine)
        ObjectId objectId = new ObjectId(myID);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        User user = userServices.findUserByName(userName);
        List<JournalEntry> list = user.getJournalEntriesOfUser().stream().filter(x -> x.getId().equals(objectId)).toList();
        if(!list.isEmpty()){
            Optional<JournalEntry> obj = journalServices.findByJournalId(objectId);
            if(obj.isPresent()){
                return new ResponseEntity<>(obj.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("id/{myID}")
    // FIX 3 (Wildcard): Changed from ResponseEntity<?>
    public ResponseEntity<Void> deleteInfoFromID(@PathVariable ObjectId myID){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        boolean b = journalServices.deleteJournalEntry(myID, userName);
        if(b)  return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Use <Void> for NO_CONTENT
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("id/{myID}")
    // FIX 4 (Security): Changed from JournalEntry to JournalEntryDto
    // FIX 5 (Wildcard): Changed from ResponseEntity<?>
    public ResponseEntity<JournalEntry> updateInfoFromID(@PathVariable ObjectId myID, @RequestBody JournalEntryDto newEntryDto){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        User user = userServices.findUserByName(userName);
        List<JournalEntry> list = user.getJournalEntriesOfUser().stream().filter(x -> x.getId().equals(myID)).toList();

        if(!list.isEmpty()){
            Optional<JournalEntry> obj = journalServices.findByJournalId(myID);
            if(obj.isPresent()){
                JournalEntry old = obj.get();

                // Map safely from DTO
                old.setTitle(!newEntryDto.getTitle().isEmpty() ? newEntryDto.getTitle() : old.getTitle());
                old.setContent(newEntryDto.getContent() != null && !newEntryDto.getContent().isEmpty() ? newEntryDto.getContent() : old.getContent());

                journalServices.jEntry(old); // Assuming jEntry is a save method
                return new ResponseEntity<>(old, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}