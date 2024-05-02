package controller;

import org.example.model.Director;
import org.example.model.Film;
import org.example.service.DirectorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = org.example.Application.class)
@AutoConfigureMockMvc
public class DirectorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DirectorService directorService;

    @BeforeEach
    public void setup() {
        Director director = new Director();
        director.setId(1);
        Mockito.when(directorService.getDirectorById(1)).thenReturn(Optional.of(director));
    }

    @Test
    public void getAllDirectorsReturnsListOfDirectors() throws Exception {
        mockMvc.perform(get("/api/director/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void createDirectorReturnsCreatedWhenDirectorDoesNotExist() throws Exception {
        mockMvc.perform(post("/api/director/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test Director\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    public void updateDirectorReturnsUpdatedWhenDirectorExistsAndNameIsUnique() throws Exception {
        mockMvc.perform(put("/api/director/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Director\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void updateDirectorReturnsNotFoundWhenDirectorDoesNotExist() throws Exception {
        mockMvc.perform(put("/api/director/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Director\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteDirectorReturnsNoContentWhenDirectorExists() throws Exception {
        mockMvc.perform(delete("/api/director/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteDirectorReturnsNotFoundWhenDirectorDoesNotExist() throws Exception {
        mockMvc.perform(delete("/api/director/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}