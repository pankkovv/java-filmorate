package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class User {
    private int id;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String login;
    private String name;
    @NotNull
    @PastOrPresent
    private LocalDate birthday;
    private Set<Integer> friends;

    public void addFriends(int userId) {
        friends.add(userId);
    }

    public void removeFriends(int userId) {
        friends.remove(userId);
    }
}
