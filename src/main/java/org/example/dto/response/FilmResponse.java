package org.example.dto.response;

import lombok.*;
import org.example.model.Actor;
import org.example.model.Film;
import org.example.model.Genre;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FilmResponse {
    private String director;
    private List<String> actors;
    private List<String> genres;

    public FilmResponse(Film film) {
        this.director = film.getDirector().getName();

        List<String> genres = (film.getGenres().stream()
                .map(Genre::getName))
                .toList();

        List<String> actors = (film.getActors().stream()
                .map(Actor::getName))
                .toList();

        this.actors = actors;
        this.genres = genres;
    }

    public static List<FilmResponse> convertToFilmResponseList(List<Film> film) {
        return film.stream()
                .map(FilmResponse::new)
                .toList();
    }
}