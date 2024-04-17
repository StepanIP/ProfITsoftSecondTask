package org.example.Service.impl;

import org.example.Repository.FilmRepository;
import org.example.model.Film;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FilmServiceImpl {

    private final FilmRepository filmRepository;
    @Autowired
    public FilmServiceImpl(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    public Film saveFilm(Film film) {
        return filmRepository.save(film);
    }

    public List<Film> getAllFilms() {
        return filmRepository.findAll();
    }

    public Optional<Film> getFilmById(int id) {
        return filmRepository.findById(id);
    }

    public Film updateFilm(Film film) {
        return filmRepository.save(film);
    }

    public void deleteFilm(int id) {
        filmRepository.deleteById(id);
    }
}
