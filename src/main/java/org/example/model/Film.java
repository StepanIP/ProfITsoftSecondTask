package org.example.model;

import lombok.*;
import org.example.validation.annotation.ValidReleaseYear;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.Pattern;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "films")
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title cannot be longer than 255 characters")
    @Pattern(regexp = "^(?:[A-Z][a-z]*)(?:\\s+[A-Z][a-z]*|-[A-Z][a-z]*)*$",
            message = "Each word should start with a capital letter followed by lowercase letters")
    private String title;

    @NotNull(message = "Year is required")
    @ValidReleaseYear
    private int year;

    @ManyToOne
    @JoinColumn(name = "director_id", nullable = false)
    private Director director;

    @ManyToMany
    @JoinTable(
            name = "film_actors",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
    private List<Actor> actors;

    @ManyToMany
    @JoinTable(
            name = "film_genres",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<Genre> genres;
}