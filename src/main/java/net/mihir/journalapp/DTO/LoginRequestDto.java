package net.mihir.journalapp.DTO;
import lombok.Data;

@Data
public class LoginRequestDto {
    private String userName;
    private String password;
}