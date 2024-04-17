package org.example.Service;

import org.example.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmService {
    public Film saveFilm(Film film);

    public List<Film> getAllFilms();

    public Optional<Film> getFilmById(int id);

    public Film updateFilm(Film film);

    public void deleteFilm(int id);
}
