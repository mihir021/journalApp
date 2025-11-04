package net.mihir.journalapp.entity;

import lombok.Data;
import lombok.NoArgsConstructor; // <-- 1. IMPORT THIS
import lombok.NonNull;
import net.mihir.journalapp.Enum.Sentiment;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor // <-- 2. ADD THIS
@Document(collection = "journal_entries")
public class JournalEntry {

    @Id
    private ObjectId id;
    LocalDateTime date;
    @NonNull
    private String title;
    private String content;
    private Sentiment sentiment;

}