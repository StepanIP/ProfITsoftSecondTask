package org.example.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FilmRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title cannot be longer than 255 characters")
    private String title;

    @NotNull(message = "Year is required")
    private int year;

    @NotNull(message = "Director is required")
    private String director;

    @NotNull(message = "Actors are required")
    private List<String> actors;

    @NotNull(message = "Genres are required")
    private List<String> genres;
}