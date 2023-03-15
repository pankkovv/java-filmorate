package ru.yandex.practicum.filmorate.dao.methods;

import ru.yandex.practicum.filmorate.model.FriendshipStatus;

import java.util.List;
import java.util.Optional;

public interface FriendshipStatusDao {
    List<FriendshipStatus> findStatus();

    Optional<FriendshipStatus> findStatusId(Integer id);
}
