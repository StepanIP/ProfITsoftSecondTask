package org.example.controller;

import org.example.model.Film;
import org.example.service.FilmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api")
public class FilmController {

    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping("/film")
    public ResponseEntity<Film> createFilm(@RequestBody Film filmData) {
        Film createdFilm = filmService.saveFilm(filmData);
        return new ResponseEntity<>(createdFilm, HttpStatus.CREATED);
    }

    @GetMapping("/film/{id}")
    public ResponseEntity<Film> getFilmById(@PathVariable int id) {
        Optional<Film> film = filmService.getFilmById(id);
        return film.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/film/{id}")
    public ResponseEntity<Film> updateFilm(@PathVariable int id, @RequestBody Film filmData) {
        Optional<Film> existingFilm = filmService.getFilmById(id);
        if (existingFilm.isPresent()) {
            filmData.setId(id);
            Film updatedFilm = filmService.updateFilm(filmData);
            return new ResponseEntity<>(updatedFilm, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/film/{id}")
    public ResponseEntity<Void> deleteFilm(@PathVariable int id) {
        Optional<Film> existingFilm = filmService.getFilmById(id);
        if (existingFilm.isPresent()) {
            filmService.deleteFilm(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/film/film_list")
    public ResponseEntity<List<Film>> getFilmsWithPagination(
            @RequestParam Map<String, String> filters) {
        // Implementation for getting a list of films with pagination and filtering
        return null;
    }

    @PostMapping("/film/film_report")
    public ResponseEntity<byte[]> generateFilmReport(@RequestParam int directorId,
                                                     @RequestParam Map<String, String> filters) {
        // Implementation for generating and downloading film report
        return null;
    }

    @PostMapping("/film/upload")
    public ResponseEntity<Map<String, Integer>> importFilmsFromJson(@RequestParam("file") MultipartFile jsonFile) {
        // Implementation for importing films from JSON file
        return null;
    }
}
