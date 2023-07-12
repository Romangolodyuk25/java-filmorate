package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Data
public class User {
    private int id;
    @Email
    @NotEmpty
    private String email;
    @NotNull
    private String login;
    private String name;
    @NotNull
    @PastOrPresent
    private LocalDate birthday;
    @JsonIgnore
    private final Set<Integer> friends = new HashSet<>();

    private HashMap<Integer, FriendsStatus> statusFriend = new HashMap<>();

    public void addFriend(int id) {
        friends.add(id);
    }

    public void deleteFriend(int id) {
        friends.remove(id);
    }
}
