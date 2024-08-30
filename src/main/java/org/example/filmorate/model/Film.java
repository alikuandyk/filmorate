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
public class Film {

    private int id;

    @NotNull(message = "Название не может быть null")
    @NotBlank(message = "Название не может быть пустым")
    private String title;

    @Size(max = 200, message = "Максимальная длина описания - 200 символов")
    private String description;

    @NotNull(message = "Дата релиза не может быть null")
    @PastOrPresent(message = "Дата релиза не может быть в будущем")
    private LocalDateTime releaseDate;

    @Positive(message = "Продолжительность фильма должен быть положительным")
    private int duration;
}
