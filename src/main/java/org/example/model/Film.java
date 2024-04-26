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

    @NotBlank(message = "Director is required")
    @Size(max = 255, message = "Director name cannot be longer than 255 characters")
    @Pattern(regexp = "^(?:[A-Z][a-z]*)(?:\\s+[A-Z][a-z]*|-[A-Z][a-z]*)*$",
            message = "Each word should start with a capital letter followed by lowercase letters")
    private String director;

    @ElementCollection
    @CollectionTable(name = "actors", joinColumns = @JoinColumn(name = "actor_id"))
    @Column(name = "actors")
    @NotBlank(message = "Actors are required")
    @Pattern(regexp = "^(?:[A-Z][a-z]*)(?:\\s+[A-Z][a-z]*)*$",
            message = "Each word should start with a capital letter followed by lowercase letters")
    private List<String> actors;

    @ElementCollection
    @CollectionTable(name = "genres", joinColumns = @JoinColumn(name = "genre_id"))
    @Column(name = "genres")
    @NotBlank(message = "Genres are required")
    @Pattern(regexp = "^(?:[A-Z][a-z]*)(?:\\s+[A-Z][a-z]*|-[A-Z][a-z]*)*$",
            message = "Each word should start with a capital letter followed by lowercase letters")
    private List<String> genres;
}
