package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class Genre implements Comparable<Genre> {
    private int id;
    @NotBlank
    private String name;

    @Override
    public int compareTo(Genre o) {
        if (this.getId() == o.getId())
            return 0;
        else if (this.getId() > o.getId())
            return 1;
        else
            return -1;
    }
}
