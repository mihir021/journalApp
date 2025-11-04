package MyTest;

import net.mihir.journalapp.entity.JournalEntry;
import net.mihir.journalapp.entity.User;
import net.mihir.journalapp.repo.JournalRepository;
import net.mihir.journalapp.services.JournalServices;
import net.mihir.journalapp.services.UserServices;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JournalServicesTest {

    @InjectMocks
    private JournalServices journalServices;

    @Mock
    private JournalRepository journalRepository;

    @Mock
    private UserServices userServices;

    @Test
    void testJEntry_SaveNewEntry() {
        // --- Arrange ---
        String userName = "testUser";

        // 1. Create a new journal entry
        JournalEntry newEntry = new JournalEntry();
        newEntry.setTitle("Test Title");
        newEntry.setContent("Test Content");

        // 2. Create the user who owns it
        User user = new User();
        user.setUserName(userName);
        user.setJournalEntriesOfUser(new ArrayList<>()); // Start with an empty list

        // 3. Define mock behavior
        when(userServices.findUserByName(userName)).thenReturn(user);
        when(journalRepository.save(newEntry)).thenReturn(newEntry);
        // We don't need to mock saveUser, just verify it's called

        // --- Act ---
        journalServices.jEntry(newEntry, userName);

        // --- Assert ---
        // Verify the repository saved the entry
        verify(journalRepository, times(1)).save(newEntry);
        // Verify the user service was called to re-save the user
        verify(userServices, times(1)).saveUser(user);
        // Assert that the user's journal list now contains the new entry
        assertEquals(1, user.getJournalEntriesOfUser().size());
        assertEquals("Test Title", user.getJournalEntriesOfUser().get(0).getTitle());
    }

    @Test
    void testJEntry_UpdateExistingEntry() {
        // This tests the *other* jEntry method (the one with 1 parameter)
        // --- Arrange ---
        JournalEntry entryToUpdate = new JournalEntry();
        entryToUpdate.setId(new ObjectId());
        entryToUpdate.setTitle("Updated Title");

        when(journalRepository.save(entryToUpdate)).thenReturn(entryToUpdate);

        // --- Act ---
        journalServices.jEntry(entryToUpdate);

        // --- Assert ---
        verify(journalRepository, times(1)).save(entryToUpdate);
    }

    @Test
    void testGetAllEntry() {
        // --- Arrange ---
        when(journalRepository.findAll()).thenReturn(List.of(new JournalEntry(), new JournalEntry()));

        // --- Act ---
        List<JournalEntry> allEntries = journalServices.getAllEntry();

        // --- Assert ---
        assertNotNull(allEntries);
        assertEquals(2, allEntries.size());
        verify(journalRepository, times(1)).findAll();
    }

    @Test
    void testFindByJournalId() {
        // --- Arrange ---
        ObjectId id = new ObjectId();
        JournalEntry entry = new JournalEntry();
        entry.setId(id);

        when(journalRepository.findById(id)).thenReturn(Optional.of(entry));

        // --- Act ---
        Optional<JournalEntry> foundEntry = journalServices.findByJournalId(id);

        // --- Assert ---
        assertTrue(foundEntry.isPresent());
        assertEquals(id, foundEntry.get().getId());
        verify(journalRepository, times(1)).findById(id);
    }

    @Test
    void testDeleteJournalEntry_Success() {
        // --- Arrange ---
        ObjectId entryId = new ObjectId();
        String userName = "testUser";

        JournalEntry entryToDelete = new JournalEntry();
        entryToDelete.setId(entryId);
        entryToDelete.setDate(LocalDateTime.now());
        entryToDelete.setTitle("Delete Me");

        User user = new User();
        user.setUserName(userName);
        // Add the entry to the user's list
        user.setJournalEntriesOfUser(new ArrayList<>(List.of(entryToDelete)));

        when(userServices.findUserByName(userName)).thenReturn(user);
        // For void methods
        doNothing().when(journalRepository).deleteById(entryId);

        // --- Act ---
        boolean result = journalServices.deleteJournalEntry(entryId, userName);

        // --- Assert ---
        assertTrue(result); // Check that the method returns true
        assertEquals(0, user.getJournalEntriesOfUser().size()); // Check list is now empty
        verify(userServices, times(1)).saveUser(user); // Check user was re-saved
        verify(journalRepository, times(1)).deleteById(entryId); // Check repo delete was called
    }

    @Test
    void testDeleteJournalEntry_Failure_EntryNotFound() {
        // --- Arrange ---
        ObjectId entryId = new ObjectId();
        ObjectId differentId = new ObjectId();
        String userName = "testUser";

        JournalEntry entry = new JournalEntry();
        entry.setId(differentId); // An entry with a *different* ID

        User user = new User();
        user.setUserName(userName);
        user.setJournalEntriesOfUser(new ArrayList<>(List.of(entry)));

        when(userServices.findUserByName(userName)).thenReturn(user);

        // --- Act ---
        // Try to delete the entry that isn't in the user's list
        boolean result = journalServices.deleteJournalEntry(entryId, userName);

        // --- Assert ---
        assertFalse(result); // Method should return false
        assertEquals(1, user.getJournalEntriesOfUser().size()); // List should be unchanged
        // Verify these methods were *never* called
        verify(userServices, never()).saveUser(user);
        verify(journalRepository, never()).deleteById(any());
    }
}