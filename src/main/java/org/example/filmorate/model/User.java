package org.example.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class User {

    private int id;

    @NotNull(message = "Электронная почта не может быть null")
    @NotBlank(message = "Электронная почта не может быть пустым")
    @Email(message = "Электронная почта должен быть действительным адресом")
    private String email;

    @NotNull(message = "Логин не может быть null")
    @NotBlank(message = "Логин не может быть пустым")
    @Pattern(regexp = "\\S+", message = "Логин не может содержать пробелы.")
    private String login;

    private String name;

    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    private LocalDateTime birthday;
}
