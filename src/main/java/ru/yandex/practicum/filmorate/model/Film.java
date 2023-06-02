package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@AllArgsConstructor
@Data //генерит @RequiredArgsConstructor который создает конструктор для всех final полей
public class Film {
    private int id;
    @NotNull
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;


}
