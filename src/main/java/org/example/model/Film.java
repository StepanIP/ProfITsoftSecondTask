package org.example.model;

import lombok.*;
import org.example.validation.annotation.ValidReleaseYear;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "films",
        indexes = {
                @Index(name = "index_on_director", columnList = "director_id"),
        }
)
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

    @ManyToOne(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinColumn(name = "director_id", nullable = false)
    private Director director;

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(
            name = "film_actors",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id"),
            indexes = {
                    @Index(name = "index_on_actor_id", columnList = "actor_id"),
                    @Index(name = "index_on_film_id", columnList = "film_id")
            }
    )
    private List<Actor> actors;

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(
            name = "film_genres",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"),
            indexes = {
                    @Index(name = "index_on_genre_id", columnList = "genre_id"),
                    @Index(name = "index_on_film_id", columnList = "film_id")
            }
    )
    private List<Genre> genres;

    public Film(String title, int year, Director director, List<Actor> actors, List<Genre> genres)
    {
        this.title = title;
        this.year = year;
        this.director = director;
        this.actors = actors;
        this.genres = genres;
    }
}