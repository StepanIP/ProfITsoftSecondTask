package org.example.service;

import org.example.dto.request.FilmPaginationRequest;
import org.example.dto.request.FilmRequest;
import org.example.dto.request.FilmReportRequest;
import org.example.dto.response.UploadResponse;
import org.example.dto.response.FilmListResponse;
import org.example.model.Film;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface FilmService {
    Film saveFilm(FilmRequest filmRequest);

    List<Film> getAllFilms();

    Optional<Film> getFilmById(int id);

    Film updateFilm(int id, FilmRequest filmRequest);

    void deleteFilm(int id);

    List<Film> saveAllFilms(List<Film> films);

    FilmListResponse getFilmsWithPagination(FilmPaginationRequest request);

    UploadResponse importFilmsFromJson(MultipartFile file);

    ByteArrayResource generateFilmReport(FilmReportRequest request);

}
