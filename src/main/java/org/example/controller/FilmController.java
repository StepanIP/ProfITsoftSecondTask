package org.example.controller;

import org.example.dto.request.FilmPaginationRequest;
import org.example.dto.request.FilmReportRequest;
import org.example.dto.request.FilmRequest;
import org.example.dto.response.UploadResponse;
import org.example.dto.response.FilmListResponse;
import org.example.model.Film;
import org.example.service.FilmService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;


@RestController
@RequestMapping("/api/film")
public class FilmController {

    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping("/")
    public ResponseEntity<Film> createFilm(@RequestBody FilmRequest filmData) {
        Film createdFilm = filmService.saveFilm(filmData);
        return new ResponseEntity<>(createdFilm, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Film> getFilmById(@PathVariable int id) {
        Optional<Film> film = filmService.getFilmById(id);
        return film.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Film> updateFilm(@PathVariable int id, @RequestBody FilmRequest filmData) {
        Optional<Film> existingFilm = filmService.getFilmById(id);
        if (existingFilm.isPresent()) {
            Film updatedFilm = filmService.updateFilm(id, filmData);
            return new ResponseEntity<>(updatedFilm, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFilm(@PathVariable int id) {
        Optional<Film> existingFilm = filmService.getFilmById(id);
        if (existingFilm.isPresent()) {
            filmService.deleteFilm(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/film_list")
    public ResponseEntity<FilmListResponse> getFilmsWithPagination(@RequestBody FilmPaginationRequest request) {
        return new ResponseEntity<>(filmService.getFilmsWithPagination(request), HttpStatus.OK);
    }

    @PostMapping("/film/film_report")
    public ResponseEntity<ByteArrayResource> generateFilmReport(@RequestBody FilmReportRequest filmReportRequest) {
        ByteArrayResource report = filmService.generateFilmReport(filmReportRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .body(report);
    }

    @PostMapping("/film/upload")
    public ResponseEntity<UploadResponse> importFilmsFromJson(@RequestParam("file") MultipartFile jsonFile) {
        UploadResponse response = filmService.importFilmsFromJson(jsonFile);
        return new ResponseEntity<>(response, HttpStatus.OK);}
}
