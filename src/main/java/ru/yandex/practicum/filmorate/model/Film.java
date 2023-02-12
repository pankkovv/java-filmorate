package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.exception.PostureDate;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class Film {
    private int id;
    @NotBlank
    private String name;
    @NotBlank
    @Size(max = 200)
    private String description;
    @NotNull
    @PostureDate
    private LocalDate releaseDate;
    @NotNull
    @Positive
    private Integer duration;
    private Set<Integer> likes;

    public void addLikes(Integer userId) {
        likes.add(userId);
    }

    public void removeLikes(Integer userId) {
        likes.remove(userId);
    }
}
