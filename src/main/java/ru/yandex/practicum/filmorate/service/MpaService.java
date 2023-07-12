package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaDbStorage;
import ru.yandex.practicum.filmorate.exception.ObjectNotExistException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Slf4j
@Service
public class MpaService {
    MpaDbStorage mpaDbStorage;

    @Autowired
    public MpaService(MpaDbStorage mpaDbStorage) {
        this.mpaDbStorage = mpaDbStorage;
    }

    public List<Mpa> getAllMpa() {
        return mpaDbStorage.getAllMpa();
    }

    public Mpa findMpaById(int id) {
        Mpa receivedMpa = mpaDbStorage.findRatingById(id);
        if (receivedMpa == null) {
            log.info("Рейтинг с айди " + id + " не существует");
            throw new ObjectNotExistException("Рейтинг не существует");
        }
        log.info("Переданный айди " + id);
        log.info("Вернувшийся объект " + receivedMpa);
        return receivedMpa;
    }

}
