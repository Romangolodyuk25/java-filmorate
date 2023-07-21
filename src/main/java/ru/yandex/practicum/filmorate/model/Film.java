package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.*;

@AllArgsConstructor
@Data
public class Film {
    private int id;
    @NotNull
    private String name;
    @Size(min = 1, max = 200)
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @Min(1)
    private int duration;
    @JsonIgnore
    private Set<Integer> likes = new HashSet<>();

    private int rate;

    private final Set<Genre> genres = new HashSet<>();

    private Mpa mpa;

    public void addLike(int id) {
        likes.add(id);
    }

    public void deleteId(int id) {
        likes.remove(id);
    }

    public void addGenre(Genre genre) {
        genres.add(genre);
    }

    public void addGenre(List<Genre> genre) {
        genres.addAll(genre);
    }

    public Set<Genre> getGenres() {
         return new TreeSet<>(genres);
    }
}
