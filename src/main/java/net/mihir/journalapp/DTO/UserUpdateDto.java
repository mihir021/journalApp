package net.mihir.journalapp.DTO;

import lombok.Data;

@Data
public class UserUpdateDto {
    private String userName;
    private String password; // A user should provide their new password
}