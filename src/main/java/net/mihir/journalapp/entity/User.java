package net.mihir.journalapp.entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.ArrayList;
import java.util.List;

@Data  // --> eq to getter setter and contractures
@Document(collection = "users") // to tall it's a collection in DataABse
@Builder
@NoArgsConstructor
@AllArgsConstructor// collection = database name
public class User {

    @Id
    private ObjectId id;
    @Indexed(unique = true)
    @NonNull
    private String userName;
    @NonNull
    private String password;

    private String email;
    private boolean sentimentAnalysis;

    @DBRef // like foreign key connect with the journal enteritis
    private List<JournalEntry> journalEntriesOfUser = new ArrayList<>();
    private List<String> roles;


}
