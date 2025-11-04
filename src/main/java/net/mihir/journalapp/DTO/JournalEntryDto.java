package net.mihir.journalapp.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JournalEntryDto {
    private String title;
    private String content;
}