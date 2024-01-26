package main.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterRequest {

    private String firstname;
    private String lastname;
    private String username;
    private String password;
}
