package org.example.service.impl;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.example.dto.request.FilmReportRequest;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.example.dto.request.FilmPaginationRequest;
import org.example.dto.request.FilmRequest;
import org.example.dto.response.UploadResponse;
import org.example.dto.response.FilmListResponse;
import org.example.dto.response.FilmResponse;
import org.example.model.Actor;
import org.example.model.Director;
import org.example.model.Genre;
import org.example.repository.ActorRepository;
import org.example.repository.DirectorRepository;
import org.example.repository.FilmRepository;
import org.example.model.Film;
import org.example.repository.GenreRepository;
import org.example.service.FilmService;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmServiceImpl implements FilmService {

    private final FilmRepository filmRepository;

    private final DirectorRepository directorRepository;

    private final ActorRepository actorRepository;

    private final GenreRepository genreRepository;

    private final JSONParser parser = new JSONParser();


    @Autowired
    public FilmServiceImpl(FilmRepository filmRepository, DirectorRepository directorRepository, ActorRepository actorRepository, GenreRepository genreRepository) {
        this.filmRepository = filmRepository;
        this.directorRepository = directorRepository;
        this.actorRepository = actorRepository;
        this.genreRepository = genreRepository;
    }

    @Override
    public Film saveFilm(FilmRequest filmRequest) {
        return filmRepository.save(convertToFilm(filmRequest));
    }

    @Override
    public List<Film> getAllFilms() {
        return filmRepository.findAll();
    }

    @Override
    public Optional<Film> getFilmById(int id) {
        return filmRepository.findById(id);
    }

    @Override
    public Film updateFilm(int id, FilmRequest filmRequest) {
        Film film = convertToFilm(filmRequest);
        film.setId(id);
        return filmRepository.save(film);
    }

    @Override
    public void deleteFilm(int id) {
        filmRepository.deleteById(id);
    }

    @Override
    public List<Film> saveAllFilms(List<Film> films) {
        return filmRepository.saveAll(films);
    }

    @Override
    public UploadResponse importFilmsFromJson(MultipartFile jsonFile) {
        JSONArray a = null;
        int successful = 0;
        int failed = 0;
        boolean isFailed = false;

        try {
            a = (JSONArray) parser.parse(new String(jsonFile.getBytes()));
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }

        for (Object o : a) {
            JSONObject film = (JSONObject) o;
            String title = (String) film.get("title");
            String year = String.valueOf(film.get("year"));
            String director = (String) film.get("director");
            JSONArray actors = (JSONArray) film.get("actors");
            JSONArray genres = (JSONArray) film.get("genres");

            List<String> actorsNames = new ArrayList<>();
            for (Object actor : actors) {
                actorsNames.add(actor.toString());
            }

            List<String> genresNames = new ArrayList<>();
            for (Object genre : genres) {
                genresNames.add(genre.toString());
            }

            if(!directorRepository.existsByName(director)) {
                failed++;
                isFailed = true;
            }
            if (isFailed) {
                continue;
            }
            for (Object c : genres) {
                if (!genreRepository.existsByName(c.toString())) {
                    failed++;
                    isFailed = true;
                    break;
                }
            }
            if (isFailed) {
                continue;
            }
            for (Object c : actors) {
                if (!actorRepository.existsByName(c.toString())) {
                    failed++;
                    break;
                }
            }
            Film newFilm = new Film(title, Integer.parseInt(year), directorRepository.findByName(director).get(), this.findActors(actorsNames), this.findGenres(genresNames));
            try {
                filmRepository.save(newFilm);
                successful++;
            }
            catch (Exception e) {
                failed++;
            }
            finally {
                isFailed= false;
            }
        }

        return new UploadResponse(successful, failed);
    }

    @Override
    public FilmListResponse getFilmsWithPagination(FilmPaginationRequest request) {
        int page = request.getPage();
        int size = request.getSize();
        Map<String, String> filters = request.getFilters();

        List<Specification<Film>> specs = getSpecifications(filters);

        Specification<Film> spec = Specification.where(specs.get(0));
        for (int i = 1; i < specs.size(); i++) {
            spec = spec.and(specs.get(i));
        }

        Pageable pageable = PageRequest.of(page, size);

        Page<Film> filmsPage = filmRepository.findAll(spec, pageable);

        FilmListResponse response = new FilmListResponse();
        response.setList(FilmResponse.convertToFilmResponseList(filmsPage.getContent()));
        response.setTotalPages(filmsPage.getTotalPages());

        return response;
    }

    @Override
    public ByteArrayResource generateFilmReport(FilmReportRequest request) {
        List<Film> films = findFilms(request);

        HSSFWorkbook  workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("Films");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Title");
        headerRow.createCell(1).setCellValue("Year");
        headerRow.createCell(2).setCellValue("Director");
        headerRow.createCell(3).setCellValue("Actors");
        headerRow.createCell(4).setCellValue("Genres");

        for (int i = 0; i < films.size(); i++) {
            Film film = films.get(i);
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(film.getTitle());
            row.createCell(1).setCellValue(film.getYear());
            row.createCell(2).setCellValue(film.getDirector().getName());
            row.createCell(3).setCellValue(film.getActors().stream().map(Actor::getName).collect(Collectors.joining(", ")));
            row.createCell(4).setCellValue(film.getGenres().stream().map(Genre::getName).collect(Collectors.joining(", ")));
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        HSSFFont font = workbook.createFont();
        HSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        try {
            workbook.write(outputStream);
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ByteArrayResource(outputStream.toByteArray());
    }

    private static List<Specification<Film>> getSpecifications(Map<String, String> filters) {
        List<Specification<Film>> specs = new ArrayList<>();

        for (Map.Entry<String, String> filter : filters.entrySet()) {
            String key = filter.getKey();
            String value = filter.getValue();

            if (key.equalsIgnoreCase("genre")) {
                specs.add((root, query, cb) -> root.join("genres").get("name").in(value));
            } else if (key.equalsIgnoreCase("actor")) {
                specs.add((root, query, cb) -> root.join("actors").get("name").in(value));
            } else if (key.equalsIgnoreCase("director")) {
                specs.add((root, query, cb) -> root.get("director").get("name").in(value));
            } else if (key.equalsIgnoreCase("page")) {
            } else {
                throw new IllegalArgumentException("Invalid filter: " + key);
            }
        }
        return specs;
    }

    private Film convertToFilm(FilmRequest filmData) {
        Film film = new Film();
        film.setTitle(filmData.getTitle());
        film.setYear(filmData.getYear());

        Director director = directorRepository.findByName(filmData.getDirector())
                .orElseGet(() -> directorRepository.save(new Director(filmData.getDirector())));
        film.setDirector(director);

        List<Genre> genres = filmData.getGenres().stream()
                .map(genreName -> genreRepository.findByName(genreName)
                        .orElseGet(() -> genreRepository.save(new Genre(genreName))))
                .toList();

        List<Actor> actors = filmData.getActors().stream()
                .map(actorName -> actorRepository.findByName(actorName)
                        .orElseGet(() -> actorRepository.save(new Actor(actorName))))
                .toList();

        film.setGenres(genres);
        film.setActors(actors);
        return film;
    }

    private List<Actor> findActors(List<String> actorsNames) {
        List<Actor> actors = new ArrayList<>();
        for (String actorName : actorsNames) {
            Optional<Actor> actor = actorRepository.findByName(actorName);
            actor.ifPresent(actors::add);
        }
        return actors;
    }

    private List<Genre> findGenres(List<String> genresNames) {
        List<Genre> genres = new ArrayList<>();
        for (String genreName : genresNames) {
            Optional<Genre> genre = genreRepository.findByName(genreName);
            genre.ifPresent(genres::add);
        }
        return genres;
    }

    private List<Film> findFilms(FilmReportRequest request) {
        Specification<Film> spec = Specification.where(null);

        if (request.getDirector() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("director").get("name"), request.getDirector()));
        }

        if (request.getActors() != null && !request.getActors().isEmpty()) {
            spec = spec.and((root, query, cb) -> root.join("actors").get("name").in(request.getActors()));
        }

        if (request.getGenres() != null && !request.getGenres().isEmpty()) {
            spec = spec.and((root, query, cb) -> root.join("genres").get("name").in(request.getGenres()));
        }

        return filmRepository.findAll(spec);
    }
}