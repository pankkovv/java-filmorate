package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class User {
    private int id;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(max = 15)
    private String login;
    private String name;
    @NotNull
    @PastOrPresent
    private LocalDate birthday;
}
