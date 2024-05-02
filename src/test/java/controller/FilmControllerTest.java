package controller;

import org.example.dto.request.FilmPaginationRequest;
import org.example.dto.request.FilmReportRequest;
import org.example.dto.request.FilmRequest;
import org.example.model.Film;
import org.example.service.FilmService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = org.example.Application.class)
@AutoConfigureMockMvc
public class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FilmService filmService;

    @BeforeEach
    public void setup() {
        Film film = new Film();
        film.setId(1);
        Mockito.when(filmService.getFilmById(1)).thenReturn(Optional.of(film));
    }

    @Test
    public void createFilmReturnsCreatedWhenFilmDoesNotExist() throws Exception {
        FilmRequest filmRequest = new FilmRequest();
        mockMvc.perform(post("/api/film/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Test Film\",\"director\":\"Test Director\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    public void getFilmByIdReturnsOkWhenFilmExists() throws Exception {
        mockMvc.perform(get("/api/film/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getFilmByIdReturnsNotFoundWhenFilmDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/film/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateFilmReturnsOkWhenFilmExists() throws Exception {
        mockMvc.perform(put("/api/film/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Updated Film\",\"director\":\"Updated Director\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void updateFilmReturnsNotFoundWhenFilmDoesNotExist() throws Exception {
        mockMvc.perform(put("/api/film/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Updated Film\",\"director\":\"Updated Director\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteFilmReturnsNoContentWhenFilmExists() throws Exception {
        mockMvc.perform(delete("/api/film/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteFilmReturnsNotFoundWhenFilmDoesNotExist() throws Exception {
        mockMvc.perform(delete("/api/film/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getFilmsWithPaginationReturnsOk() throws Exception {
        FilmPaginationRequest request = new FilmPaginationRequest();
        mockMvc.perform(post("/api/film/film_list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"page\":1,\"size\":10}"))
                .andExpect(status().isOk());
    }

    @Test
    public void generateFilmReportReturnsOk() throws Exception {
        FilmReportRequest request = new FilmReportRequest();
        mockMvc.perform(post("/api/film/film/film_report")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"director\":\"Test Director\",\"actors\":[\"Actor 1\",\"Actor 2\"],\"genres\":[\"Genre 1\",\"Genre 2\"]}"))
                .andExpect(status().isOk());
    }

    @Test
    public void importFilmsFromJsonReturnsOk() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.json", "application/json", "{\"title\":\"Test Film\",\"director\":\"Test Director\"}".getBytes());
        mockMvc.perform(multipart("/api/film/film/upload").file(file))
                .andExpect(status().isOk());
    }
}