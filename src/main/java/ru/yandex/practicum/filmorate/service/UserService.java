package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserService {

    private final UserStorage inMemoryUserStorage;

    @Autowired
    public UserService(UserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public void addFriend(int userId, int friendId) {
        User receivedUser = inMemoryUserStorage.getUserById(userId);
        User receivedFriend = inMemoryUserStorage.getUserById(friendId);
        if (receivedUser == null || receivedFriend == null) {
            log.debug("Некорректно передан айди юзера или друга");
            throw new ObjectNotExistException("Переданный айди не найден");
        }
        receivedUser.addFriend(friendId);
        receivedFriend.addFriend(userId);
        inMemoryUserStorage.updateUser(receivedUser);
        inMemoryUserStorage.updateUser(receivedFriend);
    }

    public void deleteFriend(int userId, int friendId) {
        User receivedUser = inMemoryUserStorage.getUserById(userId);
        User receivedFriend = inMemoryUserStorage.getUserById(friendId);
        if (receivedUser == null || receivedFriend == null) {
            log.debug("Некорректно передан айди юзера или друга");
            throw new ObjectNotExistException("Переданный айди не найден");
        }
        receivedUser.deleteFriend(friendId);
        receivedFriend.deleteFriend(userId);
        inMemoryUserStorage.updateUser(receivedUser);
        inMemoryUserStorage.updateUser(receivedFriend);
    }

    public List<User> getAllFriends(int userId) {
        List<User> friends = new ArrayList<>();
        User receivedUser = inMemoryUserStorage.getUserById(userId);
        if (receivedUser == null) {
            log.debug("User с айди " + userId + " не существует");
            throw new ObjectNotExistException("Переданный айди юзера не найден");
        }
        for (Integer id : receivedUser.getAllFriends()) {
            friends.add(inMemoryUserStorage.getUserById(id));
        }
        return friends;
    }

    public List<User> getCommonFriends(int userId, int otherId) {
        List<User> commonFriends = new ArrayList<>();
        User receivedUser = inMemoryUserStorage.getUserById(userId);
        User receivedFriend = inMemoryUserStorage.getUserById(otherId);
        if (receivedUser == null || receivedFriend == null) {
            log.debug("Некорректно передан айди юзера или друга");
            throw new ObjectNotExistException("Переданный айди не найден");
        }
        for (Integer id : receivedUser.getAllFriends()) {
            if (receivedFriend.getAllFriends().contains(id)) {
                commonFriends.add(inMemoryUserStorage.getUserById(id));
            }
        }
        return commonFriends;
    }

    public void deleteAllUsers() {
        inMemoryUserStorage.deleteAllUser();
    }
}
